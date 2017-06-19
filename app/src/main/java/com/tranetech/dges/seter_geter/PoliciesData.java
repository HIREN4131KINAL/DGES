package com.tranetech.dges.seter_geter;

import java.io.Serializable;

/**
 * Created by HIREN AMALIYAR on 08-06-2017.
 */

public class PoliciesData implements Serializable {
    String sPolicesTitle, sPolicesDesc;

    public void setsPolicesTitle(String sPolicesTitle) {
        this.sPolicesTitle = sPolicesTitle;
    }

    public void setsPolicesDesc(String sPolicesDesc) {
        this.sPolicesDesc = sPolicesDesc;
    }

    public String getsPolicesDesc() {
        return sPolicesDesc;
    }

    public String getsPolicesTitle() {
        return sPolicesTitle;
    }
}
