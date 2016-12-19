package com.nsb.enms.adapter.server.common.api.itf;

import java.util.List;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.model.adapter.AdpXc;

public interface AdpXcApiItf extends AdpCommonApiItf {

	public AdpXc createXc(AdpXc xc) throws AdapterException;

	public List<AdpXc> getXcsByNeId() throws AdapterException;

	public AdpXc getXcById(Integer xcId) throws AdapterException;

	public void deleteXcsByNeId() throws AdapterException;

	public void deleteXcById(Integer xcid) throws AdapterException;

}
