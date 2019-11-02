package com.sinch.rcssdk.rcs.model;

import com.sinch.rcssdk.rcs.message.Utils.UtilsToString;

import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.List;

public class Order {

    public boolean IsClosed = true;
    public String OrderDateTime = "2019-11-02T14:28:29.738Z";
    public String OrderDueDateTime = "2019-11-02T14:28:29.323Z";

    public boolean IsPaid = true;

    public Customer customer = new Customer();

    public Double TaxAmount = 0.0;

    public List<LineItem> lineItems = new ArrayList<>();

    public Order() {
        lineItems.add(new LineItem());
    }

    @Override
    public String toString() {
        return UtilsToString.convertString(this) ;
    }

}
