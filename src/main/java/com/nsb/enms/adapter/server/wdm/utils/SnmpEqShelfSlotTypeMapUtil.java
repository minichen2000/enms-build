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
        oidShelfTypeMap.put( M_tropicEmptyShelf.oid, "Empty" );
        oidShelfTypeMap.put( M_tropicUnknownShelf.oid, "Unknow" );
        oidShelfTypeMap.put( M_aluWdmSfd44Shelf.oid, "SFD44" );
        oidShelfTypeMap.put( M_aluWdmDcmShelf.oid, "DCM" );
        oidShelfTypeMap.put( M_aluWdmUniversalShelf.oid, "Universal" );
        oidShelfTypeMap.put( M_aluWdmSfd44bShelf.oid, "SFD44B" );
        oidShelfTypeMap.put( M_aluWdmItlbShelf.oid, "ITLB" );
        oidShelfTypeMap.put( M_aluWdmPSS32UniversalShelf.oid, "PSS-32" );
        oidShelfTypeMap.put( M_aluWdmPSS16UniversalShelf.oid, "PSS-16" );
        oidShelfTypeMap.put( M_aluWdmSfd40Shelf.oid, "SFD40" );
        oidShelfTypeMap.put( M_aluWdmSfd40bShelf.oid, "SFD40B" );
        oidShelfTypeMap.put( M_aluWdmPSS4UniversalShelf.oid, "PSS-4" );
        oidShelfTypeMap.put( M_aluWdmPSS36UniversalShelf.oid, "PSS-36" );
        oidShelfTypeMap.put( M_aluWdmPSS64UniversalShelf.oid, "PSS-64" );
        oidShelfTypeMap.put( M_aluWdmItluShelf.oid, "ITLU" );
        oidShelfTypeMap.put( M_aluWdmPSS32SwitchUniversalShelf.oid, "PSS32 Switch Universal" );
        oidShelfTypeMap.put( M_aluWdmPSS32Switch1P2TUniversalShelf.oid, "PSS32 Switch 1P2T Universal" );
        oidShelfTypeMap.put( M_aluWdmPsc1x6Shelf.oid, "PSC1*6" );
        oidShelfTypeMap.put( M_aluWdmPSS8UniversalShelf.oid, "PSS-8" );
        oidShelfTypeMap.put( M_aluWdmPSS16IIUniversalShelf.oid, "PSS-16II" );
        oidShelfTypeMap.put( M_aluWdmPSS48UniversalShelf.oid, "PSS-48" );
        oidShelfTypeMap.put( M_aluWdmPSS96UniversalShelf.oid, "PSS-96" );
        oidShelfTypeMap.put( M_aluWdmMsh8fsmShelf.oid, "MSH8-FSM" );
        oidShelfTypeMap.put( M_aluWdmVwmCwUniversalShelf.oid, "VWM-CW" );
        oidShelfTypeMap.put( M_aluWdmVwmDwUniversalShelf.oid, "VWM-DM" );
        //oidShelfTypeMap.put( M_aluWdmPSI1TUniversalShelf.oid, "PSI1T Universal" );
        //oidShelfTypeMap.put( M_aluWdmPSI2TUniversalShelf.oid, "PSI2T Universal" );
        
        //Card type
        oidCardTypeMap.put( M_tropicEmptyCard.oid, "Empty" );
        oidCardTypeMap.put( M_aluWdm112sca1Card.oid, "112SCA1" );
        oidCardTypeMap.put( M_aluWdm112scx10Card.oid, "112SCX10" );
        oidCardTypeMap.put( M_aluWdmOt112sdx11Card.oid, "112SDX11" );
        oidCardTypeMap.put( M_aluWdm112sna1Card.oid, "112SNA1" );
        oidCardTypeMap.put( M_aluWdm112snx10Card.oid, "112SNX10" );
        oidCardTypeMap.put( M_aluWdm11dpge12Card.oid, "11DPE12" );
        oidCardTypeMap.put( M_aluWdm11dpe12aCard.oid, "11DPE12A" );
        oidCardTypeMap.put( M_aluWdm11dpe12eCard.oid, "11DPE12E" );
        oidCardTypeMap.put( M_aluWdm11dpm12Card.oid, "11DPM12" );
        oidCardTypeMap.put( M_aluWdmOt11dpm4eCard.oid, "11DPM4E" );
        oidCardTypeMap.put( M_aluWdmOt11dpm4mCard.oid, "11DPM4M" );
        oidCardTypeMap.put( M_aluWdmOt11dpm8Card.oid, "11DPM8" );
        oidCardTypeMap.put( M_aluWdm11ope8Card.oid, "11OPE8" );
        oidCardTypeMap.put( M_aluWdm11qce12xCard.oid, "11QCE12X" );
        oidCardTypeMap.put( M_aluWdm11qpa4Card.oid, "11QPA4" );
        oidCardTypeMap.put( M_aluWdm11qpe24Card.oid, "11QPE24" );
        oidCardTypeMap.put( M_aluWdm11qpen4Card.oid, "11QPEN4" );
        oidCardTypeMap.put( M_aluWdm11star1Card.oid, "11STAR1" );
        oidCardTypeMap.put( M_aluWdm11star1aCard.oid, "11STAR1A" );
        oidCardTypeMap.put( M_aluWdm11stge12Card.oid, "11STGE12" );
        oidCardTypeMap.put( M_aluWdm11stmm10Card.oid, "11STMM10" );
        oidCardTypeMap.put( M_aluWdm12ce120Card.oid, "12CE120" );
        oidCardTypeMap.put( M_aluWdm12p120Card.oid, "12P120" );
        oidCardTypeMap.put( M_aluWdmOt130sca1Card.oid, "130SCA1" );
        oidCardTypeMap.put( M_aluWdm130scx10Card.oid, "130SCX10" );
        oidCardTypeMap.put( M_aluWdmOt130snq10Card.oid, "130SNQ10" );
        oidCardTypeMap.put( M_aluWdmOt130snx10Card.oid, "130SNX10" );
        oidCardTypeMap.put( M_aluWdm1dpp24mCard.oid, "1DPP24M" );
        
        oidCardTypeMap.put( M_aluWdm1ud200Card.oid, "1UD200" );
        oidCardTypeMap.put( M_aluWdm20p200Card.oid, "20P200" );
        
        oidCardTypeMap.put( M_aluWdmUl20uc200Card.oid, "20UC200" );
        oidCardTypeMap.put( M_aluWdmOt260scx2Card.oid, "260SCX2" );
        oidCardTypeMap.put( M_aluWdmUl2uc400Card.oid, "2UC400" );
        oidCardTypeMap.put( M_aluWdmIo30an300Card.oid, "30AN300" );
        oidCardTypeMap.put( M_aluWdm43sca1Card.oid, "43SCA1" );
        oidCardTypeMap.put( M_aluWdm43scge1Card.oid, "43SCGE1" );
        oidCardTypeMap.put( M_aluWdm43scx4Card.oid, "43SCX4" );
        oidCardTypeMap.put( M_aluWdm43scx4eCard.oid, "43SCX4E" );
        oidCardTypeMap.put( M_aluWdm43sta1pCard.oid, "43STA1P" );
        oidCardTypeMap.put( M_aluWdm43stx4Card.oid, "43STX4" );
        oidCardTypeMap.put( M_aluWdm43stx4pCard.oid, "43STX4P" );
        oidCardTypeMap.put( M_aluWdmIo4an400Card.oid, "4AN400" );
        oidCardTypeMap.put( M_aluWdmUl4uc400Card.oid, "4UC400" );
        oidCardTypeMap.put( M_aluWdm4dpa2Card.oid, "4DPA2" );
        oidCardTypeMap.put( M_aluWdm4dpa4Card.oid, "4DPA4" );
        oidCardTypeMap.put( M_aluWdm4qpa8Card.oid, "4QPA8" );
        
        oidCardTypeMap.put( M_aluWdmA2325aCard.oid, "A2325A" );        
        oidCardTypeMap.put( M_aluWdmA2p2125Card.oid, "A2P2125" );
        oidCardTypeMap.put( M_aluWdmA4pswgCard.oid, "A4PSWG" );
        oidCardTypeMap.put( M_aluWdmAa2donwCard.oid, "AA2DONW" );
        oidCardTypeMap.put( M_aluWdmAar8aCard.oid, "AAR-8A" );
        oidCardTypeMap.put( M_aluWdmAhphgCard.oid, "AHPHG" );
        oidCardTypeMap.put( M_aluWdmAhplgCard.oid, "AHPLG" );
        oidCardTypeMap.put( M_aluWdmAlphgCard.oid, "ALPHG" );
        oidCardTypeMap.put( M_aluWdmAm2032aCard.oid, "AM2032A" );
        oidCardTypeMap.put( M_aluWdmAm2125aCard.oid, "AM2125A" );
        oidCardTypeMap.put( M_aluWdmAm2125bCard.oid, "AM2125B" );
        oidCardTypeMap.put( M_aluWdmAm2318aCard.oid, "AM2318A" );
        oidCardTypeMap.put( M_aluWdmAm2625aCard.oid, "AM2625A" );
        oidCardTypeMap.put( M_aluWdmAswgCard.oid, "ASWG" );
        oidCardTypeMap.put( M_aluWdmAwbegrCard.oid, "AWBEGR" );
        oidCardTypeMap.put( M_aluWdmAwbilaCard.oid, "AWBILA" );
        oidCardTypeMap.put( M_aluWdmAwbingCard.oid, "AWBING" );
        //oidCardTypeMap.put( "TBD", "CEC2" );
        oidCardTypeMap.put( M_aluWdmClockControllerCard96Card.oid, "CEC2" );
        
        oidCardTypeMap.put( M_aluWdmCwr8Card.oid, "CWR8" );
        oidCardTypeMap.put( M_aluWdmCwr8c88Card.oid, "CWR8-88" );
        
        oidCardTypeMap.put( M_aluWdmD5x500Card.oid, "D5X500" );
        
        oidCardTypeMap.put( M_aluWdmDcmCard.oid, "DCM" );
        oidCardTypeMap.put( M_aluWdmEquipmentControllerCard.oid, "EC" );
        oidCardTypeMap.put( M_aluWdmPSS4EquipmentControllerCard.oid, "EC" );
        //oidCardTypeMap.put( M_aluWdmPSI1TEquipmentControllerCard.oid, "EC2I" );
        oidCardTypeMap.put( M_aluWdmPSS8EquipmentController2Card.oid, "8EC2" );
        oidCardTypeMap.put( M_aluWdmPSS8EquipmentController2EncryptionCard.oid, "8EC2E" );
        oidCardTypeMap.put( M_aluWdmPSS32EquipmentController2Card.oid, "32EC2" );
        oidCardTypeMap.put( M_aluWdmPSS32EquipmentController2EncryptionCard.oid, "32EC2E" );
        oidCardTypeMap.put( M_aluWdmVwmCwEquipmentControllerCard.oid, "EC-CW" );
        oidCardTypeMap.put( M_aluWdmVwmDwEquipmentControllerCard.oid, "EC-DW" );
        oidCardTypeMap.put( M_aluWdmFanUnitCard.oid, "FAN" );
        //oidCardTypeMap.put( M_aluWdmPSI1TFanUnitCard.oid, "FAN" );
        oidCardTypeMap.put( M_aluWdmIroadm9mCard.oid, "IROADM9M" );
        oidCardTypeMap.put( M_aluWdmIroadmfCard.oid, "IROADMF" );
        oidCardTypeMap.put( M_aluWdmIroadmvCard.oid, "IROADMV" );
        oidCardTypeMap.put( M_aluWdmItlbCard.oid, "ITLB" );
        oidCardTypeMap.put( M_aluWdmItluCard.oid, "ITLU" );
        oidCardTypeMap.put( M_aluWdmLcI1000Card.oid, "LCI1000" );
        oidCardTypeMap.put( M_aluWdmMcs8x16Card.oid, "MCS8-16" );
        oidCardTypeMap.put( M_aluWdmMesh4Card.oid, "MESH4" );
        oidCardTypeMap.put( M_aluWdmMultiFunctionCard.oid, "MFC" );
        oidCardTypeMap.put( M_aluWdmMonOcmCard.oid, "MON-OCM" );
        oidCardTypeMap.put( M_aluWdmMonOtdrCard.oid, "MON-OTDR" );
        oidCardTypeMap.put( M_aluWdmMsh8fsmCard.oid, "MSH8-FSM" );
        oidCardTypeMap.put( M_aluWdmMVACCard.oid, "MVAC" );
        oidCardTypeMap.put( M_aluWdmMVAC8BCard.oid, "MVAC8B" );
        oidCardTypeMap.put( M_aluWdmOpsaCard.oid, "OPSA" );
        oidCardTypeMap.put( M_aluWdmOpsbCard.oid, "OPSB" );
        oidCardTypeMap.put( M_aluWdmOsctCard.oid, "OSCT" );
        oidCardTypeMap.put( M_aluWdmOtdrCard.oid, "OTDR" );
        oidCardTypeMap.put( M_aluWdmPSS8PowerFilterAcCard.oid, "PF" );
        //oidCardTypeMap.put( "TBD", "PF" );
        oidCardTypeMap.put( M_aluWdmPSS96PowerFilterCard.oid, "PF" );
        //oidCardTypeMap.put( M_aluWdmPSI1TPowerFilterCard.oid, "PF" );
        oidCardTypeMap.put( M_aluWdmPowerFilterCard.oid, "PF" );
        oidCardTypeMap.put( M_aluWdmPsc1x6Card.oid, "PSC1-6" );
        oidCardTypeMap.put( M_aluWdmPtpctlCard.oid, "PTPCTL" );
        oidCardTypeMap.put( M_aluWdmPtpioCard.oid, "PTPIO" );
        oidCardTypeMap.put( M_aluWdmPtpioctlCard.oid, "PTPIOC" );
        oidCardTypeMap.put( M_aluWdmRa2pCard.oid, "RA2P" );
        oidCardTypeMap.put( M_aluWdmRa5pCard.oid, "RA5P" );
        oidCardTypeMap.put( M_aluWdmS13x100Card.oid, "S13X100R" );
        //oidCardTypeMap.put( M_aluWdmSwitchingCard.oid, "SC16" );
        oidCardTypeMap.put( M_aluWdmSwitchingCard.oid, "SC96" );
        oidCardTypeMap.put( M_aluWdmSFC1ACard.oid, "SFC1A" );
        oidCardTypeMap.put( M_aluWdmSFC1BCard.oid, "SFC1B" );
        oidCardTypeMap.put( M_aluWdmSFC1CCard.oid, "SFC1C" );
        oidCardTypeMap.put( M_aluWdmSFC1DCard.oid, "SFC1D" );
        oidCardTypeMap.put( M_aluWdmSFC1ECard.oid, "SFC1E" );
        oidCardTypeMap.put( M_aluWdmSFC1FCard.oid, "SFC1F" );
        oidCardTypeMap.put( M_aluWdmSFC1GCard.oid, "SFC1G" );
        oidCardTypeMap.put( M_aluWdmSFC1HCard.oid, "SFC1H" );
        oidCardTypeMap.put( M_aluWdmSFC2ACard.oid, "SFC2A" );
        oidCardTypeMap.put( M_aluWdmSFC2BCard.oid, "SFC2B" );
        oidCardTypeMap.put( M_aluWdmSFC2CCard.oid, "SFC2C" );
        oidCardTypeMap.put( M_aluWdmSFC2DCard.oid, "SFC2D" );
        oidCardTypeMap.put( M_aluWdmSFC4ACard.oid, "SFC4A" );
        oidCardTypeMap.put( M_aluWdmSFC4BCard.oid, "SFC4B" );
        oidCardTypeMap.put( M_aluWdmSFC8Card.oid, "SFC8" );
        oidCardTypeMap.put( M_aluWdmSFD2ACard.oid, "SFD2A" );
        oidCardTypeMap.put( M_aluWdmSFD2BCard.oid, "SFD2B" );
        oidCardTypeMap.put( M_aluWdmSFD2CCard.oid, "SFD2C" );
        oidCardTypeMap.put( M_aluWdmSFD2DCard.oid, "SFD2D" );
        oidCardTypeMap.put( M_aluWdmSFD2ECard.oid, "SFD2E" );
        oidCardTypeMap.put( M_aluWdmSFD2FCard.oid, "SFD2F" );
        oidCardTypeMap.put( M_aluWdmSFD2GCard.oid, "SFD2G" );
        oidCardTypeMap.put( M_aluWdmSFD2HCard.oid, "SFD2H" );
        oidCardTypeMap.put( M_aluWdmSFD2ICard.oid, "SFD2I" );
        oidCardTypeMap.put( M_aluWdmSFD2LCard.oid, "SFD2L" );
        oidCardTypeMap.put( M_aluWdmSFD2MCard.oid, "SFD2M" );
        oidCardTypeMap.put( M_aluWdmSFD2NCard.oid, "SFD2N" );
        oidCardTypeMap.put( M_aluWdmSFD2OCard.oid, "SFD2O" );
        oidCardTypeMap.put( M_aluWdmSFD2PCard.oid, "SFD2P" );
        oidCardTypeMap.put( M_aluWdmSFD2QCard.oid, "SFD2Q" );
        oidCardTypeMap.put( M_aluWdmSFD2RCard.oid, "SFD2R" );
        
        oidCardTypeMap.put( M_aluWdmSFD4ACard.oid, "SFD4A" );
        oidCardTypeMap.put( M_aluWdmSFD4BCard.oid, "SFD4B" );
        oidCardTypeMap.put( M_aluWdmSFD4CCard.oid, "SFD4C" );
        oidCardTypeMap.put( M_aluWdmSFD4DCard.oid, "SFD4D" );
        oidCardTypeMap.put( M_aluWdmSFD4ECard.oid, "SFD4E" );
        oidCardTypeMap.put( M_aluWdmSFD4FCard.oid, "SFD4F" );
        oidCardTypeMap.put( M_aluWdmSFD4GCard.oid, "SFD4G" );
        oidCardTypeMap.put( M_aluWdmSFD4HCard.oid, "SFD4H" );
        
        oidCardTypeMap.put( M_aluWdmSFD5ACard.oid, "SFD5A" );
        oidCardTypeMap.put( M_aluWdmSFD5BCard.oid, "SFD5B" );
        oidCardTypeMap.put( M_aluWdmSFD5CCard.oid, "SFD5C" );
        oidCardTypeMap.put( M_aluWdmSFD5DCard.oid, "SFD5D" );
        oidCardTypeMap.put( M_aluWdmSFD5ECard.oid, "SFD5E" );
        oidCardTypeMap.put( M_aluWdmSFD5GCard.oid, "SFD5F" );
        oidCardTypeMap.put( M_aluWdmSFD5GCard.oid, "SFD5G" );
        oidCardTypeMap.put( M_aluWdmSFD5HCard.oid, "SFD5H" );
        
        oidCardTypeMap.put( M_aluWdmSFD8ACard.oid, "SFD8A" );
        oidCardTypeMap.put( M_aluWdmSFD8BCard.oid, "SFD8B" );
        oidCardTypeMap.put( M_aluWdmSFD8CCard.oid, "SFD8C" );
        oidCardTypeMap.put( M_aluWdmSFD8DCard.oid, "SFD8D" );
        
        oidCardTypeMap.put( M_aluWdmSfd40Card.oid, "SFD40" );
        oidCardTypeMap.put( M_aluWdmSfd40bCard.oid, "SFD40B" );
        oidCardTypeMap.put( M_aluWdmSfd44Card.oid, "SFD44" );
        oidCardTypeMap.put( M_aluWdmSfd44bCard.oid, "SFD44B" );
        oidCardTypeMap.put( M_aluWdmPSS8ShelfPanelCard.oid, "SHFPNL" );
        oidCardTypeMap.put( M_aluWdmSVACCard.oid, "SVAC" );
        oidCardTypeMap.put( M_aluWdmUserInterfacePanelCard.oid, "USRPNL" );
        oidCardTypeMap.put( M_aluWdmPSS8UserInterfacePanelCard.oid, "8USRPNL" );
        oidCardTypeMap.put( M_aluWdmVirtualCard.oid, "Virtual/Preset" );
        oidCardTypeMap.put( M_aluWdmWr2c88Card.oid, "WR2-88" );
        oidCardTypeMap.put( M_aluWdmWr20tfCard.oid, "WR20-TF" );
        oidCardTypeMap.put( M_aluWdmWr20tfmCard.oid, "WR20-TFM" );
        oidCardTypeMap.put( M_aluWdmWr8c88aCard.oid, "WR8-88A" );
        oidCardTypeMap.put( M_aluWdmWr8c88afCard.oid, "WR8-88AF" );
        oidCardTypeMap.put( M_aluWdmWtocmCard.oid, "WTOCM" );
        oidCardTypeMap.put( M_aluWdmWtocmaCard.oid, "WTOCMA" );
        oidCardTypeMap.put( M_aluWdmWtocmfCard.oid, "WTOCM-F" );        
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