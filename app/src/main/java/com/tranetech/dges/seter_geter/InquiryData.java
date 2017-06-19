package com.tranetech.dges.seter_geter;

import java.io.Serializable;

/**
 * Created by DHWANI-ANDROID on 08-06-17.
 */

public class InquiryData implements Serializable {

    private String sInquiryeId, sInquiryenquiry, sInquirydate;


    public void setsInquiryeId(String sInquiryeId) {
        this.sInquiryeId = sInquiryeId;
    }

    public void setsInquiryenquiry(String sInquiryenquiry) {
        this.sInquiryenquiry = sInquiryenquiry;
    }

    public void setsInquirydate(String sInquirydate) {
        this.sInquirydate = sInquirydate;
    }

    public String getsInquirydate() {
        return sInquirydate;
    }

    public String getsInquiryeId() {
        return sInquiryeId;
    }

    public String getsInquiryenquiry() {
        return sInquiryenquiry;
    }
}