package com.tranetech.dges.seter_geter;

import java.io.Serializable;

/**
 * Created by DHWANI-ANDROID on 27-05-17.
 */

public class FeesData implements Serializable {

    private String sFeesAmount,sFeesDue,sFeesPaid,sFeesCal;

    public void setsFeesAmount(String sFeesAmount) {
        this.sFeesAmount = sFeesAmount;
    }

    public String getsFeesAmount() {
        return sFeesAmount;
    }

    public void setsFeesDue(String sFeesDue) {
        this.sFeesDue = sFeesDue;
    }

    public String getsFeesDue() {
        return sFeesDue;
    }

    public void setsFeesPaid(String sFeesPaid) {
        this.sFeesPaid = sFeesPaid;
    }

    public String getsFeesPaid() {
        return sFeesPaid;
    }

    public void setsFeesCal(String sFeesCal) {
        this.sFeesCal = sFeesCal;
    }

    public String getsFeesCal() {
        return sFeesCal;
    }

}
