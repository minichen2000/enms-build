package com.nsb.enms.adapter.server.sdh.action.method.xc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.ExternalScriptType;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.sdh.action.method.ExecExternalScript;
import com.nsb.enms.common.ErrorCode;

public class DeleteXc {
	private static final Logger log = LogManager.getLogger(DeleteXc.class);

	private static final String SCENARIO = ConfLoader.getInstance().getConf(ConfigKey.DELETE_XC_REQ,
			ConfigKey.DEFAULT_DELETE_XC_REQ);

	public static void deleteXc(String groupId, String neId, String corssConnectionId) throws AdapterException {
		log.debug("------------Start deleteXc-------------------");
		try {
			Process process = ExecExternalScript.run(ExternalScriptType.TSTMGR, SCENARIO, groupId, neId,
					corssConnectionId);
			InputStream inputStream = process.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			String line;
			boolean flag = false;
			while ((line = br.readLine()) != null) {
				if (line.indexOf("DeleteReply received") >= 0) {
					continue;
				}
				if (line.indexOf("Ok") >= 0) {
					flag = true;
				}
			}
			br.close();
			process.waitFor();
			if (!flag) {
				log.error("Delete XC failed!!!");
				throw new AdapterException(ErrorCode.FAIL_DELETE_XC_BY_EMLIM);
			}
			log.debug("------------End deleteXc-------------------");
		} catch (Exception e) {
			log.error("deleteXc", e);
			throw new AdapterException(ErrorCode.FAIL_DELETE_XC_BY_EMLIM);
		}
	}
}
