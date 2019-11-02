package com.sinch.rcssdk.rcs.model;

import com.sinch.rcssdk.rcs.message.Utils.UtilsToString;

public class LineItem {
    public String ExternalItemId = "1713006";
    public String ItemName = "bababbababab";
    public int Quantity = 1;

    public int UnitPrice = 0;

    public int UnitSellPrice = 1;

    public int ExtendedSellPrice = 65;

    public String BagName = "string";
     @Override
    public String toString() {
        return UtilsToString.convertString(this) ;
    }
}
