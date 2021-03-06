package com.nsb.enms.adapter.server.sdh.action.method.ne;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.sdh.constants.SdhConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.constants.AdapterExceptionType;
import com.nsb.enms.adapter.server.common.utils.ParseUtil;
import com.nsb.enms.adapter.server.sdh.action.method.ExecExternalScript;
import com.nsb.enms.adapter.server.sdh.constants.ExternalScriptType;
import com.nsb.enms.common.utils.Pair;

public class GetManagerAddress {
	private static final Logger log = LogManager.getLogger(GetNe.class);

	private static final String SCENARIO = ConfLoader.getInstance().getConf(SdhConfigKey.GET_MANAGER_ADDRESS_REQ,
			SdhConfigKey.DEFAULT_GET_MANAGER_ADDRESS_REQ);

	public static Pair<String, String> getManagerAddress(int groupId, int neId) throws AdapterException {
		log.debug("------------Start getManagerAddress-------------------");
		try {
			Process process = ExecExternalScript.run(ExternalScriptType.TSTMGR, SCENARIO, String.valueOf(groupId),
					String.valueOf(neId));
			InputStream inputStream = process.getInputStream();
			Pair<String, String> pair = new Pair<String, String>();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.contains("GetReply received")) {
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (line.startsWith("osMainAddress")) {
							String osMainAddress = ParseUtil.parseAttr(line);
							pair.setFirst(osMainAddress);
							continue;
						}

						if (line.startsWith("osSpareAddress")) {
							String osSpareAddress = ParseUtil.parseAttr(line);
							pair.setSecond(osSpareAddress);
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
				throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, "Get manager address failed!!!");
			}
			log.debug("------------End getManagerAddress-------------------");
			return pair;
		} catch (Exception e) {
			log.error("getManagerAddress", e);
			throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage());
		}
	}
}
