/*
 * Created by Robin Steidle on 15.05.22, 21:07
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 15.05.22, 18:22
 */

package xyz.steidle.cellnetinfo.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.telephony.CellIdentity;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityTdscdma;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoTdscdma;
import android.telephony.CellInfoWcdma;

import androidx.core.util.Pair;

import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.runner.RunWith;

public class CellParserTest {

    @Test
    public void CellParser() {
        assertThrows(IllegalStateException.class, CellParser::new);
    }

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
        assertEquals("?", CellParser.getProvider(cellInfoLte));

        CellIdentity cellIdentity = mock(CellIdentity.class);
        when(cellIdentity.getOperatorAlphaShort()).thenReturn("Mocked Operator");

        // NR
        CellInfoNr cellInfoNR = mock(CellInfoNr.class);
        when(cellInfoNR.getCellIdentity()).thenReturn(cellIdentity);

        CellParser.VERSION_SDK_INT = 30;
        assertEquals("Mocked Operator", CellParser.getProvider(cellInfoNR));
        CellParser.VERSION_SDK_INT = 28;
        assertEquals("?", CellParser.getProvider(cellInfoNR));

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
        assertEquals("?", CellParser.getProvider(cellInfoCdma));
    }



    @Test
    public void getMcc() {
        // Version_Code > P
        CellParser.VERSION_SDK_INT = 30;

        String mcc = "262";

        // NR cell
        CellInfoNr cellInfoNr = mock(CellInfoNr.class);
        CellIdentityNr cellIdentityNr = mock(CellIdentityNr.class);
        when(cellInfoNr.getCellIdentity()).thenReturn(cellIdentityNr);
        when(cellIdentityNr.getMccString()).thenReturn(mcc);
        assertEquals(mcc, CellParser.getMcc(cellInfoNr));

        CellParser.VERSION_SDK_INT = 29;
        assertEquals("", CellParser.getMcc(cellInfoNr));

        // TDSCDMA cell
        CellInfoTdscdma cellInfoTdscdma = mock(CellInfoTdscdma.class);
        CellIdentityTdscdma cellIdentityTdscdma = mock(CellIdentityTdscdma.class);
        when(cellInfoTdscdma.getCellIdentity()).thenReturn(cellIdentityTdscdma);
        when(cellIdentityTdscdma.getMccString()).thenReturn(mcc);
        assertEquals(mcc, CellParser.getMcc(cellInfoTdscdma));

        CellParser.VERSION_SDK_INT = 28;
        assertEquals("", CellParser.getMcc(cellInfoTdscdma));

        // LTE cell
        CellInfoLte cellInfoLte= mock(CellInfoLte.class);
        CellIdentityLte cellIdentityLte = mock(CellIdentityLte.class);
        when(cellInfoLte.getCellIdentity()).thenReturn(cellIdentityLte);
        when(cellIdentityLte.getMccString()).thenReturn(mcc);
        assertEquals(mcc, CellParser.getMcc(cellInfoLte));

        // GSM cell
        CellInfoGsm cellInfoGsm= mock(CellInfoGsm.class);
        CellIdentityGsm cellIdentityGsm = mock(CellIdentityGsm.class);
        when(cellInfoGsm.getCellIdentity()).thenReturn(cellIdentityGsm);
        when(cellIdentityGsm.getMccString()).thenReturn(mcc);
        assertEquals(mcc, CellParser.getMcc(cellInfoGsm));

        // WCDMA cell
        CellInfoWcdma cellInfoWcdma = mock(CellInfoWcdma.class);
        CellIdentityWcdma cellIdentityWcdma = mock(CellIdentityWcdma.class);
        when(cellInfoWcdma.getCellIdentity()).thenReturn(cellIdentityWcdma);
        when(cellIdentityWcdma.getMccString()).thenReturn(mcc);
        assertEquals(mcc, CellParser.getMcc(cellInfoWcdma));

        // Version_Code < 28 --------------------------------------
        CellParser.VERSION_SDK_INT = 27;

        // LTE
        int intMcc = 262;
        when(cellIdentityLte.getMcc()).thenReturn(intMcc);
        assertEquals(mcc, CellParser.getMcc(cellInfoLte));

        // GSM
        when(cellIdentityGsm.getMcc()).thenReturn(intMcc);
        assertEquals(mcc, CellParser.getMcc(cellInfoGsm));

        // WCDMA
        when(cellIdentityWcdma.getMcc()).thenReturn(intMcc);
        assertEquals(mcc, CellParser.getMcc(cellInfoWcdma));
    }

    @Test
    public void getMnc() {
        // Version_Code > P
        CellParser.VERSION_SDK_INT = 30;

        String mnc = "4";

        // NR cell
        CellInfoNr cellInfoNr = mock(CellInfoNr.class);
        CellIdentityNr cellIdentityNr = mock(CellIdentityNr.class);
        when(cellInfoNr.getCellIdentity()).thenReturn(cellIdentityNr);
        when(cellIdentityNr.getMncString()).thenReturn(mnc);
        assertEquals(mnc, CellParser.getMnc(cellInfoNr));

        CellParser.VERSION_SDK_INT = 29;
        assertEquals("", CellParser.getMnc(cellInfoNr));

        // TDSCDMA cell
        CellInfoTdscdma cellInfoTdscdma = mock(CellInfoTdscdma.class);
        CellIdentityTdscdma cellIdentityTdscdma = mock(CellIdentityTdscdma.class);
        when(cellInfoTdscdma.getCellIdentity()).thenReturn(cellIdentityTdscdma);
        when(cellIdentityTdscdma.getMncString()).thenReturn(mnc);
        assertEquals(mnc, CellParser.getMnc(cellInfoTdscdma));

        CellParser.VERSION_SDK_INT = 28;
        assertEquals("", CellParser.getMnc(cellInfoTdscdma));

        // LTE cell
        CellInfoLte cellInfoLte= mock(CellInfoLte.class);
        CellIdentityLte cellIdentityLte = mock(CellIdentityLte.class);
        when(cellInfoLte.getCellIdentity()).thenReturn(cellIdentityLte);
        when(cellIdentityLte.getMncString()).thenReturn(mnc);
        assertEquals(mnc, CellParser.getMnc(cellInfoLte));

        // GSM cell
        CellInfoGsm cellInfoGsm= mock(CellInfoGsm.class);
        CellIdentityGsm cellIdentityGsm = mock(CellIdentityGsm.class);
        when(cellInfoGsm.getCellIdentity()).thenReturn(cellIdentityGsm);
        when(cellIdentityGsm.getMncString()).thenReturn(mnc);
        assertEquals(mnc, CellParser.getMnc(cellInfoGsm));

        // WCDMA cell
        CellInfoWcdma cellInfoWcdma = mock(CellInfoWcdma.class);
        CellIdentityWcdma cellIdentityWcdma = mock(CellIdentityWcdma.class);
        when(cellInfoWcdma.getCellIdentity()).thenReturn(cellIdentityWcdma);
        when(cellIdentityWcdma.getMncString()).thenReturn(mnc);
        assertEquals(mnc, CellParser.getMnc(cellInfoWcdma));

        // Version_Code < 28 --------------------------------------
        CellParser.VERSION_SDK_INT = 27;

        // LTE
        int intMcc = 4;
        when(cellIdentityLte.getMnc()).thenReturn(intMcc);
        assertEquals(mnc, CellParser.getMnc(cellInfoLte));

        // GSM
        when(cellIdentityGsm.getMnc()).thenReturn(intMcc);
        assertEquals(mnc, CellParser.getMnc(cellInfoGsm));

        // WCDMA
        when(cellIdentityWcdma.getMnc()).thenReturn(intMcc);
        assertEquals(mnc, CellParser.getMnc(cellInfoWcdma));
    }



    @Test
    public void getLacTac() {
        // null object
        Pair<Integer, Integer> pair = CellParser.getLacTac(null);
        int result = pair.second;
        assertEquals(0, result);

        // CellInfoNr
        CellParser.VERSION_SDK_INT = 30;
        CellIdentityNr cellIdentityNr = mock(CellIdentityNr.class);
        when(cellIdentityNr.getTac()).thenReturn(123);
        CellInfoNr cellInfoNR = mock(CellInfoNr.class);
        when(cellInfoNR.getCellIdentity()).thenReturn(cellIdentityNr);
        assertEquals(123, (int)CellParser.getLacTac(cellInfoNR).second);
        CellParser.VERSION_SDK_INT = 29;
        assertEquals(0, (int)CellParser.getLacTac(cellInfoNR).second);

        // CellInfoTdscdma
        CellIdentityTdscdma cellIdentityTdscdma = mock(CellIdentityTdscdma.class);
        when(cellIdentityTdscdma.getLac()).thenReturn(12345);
        CellInfoTdscdma cellInfoTdscdma = mock(CellInfoTdscdma.class);
        when(cellInfoTdscdma.getCellIdentity()).thenReturn(cellIdentityTdscdma);
        assertEquals(12345, (int)CellParser.getLacTac(cellInfoTdscdma).second);
        CellParser.VERSION_SDK_INT = 23;
        assertEquals(0, (int)CellParser.getLacTac(cellInfoTdscdma).second);

        // CellInfoLte
        CellIdentityLte cellIdentityLte = mock(CellIdentityLte.class);
        when(cellIdentityLte.getTac()).thenReturn(123456);
        CellInfoLte cellInfoLte = mock(CellInfoLte.class);
        when(cellInfoLte.getCellIdentity()).thenReturn(cellIdentityLte);
        assertEquals(123456, (int)CellParser.getLacTac(cellInfoLte).second);

        // CellInfoGsm
        CellIdentityGsm cellIdentityGsm = mock(CellIdentityGsm.class);
        when(cellIdentityGsm.getLac()).thenReturn(1234);
        CellInfoGsm cellInfoGsm = mock(CellInfoGsm.class);
        when(cellInfoGsm.getCellIdentity()).thenReturn(cellIdentityGsm);
        assertEquals(1234, (int)CellParser.getLacTac(cellInfoGsm).second);

        // CellInfoWcdma
        CellIdentityWcdma cellIdentityWcdma = mock(CellIdentityWcdma.class);
        when(cellIdentityWcdma.getLac()).thenReturn(12);
        CellInfoWcdma cellInfoWcdma = mock(CellInfoWcdma.class);
        when(cellInfoWcdma.getCellIdentity()).thenReturn(cellIdentityWcdma);
        assertEquals(12, (int)CellParser.getLacTac(cellInfoWcdma).second);
    }

    @Test
    public void getCellId() {
        // NR
        CellIdentityNr cellIdentity = mock(CellIdentityNr.class);
        when(cellIdentity.getNci()).thenReturn(1234L);
        CellInfoNr cellInfoNR = mock(CellInfoNr.class);
        when(cellInfoNR.getCellIdentity()).thenReturn(cellIdentity);

        CellParser.VERSION_SDK_INT = 30;
        assertEquals(1234L, CellParser.getCellId(cellInfoNR));
        CellParser.VERSION_SDK_INT = 28;
        assertEquals(0, CellParser.getCellId(cellInfoNR));

        // CellInfoTdscdma
        CellIdentityTdscdma cellIdentityTdscdma = mock(CellIdentityTdscdma.class);
        when(cellIdentityTdscdma.getCid()).thenReturn(12345);
        CellInfoTdscdma cellInfoTdscdma = mock(CellInfoTdscdma.class);
        when(cellInfoTdscdma.getCellIdentity()).thenReturn(cellIdentityTdscdma);

        CellParser.VERSION_SDK_INT = 29;
        assertEquals(12345L, CellParser.getCellId(cellInfoTdscdma));
        CellParser.VERSION_SDK_INT = 28;
        assertEquals(0, CellParser.getCellId(cellInfoTdscdma));

        // Test LTE
        CellInfoLte cellInfoLte = mock(CellInfoLte.class);
        CellIdentityLte cellIdentityLte = mock(CellIdentityLte.class);
        when(cellIdentityLte.getCi()).thenReturn(123456);
        when(cellInfoLte.getCellIdentity()).thenReturn(cellIdentityLte);
        assertEquals(123456, CellParser.getCellId(cellInfoLte));

        // Test GSM
        CellInfoGsm cellInfoGsm = mock(CellInfoGsm.class);
        CellIdentityGsm cellIdentityGsm= mock(CellIdentityGsm.class);
        when(cellIdentityGsm.getCid()).thenReturn(1234567);
        when(cellInfoGsm.getCellIdentity()).thenReturn(cellIdentityGsm);
        assertEquals(1234567, CellParser.getCellId(cellInfoGsm));


        // Test WCDMA
        CellInfoWcdma cellInfoWcdma = mock(CellInfoWcdma.class);
        CellIdentityWcdma cellIdentityWcdma = mock(CellIdentityWcdma.class);
        when(cellIdentityWcdma.getCid()).thenReturn(12345678);
        when(cellInfoWcdma.getCellIdentity()).thenReturn(cellIdentityWcdma);
        assertEquals(12345678, CellParser.getCellId(cellInfoWcdma));
    }

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
        // CellIdentity cellIdentity = mock(CellIdentity.class);
        // when(cellIdentity.getOperatorAlphaShort()).thenReturn("Mocked Operator");
        assertEquals(0, CellParser.getSignalStrength(cellInfo));
    }

    @Test
    public void getSignalDbm() {
        // CellSignalStrength -> null
        CellInfo cellInfo = mock(CellInfo.class);
        assertEquals(0, CellParser.getSignalDbm(cellInfo));
    }

    @Test
    public void getCdmaLocation() {
        Pair<Integer, Integer> testLatLong = new Pair<>(123, 321);

        CellInfoCdma cellInfoCdma = mock(CellInfoCdma.class);
        CellIdentityCdma cellIdentityCdma = mock(CellIdentityCdma.class);
        when(cellInfoCdma.getCellIdentity()).thenReturn(cellIdentityCdma);
        when(cellIdentityCdma.getLatitude()).thenReturn(testLatLong.first);
        when(cellIdentityCdma.getLongitude()).thenReturn(testLatLong.second);

        assertEquals(testLatLong, CellParser.getCdmaLocation(cellInfoCdma));
    }

    @Test
    public void getCdmaBaseStationId() {
        int bsId = 184;
        CellInfoCdma cellInfoCdma = mock(CellInfoCdma.class);
        CellIdentityCdma cellIdentityCdma = mock(CellIdentityCdma.class);
        when(cellInfoCdma.getCellIdentity()).thenReturn(cellIdentityCdma);
        when(cellIdentityCdma.getBasestationId()).thenReturn(bsId);

        assertEquals(bsId, CellParser.getCdmaBaseStationId(cellInfoCdma));
    }

    @Test
    public void getCdmaNetworkId() {
        int netId = 2;
        CellInfoCdma cellInfoCdma = mock(CellInfoCdma.class);
        CellIdentityCdma cellIdentityCdma = mock(CellIdentityCdma.class);
        when(cellInfoCdma.getCellIdentity()).thenReturn(cellIdentityCdma);
        when(cellIdentityCdma.getNetworkId()).thenReturn(netId);

        assertEquals(netId, CellParser.getCdmaNetworkId(cellInfoCdma));
    }

    @Test
    public void getCdmaSystemId() {
        int sysId = 2;
        CellInfoCdma cellInfoCdma = mock(CellInfoCdma.class);
        CellIdentityCdma cellIdentityCdma = mock(CellIdentityCdma.class);
        when(cellInfoCdma.getCellIdentity()).thenReturn(cellIdentityCdma);
        when(cellIdentityCdma.getSystemId()).thenReturn(sysId);

        assertEquals(sysId, CellParser.getCdmaSystemId(cellInfoCdma));
    }

    /*
    @Test
    public void getCellSignalStrength() {

    }

    */
}