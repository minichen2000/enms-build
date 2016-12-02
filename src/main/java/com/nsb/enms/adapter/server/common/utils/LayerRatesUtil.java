package com.nsb.enms.adapter.server.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class LayerRatesUtil {

	public static boolean isEquals(List<String> x, List<String> y) {
		if ((x == null && y == null) || x == y) {
			return true;
		}

		String x_str = String.join(",", x.toArray(new String[x.size()]));
		String y_str = String.join(",", y.toArray(new String[y.size()]));
		if (StringUtils.equals(x_str, y_str)) {
			return true;
		}

		return false;
	}

	public static void main(String args[]) {
		List<String> a = new ArrayList<String>();
		a.add("a");
		a.add("b");
		List<String> b = new ArrayList<String>();
		b.add("a");
		b.add("b");

		System.out.println(isEquals(a, b));

	}
}
