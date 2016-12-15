package com.nsb.enms.adapter.server.common.api.itf;

import java.util.List;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.AdpTp;

public interface AdpNeApiItf {
	public AdpNe addNe() throws AdapterException;

	public AdpNe getData();

	public void create() throws AdapterException;

	public void assign() throws AdapterException;

	public void delete() throws AdapterException;

	public void update() throws AdapterException;

	public void supervise() throws AdapterException;

	public void synchronize() throws AdapterException;

	public AdpNe getNeById() throws AdapterException;

	public List<AdpTp> getNeTps() throws AdapterException;

	public List<AdpTp> getChildrenTps(Integer tpId) throws AdapterException;

	public List<AdpTp> getTpsByLayerrate(Integer layerrate) throws AdapterException;

	public AdpTp getTp(Integer tpid) throws AdapterException;

	public List<AdpTp> getTpsByType(Integer tptype) throws AdapterException;

}
