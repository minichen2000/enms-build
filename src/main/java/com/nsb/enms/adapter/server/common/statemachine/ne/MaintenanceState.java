package com.nsb.enms.adapter.server.common.statemachine.ne;

public enum MaintenanceState {
    TRUE(true), FALSE(false);

    private boolean value;

    private MaintenanceState( boolean value )
    {
        this.value = value;
    }

    public boolean getValue()
    {
        return value;
    }
}