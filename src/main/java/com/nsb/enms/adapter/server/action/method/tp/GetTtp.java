package com.nsb.enms.adapter.server.action.method.tp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.TpEntity;
import com.nsb.enms.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.adapter.server.common.ExternalScriptType;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.exception.AdapterExceptionType;
import com.nsb.enms.adapter.server.common.util.ParseUtils;

public class GetTtp {
	private static final Logger log = LogManager.getLogger(GetTtp.class);

	private static final String SCENARIO = ConfLoader.getInstance().getConf(ConfigKey.GET_T_PORT_REQ,
			ConfigKey.DEFAULT_GET_T_PORT_REQ);

	public static List<TpEntity> getVc4Ttp(String groupId, String neId) throws AdapterException {
		List<TpEntity> tpList = new ArrayList<TpEntity>();

		try {
			String scope = "firstLevelOnly";
			String vc4TTPId = "0.0.7.774.0.7.42";
			String filterParam = vc4TTPId;

			Process process = new ExecExternalScript().run(ExternalScriptType.TSTMGR, SCENARIO, groupId, neId, scope,
					filterParam);
			InputStream inputStream = process.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("GetReply received")) {
					TpEntity portEntity = new TpEntity();
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (line.startsWith("managedObjectClass")) {
							String moc = ParseUtils.parseAttrWithSingleValue(line);
							portEntity.setMoc(moc);
							continue;
						}
						if (line.startsWith("managedObjectInstance")) {
							String moi = ParseUtils.parseAttrWithMultiValue(line);
							portEntity.setMoi(moi);
							continue;
						}

						if (line.startsWith("alarmStatus")) {
							portEntity.setAlarmStatus(ParseUtils.parseAttr(line));
							continue;
						}

						if (line.startsWith("supportedByObjectList")) {
							portEntity.setSupportedByObjectList(ParseUtils.parseList(line));
							continue;
						}

						if (line.startsWith("operationalState")) {
							portEntity.setOperationalState(ParseUtils.parseAttr(line));
							continue;
						}

						if (line.startsWith("-----------------")) {
							tpList.add(portEntity);
							break;
						}
					}
				}
			}
			br.close();

			if (process.waitFor() != 0) {
				throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, "Get VC4TTP failed!!!");
			}
			return tpList;
		} catch (Exception e) {
			log.error("getVc4Ttp", e);
			throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage());
		}
	}
}
