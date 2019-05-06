package com.sinch.rcssdk.rcs.message.component.action;


import com.sinch.rcssdk.rcs.message.enums.ActionType;

public class OpenUrl extends Action {
    private String url;

    public OpenUrl() {
        super(ActionType.open_url);
    }

    public OpenUrl(String url) {
        super(ActionType.open_url);
        this.url = url;
    }

    public String getUrl() {
        return this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
