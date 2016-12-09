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
import com.nsb.enms.common.ErrorCode;

public class TerminateTp {
	private static final Logger log = LogManager.getLogger(TerminateTp.class);

	private static final String SCENARIO_TU12 = ConfLoader.getInstance().getConf(ConfigKey.TERMINATE_TUG3_TO_TU12_REQ,
			ConfigKey.DEFAULT_TERMINATE_TUG3_TO_TU12_REQ);

	private static final String SCENARIO_TU3 = ConfLoader.getInstance().getConf(ConfigKey.TERMINATE_TUG3_TO_TU3_REQ,
			ConfigKey.DEFAULT_TERMINATE_TUG3_TO_TU3_REQ);

	public static void terminateTug3ToTu12(String groupId, String neId, String vc4TTPId) throws AdapterException {
		String[] tug3IdList = { "1", "2", "3" };
		for (int i = 0; i < tug3IdList.length; i++) {
			String tug3Id = tug3IdList[i];
			boolean isOk = terminateTug3ToTu12(groupId, neId, vc4TTPId, tug3Id);
			log.info("the result of executing terminateTug3ToTu12 is {}, vc4TTPId={}, tug3Id={}", isOk, vc4TTPId,
					tug3Id);
		}
	}

	public static boolean terminateTug3ToTu12(String groupId, String neId, String vc4TTPId, String tug3Id)
			throws AdapterException {
	    log.debug( "------------Start terminateTug3ToTu12-------------------" );
	    boolean isOk = false;
		try {
			Process process = ExecExternalScript.run(ExternalScriptType.TSTMGR, SCENARIO_TU12, groupId, neId, vc4TTPId,
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
				log.error("terminateTug3ToTu12 failed!!!");
				throw new AdapterException(ErrorCode.FAIL_TERMINATE_TP_BY_EMLIM);
			}
			log.debug( "------------End terminateTug3ToTu12-------------------" );
			return isOk;
		} catch (Exception e) {
			log.error("terminateTug3ToTu12", e);
			throw new AdapterException(ErrorCode.FAIL_TERMINATE_TP_BY_EMLIM);
		}
	}

	public static boolean terminateTug3ToTu3(String groupId, String neId, String vc4TTPId, String tug3Id)
			throws AdapterException {
	    log.debug( "------------Start terminateTug3ToTu3-------------------" );
	    boolean isOk = false;
		try {
			Process process = ExecExternalScript.run(ExternalScriptType.TSTMGR, SCENARIO_TU3, groupId, neId, vc4TTPId,
					tug3Id);
			InputStream inputStream = process.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains(" ActionReply received")) {
					log.info("successed to terminateTug3ToTu3, vc4TTPId={}, tug3Id={}", vc4TTPId, tug3Id);
					isOk = true;
				}
			}
			br.close();

			if (process.waitFor() != 0) {
				log.error("terminateTug3ToTu3 failed!!!");
				throw new AdapterException(ErrorCode.FAIL_TERMINATE_TP_BY_EMLIM);
			}
			log.debug( "------------End terminateTug3ToTu3-------------------" );
			return isOk;
		} catch (Exception e) {
			log.error("terminateTug3ToTu3", e);
			throw new AdapterException(ErrorCode.FAIL_TERMINATE_TP_BY_EMLIM);
		}
	}
}
