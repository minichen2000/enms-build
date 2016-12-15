package com.nsb.enms.adapter.server.sdh.action.method.xc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.ParseUtil;
import com.nsb.enms.adapter.server.sdh.action.entity.XcEntity;
import com.nsb.enms.adapter.server.sdh.action.method.ExecExternalScript;
import com.nsb.enms.adapter.server.sdh.constants.ExternalScriptType;
import com.nsb.enms.common.ErrorCode;

public class CreateXc {
	private static final Logger log = LogManager.getLogger(CreateXc.class);

	private static final String SCENARIO_VC4 = ConfLoader.getInstance().getConf(ConfigKey.CREATE_XC_VC4_REQ,
			ConfigKey.DEFAULT_CREATE_XC_VC4_REQ);

	private static final String SCENARIO_VC3 = ConfLoader.getInstance().getConf(ConfigKey.CREATE_XC_VC3_REQ,
			ConfigKey.DEFAULT_CREATE_XC_VC3_REQ);

	private static final String SCENARIO_VC12 = ConfLoader.getInstance().getConf(ConfigKey.CREATE_XC_VC12_REQ,
			ConfigKey.DEFAULT_CREATE_XC_VC12_REQ);

	private static final String SCENARIO_TU12 = ConfLoader.getInstance().getConf(ConfigKey.CREATE_XC_TU12_REQ,
			ConfigKey.DEFAULT_CREATE_XC_TU12_REQ);

	private static final String SCENARIO_TU3 = ConfLoader.getInstance().getConf(ConfigKey.CREATE_XC_TU3_REQ,
			ConfigKey.DEFAULT_CREATE_XC_TU3_REQ);

