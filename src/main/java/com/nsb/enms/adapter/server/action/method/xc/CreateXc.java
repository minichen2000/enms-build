package com.nsb.enms.adapter.server.action.method.xc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.XcEntity;
import com.nsb.enms.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.adapter.server.common.ExternalScriptType;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.exception.AdapterExceptionType;
import com.nsb.enms.adapter.server.common.util.ParseUtils;

public class CreateXc {
	private static final Logger log = LogManager.getLogger(CreateXc.class);

	private static final String SCENARIO_VC4 = ConfLoader.getInstance().getConf(ConfigKey.CREATE_XC_VC4_REQ,
			ConfigKey.DEFAULT_CREATE_XC_VC4_REQ);

	private static final String SCENARIO_VC12 = ConfLoader.getInstance().getConf(ConfigKey.CREATE_XC_VC12_REQ,
			ConfigKey.DEFAULT_CREATE_XC_VC12_REQ);

	public static XcEntity createXcVc4(String groupId, String neId, String pTTPId, String augId, String au4CTPId)
			throws AdapterException {
		try {
			Process process = new ExecExternalScript().run(ExternalScriptType.TSTMGR, SCENARIO_VC4, groupId, neId,
					pTTPId, augId, au4CTPId);
			InputStream inputStream = process.getInputStream();
			XcEntity xcEntity = new XcEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.indexOf("ActionReply received") >= 0) {
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (line.startsWith("managedObjectClass")) {
							String moc = ParseUtils.parseAttrWithSingleValue(line);
							xcEntity.setMoc(moc);
							continue;
						}

						if (line.startsWith("managedObjectInstance")) {
							String moi = ParseUtils.parseAttrWithMultiValue(line);
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
							fromTp = ParseUtils.parseMultiValueWithNoBlank(fromTp);
							log.debug("fromTp=" + fromTp);
							xcEntity.setFromTermination(fromTp);

							String toTp = line.substring(line.indexOf("toTp") + 4, line.indexOf("xCon") - 1);
							toTp = ParseUtils.parseMultiValueWithNoBlank(toTp);
							log.debug("toTp=" + toTp);

							xcEntity.setToTermination(toTp);
							continue;
						}

						if (line.startsWith("-----------------")) {
							break;
						}
					}
				}
			}
			br.close();

			if (process.waitFor() != 0) {
				throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, "create xc failed!!!");
			}
			return xcEntity;

		} catch (Exception e) {
			log.error("createXcVc4", e);
			throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage());
		}
	}

	public static XcEntity createXcVc12(String groupId, String neId, String vc4TtpId, String tug3Id, String tug2Id,
			String tu12CtpId, String vc12TtpId) throws AdapterException {
		try {
			Process process = new ExecExternalScript().run(ExternalScriptType.TSTMGR, SCENARIO_VC12, groupId, neId,
					vc4TtpId, tug3Id, tug2Id, tu12CtpId, vc12TtpId);
			InputStream inputStream = process.getInputStream();
			XcEntity xcEntity = new XcEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.indexOf("ActionReply received") >= 0) {
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (line.startsWith("managedObjectClass")) {
							String moc = ParseUtils.parseAttrWithSingleValue(line);
							xcEntity.setMoc(moc);
							continue;
						}

						if (line.startsWith("managedObjectInstance")) {
							String moi = ParseUtils.parseAttrWithMultiValue(line);
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
							fromTp = ParseUtils.parseMultiValueWithNoBlank(fromTp);
							log.debug("fromTp=" + fromTp);
							xcEntity.setFromTermination(fromTp);

							String toTp = line.substring(line.indexOf("toTp") + 4, line.indexOf("xCon") - 1);
							toTp = ParseUtils.parseMultiValueWithNoBlank(toTp);
							log.debug("toTp=" + toTp);

							xcEntity.setToTermination(toTp);
							continue;
						}

						if (line.startsWith("-----------------")) {
							break;
						}
					}
				}
			}
			br.close();

			if (process.waitFor() != 0) {
				throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, "create xc failed!!!");
			}
			return xcEntity;

		} catch (Exception e) {
			log.error("createXcVc12", e);
			throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage());
		}
	}

	public static void main(String args[]) {
		String s = "connected pointToPoint { fromTp { {{protectedTTPId=11060101 } },{ {augId=7 } },{ {au4CTPId=1 } } },toTp{{{vc4TTPId=11060901}}},xCon{{{fabricId=1}},{{crossConnectionId=6001}}}}}}";
		s = s.replaceAll(" ", "");
		System.out.println(s);

		s = s.replaceAll("connected pointToPoint", "");
		String fromTp = s.substring(s.indexOf("fromTp") + 6, s.indexOf("toTp") - 1);
		fromTp = ParseUtils.parseMultiValueWithNoBlank(fromTp);
		System.out.println("fromTp=" + fromTp);

		String toTp = s.substring(s.indexOf("toTp") + 4, s.indexOf("xCon") - 1);
		toTp = ParseUtils.parseMultiValueWithNoBlank(toTp);
		System.out.println("toTp=" + toTp);
	}
}