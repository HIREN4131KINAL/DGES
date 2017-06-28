package com.tranetech.dges.seter_geter;

/**
 * Created by Markand on 28-06-2017 at 09:58 AM.
 */

public class PhotoData {
    private String sGid,sPhotoImgURL,sPhotoTitle,sImgDate;

    public void setsGid(String sGid) {
        this.sGid = sGid;
    }

    public String getsGid() {
        return sGid;
    }

    public void setsImgDate(String sImgDate) {
        this.sImgDate = sImgDate;
    }

    public String getsImgDate() {
        return sImgDate;
    }

    public void setsPhotoImgURL(String sPhotoImgURL) {
        this.sPhotoImgURL = sPhotoImgURL;
    }

    public String getsPhotoImgURL() {
        return sPhotoImgURL;
    }

    public void setsPhotoTitle(String sPhotoTitle) {
        this.sPhotoTitle = sPhotoTitle;
    }

    public String getsPhotoTitle() {
        return sPhotoTitle;
    }
}
