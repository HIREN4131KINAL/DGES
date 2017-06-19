package com.tranetech.dges.seter_geter;

import java.io.Serializable;

/**
 * Created by Markand on 26-05-2017 at 09:29 AM.
 */

public class HomeworkData implements Serializable {
    private String standrdID, sSubName,sHWDate,sHWDescription,teachers;

public String getsSubName(){
    return sSubName;
}

    public String getsHWDate() {
        return sHWDate;
    }

    public String getsHWDescription() {
        return sHWDescription;
    }

    public void setsHWDate(String sHWDate) {
        this.sHWDate = sHWDate;
    }

    public void setsHWDescription(String sHWDescription) {
        this.sHWDescription = sHWDescription;
    }

    public void setsSubName(String sSubName) {
        this.sSubName = sSubName;
    }

    public void setStandrdID(String standrdID) {
        this.standrdID = standrdID;
    }

    public String getStandrdID() {
        return standrdID;
    }

    public void setTeachers(String teachers) {
        this.teachers = teachers;
    }

    public String getTeachers() {
        return teachers;
    }
}
