package com.sinch.rcssdk.rcs.message.component.action;


import com.sinch.rcssdk.rcs.message.enums.ActionType;

public class DialPhoneNumber extends Action {

    private String phone_number;

    public DialPhoneNumber() {
        super(ActionType.dial_phone_number);
    }

    public DialPhoneNumber(String phoneNumber) {
        super(ActionType.dial_phone_number);
        this.phone_number = phoneNumber;
    }

    public String getPhoneNumber() {
        return this.phone_number;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phone_number = phoneNumber;
    }

}
