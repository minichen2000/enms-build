package com.nsb.enms.adapter.server.common.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.nsb.enms.adapter.server.common.constants.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.constants.AdapterExceptionType;

public class ConfLoader {
	private Properties conf = new Properties();

	private static ConfLoader confLoader = new ConfLoader();

	private ConfLoader() {
	}

	public static ConfLoader getInstance() {
		return confLoader;
	}

	public void loadConf(String confFile) throws AdapterException {
		loadOneConfFile(confFile);
		loadReferenceConfFiles(new File(confFile).getParentFile());
	}

	public String getConf(String name) throws AdapterException {
		String value = conf.getProperty(name);
		if (value == null)
			throw new AdapterException(AdapterExceptionType.EXCPT_CONF_NOT_FOUND,
					"No such configuration: '" + name + "'");
		return value.trim();
	}

	public String getConf(String name, String defaultValue) {
		return conf.getProperty(name, defaultValue);
	}

	public boolean containsKey(String name) {
		return conf.containsKey(name);
	}

	public int getInt(String name) throws AdapterException {
		String val = getConf(name);
		try {
			return Integer.parseInt(val);
		} catch (NumberFormatException e) {
			throw new AdapterException(AdapterExceptionType.EXCPT_CONF_NOT_FOUND,
					"Illegal int format: '" + val + "' for: " + name, e);
		}
	}

	public int getInt(String name, int defaultValue) {
		try {
			return getInt(name);
		} catch (AdapterException e) {
			return defaultValue;
		}
	}

	public boolean getBoolean(String name) throws AdapterException {
		String value = getConf(name);
		if ("TRUE".equalsIgnoreCase(value))
			return true;
		if ("FALSE".equalsIgnoreCase(value))
			return false;
		throw new AdapterException(AdapterExceptionType.EXCPT_CONF_NOT_FOUND,
				"Illegal boolean format: '" + value + "' for: " + name);
	}

	public boolean getBoolean(String name, boolean defaultValue) {
		try {
			return getBoolean(name);
		} catch (AdapterException e) {
			return defaultValue;
		}
	}

	private void loadOneConfFile(String file) throws AdapterException {
		try {
			FileInputStream fin = new FileInputStream(file);
			conf.load(fin);
			fin.close();
		} catch (IOException e) {
			throw new AdapterException(AdapterExceptionType.EXCPT_IO_ERROR, e.getMessage(), e);
		}
	}

	private void loadReferenceConfFiles(File dir) throws AdapterException {
		String referenceConfFiles = null;
		try {
			referenceConfFiles = getConf(ConfigKey.REFERENCE_CONF_FILES);
		} catch (AdapterException e) {
			return;
		}
		String[] files = referenceConfFiles.split("\\s*,\\s*");
		for (String confFile : files) {
			loadOneConfFile(new File(dir, confFile).getAbsolutePath());
		}
	}
}