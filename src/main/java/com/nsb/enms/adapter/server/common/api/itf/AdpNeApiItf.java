package com.nsb.enms.adapter.server.common.api.itf;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.model.adapter.AdpNe;

public interface AdpNeApiItf extends AdpCommonApiItf {
	public AdpNe addNe() throws AdapterException;

	public void deleteNe() throws AdapterException;

	public void update() throws AdapterException;

	public void supervise() throws AdapterException;

	public void synchronize() throws AdapterException;

	public AdpNe getNeById() throws AdapterException;

	public void stopNeSupervision() throws AdapterException;
}
