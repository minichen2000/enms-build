package com.nsb.enms.adapter.server.sdh.action.method.xc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.sdh.constants.SdhConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.ParseUtil;
import com.nsb.enms.adapter.server.sdh.action.entity.XcEntity;
import com.nsb.enms.adapter.server.sdh.action.method.ExecExternalScript;
import com.nsb.enms.adapter.server.sdh.constants.ExternalScriptType;
import com.nsb.enms.common.ErrorCode;

public class GetXc {
	private static final Logger log = LogManager.getLogger(GetXc.class);

	private static final String SCENARIO = ConfLoader.getInstance().getConf(SdhConfigKey.GET_XC_REQ,
			SdhConfigKey.DEFAULT_XC_GET_REQ);

	public static List<XcEntity> getXcs(int groupId, int neId) throws AdapterException {
		log.debug("------------Start getXcs-------------------");
		try {
			Process process = ExecExternalScript.run(ExternalScriptType.TSTMGR, SCENARIO, String.valueOf(groupId),
					String.valueOf(neId));
			InputStream inputStream = process.getInputStream();
			List<XcEntity> xcList = new LinkedList<XcEntity>();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.indexOf("GetReply received") >= 0) {
					XcEntity xcEntity = new XcEntity();
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (line.startsWith("managedObjectClass")) {
							String moc = ParseUtil.parseAttrWithSingleValue(line);
							xcEntity.setMoc(moc);
							continue;
						}

						if (line.startsWith("managedObjectInstance")) {
							String moi = ParseUtil.parseAttrWithMultiValue(line);
							xcEntity.setMoi(moi);
							continue;
						}

						if (line.startsWith("directionality")) {
							xcEntity.setDirectionality(ParseUtil.parseAttr(line));
							continue;
						}

						if (line.startsWith("toTermination")) {
							xcEntity.setToTermination(ParseUtil.parseAttrWithMultiValue(line));
							continue;
						}

						if (line.startsWith("fromTermination")) {
							xcEntity.setFromTermination(ParseUtil.parseAttrWithMultiValue(line));
							continue;
						}

						if (line.startsWith("signalType")) {
							xcEntity.setSignalType(ParseUtil.parseAttr(line));
							continue;
						}

						if (line.startsWith("operationalState")) {
							xcEntity.setOperationalState(ParseUtil.parseAttr(line));
							continue;
						}

						if (line.startsWith("administrativeState")) {
							xcEntity.setAdministrativeState(ParseUtil.parseAttr(line));
							continue;
						}

						if (line.startsWith("-----------------")) {
							xcList.add(xcEntity);
							break;
						}
					}
				}
			}
			br.close();

			if (process.waitFor() != 0) {
				log.error("Get xc failed!!!");
				throw new AdapterException(ErrorCode.FAIL_GET_XC_BY_EMLIM);
			}
			log.debug("------------End getXcs-------------------");
			return xcList;

		} catch (Exception e) {
			log.error("getXcs", e);
			throw new AdapterException(ErrorCode.FAIL_GET_XC_BY_EMLIM);
		}
	}
}