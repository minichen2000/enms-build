package com.nsb.enms.restful.adapterserver;

import com.nsb.enms.restful.adapterserver.api.impl.NesApiServiceImplTest;
import com.nsb.enms.restful.adapterserver.util.LoadConf;

public class TestMain {
	public static void main(String args[]) {
		new LoadConf();
		NesApiServiceImplTest.main(args);
	}
}
