package com.sinch.rcssdk.rcs.message.component.agentevent;


import com.sinch.rcssdk.rcs.message.enums.AgentEventType;

public class AgentReadEvent extends AgentEvent {

    private String message_id;

    public AgentReadEvent() {
        super(AgentEventType.agent_read);
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }


}
