package com.tranetech.dges.seter_geter;

/**
 * Created by HIREN AMALIYAR on 27-05-2017.
 */

public class ParentChildData {
    private String sStudentID;
    private String sName,mName,lName;
    private String sStandard;


    public void setsStudentID(String sStudentID) {
        this.sStudentID = sStudentID;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public void setsStandard(String sStandard) {
        this.sStandard = sStandard;
    }

    public String getsName() {
        return sName;
    }

    public String getsStandard() {
        return sStandard;
    }

    public String getsStudentID() {
        return sStudentID;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmName() {
        return mName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getlName() {
        return lName;
    }
}
