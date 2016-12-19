package com.nsb.enms.adapter.server.sdh.action.method;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.constants.EnmsEnv;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.sdh.constants.SdhConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.constants.AdapterExceptionType;
import com.nsb.enms.adapter.server.common.utils.Env;
import com.nsb.enms.adapter.server.sdh.constants.ExternalScriptType;

public class ExecExternalScript {
	private static final Logger log = LogManager.getLogger(ExecExternalScript.class);

	private static String tstmgrScript = ConfLoader.getInstance().getConf(SdhConfigKey.TSTMGR_SCRIPT,
			SdhConfigKey.DEFAULT_TSTMGR_SCRIPT);

	/*
	 * private static String q3EmlImScript = ConfLoader.getInstance().getConf(
	 * ConfigKey.START_Q3_EMLIM_SCRIPT, ConfigKey.DEFAULT_START_Q3_EMLIM_SCRIPT
	 * );
	 */

	private static String tstmgrDir = ConfLoader.getInstance().getConf(SdhConfigKey.TSTMGR_SCRIPT_DIR,
			SdhConfigKey.DEFAULT_TSTMGR_SCRIPT_DIR);

	/*
	 * private static String emlImDir = ConfLoader.getInstance().getConf(
	 * ConfigKey.Q3_EMLIM_SCRIPT_DIR, ConfigKey.DEFAULT_Q3_EMLIM_SCRIPT_DIR );
	 * 
	 * private static String killEmlImScript = ConfLoader.getInstance().getConf(
	 * ConfigKey.KILL_EMLIM_SCRIPT, ConfigKey.DEFAULT_KILL_EMLIM_SCRIPT );
	 */

	private static File fileDir;

	public static Process run(ExternalScriptType scriptType, String... params) throws AdapterException {
		Env.checkEnvironment();
		if (EnmsEnv.ENMS_STUB_MODE) {
			return run4Stub(scriptType, params);
		} else {
			return run4Normal(scriptType, params);
		}
	}

	private static Process run4Normal(ExternalScriptType scriptType, String... params) throws AdapterException {
		String[] cmdArray = new String[params.length + 1];
		switch (scriptType) {
		case TSTMGR:
			fileDir = new File(tstmgrDir);
			cmdArray[0] = tstmgrScript;
			break;
		/*
		 * case EMLIM: fileDir = new File( emlImDir ); cmdArray[0] =
		 * q3EmlImScript; break; case KILL_EMLIM: fileDir = new File( tstmgrDir
		 * ); cmdArray[0] = killEmlImScript; break;
		 */
		default:
			break;
		}

		System.arraycopy(params, 0, cmdArray, 1, params.length);
		try {
			log.debug("exec:" + Arrays.asList(cmdArray));
			return Runtime.getRuntime().exec(cmdArray, null, fileDir);
		} catch (IOException e) {
			log.error("execExternalScript", e);
			throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage());
		}
	}

	private static Process run4Stub(ExternalScriptType scriptType, String... params) throws AdapterException {
		String[] cmdArray = new String[3];
		switch (scriptType) {
		case TSTMGR:
			cmdArray[0] = "python";
			cmdArray[1] = "emlim.py";
			break;
		default:
			break;
		}

		String cmd = params[0];
		log.debug("cmd:" + cmd);
		if (cmd.endsWith("syncCTP.req")) {
			String objectClass = params[1];
			if (objectClass.equals("0.0.7.774.127.7.0.3.4")) {
				cmdArray[2] = cmd.replace("syncCTP", "getRsCtp");
			} else if (objectClass.equals("0.0.7.774.0.3.40")) {
				cmdArray[2] = cmd.replace("syncCTP", "getMsCtp");
			} else if (objectClass.equals("0.0.7.774.0.3.25")) {
				cmdArray[2] = cmd.replace("syncCTP", "getUnProtectedCtp");
			} else if (objectClass.equals("0.0.7.774.127.3.0.3.2")) {
				cmdArray[2] = cmd.replace("syncCTP", "getAu4Ctp");
			} else if (objectClass.equals("1.3.12.2.1006.54.0.0.3.210")) {
				cmdArray[2] = cmd.replace("syncCTP", "getEPdhCtp");
			} else if (objectClass.equals("0.0.7.774.127.7.0.3.10")) {
				cmdArray[2] = cmd.replace("syncCTP", "getVc12Ttp");
			} else if (objectClass.equals("0.0.7.774.0.3.101")) {
				cmdArray[2] = cmd.replace("syncCTP", "getVc3Ttp");
			} else if (objectClass.equals("0.0.7.774.127.2.0.3.25")) {
				cmdArray[2] = cmd.replace("syncCTP", "getTu12Ctp");
			}
		} else {
			cmdArray[2] = cmd;
		}

		try {
			File stubPath = new File(ConfLoader.getInstance().getConf("EMLIM_STUB_PATH", ""));
			return Runtime.getRuntime().exec(cmdArray, null, stubPath);
		} catch (IOException e) {
			log.error("execExternalScript", e);
			throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage());
		}
	}

	public static void main(String args[]) {
		String os = System.getProperty("os.name");
		System.out.println(os);
		String cmdArray[] = { "python", "emlim.py", "/opt/nms/EML_1/TSTMGR/scenario/CreateNe.req" };
		File stubPath = new File("D:\\workspace6\\eNMS\\enms-test\\stub\\emlim.stub");
		try {
			Runtime.getRuntime().exec(cmdArray, null, stubPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
