package com.nsb.enms.adapter.server.statemachine.ne.model;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.exception.AdapterExceptionType;
import com.nsb.enms.adapter.server.db.mgr.AdpNesDbMgr;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.common.AlignmentState;
import com.nsb.enms.common.CommunicationState;
import com.nsb.enms.common.MaintenanceState;
import com.nsb.enms.common.OperationState;
import com.nsb.enms.common.SupervisionState;

public class NeStateCallBack {
	private MaintenanceState maintenanceState;

	private CommunicationState communicationState;

	private OperationState operationState;

	private SupervisionState supervisionState;

	private AlignmentState alignmentState;

	private Integer id;

	private static AdpNesDbMgr nesDbMgr = new AdpNesDbMgr();

	public void tellMe(CommunicationState communicationState) throws AdapterException {
		AdpNe ne = new AdpNe();
		ne.setId(id);
		ne.setCommunicationState(communicationState.name());
		updateNe(ne);
	}

	public void tellMe(MaintenanceState maintenanceState) throws AdapterException {
		AdpNe ne = new AdpNe();
		ne.setId(id);
		ne.setMaintenanceState(maintenanceState.name());
		updateNe(ne);
	}

	public void tellMe(OperationState operationalState) throws AdapterException {
		AdpNe ne = new AdpNe();
		ne.setId(id);
		ne.setOperationalState(operationalState.name());
		updateNe(ne);
	}

	public void tellMe(SupervisionState supervisionState) throws AdapterException {
		AdpNe ne = new AdpNe();
		ne.setId(id);
		ne.setSupervisionState(supervisionState.name());
		updateNe(ne);
	}

	public void tellMe(AlignmentState alignmentState) throws AdapterException {
		AdpNe ne = new AdpNe();
		ne.setId(id);
		ne.setSupervisionState(alignmentState.name());
		updateNe(ne);
	}

	private void updateNe(AdpNe ne) throws AdapterException {
		try {
			nesDbMgr.updateNe(ne);
		} catch (Exception e) {
			throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, ErrorCode.FAIL_DB_OPERATION);
		}
	}

	public CommunicationState getCommunicationState() {
		return communicationState;
	}

	public void setCommunicationState(CommunicationState communicationState) {
		this.communicationState = communicationState;
	}

	public OperationState getOperationState() {
		return operationState;
	}

	public void setOperationState(OperationState operationalState) {
		this.operationState = operationalState;
	}

	public SupervisionState getSupervisionState() {
		return supervisionState;
	}

	public void setSupervisionState(SupervisionState supervisionState) {
		this.supervisionState = supervisionState;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MaintenanceState getMaintenanceState() {
		return maintenanceState;
	}

	public void setMaintenanceState(MaintenanceState maintenanceState) {
		this.maintenanceState = maintenanceState;
	}

	public AlignmentState getAlignmentState() {
		return alignmentState;
	}

	public void setAlignmentState(AlignmentState alignmentState) {
		this.alignmentState = alignmentState;
	}
}