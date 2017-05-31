package com.tranetech.dges.seter_geter;

import java.io.Serializable;

/**
 * Created by HIREN AMALIYAR on 27-05-2017.
 */

public class ParentChildData implements Serializable {
    private String sStudentID;
    private String sName, mName, lName;
    private String sStandard, division, adhar, grNo, rollno, address, mobile, dob, nationality, phHndicap, category, photo, lastschool, laststd, percentage, status, message;


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

    public void setDivision(String division) {
        this.division = division;
    }

    public void setAdhar(String adhar) {
        this.adhar = adhar;
    }

    public void setGrNo(String grNo) {
        this.grNo = grNo;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void seBloodgroop(String bloodgroup) {
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setPhHndicap(String phHndicap) {
        this.phHndicap = phHndicap;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setLastschool(String lastschool) {
        this.lastschool = lastschool;
    }

    public void setLaststd(String laststd) {
        this.laststd = laststd;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAddress() {
        return address;
    }

    public String getAdhar() {
        return adhar;
    }

    public String getCategory() {
        return category;
    }

    public String getDivision() {
        return division;
    }

    public String getDob() {
        return dob;
    }

    public String getGrNo() {
        return grNo;
    }

    public String getLastschool() {
        return lastschool;
    }

    public String getLaststd() {
        return laststd;
    }

    public String getMessage() {
        return message;
    }

    public String getMobile() {
        return mobile;
    }

    public String getNationality() {
        return nationality;
    }

    public String getPercentage() {
        return percentage;
    }

    public String getPhHndicap() {
        return phHndicap;
    }

    public String getPhoto() {
        return photo;
    }

    public String getRollno() {
        return rollno;
    }

    public String getStatus() {
        return status;
    }

}
