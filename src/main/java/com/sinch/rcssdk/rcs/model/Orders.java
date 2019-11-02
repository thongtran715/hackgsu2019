package com.sinch.rcssdk.rcs.model;

import com.sinch.rcssdk.rcs.message.Utils.UtilsToString;

import java.util.ArrayList;
import java.util.List;

public class Orders {

    public List<Order> Orders = new ArrayList<>();

    public String SourceApplicationName = "alpha team";

    public Orders() {
        this.Orders.add(new Order());
    }

    @Override
    public String toString() {
        return UtilsToString.convertString(this) ;
    }

}
