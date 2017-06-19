package com.tranetech.dges.seter_geter;

import java.io.Serializable;

/**
 * Created by DHWANI-ANDROID on 27-05-17.
 */

public class FeesData implements Serializable {

    private String sFeesDue,sFeesPaid,sFeesTotal,sFeesMonth ,sFeesFname,sFeesCheck ,sFeesBank,sFeesPaymode;



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



    public void setsFeesMonth(String sFeesMonth) {
        this.sFeesMonth = sFeesMonth;
    }

    public String getsFeesMonth() {
        return sFeesMonth;
    }


    public void setsFeesFname(String sFeesFname) {
        this.sFeesFname = sFeesFname;
    }

    public String getsFeesFname() {
        return sFeesFname;
    }

    public void setsFeesCheck(String sFeesCheck) {
        this.sFeesCheck = sFeesCheck;
    }

    public String getsFeesCheck() {
        return sFeesCheck;
    }

    public void setsFeesBank(String sFeesBank) {
        this.sFeesBank = sFeesBank;
    }

    public String getsFeesBank() {
        return sFeesBank;
    }

    public void setsFeesPaymode(String sFeesPaymode) {
        this.sFeesPaymode = sFeesPaymode;
    }

    public String getsFeesPaymode() {
        return sFeesPaymode;
    }

    public void setsFeesTotal(String sFeesTotal) {
        this.sFeesTotal = sFeesTotal;
    }

    public String getsFeesTotal() {
        return sFeesTotal;
    }
}
