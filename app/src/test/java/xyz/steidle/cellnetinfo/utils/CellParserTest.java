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
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthTdscdma;
import android.telephony.CellSignalStrengthWcdma;

import androidx.core.util.Pair;

import org.junit.Test;

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
        CellParser.VERSIONSDKINT = 30;

        // Test NR
        CellInfoNr cellInfoNr = mock(CellInfoNr.class);
        assertEquals("NR", CellParser.getGeneration(cellInfoNr));

        // set version code to 29 (minimum Build version for TDSCDMA)
        CellParser.VERSIONSDKINT = 29;

        // Test TDSCMA
        CellInfoTdscdma cellInfoTdscdma = mock(CellInfoTdscdma.class);
        assertEquals("TDSCDMA", CellParser.getGeneration(cellInfoTdscdma));

        // ----------------------------------------------------------------
        // Testing none version specific cell information

        CellParser.VERSIONSDKINT = 23;

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
        CellParser.VERSIONSDKINT = 23;
        CellInfoLte cellInfoLte = mock(CellInfoLte.class);
        assertEquals("?", CellParser.getProvider(cellInfoLte));

        CellIdentity cellIdentity = mock(CellIdentity.class);
        when(cellIdentity.getOperatorAlphaShort()).thenReturn("Mocked Operator");

        // NR
        CellInfoNr cellInfoNR = mock(CellInfoNr.class);
        when(cellInfoNR.getCellIdentity()).thenReturn(cellIdentity);

        CellParser.VERSIONSDKINT = 30;
        assertEquals("Mocked Operator", CellParser.getProvider(cellInfoNR));
        CellParser.VERSIONSDKINT = 28;
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
        CellParser.VERSIONSDKINT = 30;

        String mcc = "262";

        // NR cell
        CellInfoNr cellInfoNr = mock(CellInfoNr.class);
        CellIdentityNr cellIdentityNr = mock(CellIdentityNr.class);
        when(cellInfoNr.getCellIdentity()).thenReturn(cellIdentityNr);
        when(cellIdentityNr.getMccString()).thenReturn(mcc);
        assertEquals(mcc, CellParser.getMcc(cellInfoNr));

        CellParser.VERSIONSDKINT = 29;
        assertEquals("", CellParser.getMcc(cellInfoNr));

        // TDSCDMA cell
        CellInfoTdscdma cellInfoTdscdma = mock(CellInfoTdscdma.class);
        CellIdentityTdscdma cellIdentityTdscdma = mock(CellIdentityTdscdma.class);
        when(cellInfoTdscdma.getCellIdentity()).thenReturn(cellIdentityTdscdma);
        when(cellIdentityTdscdma.getMccString()).thenReturn(mcc);
        assertEquals(mcc, CellParser.getMcc(cellInfoTdscdma));

        CellParser.VERSIONSDKINT = 28;
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
        CellParser.VERSIONSDKINT = 27;

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
        CellParser.VERSIONSDKINT = 30;

        String mnc = "4";

        // NR cell
        CellInfoNr cellInfoNr = mock(CellInfoNr.class);
        CellIdentityNr cellIdentityNr = mock(CellIdentityNr.class);
        when(cellInfoNr.getCellIdentity()).thenReturn(cellIdentityNr);
        when(cellIdentityNr.getMncString()).thenReturn(mnc);
        assertEquals(mnc, CellParser.getMnc(cellInfoNr));

        CellParser.VERSIONSDKINT = 29;
        assertEquals("", CellParser.getMnc(cellInfoNr));

        // TDSCDMA cell
        CellInfoTdscdma cellInfoTdscdma = mock(CellInfoTdscdma.class);
        CellIdentityTdscdma cellIdentityTdscdma = mock(CellIdentityTdscdma.class);
        when(cellInfoTdscdma.getCellIdentity()).thenReturn(cellIdentityTdscdma);
        when(cellIdentityTdscdma.getMncString()).thenReturn(mnc);
        assertEquals(mnc, CellParser.getMnc(cellInfoTdscdma));

        CellParser.VERSIONSDKINT = 28;
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
        CellParser.VERSIONSDKINT = 27;

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
        CellParser.VERSIONSDKINT = 30;
        CellIdentityNr cellIdentityNr = mock(CellIdentityNr.class);
        when(cellIdentityNr.getTac()).thenReturn(123);
        CellInfoNr cellInfoNR = mock(CellInfoNr.class);
        when(cellInfoNR.getCellIdentity()).thenReturn(cellIdentityNr);
        assertEquals(123, (int)CellParser.getLacTac(cellInfoNR).second);
        CellParser.VERSIONSDKINT = 29;
        assertEquals(0, (int)CellParser.getLacTac(cellInfoNR).second);

        // CellInfoTdscdma
        CellIdentityTdscdma cellIdentityTdscdma = mock(CellIdentityTdscdma.class);
        when(cellIdentityTdscdma.getLac()).thenReturn(12345);
        CellInfoTdscdma cellInfoTdscdma = mock(CellInfoTdscdma.class);
        when(cellInfoTdscdma.getCellIdentity()).thenReturn(cellIdentityTdscdma);
        assertEquals(12345, (int)CellParser.getLacTac(cellInfoTdscdma).second);
        CellParser.VERSIONSDKINT = 23;
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

        CellParser.VERSIONSDKINT = 30;
        assertEquals(1234L, CellParser.getCellId(cellInfoNR));
        CellParser.VERSIONSDKINT = 28;
        assertEquals(0, CellParser.getCellId(cellInfoNR));

        // CellInfoTdscdma
        CellIdentityTdscdma cellIdentityTdscdma = mock(CellIdentityTdscdma.class);
        when(cellIdentityTdscdma.getCid()).thenReturn(12345);
        CellInfoTdscdma cellInfoTdscdma = mock(CellInfoTdscdma.class);
        when(cellInfoTdscdma.getCellIdentity()).thenReturn(cellIdentityTdscdma);

        CellParser.VERSIONSDKINT = 29;
        assertEquals(12345L, CellParser.getCellId(cellInfoTdscdma));
        CellParser.VERSIONSDKINT = 28;
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
        CellParser.VERSIONSDKINT = 30;
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

    @Test
    public void getNrarfcn() {
        int result = 123;

        CellInfoNr cellInfoNr = mock(CellInfoNr.class);
        CellIdentityNr cellIdentityNr = mock(CellIdentityNr.class);
        when(cellIdentityNr.getNrarfcn()).thenReturn(result);
        when(cellInfoNr.getCellIdentity()).thenReturn(cellIdentityNr);

        assertEquals(result, CellParser.getNrarfcn(cellInfoNr));
    }

    @Test
    public void getBandwidth() {
        int result = 20000;

        CellInfoLte cellInfoLte = mock(CellInfoLte.class);
        CellIdentityLte cellIdentityLte = mock(CellIdentityLte.class);
        when(cellIdentityLte.getBandwidth()).thenReturn(result);
        when(cellInfoLte.getCellIdentity()).thenReturn(cellIdentityLte);

        assertEquals(result, CellParser.getBandwidth(cellInfoLte));
    }

    @Test
    public void getArfcn() {
        int arfcn = 10;

        // GSM
        CellInfoGsm cellInfoGsm = mock(CellInfoGsm.class);
        CellIdentityGsm cellIdentityGsm = mock(CellIdentityGsm.class);
        when(cellIdentityGsm.getArfcn()).thenReturn(arfcn);
        when(cellInfoGsm.getCellIdentity()).thenReturn(cellIdentityGsm);
        assertEquals(arfcn, CellParser.getArfcn(cellInfoGsm));

        // TDSCDMA
        CellInfoTdscdma cellInfoTdscdma = mock(CellInfoTdscdma.class);
        CellIdentityTdscdma cellIdentityTdscdma = mock(CellIdentityTdscdma.class);
        when(cellIdentityTdscdma.getUarfcn()).thenReturn(arfcn);
        when(cellInfoTdscdma.getCellIdentity()).thenReturn(cellIdentityTdscdma);
        // SDK < Q
        CellParser.VERSIONSDKINT = 28;
        assertEquals(CellInfo.UNAVAILABLE, CellParser.getArfcn(cellInfoTdscdma));
        //SDK >= Q
        CellParser.VERSIONSDKINT = 29;
        assertEquals(arfcn, CellParser.getArfcn(cellInfoTdscdma));

        // WCDMA
        CellInfoWcdma cellInfoWcdma = mock(CellInfoWcdma.class);
        CellIdentityWcdma cellIdentityWcdma = mock(CellIdentityWcdma.class);
        when(cellIdentityWcdma.getUarfcn()).thenReturn(arfcn);
        when(cellInfoWcdma.getCellIdentity()).thenReturn(cellIdentityWcdma);
        assertEquals(arfcn, CellParser.getArfcn(cellInfoWcdma));
    }

    @Test
    public void getBsic() {
        int result = 113;

        CellInfoGsm cellInfoGsm = mock(CellInfoGsm.class);
        CellIdentityGsm cellIdentityGsm = mock(CellIdentityGsm.class);
        when(cellIdentityGsm.getBsic()).thenReturn(result);
        when(cellInfoGsm.getCellIdentity()).thenReturn(cellIdentityGsm);

        assertEquals(result, CellParser.getBsic(cellInfoGsm));
    }

    @Test
    public void getPsc() {
        int result = 113;

        CellInfoWcdma cellInfoWcdma = mock(CellInfoWcdma.class);
        CellIdentityWcdma cellIdentityWcdma = mock(CellIdentityWcdma.class);
        when(cellIdentityWcdma.getPsc()).thenReturn(result);
        when(cellInfoWcdma.getCellIdentity()).thenReturn(cellIdentityWcdma);

        assertEquals(result, CellParser.getPsc(cellInfoWcdma));
    }

    @Test
    public void getEcNo() {
        CellInfoWcdma cellInfoWcdma = mock(CellInfoWcdma.class);
        CellSignalStrengthWcdma cellSignalStrength = mock(CellSignalStrengthWcdma.class);
        when(cellInfoWcdma.getCellSignalStrength()).thenReturn(cellSignalStrength);
        when(cellSignalStrength.getEcNo()).thenReturn(1);

        CellParser.VERSIONSDKINT = 29;
        assertEquals(CellParser.UNAVAILABLE, CellParser.getEcNo(cellInfoWcdma));
        CellParser.VERSIONSDKINT = 30;
        assertEquals(1, CellParser.getEcNo(cellInfoWcdma));
    }

    @Test
    public void getRscp() {
        CellInfoTdscdma cellInfo = mock(CellInfoTdscdma.class);
        CellSignalStrengthTdscdma cellSignalStrength = mock(CellSignalStrengthTdscdma.class);
        when(cellInfo.getCellSignalStrength()).thenReturn(cellSignalStrength);
        when(cellSignalStrength.getRscp()).thenReturn(1);

        assertEquals(1, CellParser.getRscp(cellInfo));
    }

    @Test
    public void getBitErrorRate() {
        CellInfoGsm cellInfoGsm = mock(CellInfoGsm.class);
        CellSignalStrengthGsm cellSignalStrength = mock(CellSignalStrengthGsm.class);
        when(cellSignalStrength.getBitErrorRate()).thenReturn(123);
        when(cellInfoGsm.getCellSignalStrength()).thenReturn(cellSignalStrength);

        CellParser.VERSIONSDKINT = 28;
        assertEquals(CellParser.UNAVAILABLE, CellParser.getBitErrorRate(cellInfoGsm));

        CellParser.VERSIONSDKINT = 29;
        assertEquals(123, CellParser.getBitErrorRate(cellInfoGsm));
    }

    @Test
    public void getRssi() {
        CellInfoGsm cellInfoGsm = mock(CellInfoGsm.class);
        CellSignalStrengthGsm cellSignalStrength = mock(CellSignalStrengthGsm.class);
        when(cellSignalStrength.getRssi()).thenReturn(123);
        when(cellInfoGsm.getCellSignalStrength()).thenReturn(cellSignalStrength);

        assertEquals(123, CellParser.getRssi(cellInfoGsm));

        CellInfoLte cellInfoLte = mock(CellInfoLte.class);
        CellSignalStrengthLte cellSignalStrengthLte = mock(CellSignalStrengthLte.class);
        when(cellSignalStrengthLte.getRssi()).thenReturn(1234);
        when(cellInfoLte.getCellSignalStrength()).thenReturn(cellSignalStrengthLte);

        assertEquals(1234, CellParser.getRssi(cellInfoLte));

        assertEquals(CellParser.UNAVAILABLE, CellParser.getRssi(mock(CellInfo.class)));
    }

    @Test
    public void getRsrp() {
        CellInfoLte cellInfoLte = mock(CellInfoLte.class);
        CellSignalStrengthLte cellSignalStrengthLte = mock(CellSignalStrengthLte.class);
        when(cellSignalStrengthLte.getRsrp()).thenReturn(1);
        when(cellInfoLte.getCellSignalStrength()).thenReturn(cellSignalStrengthLte);

        assertEquals(1, CellParser.getRsrp(cellInfoLte));
    }

    @Test
    public void getRsrq() {
        CellInfoLte cellInfoLte = mock(CellInfoLte.class);
        CellSignalStrengthLte cellSignalStrengthLte = mock(CellSignalStrengthLte.class);
        when(cellSignalStrengthLte.getRsrq()).thenReturn(1);
        when(cellInfoLte.getCellSignalStrength()).thenReturn(cellSignalStrengthLte);

        assertEquals(1, CellParser.getRsrq(cellInfoLte));
    }

    @Test
    public void getRssnr() {
        CellInfoLte cellInfoLte = mock(CellInfoLte.class);
        CellSignalStrengthLte cellSignalStrengthLte = mock(CellSignalStrengthLte.class);
        when(cellSignalStrengthLte.getRssnr()).thenReturn(1);
        when(cellInfoLte.getCellSignalStrength()).thenReturn(cellSignalStrengthLte);

        assertEquals(1, CellParser.getRssnr(cellInfoLte));
    }

    @Test
    public void getCqi() {
        CellInfoLte cellInfoLte = mock(CellInfoLte.class);
        CellSignalStrengthLte cellSignalStrengthLte = mock(CellSignalStrengthLte.class);
        when(cellSignalStrengthLte.getCqi()).thenReturn(1);
        when(cellInfoLte.getCellSignalStrength()).thenReturn(cellSignalStrengthLte);

        assertEquals(1, CellParser.getCqi(cellInfoLte));
    }
}