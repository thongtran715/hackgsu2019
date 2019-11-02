package com.sinch.rcssdk.rcs.model;

public class Store {
    public String storeName;
    public String address;
    public String phoneNumber;

    public Store(String storeName, String address, String phoneNumber) {
        this.storeName = storeName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
