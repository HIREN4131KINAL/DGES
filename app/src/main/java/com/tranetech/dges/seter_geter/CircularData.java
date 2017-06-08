package com.tranetech.dges.seter_geter;

import java.io.Serializable;

/**
 * Created by Markand on 26-05-2017 at 10:55 AM.
 */

public class CircularData implements Serializable{

    private String sCircularTitle,sCircularDesc,sCircualarDate,sCircualarStatus,sCircularURL,sCircularFileName;

    public void setsCircularTitle(String sCircularTitle) {
        this.sCircularTitle = sCircularTitle;
    }

    public String getsCircularTitle() {
        return sCircularTitle;
    }

    public void setsCircularDesc(String sCircularDesc) {
        this.sCircularDesc = sCircularDesc;
    }

    public String getsCircularDesc() {
        return sCircularDesc;
    }

    public void setsCircualarDate(String sCircualarDate) {
        this.sCircualarDate = sCircualarDate;
    }

    public String getsCircualarDate() {
        return sCircualarDate;
    }

    public void setsCircualarStatus(String sCircualarStatus) {
        this.sCircualarStatus = sCircualarStatus;
    }
    public String getsCircualarStatus() {
        return sCircualarStatus;
    }

    public void setsCircularURL(String sCircularURL) {
        this.sCircularURL = sCircularURL;
    }

    public String getsCircularURL() {
        return sCircularURL;
    }

    public void setsCircularFileName(String sCircularFileName) {
        this.sCircularFileName = sCircularFileName;
    }

    public String getsCircularFileName() {
        return sCircularFileName;
    }
}
