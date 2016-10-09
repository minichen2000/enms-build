package com.nsb.enms.adapter.server.common.statemachine.ne;

public class NeState
{

    public static enum AdminStates {
        TRUE(true), FALSE(false);
        private boolean value;

        private AdminStates( boolean value )
        {
            this.value = value;
        }

        public boolean getValue()
        {
            return value;
        }
    }

    public static enum SupervisionState {
        SUPERVISED("supervised"), UNSUPERVISED("unsupervised");

        private String value;

        private SupervisionState( String value )
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }

    public static enum CommunicationState {
        REACHABLE("reachable"), UNREACHABLE("unreachable");
        private String value;

        private CommunicationState( String value )
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }

    public static enum SynchState {
        SYNCHRONIZED("synchronized"), UNSYNCHRONIZED("unsynchronized");
        private String value;

        private SynchState( String value )
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }

    public static enum MaintenanceState {
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

    public static enum OperationalState {
        IDLE("idle"), SUPERVISING("supervising"), CONNECTING(
                "connecting"), SYNCHRONIZING("synchronizing");

        private String value;

        private OperationalState( String value )
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }
}