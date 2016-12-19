package com.nsb.enms.adapter.server.common.api.itf;

import java.util.List;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.model.adapter.AdpTp;

public interface AdpTpApiItf extends AdpCommonApiItf {

	public List<AdpTp> getNeTps() throws AdapterException;

	public List<AdpTp> getChildrenTps(Integer tpId) throws AdapterException;

	public List<AdpTp> getTpsByLayerrate(String layerrate) throws AdapterException;

	public AdpTp getTpById(Integer tpid) throws AdapterException;

	public List<AdpTp> getTpsByType(String tpType) throws AdapterException;
}
