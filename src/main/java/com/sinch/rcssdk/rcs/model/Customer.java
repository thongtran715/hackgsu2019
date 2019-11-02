package com.sinch.rcssdk.rcs.model;

import com.sinch.rcssdk.rcs.message.Utils.UtilsToString;

public class Customer {

    public String CustomerId="11";
    public String CustomerName="thong tran 69";

      @Override
    public String toString() {
        return UtilsToString.convertString(this) ;
    }
}
