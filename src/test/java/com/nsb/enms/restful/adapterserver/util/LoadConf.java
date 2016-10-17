package com.nsb.enms.restful.adapterserver.util;

import java.io.InputStream;
import java.util.Properties;

public class LoadConf {
	public static String ADP_IP = "";
	public static String ADP_PORT = "";

	public LoadConf() {
		try {
			InputStream is = getClass().getResourceAsStream("/conf.properties");
			Properties props = new Properties();
			props.load(is);
			is.close();

			// 读取特定属性
			String ip = props.getProperty("ADP_IP");
			ADP_IP = ip;
			String port = props.getProperty("ADP_PORT");
			ADP_PORT = port;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
