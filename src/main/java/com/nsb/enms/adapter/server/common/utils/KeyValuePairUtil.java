package com.nsb.enms.adapter.server.common.utils;

import java.util.List;

import com.nsb.enms.restful.model.adapter.AdpKVPair;

public class KeyValuePairUtil {

	public static String getGroupId(List<AdpKVPair> pairs) {
		for (AdpKVPair pair : pairs) {
			if ("GROUP_ID".equalsIgnoreCase(pair.getKey())) {
				return pair.getValue();
			}
		}
		return null;
	}

	public static String getNeId(List<AdpKVPair> pairs) {
		for (AdpKVPair pair : pairs) {
			if ("NE_ID".equalsIgnoreCase(pair.getKey())) {
				return pair.getValue();
			}
		}

		return null;
	}

	public static String[] getGroupAndNeId(List<AdpKVPair> pairs) {
		String[] groupAndNeId = new String[2];
		for (AdpKVPair pair : pairs) {
			if ("GROUP_ID".equalsIgnoreCase(pair.getKey())) {
				groupAndNeId[0] = pair.getValue();
				continue;
			}

			if ("NE_ID".equalsIgnoreCase(pair.getKey())) {
				groupAndNeId[1] = pair.getValue();
			}
		}
		return groupAndNeId;
	}
}
