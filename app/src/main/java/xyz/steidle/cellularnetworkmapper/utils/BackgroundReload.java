package xyz.steidle.cellularnetworkmapper.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class BackgroundReload extends Worker {

    Context context;
    CellInfoHandler cellInfoHandler;

    public BackgroundReload(Context context, WorkerParameters workerParams) {
        super(context, workerParams);

        this.context = context;
        cellInfoHandler = new CellInfoHandler(this.context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("BackgroundReload", "DoWork");

        return Result.success();
    }
}
