package com.tranetech.dges.seter_geter;

import java.io.Serializable;

/**
 * Created by HIREN AMALIYAR on 09-06-2017.
 */

public class LeaveData implements Serializable {
    String   sLeavelId, sLeavedescription, sLeavedate, sLeavestatus;


    public void setsLeavelId(String sLeavelId) {
        this.sLeavelId = sLeavelId;
    }

    public String getsLeavelId() {
        return sLeavelId;
    }

    public void setsLeavedescription(String sLeavedescription) {
        this.sLeavedescription = sLeavedescription;
    }

    public String getsLeavedescription() {
        return sLeavedescription;
    }

    public void setsLeavedate(String sLeavedate) {
        this.sLeavedate = sLeavedate;
    }


    public void setsLeavestatus(String sLeavestatus) {
        this.sLeavestatus = sLeavestatus;
    }

    public String getsLeavedate() {
        return sLeavedate;
    }

    public String getsLeavestatus() {
        return sLeavestatus;
    }

}
