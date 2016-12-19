package com.nsb.enms.adapter.server.sdh.action.method.ne;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.sdh.constants.SdhConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.sdh.action.entity.NeEntity;
import com.nsb.enms.adapter.server.sdh.action.method.ExecExternalScript;
import com.nsb.enms.adapter.server.sdh.constants.ExternalScriptType;
import com.nsb.enms.adapter.server.sdh.q3manager.Q3EmlImMgr;
import com.nsb.enms.common.ErrorCode;

public class CreateNe {
	private final static Logger log = LogManager.getLogger(CreateNe.class);

	private static String createNeScenario = ConfLoader.getInstance().getConf(SdhConfigKey.CREATE_NE_REQ,
			SdhConfigKey.DEFAULT_CREATE_NE_REQ);

	private static String setNeAddressScenario = ConfLoader.getInstance().getConf(SdhConfigKey.SET_NE_ADDR_REQ,
			SdhConfigKey.DEFAULT_SET_NE_ADDR_REQ);

	private static String setNeIsaAddressScenario = ConfLoader.getInstance().getConf(SdhConfigKey.SET_NE_ISA_ADDR_REQ,
			SdhConfigKey.DEFAULT_SET_NE_ISA_ADDR_REQ);

	private static Pattern pattern = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+");

	public static NeEntity createNe(Integer id, String neRelease, String neType, String userLabel, String locationName,
			String neAddress) throws AdapterException {
		// Pair<Integer, Integer> groupNeId =
		// Q3EmlImMgr.instance().getGroupNeId();
		// int groupId = groupNeId.getFirst();
		// int neId = groupNeId.getSecond();
		int groupId = Q3EmlImMgr.instance().getGroupId(id);
		log.debug("The groupId = " + groupId + ", neId = " + id);
		createNe(groupId, id, neRelease, neType, userLabel, locationName);
		setNeAddress(groupId, id, neAddress);
		return GetNe.getNe(groupId, id);
	}

	private static void createNe(int groupId, int neId, String neRelease, String neType, String userLabel,
			String locationName) throws AdapterException {
		log.debug("------------Start createNe-------------------");
		try {
			Process process = ExecExternalScript.run(ExternalScriptType.TSTMGR, createNeScenario,
					String.valueOf(groupId), String.valueOf(neId), neRelease, neType, userLabel, locationName);

			InputStream inputStream = process.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			boolean flag = false;
			while ((line = br.readLine()) != null) {
				if (line.contains("CreateReply received")) {
					flag = true;
				}
			}
			br.close();
			process.waitFor();
			if (!flag) {
				throw new AdapterException(ErrorCode.FAIL_CREATE_NE_BY_EMLIM);
			}
		} catch (Exception e) {
			log.error("createNe", e);
			throw new AdapterException(ErrorCode.FAIL_CREATE_NE_BY_EMLIM);
		}
		log.debug("------------End createNe-------------------");
	}

	private static void setNeAddress(int groupId, int neId, String neAddress) throws AdapterException {
		log.debug("------------Start setNeAddress-------------------");
		try {
			String scenario = setNeAddressScenario;
			if (pattern.matcher(neAddress).find()) {
				scenario = setNeIsaAddressScenario;
			}
			Process process = ExecExternalScript.run(ExternalScriptType.TSTMGR, scenario, String.valueOf(groupId),
					String.valueOf(neId), neAddress);
			InputStream inputStream = process.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			boolean flag = false;
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("SetReply received")) {
					flag = true;
				}
			}
			br.close();
			process.waitFor();
			if (!flag) {
				throw new AdapterException(ErrorCode.FAIL_SET_NE_ADDRESS_BY_EMLIM);
			}
		} catch (Exception e) {
			log.error("setNeAddress", e);
			throw new AdapterException(ErrorCode.FAIL_SET_NE_ADDRESS_BY_EMLIM);
		}
		log.debug("------------End setNeAddress-------------------");
	}
}
