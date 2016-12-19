package com.nsb.enms.adapter.server.sdh.q3gateway.entity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.constants.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;

public class Moc {
	private static final Logger log = LogManager.getLogger(Moc.class);

	private String moc;

	private static boolean isInit = false;

	private static Map<String, String> mocOidMap = new HashMap<String, String>();

	public Moc(String moc) {
		this.moc = moc;
	}

	public Moc() {

	}

	public String getMoc() {
		return moc;
	}

	public void setMoc(String moc) {
		this.moc = moc;
	}

	@Override
	public String toString() {
		return moc;
	}

	public String getOid(String moc) {
		if (!isInit) {
			init();
		}

		if (mocOidMap.containsKey(moc)) {
			return mocOidMap.get(moc);
		}

		return null;
	}

	private void init() {
		try {
			isInit = true;

			String file = ConfLoader.getInstance().getConf(ConfigKey.OID_TABLE_FILE);

			Scanner scanner = new Scanner(new File(file));

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] mapStr = line.split(":");

				if (mapStr.length != 4) {
					log.debug(line + " is invalid ");
					continue;
				}
				String moc = mapStr[0].trim();
				String type = mapStr[1].trim();
				String oid = mapStr[3].trim();

				if (moc.startsWith("#")) {
					log.debug(line + " is comment ");
					continue;
				}

				if ("Moc".equals(type)) {
					mocOidMap.put(moc, oid);
				}
			}
			scanner.close();
		} catch (FileNotFoundException e1) {
			log.error(e1);
		}

		catch (AdapterException e) {
			log.error(e);
		}
	}
}
