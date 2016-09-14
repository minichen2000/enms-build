package com.nsb.enms.adapter.server.action.method.tp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.adapter.server.common.ExternalScriptType;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.exception.AdapterExceptionType;

public class TerminateTug3ToTu12 {
	private static final Logger log = LogManager.getLogger(TerminateTug3ToTu12.class);

	private static final String SCENARIO = ConfLoader.getInstance().getConf(ConfigKey.TERMINATE_TUG3_TO_TU12_REQ,
			ConfigKey.DEFAULT_TERMINATE_TUG3_TO_TU12_REQ);

	public static void terminateTug3ToTu12(String groupId, String neId, String vc4TTPId) throws AdapterException {
		String[] tug3IdList = { "1", "2", "3" };
		for (int i = 0; i < tug3IdList.length; i++) {
			String tug3Id = tug3IdList[i];
			boolean isOk = terminateTug3ToTu12(groupId, neId, vc4TTPId, tug3Id);
			log.info("the result of executing terminateTug3ToTu12 is {}, vc4TTPId={}, tug3Id={}", isOk, vc4TTPId,
					tug3Id);
		}
	}

	private static boolean terminateTug3ToTu12(String groupId, String neId, String vc4TTPId, String tug3Id)
			throws AdapterException {
		boolean isOk = false;

		try {
			Process process = new ExecExternalScript().run(ExternalScriptType.TSTMGR, SCENARIO, groupId, neId, vc4TTPId,
					tug3Id);
			InputStream inputStream = process.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains(" ActionReply received")) {
					log.info("successed to terminateTug3ToTu12, vc4TTPId={}, tug3Id={}", vc4TTPId, tug3Id);
					isOk = true;
				}
			}
			br.close();

			if (process.waitFor() != 0) {
				throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, "terminateTug3ToTu12 failed!!!");
			}
			return isOk;
		} catch (Exception e) {
			log.error("terminateTug3ToTu12", e);
			throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage());
		}
	}
}
