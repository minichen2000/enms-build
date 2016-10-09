package com.nsb.enms.adapter.server.common.statemachine.ne.model;

public enum AdminState {
    TRUE(true), FALSE(false);

    private boolean value;

    private AdminState( boolean value )
    {
        this.value = value;
    }

    public String toString()
    {
        return String.valueOf( value );
    }
}