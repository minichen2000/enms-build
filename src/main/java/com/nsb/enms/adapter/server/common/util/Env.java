package com.nsb.enms.adapter.server.common.util;

import java.util.Map;
import com.nsb.enms.adapter.server.common.EnmsEnv;

public final class Env {

	public static void checkEnvironment(){
		Map<String, String> m = System.getenv();
		if (m.containsKey("ENMS_STUB")) {
			String stub = ((String) m.get("ENMS_STUB")).toLowerCase();
			if (stub.equals("true"))
				EnmsEnv.ENMS_STUB_MODE = true;
		}
		
	}
}
