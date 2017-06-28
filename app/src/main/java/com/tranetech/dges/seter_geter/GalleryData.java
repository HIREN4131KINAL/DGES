package com.tranetech.dges.seter_geter;

/**
 * Created by Markand on 27-06-2017 at 02:19 PM.
 */

public class GalleryData {
    String strAlbumDate,strAlbumTitle,strAlbumCount,strAlbumImgUrl,sGTid;

    public String getStrAlbumCount() {
        return strAlbumCount;
    }

    public void setStrAlbumCount(String strAlbumCount) {
        this.strAlbumCount = strAlbumCount;
    }

    public void setStrAlbumDate(String strAlbumDate) {
        this.strAlbumDate = strAlbumDate;
    }

    public String getStrAlbumDate() {
        return strAlbumDate;
    }

    public void setStrAlbumImgUrl(String strAlbumImgUrl) {
        this.strAlbumImgUrl = strAlbumImgUrl;
    }

    public String getStrAlbumImgUrl() {
        return strAlbumImgUrl;
    }

    public void setStrAlbumTitle(String strAlbumTitle) {
        this.strAlbumTitle = strAlbumTitle;
    }

    public String getStrAlbumTitle() {
        return strAlbumTitle;
    }

    public void setsGTid(String sGTid) {
        this.sGTid = sGTid;
    }

    public String getsGTid() {
        return sGTid;
    }
}
