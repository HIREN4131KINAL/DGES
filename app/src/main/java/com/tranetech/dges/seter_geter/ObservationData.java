package com.tranetech.dges.seter_geter;

/**
 * Created by Markand on 26-05-2017 at 11:33 AM.
 */

public class ObservationData {

    private String sObStudentName,sObTitle,sObDate,sObDesc;

    public void setsObStudentName(String sObStudentName) {
        this.sObStudentName = sObStudentName;
    }

    public String getsObStudentName() {
        return sObStudentName;
    }

    public void setsObDate(String sObDate) {
        this.sObDate = sObDate;
    }

    public String getsObDate() {
        return sObDate;
    }

    public void setsObTitle(String sObTitle) {
        this.sObTitle = sObTitle;
    }

    public String getsObTitle() {
        return sObTitle;
    }

    public void setsObDesc(String sObDesc) {
        this.sObDesc = sObDesc;
    }

    public String getsObDesc() {
        return sObDesc;
    }
}