	public static XcEntity createXcVc4(String groupId, String neId, String pTTPId, String augId, String au4CTPId)
			throws AdapterException {
		log.debug("------------Start createXcVc4-------------------");
		try {
			Process process = ExecExternalScript.run(ExternalScriptType.TSTMGR, SCENARIO_VC4, groupId, neId, pTTPId,
					augId, au4CTPId);
			XcEntity xcEntity = handleInputStream(process);

			if (process.waitFor() != 0) {
				throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_EMLIM);
			}
			log.debug("------------End createXcVc4-------------------");
			return xcEntity;

		} catch (Exception e) {
			log.error("createXcVc4", e);
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_EMLIM);
		}
	}

	public static XcEntity createXcVc12(String groupId, String neId, String vc4TtpId, String tug3Id, String tug2Id,
			String tu12CtpId, String vc12TtpId) throws AdapterException {
		log.debug("------------Start createXcVc12-------------------");
		try {
			Process process = ExecExternalScript.run(ExternalScriptType.TSTMGR, SCENARIO_VC12, groupId, neId, vc4TtpId,
					tug3Id, tug2Id, tu12CtpId, vc12TtpId);
			XcEntity xcEntity = handleInputStream(process);

			if (process.waitFor() != 0) {
				throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_EMLIM);
			}
			log.debug("------------End createXcVc12-------------------");
			return xcEntity;

		} catch (Exception e) {
			log.error("createXcVc12", e);
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_EMLIM);
		}
	}

	public static XcEntity createXcVc3(String groupId, String neId, String vc4TtpId, String tug3Id, String tu3CtpId,
			String vc3TtpId) throws AdapterException {
		log.debug("------------Start createXcVc3-------------------");
		try {
			Process process = ExecExternalScript.run(ExternalScriptType.TSTMGR, SCENARIO_VC3, groupId, neId, vc4TtpId,
					tug3Id, tu3CtpId, vc3TtpId);
			XcEntity xcEntity = handleInputStream(process);

			if (process.waitFor() != 0) {
				throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_EMLIM);
			}
			log.debug("------------End createXcVc3-------------------");
			return xcEntity;

		} catch (Exception e) {
			log.error("createXcVc3", e);
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_EMLIM);
		}
	}

	public static XcEntity createXcTu12(String groupId, String neId, String a_vc4TtpId, String a_tug3Id,
			String a_tug2Id, String a_tu12CtpId, String z_vc4TtpId, String z_tug3Id, String z_tug2Id,
			String z_tu12CtpId) throws AdapterException {
		log.debug("------------Start createXcTu12-------------------");
		try {
			Process process = ExecExternalScript.run(ExternalScriptType.TSTMGR, SCENARIO_TU12, groupId, neId,
					a_vc4TtpId, a_tug3Id, a_tug2Id, a_tu12CtpId, z_vc4TtpId, z_tug3Id, z_tug2Id, z_tu12CtpId);
			XcEntity xcEntity = handleInputStream(process);

			if (process.waitFor() != 0) {
				throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_EMLIM);
			}
			log.debug("------------End createXcTu12-------------------");
			return xcEntity;

		} catch (Exception e) {
			log.error("createXcTu12", e);
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_EMLIM);
		}
	}

	public static XcEntity createXcTu3(String groupId, String neId, String a_vc4TtpId, String a_tug3Id,
			String a_tu3CtpId, String z_vc4TtpId, String z_tug3Id, String z_tu3CtpId) throws AdapterException {
		log.debug("------------Start createXcTu3-------------------");
		try {
			Process process = ExecExternalScript.run(ExternalScriptType.TSTMGR, SCENARIO_TU3, groupId, neId, a_vc4TtpId,
					a_tug3Id, a_tu3CtpId, z_vc4TtpId, z_tug3Id, z_tu3CtpId);
			XcEntity xcEntity = handleInputStream(process);

			if (process.waitFor() != 0) {
				throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_EMLIM);
			}
			log.debug("------------End createXcTu3-------------------");
			return xcEntity;

		} catch (Exception e) {
			log.error("createXcTu3", e);
			throw new AdapterException(ErrorCode.FAIL_CREATE_XC_BY_EMLIM);
		}
	}

	private static XcEntity handleInputStream(Process process) throws IOException {
		InputStream inputStream = process.getInputStream();
		XcEntity xcEntity = new XcEntity();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.indexOf("ActionReply received") >= 0) {
				while ((line = br.readLine()) != null) {
					line = line.trim();
					if (line.startsWith("managedObjectClass")) {
						String moc = ParseUtil.parseAttrWithSingleValue(line);
						xcEntity.setMoc(moc);
						continue;
					}

					if (line.startsWith("managedObjectInstance")) {
						log.debug("line = " + line);
						String moi = ParseUtil.parseAttrWithMultiValue(line);
						xcEntity.setMoi(moi);
						continue;
					}

					if (line.startsWith("connected pointToPoint")) {
						log.debug("line = " + line);
						// connected pointToPoint { fromTp { {
						// {protectedTTPId=11060101 } }, { {augId=7 } }, {
						// {au4CTPId=1 } } }, toTp { { {vc4TTPId=11060901 }
						// } }, xCon { { {fabricId=1 } }, {
						// {crossConnectionId=6001 } } } }

						line = line.replaceAll(StringUtils.SPACE, StringUtils.EMPTY);
						line = line.replaceAll("connected pointToPoint", StringUtils.EMPTY);
						String fromTp = line.substring(line.indexOf("fromTp") + 6, line.indexOf("toTp") - 1);
						fromTp = ParseUtil.parseMultiValueWithNoBlank(fromTp);
						log.debug("fromTp=" + fromTp);
						xcEntity.setFromTermination(fromTp);

						String toTp = line.substring(line.indexOf("toTp") + 4, line.indexOf("xCon") - 1);
						toTp = ParseUtil.parseMultiValueWithNoBlank(toTp);
						log.debug("toTp=" + toTp);
						xcEntity.setToTermination(toTp);

						String crossConnectionId = line.substring(line.indexOf("{{fabricId=1}},{{") + 17,
								line.length() - 4);
						String moi = xcEntity.getMoi();
						moi = moi + "/" + crossConnectionId;
						log.debug("moi=" + moi);
						xcEntity.setMoi(moi);

						continue;
					}

					if (line.startsWith("-----------------")) {
						break;
					}
				}
			}
		}
		br.close();
		return xcEntity;
	}

	public static void main(String args[]) {
		String s = "connected pointToPoint { fromTp { { {vc4TTPId=11060301 } }, { {tug3Id=1 } }, { {tug2Id=2 } }, { {tu12CTPId=1 } } }, toTp { { {vc12TTPId=11195801 } } }, xCon { { {fabricId=1 } }, { {crossConnectionId=72058 } } } }";
		s = s.replaceAll(" ", "");
		System.out.println(s);

		s = s.replaceAll("connected pointToPoint", "");
		String fromTp = s.substring(s.indexOf("fromTp") + 6, s.indexOf("toTp") - 1);
		fromTp = ParseUtil.parseMultiValueWithNoBlank(fromTp);
		System.out.println("fromTp=" + fromTp);

		String toTp = s.substring(s.indexOf("toTp") + 4, s.indexOf("xCon") - 1);
		toTp = ParseUtil.parseMultiValueWithNoBlank(toTp);
		System.out.println("toTp=" + toTp);
		System.out.println("s=" + s);
		String crossConnectionId = s.substring(s.indexOf("{{fabricId=1}},{{") + 17, s.length() - 4);
		System.out.println(crossConnectionId);
	}
}