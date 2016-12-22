package com.nsb.enms.adapter.server.wdm.utils;

import java.util.HashMap;
import java.util.Map;

import com.nsb.enms.mib.pss.def.*;

public class SnmpEqShelfSlotTypeMapUtil
{
    private static Map<String, String> oidShelfTypeMap = new HashMap<String, String>();
    
    private static Map<String, String> oidCardTypeMap = new HashMap<String, String>();
    
    static 
    {
        //Shelf type
        oidShelfTypeMap.put( TropicEmptyShelf.oid, "Empty" );
        oidShelfTypeMap.put( TropicUnknownShelf.oid, "Unknow" );
        oidShelfTypeMap.put( AluWdmSfd44Shelf.oid, "SFD44" );
        oidShelfTypeMap.put( AluWdmDcmShelf.oid, "DCM" );
        oidShelfTypeMap.put( AluWdmUniversalShelf.oid, "Universal" );
        oidShelfTypeMap.put( AluWdmSfd44bShelf.oid, "SFD44B" );
        oidShelfTypeMap.put( AluWdmItlbShelf.oid, "ITLB" );
        oidShelfTypeMap.put( AluWdmPSS32UniversalShelf.oid, "PSS-32" );
        oidShelfTypeMap.put( AluWdmPSS16UniversalShelf.oid, "PSS-16" );
        oidShelfTypeMap.put( AluWdmSfd40Shelf.oid, "SFD40" );
        oidShelfTypeMap.put( AluWdmSfd40bShelf.oid, "SFD40B" );
        oidShelfTypeMap.put( AluWdmPSS4UniversalShelf.oid, "PSS-4" );
        oidShelfTypeMap.put( AluWdmPSS36UniversalShelf.oid, "PSS-36" );
        oidShelfTypeMap.put( AluWdmPSS64UniversalShelf.oid, "PSS-64" );
        oidShelfTypeMap.put( AluWdmItluShelf.oid, "ITLU" );
        oidShelfTypeMap.put( AluWdmPSS32SwitchUniversalShelf.oid, "PSS32 Switch Universal" );
        oidShelfTypeMap.put( AluWdmPSS32Switch1P2TUniversalShelf.oid, "PSS32 Switch 1P2T Universal" );
        oidShelfTypeMap.put( AluWdmPsc1x6Shelf.oid, "PSC1*6" );
        oidShelfTypeMap.put( AluWdmPSS8UniversalShelf.oid, "PSS-8" );
        oidShelfTypeMap.put( AluWdmPSS16IIUniversalShelf.oid, "PSS-16II" );
        oidShelfTypeMap.put( AluWdmPSS48UniversalShelf.oid, "PSS-48" );
        oidShelfTypeMap.put( AluWdmPSS96UniversalShelf.oid, "PSS-96" );
        oidShelfTypeMap.put( AluWdmMsh8fsmShelf.oid, "MSH8-FSM" );
        oidShelfTypeMap.put( AluWdmVwmCwUniversalShelf.oid, "VWM-CW" );
        oidShelfTypeMap.put( AluWdmVwmDwUniversalShelf.oid, "VWM-DM" );
        //oidShelfTypeMap.put( AluWdmPSI1TUniversalShelf.oid, "PSI1T Universal" );
        //oidShelfTypeMap.put( AluWdmPSI2TUniversalShelf.oid, "PSI2T Universal" );
        
        //Card type
        oidCardTypeMap.put( TropicEmptyCard.oid, "Empty" );
        oidCardTypeMap.put( AluWdm112sca1Card.oid, "112SCA1" );
        oidCardTypeMap.put( AluWdm112scx10Card.oid, "112SCX10" );
        oidCardTypeMap.put( AluWdmOt112sdx11Card.oid, "112SDX11" );
        oidCardTypeMap.put( AluWdm112sna1Card.oid, "112SNA1" );
        oidCardTypeMap.put( AluWdm112snx10Card.oid, "112SNX10" );
        oidCardTypeMap.put( AluWdm11dpge12Card.oid, "11DPE12" );
        oidCardTypeMap.put( AluWdm11dpe12aCard.oid, "11DPE12A" );
        oidCardTypeMap.put( AluWdm11dpe12eCard.oid, "11DPE12E" );
        oidCardTypeMap.put( AluWdm11dpm12Card.oid, "11DPM12" );
        oidCardTypeMap.put( AluWdmOt11dpm4eCard.oid, "11DPM4E" );
        oidCardTypeMap.put( AluWdmOt11dpm4mCard.oid, "11DPM4M" );
        oidCardTypeMap.put( AluWdmOt11dpm8Card.oid, "11DPM8" );
        oidCardTypeMap.put( AluWdm11ope8Card.oid, "11OPE8" );
        oidCardTypeMap.put( AluWdm11qce12xCard.oid, "11QCE12X" );
        oidCardTypeMap.put( AluWdm11qpa4Card.oid, "11QPA4" );
        oidCardTypeMap.put( AluWdm11qpe24Card.oid, "11QPE24" );
        oidCardTypeMap.put( AluWdm11qpen4Card.oid, "11QPEN4" );
        oidCardTypeMap.put( AluWdm11star1Card.oid, "11STAR1" );
        oidCardTypeMap.put( AluWdm11star1aCard.oid, "11STAR1A" );
        oidCardTypeMap.put( AluWdm11stge12Card.oid, "11STGE12" );
        oidCardTypeMap.put( AluWdm11stmm10Card.oid, "11STMM10" );
        oidCardTypeMap.put( AluWdm12ce120Card.oid, "12CE120" );
        oidCardTypeMap.put( AluWdm12p120Card.oid, "12P120" );
        oidCardTypeMap.put( AluWdmOt130sca1Card.oid, "130SCA1" );
        oidCardTypeMap.put( AluWdm130scx10Card.oid, "130SCX10" );
        oidCardTypeMap.put( AluWdmOt130snq10Card.oid, "130SNQ10" );
        oidCardTypeMap.put( AluWdmOt130snx10Card.oid, "130SNX10" );
        oidCardTypeMap.put( AluWdm1dpp24mCard.oid, "1DPP24M" );
        
        oidCardTypeMap.put( AluWdm1ud200Card.oid, "1UD200" );
        oidCardTypeMap.put( AluWdm20p200Card.oid, "20P200" );
        
        oidCardTypeMap.put( AluWdmUl20uc200Card.oid, "20UC200" );
        oidCardTypeMap.put( AluWdmOt260scx2Card.oid, "260SCX2" );
        oidCardTypeMap.put( AluWdmUl2uc400Card.oid, "2UC400" );
        oidCardTypeMap.put( AluWdmIo30an300Card.oid, "30AN300" );
        oidCardTypeMap.put( AluWdm43sca1Card.oid, "43SCA1" );
        oidCardTypeMap.put( AluWdm43scge1Card.oid, "43SCGE1" );
        oidCardTypeMap.put( AluWdm43scx4Card.oid, "43SCX4" );
        oidCardTypeMap.put( AluWdm43scx4eCard.oid, "43SCX4E" );
        oidCardTypeMap.put( AluWdm43sta1pCard.oid, "43STA1P" );
        oidCardTypeMap.put( AluWdm43stx4Card.oid, "43STX4" );
        oidCardTypeMap.put( AluWdm43stx4pCard.oid, "43STX4P" );
        oidCardTypeMap.put( AluWdmIo4an400Card.oid, "4AN400" );
        oidCardTypeMap.put( AluWdmUl4uc400Card.oid, "4UC400" );
        oidCardTypeMap.put( AluWdm4dpa2Card.oid, "4DPA2" );
        oidCardTypeMap.put( AluWdm4dpa4Card.oid, "4DPA4" );
        oidCardTypeMap.put( AluWdm4qpa8Card.oid, "4QPA8" );
        
        oidCardTypeMap.put( AluWdmA2325aCard.oid, "A2325A" );        
        oidCardTypeMap.put( AluWdmA2p2125Card.oid, "A2P2125" );
        oidCardTypeMap.put( AluWdmA4pswgCard.oid, "A4PSWG" );
        oidCardTypeMap.put( AluWdmAa2donwCard.oid, "AA2DONW" );
        oidCardTypeMap.put( AluWdmAar8aCard.oid, "AAR-8A" );
        oidCardTypeMap.put( AluWdmAhphgCard.oid, "AHPHG" );
        oidCardTypeMap.put( AluWdmAhplgCard.oid, "AHPLG" );
        oidCardTypeMap.put( AluWdmAlphgCard.oid, "ALPHG" );
        oidCardTypeMap.put( AluWdmAm2032aCard.oid, "AM2032A" );
        oidCardTypeMap.put( AluWdmAm2125aCard.oid, "AM2125A" );
        oidCardTypeMap.put( AluWdmAm2125bCard.oid, "AM2125B" );
        oidCardTypeMap.put( AluWdmAm2318aCard.oid, "AM2318A" );
        oidCardTypeMap.put( AluWdmAm2625aCard.oid, "AM2625A" );
        oidCardTypeMap.put( AluWdmAswgCard.oid, "ASWG" );
        oidCardTypeMap.put( AluWdmAwbegrCard.oid, "AWBEGR" );
        oidCardTypeMap.put( AluWdmAwbilaCard.oid, "AWBILA" );
        oidCardTypeMap.put( AluWdmAwbingCard.oid, "AWBING" );
        //oidCardTypeMap.put( "TBD", "CEC2" );
        oidCardTypeMap.put( AluWdmClockControllerCard96Card.oid, "CEC2" );
        
        oidCardTypeMap.put( AluWdmCwr8Card.oid, "CWR8" );
        oidCardTypeMap.put( AluWdmCwr8c88Card.oid, "CWR8-88" );
        
        oidCardTypeMap.put( AluWdmD5x500Card.oid, "D5X500" );
        
        oidCardTypeMap.put( AluWdmDcmCard.oid, "DCM" );
        oidCardTypeMap.put( AluWdmEquipmentControllerCard.oid, "EC" );
        oidCardTypeMap.put( AluWdmPSS4EquipmentControllerCard.oid, "EC" );
        //oidCardTypeMap.put( AluWdmPSI1TEquipmentControllerCard.oid, "EC2I" );
        oidCardTypeMap.put( AluWdmPSS8EquipmentController2Card.oid, "8EC2" );
        oidCardTypeMap.put( AluWdmPSS8EquipmentController2EncryptionCard.oid, "8EC2E" );
        oidCardTypeMap.put( AluWdmPSS32EquipmentController2Card.oid, "32EC2" );
        oidCardTypeMap.put( AluWdmPSS32EquipmentController2EncryptionCard.oid, "32EC2E" );
        oidCardTypeMap.put( AluWdmVwmCwEquipmentControllerCard.oid, "EC-CW" );
        oidCardTypeMap.put( AluWdmVwmDwEquipmentControllerCard.oid, "EC-DW" );
        oidCardTypeMap.put( AluWdmFanUnitCard.oid, "FAN" );
        //oidCardTypeMap.put( AluWdmPSI1TFanUnitCard.oid, "FAN" );
        oidCardTypeMap.put( AluWdmIroadm9mCard.oid, "IROADM9M" );
        oidCardTypeMap.put( AluWdmIroadmfCard.oid, "IROADMF" );
        oidCardTypeMap.put( AluWdmIroadmvCard.oid, "IROADMV" );
        oidCardTypeMap.put( AluWdmItlbCard.oid, "ITLB" );
        oidCardTypeMap.put( AluWdmItluCard.oid, "ITLU" );
        oidCardTypeMap.put( AluWdmLcI1000Card.oid, "LCI1000" );
        oidCardTypeMap.put( AluWdmMcs8x16Card.oid, "MCS8-16" );
        oidCardTypeMap.put( AluWdmMesh4Card.oid, "MESH4" );
        oidCardTypeMap.put( AluWdmMultiFunctionCard.oid, "MFC" );
        oidCardTypeMap.put( AluWdmMonOcmCard.oid, "MON-OCM" );
        oidCardTypeMap.put( AluWdmMonOtdrCard.oid, "MON-OTDR" );
        oidCardTypeMap.put( AluWdmMsh8fsmCard.oid, "MSH8-FSM" );
        oidCardTypeMap.put( AluWdmMVACCard.oid, "MVAC" );
        oidCardTypeMap.put( AluWdmMVAC8BCard.oid, "MVAC8B" );
        oidCardTypeMap.put( AluWdmOpsaCard.oid, "OPSA" );
        oidCardTypeMap.put( AluWdmOpsbCard.oid, "OPSB" );
        oidCardTypeMap.put( AluWdmOsctCard.oid, "OSCT" );
        oidCardTypeMap.put( AluWdmOtdrCard.oid, "OTDR" );
        oidCardTypeMap.put( AluWdmPSS8PowerFilterAcCard.oid, "PF" );
        //oidCardTypeMap.put( "TBD", "PF" );
        oidCardTypeMap.put( AluWdmPSS96PowerFilterCard.oid, "PF" );
        //oidCardTypeMap.put( AluWdmPSI1TPowerFilterCard.oid, "PF" );
        oidCardTypeMap.put( AluWdmPowerFilterCard.oid, "PF" );
        oidCardTypeMap.put( AluWdmPsc1x6Card.oid, "PSC1-6" );
        oidCardTypeMap.put( AluWdmPtpctlCard.oid, "PTPCTL" );
        oidCardTypeMap.put( AluWdmPtpioCard.oid, "PTPIO" );
        oidCardTypeMap.put( AluWdmPtpioctlCard.oid, "PTPIOC" );
        oidCardTypeMap.put( AluWdmRa2pCard.oid, "RA2P" );
        oidCardTypeMap.put( AluWdmRa5pCard.oid, "RA5P" );
        oidCardTypeMap.put( AluWdmS13x100Card.oid, "S13X100R" );
        //oidCardTypeMap.put( AluWdmSwitchingCard.oid, "SC16" );
        oidCardTypeMap.put( AluWdmSwitchingCard.oid, "SC96" );
        oidCardTypeMap.put( AluWdmSFC1ACard.oid, "SFC1A" );
        oidCardTypeMap.put( AluWdmSFC1BCard.oid, "SFC1B" );
        oidCardTypeMap.put( AluWdmSFC1CCard.oid, "SFC1C" );
        oidCardTypeMap.put( AluWdmSFC1DCard.oid, "SFC1D" );
        oidCardTypeMap.put( AluWdmSFC1ECard.oid, "SFC1E" );
        oidCardTypeMap.put( AluWdmSFC1FCard.oid, "SFC1F" );
        oidCardTypeMap.put( AluWdmSFC1GCard.oid, "SFC1G" );
        oidCardTypeMap.put( AluWdmSFC1HCard.oid, "SFC1H" );
        oidCardTypeMap.put( AluWdmSFC2ACard.oid, "SFC2A" );
        oidCardTypeMap.put( AluWdmSFC2BCard.oid, "SFC2B" );
        oidCardTypeMap.put( AluWdmSFC2CCard.oid, "SFC2C" );
        oidCardTypeMap.put( AluWdmSFC2DCard.oid, "SFC2D" );
        oidCardTypeMap.put( AluWdmSFC4ACard.oid, "SFC4A" );
        oidCardTypeMap.put( AluWdmSFC4BCard.oid, "SFC4B" );
        oidCardTypeMap.put( AluWdmSFC8Card.oid, "SFC8" );
        oidCardTypeMap.put( AluWdmSFD2ACard.oid, "SFD2A" );
        oidCardTypeMap.put( AluWdmSFD2BCard.oid, "SFD2B" );
        oidCardTypeMap.put( AluWdmSFD2CCard.oid, "SFD2C" );
        oidCardTypeMap.put( AluWdmSFD2DCard.oid, "SFD2D" );
        oidCardTypeMap.put( AluWdmSFD2ECard.oid, "SFD2E" );
        oidCardTypeMap.put( AluWdmSFD2FCard.oid, "SFD2F" );
        oidCardTypeMap.put( AluWdmSFD2GCard.oid, "SFD2G" );
        oidCardTypeMap.put( AluWdmSFD2HCard.oid, "SFD2H" );
        oidCardTypeMap.put( AluWdmSFD2ICard.oid, "SFD2I" );
        oidCardTypeMap.put( AluWdmSFD2LCard.oid, "SFD2L" );
        oidCardTypeMap.put( AluWdmSFD2MCard.oid, "SFD2M" );
        oidCardTypeMap.put( AluWdmSFD2NCard.oid, "SFD2N" );
        oidCardTypeMap.put( AluWdmSFD2OCard.oid, "SFD2O" );
        oidCardTypeMap.put( AluWdmSFD2PCard.oid, "SFD2P" );
        oidCardTypeMap.put( AluWdmSFD2QCard.oid, "SFD2Q" );
        oidCardTypeMap.put( AluWdmSFD2RCard.oid, "SFD2R" );
        
        oidCardTypeMap.put( AluWdmSFD4ACard.oid, "SFD4A" );
        oidCardTypeMap.put( AluWdmSFD4BCard.oid, "SFD4B" );
        oidCardTypeMap.put( AluWdmSFD4CCard.oid, "SFD4C" );
        oidCardTypeMap.put( AluWdmSFD4DCard.oid, "SFD4D" );
        oidCardTypeMap.put( AluWdmSFD4ECard.oid, "SFD4E" );
        oidCardTypeMap.put( AluWdmSFD4FCard.oid, "SFD4F" );
        oidCardTypeMap.put( AluWdmSFD4GCard.oid, "SFD4G" );
        oidCardTypeMap.put( AluWdmSFD4HCard.oid, "SFD4H" );
        
        oidCardTypeMap.put( AluWdmSFD5ACard.oid, "SFD5A" );
        oidCardTypeMap.put( AluWdmSFD5BCard.oid, "SFD5B" );
        oidCardTypeMap.put( AluWdmSFD5CCard.oid, "SFD5C" );
        oidCardTypeMap.put( AluWdmSFD5DCard.oid, "SFD5D" );
        oidCardTypeMap.put( AluWdmSFD5ECard.oid, "SFD5E" );
        oidCardTypeMap.put( AluWdmSFD5GCard.oid, "SFD5F" );
        oidCardTypeMap.put( AluWdmSFD5GCard.oid, "SFD5G" );
        oidCardTypeMap.put( AluWdmSFD5HCard.oid, "SFD5H" );
        
        oidCardTypeMap.put( AluWdmSFD8ACard.oid, "SFD8A" );
        oidCardTypeMap.put( AluWdmSFD8BCard.oid, "SFD8B" );
        oidCardTypeMap.put( AluWdmSFD8CCard.oid, "SFD8C" );
        oidCardTypeMap.put( AluWdmSFD8DCard.oid, "SFD8D" );
        
        oidCardTypeMap.put( AluWdmSfd40Card.oid, "SFD40" );
        oidCardTypeMap.put( AluWdmSfd40bCard.oid, "SFD40B" );
        oidCardTypeMap.put( AluWdmSfd44Card.oid, "SFD44" );
        oidCardTypeMap.put( AluWdmSfd44bCard.oid, "SFD44B" );
        oidCardTypeMap.put( AluWdmPSS8ShelfPanelCard.oid, "SHFPNL" );
        oidCardTypeMap.put( AluWdmSVACCard.oid, "SVAC" );
        oidCardTypeMap.put( AluWdmUserInterfacePanelCard.oid, "USRPNL" );
        oidCardTypeMap.put( AluWdmPSS8UserInterfacePanelCard.oid, "8USRPNL" );
        oidCardTypeMap.put( AluWdmVirtualCard.oid, "Virtual/Preset" );
        oidCardTypeMap.put( AluWdmWr2c88Card.oid, "WR2-88" );
        oidCardTypeMap.put( AluWdmWr20tfCard.oid, "WR20-TF" );
        oidCardTypeMap.put( AluWdmWr20tfmCard.oid, "WR20-TFM" );
        oidCardTypeMap.put( AluWdmWr8c88aCard.oid, "WR8-88A" );
        oidCardTypeMap.put( AluWdmWr8c88afCard.oid, "WR8-88AF" );
        oidCardTypeMap.put( AluWdmWtocmCard.oid, "WTOCM" );
        oidCardTypeMap.put( AluWdmWtocmaCard.oid, "WTOCMA" );
        oidCardTypeMap.put( AluWdmWtocmfCard.oid, "WTOCM-F" );        
    }
    
    public static String getShelfType(String oid)
    {
        if (oidShelfTypeMap.containsKey( oid ))
        {
            return oidShelfTypeMap.get( oid );
        }
        return oid;
    }
    
    public static String getSlotCardType(String oid)
    {
        if (oidCardTypeMap.containsKey( oid ))
        {
            return oidCardTypeMap.get( oid );
        }
        return oid;
    }       
}