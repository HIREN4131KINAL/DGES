package com.tranetech.dges;

/**
 * Created by Markand on 26-05-2017 at 09:29 AM.
 */

public class HomeworkData {
    private String sSubName,sHWDate,sHWDescription;

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
}
