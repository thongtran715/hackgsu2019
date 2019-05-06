package com.sinch.rcssdk.rcs.message.component.action;


import com.sinch.rcssdk.rcs.message.enums.ActionType;

public abstract class Action {
    private ActionType type;

    public Action() {
        this.type = null;
    }

    public Action(ActionType type) {
        this.type = type;
    }

    public void setActionType(ActionType type) {
        this.type = type;
    }

    public ActionType getType() {
        return this.type;
    }
}
