package xyz.steidle.cellnetinfo.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.telephony.CellIdentity;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoTdscdma;
import android.telephony.CellInfoWcdma;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CellParserTest {

    @Test
    public void getGeneration() {
        // ----------------------------------------------------------------
        // Testing version specific cell information

        // set version code to 30 (minimum Build version for NR/5G)
        CellParser.VERSION_SDK_INT = 30;

        // Test NR
        CellInfoNr cellInfoNr = mock(CellInfoNr.class);
        assertEquals("NR", CellParser.getGeneration(cellInfoNr));

        // set version code to 29 (minimum Build version for TDSCDMA)
        CellParser.VERSION_SDK_INT = 29;

        // Test TDSCMA
        CellInfoTdscdma cellInfoTdscdma = mock(CellInfoTdscdma.class);
        assertEquals("TDSCDMA", CellParser.getGeneration(cellInfoTdscdma));

        // ----------------------------------------------------------------
        // Testing none version specific cell information

        CellParser.VERSION_SDK_INT = 23;

        // Testing to low build version
        assertEquals("Unknown", CellParser.getGeneration(cellInfoNr));
        assertEquals("Unknown", CellParser.getGeneration(cellInfoTdscdma));

        // Test LTE
        CellInfoLte cellInfoLte = mock(CellInfoLte.class);
        assertEquals("LTE", CellParser.getGeneration(cellInfoLte));

        // Test GSM
        CellInfoGsm cellInfoGsm = mock(CellInfoGsm.class);
        assertEquals("GSM", CellParser.getGeneration(cellInfoGsm));

        // Test WCDMA
        CellInfoWcdma cellInfoWcdma = mock(CellInfoWcdma.class);
        assertEquals("WCDMA", CellParser.getGeneration(cellInfoWcdma));

        // Test CDMA
        CellInfoCdma cellInfoCdma = mock(CellInfoCdma.class);
        assertEquals("CDMA", CellParser.getGeneration(cellInfoCdma));
    }

    @Test
    public void getProvider() {
        // Testing too low version sdk number
        CellParser.VERSION_SDK_INT = 23;
        CellInfoLte cellInfoLte = mock(CellInfoLte.class);
        assertEquals("Unknown", CellParser.getProvider(cellInfoLte));

        CellIdentity cellIdentity = mock(CellIdentity.class);
        when(cellIdentity.getOperatorAlphaShort()).thenReturn("Mocked Operator");

        // NR
        CellInfoNr cellInfoNR = mock(CellInfoNr.class);
        when(cellInfoNR.getCellIdentity()).thenReturn(cellIdentity);

        CellParser.VERSION_SDK_INT = 30;
        assertEquals("Mocked Operator", CellParser.getProvider(cellInfoNR));
        CellParser.VERSION_SDK_INT = 28;
        assertEquals("Unknown", CellParser.getProvider(cellInfoNR));

        // Test LTE
        CellIdentityLte cellIdentityLte = mock(CellIdentityLte.class);
        when(cellIdentityLte.getOperatorAlphaShort()).thenReturn("Mocked Operator LTE");
        when(cellInfoLte.getCellIdentity()).thenReturn(cellIdentityLte);
        assertEquals("Mocked Operator LTE", CellParser.getProvider(cellInfoLte));

        // Test GSM
        CellInfoGsm cellInfoGsm = mock(CellInfoGsm.class);
        CellIdentityGsm cellIdentityGsm= mock(CellIdentityGsm.class);
        when(cellIdentityGsm.getOperatorAlphaShort()).thenReturn("Mocked Operator GSM");
        when(cellInfoGsm.getCellIdentity()).thenReturn(cellIdentityGsm);
        assertEquals("Mocked Operator GSM", CellParser.getProvider(cellInfoGsm));


        // Test WCDMA
        CellInfoWcdma cellInfoWcdma = mock(CellInfoWcdma.class);
        CellIdentityWcdma cellIdentityWcdma = mock(CellIdentityWcdma.class);
        when(cellIdentityWcdma.getOperatorAlphaShort()).thenReturn("Mocked Operator WCDMA");
        when(cellInfoWcdma.getCellIdentity()).thenReturn(cellIdentityWcdma);
        assertEquals("Mocked Operator WCDMA", CellParser.getProvider(cellInfoWcdma));


        // Test CDMA
        CellInfoCdma cellInfoCdma = mock(CellInfoCdma.class);
        CellIdentityCdma cellIdentityCdma = mock(CellIdentityCdma.class);
        when(cellIdentityCdma.getOperatorAlphaShort()).thenReturn("Mocked Operator CDMA");
        when(cellInfoCdma.getCellIdentity()).thenReturn(cellIdentityCdma);
        assertEquals("Mocked Operator CDMA", CellParser.getProvider(cellInfoCdma));

        // null operator
        when(cellInfoCdma.getCellIdentity()).thenReturn(null);
        assertEquals("Unknown", CellParser.getProvider(cellInfoCdma));
    }

    /*

    @Test
    public void getMcc() {
    }

    @Test
    public void getMnc() {
    }

    @Test
    public void getLacTac() {
    }

    @Test
    public void getCellId() {
    }

    */

    @Test
    public void getPci() {
        CellInfoNr cellInfoNr = mock(CellInfoNr.class);
        CellIdentityNr cellIdentityNr = mock(CellIdentityNr.class);
        when(cellIdentityNr.getPci()).thenReturn(2);
        when(cellInfoNr.getCellIdentity()).thenReturn(cellIdentityNr);

        assertEquals(-1, CellParser.getPci(cellInfoNr));
        CellParser.VERSION_SDK_INT = 30;
        assertEquals(2, CellParser.getPci(cellInfoNr));

        CellInfoLte cellInfoLte = mock(CellInfoLte.class);
        CellIdentityLte cellIdentityLte = mock(CellIdentityLte.class);
        when(cellIdentityLte.getPci()).thenReturn(5);
        when(cellInfoLte.getCellIdentity()).thenReturn(cellIdentityLte);

        assertEquals(5, CellParser.getPci(cellInfoLte));
    }

    @Test
    public void getSignalStrength() {
        // CellSignalStrength -> null
        CellInfo cellInfo = mock(CellInfo.class);
        CellIdentity cellIdentity = mock(CellIdentity.class);
        when(cellIdentity.getOperatorAlphaShort()).thenReturn("Mocked Operator");
        assertEquals(0, CellParser.getSignalStrength(cellInfo));
    }

    @Test
    public void getSignalDbm() {
        // CellSignalStrength -> null
        CellInfo cellInfo = mock(CellInfo.class);
        assertEquals(0, CellParser.getSignalDbm(cellInfo));
    }

    /*
    @Test
    public void getCellSignalStrength() {

    }

    */
}