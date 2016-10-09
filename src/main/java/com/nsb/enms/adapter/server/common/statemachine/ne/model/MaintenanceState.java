package com.nsb.enms.adapter.server.common.statemachine.ne.model;

public enum MaintenanceState {
    TRUE(true), FALSE(false);

    private boolean value;

    private MaintenanceState( boolean value )
    {
        this.value = value;
    }

    public String toString()
    {
        return String.valueOf( value );
    }
}