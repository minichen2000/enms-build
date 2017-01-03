package com.nsb.enms.adapter.server.wdm.utils;

import java.util.HashMap;
import java.util.Map;

import com.nsb.enms.mib.pss.def.M_tropicCardReg;
import com.nsb.enms.mib.pss.def.M_tropicCommonEquipmentReg;
import com.nsb.enms.mib.pss.def.M_tropicShelfReg;

public class SnmpEqShelfSlotTypeMapUtil {
	private static Map<String, String> oidShelfTypeMap = new HashMap<String, String>();

	private static Map<String, String> oidCardTypeMap = new HashMap<String, String>();

	static {
		// Shelf type
		oidShelfTypeMap.put(M_tropicShelfReg.tropicEmptyShelf, "Empty");
		oidShelfTypeMap.put(M_tropicShelfReg.tropicUnknownShelf, "Unknow");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmSfd44Shelf, "SFD44");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmDcmShelf, "DCM");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmUniversalShelf, "Universal");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmSfd44bShelf, "SFD44B");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmItlbShelf, "ITLB");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmPSS32UniversalShelf, "PSS-32");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmPSS16UniversalShelf, "PSS-16");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmSfd40Shelf, "SFD40");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmSfd40bShelf, "SFD40B");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmPSS4UniversalShelf, "PSS-4");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmPSS36UniversalShelf, "PSS-36");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmPSS64UniversalShelf, "PSS-64");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmItluShelf, "ITLU");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmPSS32SwitchUniversalShelf, "PSS32 Switch Universal");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmPSS32Switch1P2TUniversalShelf, "PSS32 Switch 1P2T Universal");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmPsc1x6Shelf, "PSC1*6");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmPSS8UniversalShelf, "PSS-8");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmPSS16IIUniversalShelf, "PSS-16II");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmPSS48UniversalShelf, "PSS-48");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmPSS96UniversalShelf, "PSS-96");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmMsh8fsmShelf, "MSH8-FSM");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmVwmCwUniversalShelf, "VWM-CW");
		oidShelfTypeMap.put(M_tropicShelfReg.aluWdmVwmDwUniversalShelf, "VWM-DM");
		// oidShelfTypeMap.put( M_tropicShelfReg.aluWdmPSI1TUniversalShelf,
		// "PSI1T Universal" );
		// oidShelfTypeMap.put( M_tropicShelfReg.aluWdmPSI2TUniversalShelf,
		// "PSI2T Universal" );

		// Card type
		oidCardTypeMap.put(M_tropicCardReg.tropicEmptyCard, "Empty");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm112sca1Card, "112SCA1");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm112scx10Card, "112SCX10");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmOt112sdx11Card, "112SDX11");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm112snx10Card, "112SNX10");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm11dpge12Card, "11DPE12");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm11dpe12aCard, "11DPE12A");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm11dpe12eCard, "11DPE12E");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm11dpm12Card, "11DPM12");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmOt11dpm4eCard, "11DPM4E");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmOt11dpm4mCard, "11DPM4M");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmOt11dpm8Card, "11DPM8");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm11ope8Card, "11OPE8");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm11qce12xCard, "11QCE12X");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm11qpa4Card, "11QPA4");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm11qpe24Card, "11QPE24");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm11qpen4Card, "11QPEN4");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm11star1Card, "11STAR1");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm11star1aCard, "11STAR1A");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm11stge12Card, "11STGE12");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm11stmm10Card, "11STMM10");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm12ce120Card, "12CE120");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm12p120Card, "12P120");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmOt130sca1Card, "130SCA1");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm130scx10Card, "130SCX10");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmOt130snq10Card, "130SNQ10");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmOt130snx10Card, "130SNX10");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm1dpp24mCard, "1DPP24M");

		oidCardTypeMap.put(M_tropicCardReg.aluWdm1ud200Card, "1UD200");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm20p200Card, "20P200");

		oidCardTypeMap.put(M_tropicCardReg.aluWdmUl20uc200Card, "20UC200");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmOt260scx2Card, "260SCX2");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmUl2uc400Card, "2UC400");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmIo30an300Card, "30AN300");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm43sca1Card, "43SCA1");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm43scge1Card, "43SCGE1");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm43scx4Card, "43SCX4");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm43scx4eCard, "43SCX4E");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm43sta1pCard, "43STA1P");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm43stx4Card, "43STX4");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm43stx4pCard, "43STX4P");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmIo4an400Card, "4AN400");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmUl4uc400Card, "4UC400");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm4dpa2Card, "4DPA2");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm4dpa4Card, "4DPA4");
		oidCardTypeMap.put(M_tropicCardReg.aluWdm4qpa8Card, "4QPA8");

		oidCardTypeMap.put(M_tropicCardReg.aluWdmA2325aCard, "A2325A");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmA2p2125Card, "A2P2125");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmA4pswgCard, "A4PSWG");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmAa2donwCard, "AA2DONW");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmAar8aCard, "AAR-8A");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmAhphgCard, "AHPHG");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmAhplgCard, "AHPLG");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmAlphgCard, "ALPHG");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmAm2032aCard, "AM2032A");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmAm2125aCard, "AM2125A");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmAm2125bCard, "AM2125B");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmAm2318aCard, "AM2318A");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmAm2625aCard, "AM2625A");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmAswgCard, "ASWG");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmAwbegrCard, "AWBEGR");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmAwbilaCard, "AWBILA");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmAwbingCard, "AWBING");
		// oidCardTypeMap.put( "TBD", "CEC2" );
		oidCardTypeMap.put(M_tropicCardReg.aluWdmClockControllerCard96Card, "CEC2");

		oidCardTypeMap.put(M_tropicCardReg.aluWdmCwr8Card, "CWR8");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmCwr8c88Card, "CWR8-88");

		oidCardTypeMap.put(M_tropicCardReg.aluWdmD5x500Card, "D5X500");

		oidCardTypeMap.put(M_tropicCardReg.aluWdmDcmCard, "DCM");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmEquipmentControllerCard, "EC");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmPSS4EquipmentControllerCard, "EC");
		//oidCardTypeMap.put(M_tropicControlCardReg.aluWdmPSI1TEquipmentControllerCard, "EC2I" );
		oidCardTypeMap.put(M_tropicCardReg.aluWdmPSS8EquipmentController2Card, "8EC2");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmPSS8EquipmentController2EncryptionCard, "8EC2E");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmPSS32EquipmentController2Card, "32EC2");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmPSS32EquipmentController2EncryptionCard, "32EC2E");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmVwmCwEquipmentControllerCard, "EC-CW");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmVwmDwEquipmentControllerCard, "EC-DW");
		oidCardTypeMap.put(M_tropicCommonEquipmentReg.aluWdmFanUnitCard, "FAN");
		//oidCardTypeMap.put( M_tropicCommonEquipmentReg.aluWdmPSI1TFanUnitCard, "FAN" );
		oidCardTypeMap.put(M_tropicCardReg.aluWdmIroadm9mCard, "IROADM9M");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmIroadmfCard, "IROADMF");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmIroadmvCard, "IROADMV");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmItlbCard, "ITLB");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmItluCard, "ITLU");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmLcI1000Card, "LCI1000");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmMcs8x16Card, "MCS8-16");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmMesh4Card, "MESH4");
		oidCardTypeMap.put(M_tropicCommonEquipmentReg.aluWdmMultiFunctionCard, "MFC");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmMonOcmCard, "MON-OCM");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmMonOtdrCard, "MON-OTDR");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmMsh8fsmCard, "MSH8-FSM");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmMVACCard, "MVAC");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmMVAC8BCard, "MVAC8B");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmOpsaCard, "OPSA");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmOpsbCard, "OPSB");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmOsctCard, "OSCT");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmOtdrCard, "OTDR");
		oidCardTypeMap.put(M_tropicCommonEquipmentReg.aluWdmPSS8PowerFilterAcCard, "PF");
		// oidCardTypeMap.put( "TBD", "PF" );
		oidCardTypeMap.put(M_tropicCommonEquipmentReg.aluWdmPSS96PowerFilterCard, "PF");
		//oidCardTypeMap.put( M_tropicCommonEquipmentReg.aluWdmPSI1TPowerFilterCard, "PF");
		oidCardTypeMap.put(M_tropicCommonEquipmentReg.aluWdmPowerFilterCard, "PF");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmPsc1x6Card, "PSC1-6");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmPtpctlCard, "PTPCTL");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmPtpioCard, "PTPIO");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmPtpioctlCard, "PTPIOC");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmRa2pCard, "RA2P");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmRa5pCard, "RA5P");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmS13x100Card, "S13X100R");
		//oidCardTypeMap.put( M_tropicCardReg.aluWdmSwitchingCard, "SC16" );
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSwitchingCard, "SC96");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFC1ACard, "SFC1A");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFC1BCard, "SFC1B");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFC1CCard, "SFC1C");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFC1DCard, "SFC1D");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFC1ECard, "SFC1E");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFC1FCard, "SFC1F");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFC1GCard, "SFC1G");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFC1HCard, "SFC1H");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFC2ACard, "SFC2A");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFC2BCard, "SFC2B");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFC2CCard, "SFC2C");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFC2DCard, "SFC2D");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFC4ACard, "SFC4A");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFC4BCard, "SFC4B");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFC8Card, "SFC8");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD2ACard, "SFD2A");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD2BCard, "SFD2B");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD2CCard, "SFD2C");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD2DCard, "SFD2D");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD2ECard, "SFD2E");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD2FCard, "SFD2F");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD2GCard, "SFD2G");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD2HCard, "SFD2H");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD2ICard, "SFD2I");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD2LCard, "SFD2L");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD2MCard, "SFD2M");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD2NCard, "SFD2N");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD2OCard, "SFD2O");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD2PCard, "SFD2P");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD2QCard, "SFD2Q");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD2RCard, "SFD2R");

		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD4ACard, "SFD4A");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD4BCard, "SFD4B");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD4CCard, "SFD4C");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD4DCard, "SFD4D");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD4ECard, "SFD4E");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD4FCard, "SFD4F");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD4GCard, "SFD4G");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD4HCard, "SFD4H");

		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD5ACard, "SFD5A");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD5BCard, "SFD5B");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD5CCard, "SFD5C");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD5DCard, "SFD5D");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD5ECard, "SFD5E");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD5GCard, "SFD5F");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD5GCard, "SFD5G");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD5HCard, "SFD5H");

		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD8ACard, "SFD8A");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD8BCard, "SFD8B");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD8CCard, "SFD8C");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSFD8DCard, "SFD8D");

		oidCardTypeMap.put(M_tropicCardReg.aluWdmSfd40Card, "SFD40");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSfd40bCard, "SFD40B");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSfd44Card, "SFD44");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSfd44bCard, "SFD44B");
		oidCardTypeMap.put(M_tropicCommonEquipmentReg.aluWdmPSS8ShelfPanelCard, "SHFPNL");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmSVACCard, "SVAC");
		oidCardTypeMap.put(M_tropicCommonEquipmentReg.aluWdmUserInterfacePanelCard, "USRPNL");
		oidCardTypeMap.put(M_tropicCommonEquipmentReg.aluWdmPSS8UserInterfacePanelCard, "8USRPNL");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmVirtualCard, "Virtual/Preset");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmWr2c88Card, "WR2-88");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmWr20tfCard, "WR20-TF");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmWr20tfmCard, "WR20-TFM");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmWr8c88aCard, "WR8-88A");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmWr8c88afCard, "WR8-88AF");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmWtocmCard, "WTOCM");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmWtocmaCard, "WTOCMA");
		oidCardTypeMap.put(M_tropicCardReg.aluWdmWtocmfCard, "WTOCM-F");
	}

	public static String getShelfType(String oid) {
		if (oidShelfTypeMap.containsKey(oid)) {
			return oidShelfTypeMap.get(oid);
		}
		return oid;
	}

	public static String getSlotCardType(String oid) {
		if (oidCardTypeMap.containsKey(oid)) {
			return oidCardTypeMap.get(oid);
		}
		return oid;
	}
}