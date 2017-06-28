package com.tranetech.dges.seter_geter;

import java.io.Serializable;

/**
 * Created by DHWANI-ANDROID on 27-05-17.
 */

public class ResultData implements Serializable {

    private String sResultSub,sResultObtMarks,sResultSlash,sResultOutMarks,sTestType;
    private String date;

    public void setsResultSub(String sResultSub) {
        this.sResultSub = sResultSub;
    }

    public String getsResultSub() {
        return sResultSub;
    }

    public void setsResultObtMarks(String sResultObtMarks) {this.sResultObtMarks = sResultObtMarks;
    }

    public String getsResultObtMarks() {
        return sResultObtMarks;
    }

    public void setsResultOutMarks(String sResultOutMarks) {this.sResultOutMarks = sResultOutMarks;
    }

    public String getsResultOutMarks() {return sResultOutMarks;
    }

    public String  getTestType() {
        return sTestType;
    }

    public void setTestType(String testType) {
        this.sTestType = testType;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
