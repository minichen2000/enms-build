package com.nsb.enms.adapter.server.common.statemachine.ne;

public enum NeEvent {
    Create, StartSupervision, Supervisied, Synchronizing, Synchronized, E_SUPERVISIED_2_UNSUPERVISIED, E_UNSUPERVISIED_2_SUPERVISIED, E_REACHABLE_2_UNREACHABLE, E_UNREACHABLE_2_REACHABLE, E_SYNCHRONIZED_2_UNSYNCHRONIZED, E_UNSYNCHRONIZED_2_SYNCHRONIZED, E_TRUE_2_FALSE, E_FALSE_2_TRUE, E_IDLE_2_SYNCHRONIZING, E_SYNCHRONIZING_2_IDLE, E_IDLE_2_SUPERVISING, E_SUPERVISING_2_IDLE;
}