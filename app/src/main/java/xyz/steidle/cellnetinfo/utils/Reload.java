package xyz.steidle.cellnetinfo.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.telephony.CellInfo;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.List;

import xyz.steidle.cellnetinfo.MainActivity;
import xyz.steidle.cellnetinfo.R;

public class Reload extends Service {

    CellInfoHandler cellInfoHandler;
    HandlerThread handlerThread;
    Handler handler;
    DatabaseHandler databaseHandler;
    Notification notification;

    @Override
    public void onCreate() {
        super.onCreate();

        cellInfoHandler = new CellInfoHandler(this);
        databaseHandler = new DatabaseHandler(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String notificationChannelId = "steidle.reload_service_notification";
        String channelName = "Reloading cells in background...";

        Intent showTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
        showTaskIntent.setAction(Intent.ACTION_MAIN);
        showTaskIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                showTaskIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, channelName, NotificationManager.IMPORTANCE_LOW);
        getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);

        notification = new Notification.Builder(getApplicationContext(), notificationChannelId)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(channelName)
                .setSmallIcon(R.drawable.ic_signal_0)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(contentIntent)
                .build();

        startForeground(2, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        handlerThread = new HandlerThread("MyLocationThread");
        handlerThread.setDaemon(true);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        handler.postDelayed(this::loadCells, 60000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        return START_STICKY;
    }

    private void loadCells() {
        Log.d("ForegroundService:Reload", "Loading Cells...");

        List<CellInfo> cellInfoList = cellInfoHandler.getCells();

        for (CellInfo cellInfo : cellInfoList)
            databaseHandler.addCell(cellInfo);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (handlerThread != null)
            handlerThread.quit();
    }
}
