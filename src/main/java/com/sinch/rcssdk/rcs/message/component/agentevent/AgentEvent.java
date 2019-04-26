package com.sinch.rcssdk.rcs.message.component.agentevent;


import com.sinch.rcssdk.rcs.message.enums.AgentEventType;

public abstract class AgentEvent {

    private AgentEventType type;


    public AgentEvent() {
    }

    public AgentEvent(AgentEventType type) {
        this.type = type;
    }

    public AgentEventType getType() {
        return type;
    }

    public void setType(AgentEventType type) {
        this.type = type;
    }


}
