package com.tranetech.dges;

/**
 * Created by HIREN AMALIYAR on 27-05-2017.
 */

class ParentChildData {
    private String sStudentID;
    private String sName;
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
}
