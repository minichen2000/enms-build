package com.nsb.enms.adapter.server.wdm.constants;

public enum SnmpTpType {
	PHN(1), DEFAULT(2), UNKNOWN(3), OC3(4), OC12(5), OC48(6), OC192(7), OTS(8), OCH(9), OTU1(10),
	OTU2(11), GIGE(12), TENGIGE(13), STM1(14), STM4(15), STM16(16), STM64(17), FC1G(18), FC2G(19), FC4G(20),
	FC10G(21), CBR2G5(22), CBR10G(23), ANYRATE(24), HDSDI(25), FE(26), FDDI(27), ESCON(28), DVBASI(29), DVI6000(30),
	OTU3(31), OC768(32), STM256(33), OTU4(34), FC8G(35), HUNDREDGIGE(36), SDSDI(37), E1(38), SDI3G(39), DCN(40), 
	EVOA(41), FEE(42), ODUPTF(43), DS1(44), OTU3E2(45), OTU2E(46), FORTYGIGE(47), SDR(48), DDR(49), TOD(50), 
	LAGGROUP(51), OTL44(52), FC16G(53), QDR(54), BITS(55), ONETRU(56), OTU4X2(57), OTU1F(58), CBR10G3(59), FORTYGIGEMLD(60),
	INTERLAKEN(61), OTL410(62), OTU4X4(63), OTU4HALF(64), OTU4HALFX5(65), SENSOR(66);
	
	private int code;

	private SnmpTpType(int code) {
		this.code = code;
	}

	public static String getTpType(String code) {
		for (SnmpTpType type : SnmpTpType.values()) {
			if (type.code == Integer.valueOf(code)) {
				return type.name();
			}
		}
		return UNKNOWN.name();
	}
	
	public static void main(String args[]) {
		boolean isOk = "PHN".equals(SnmpTpType.PHN);
		System.out.println(isOk);
	}
}
