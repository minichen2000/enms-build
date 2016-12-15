package com.nsb.enms.adapter.server.common.api.itf;

import java.util.List;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpTp;
import com.nsb.enms.restful.model.adapter.AdpXc;

public interface AdpNeApiItf {
	public AdpNe addNe() throws AdapterException;

	public void deleteNe() throws AdapterException;

	public void update() throws AdapterException;

	public void supervise() throws AdapterException;

	public void synchronize() throws AdapterException;

	public AdpNe getNeById() throws AdapterException;

	public List<AdpTp> getNeTps() throws AdapterException;

	public List<AdpTp> getChildrenTps(Integer tpId) throws AdapterException;

	public List<AdpTp> getTpsByLayerrate(String layerrate) throws AdapterException;

	public AdpTp getTpById(Integer tpid) throws AdapterException;

	public List<AdpTp> getTpsByType(String tpType) throws AdapterException;

	public AdpXc createXc(AdpXc xc) throws AdapterException;

	public List<AdpXc> getXcsByNeId() throws AdapterException;

	public AdpXc getXcById(Integer xcId) throws AdapterException;

	public void deleteXcsByNeId() throws AdapterException;

	public void deleteXcById(Integer xcid) throws AdapterException;

}
