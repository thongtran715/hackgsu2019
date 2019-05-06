package com.sinch.rcssdk.rcs.message.component.agentevent;


import com.sinch.rcssdk.rcs.message.enums.AgentEventType;

public class AgentComposingEvent extends AgentEvent {

    public AgentComposingEvent() {
        super(AgentEventType.agent_composing);
    }

}
