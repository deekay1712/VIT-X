package com.example.vit_x.ui.alumni;

public class AlumniData {
    String name, email, linkedinUrl, programme, passOutYear, imageUrl, uid, isVerified, registrationNo, createdAt;

    public AlumniData(){

    }

    public AlumniData(String name, String email, String linkedinUrl, String programme, String passOutYear, String imageUrl, String uid, String isVerified, String registrationNo, String createdAt) {
        this.name = name;
        this.email = email;
        this.linkedinUrl = linkedinUrl;
        this.programme = programme;
        this.passOutYear = passOutYear;
        this.imageUrl = imageUrl;
        this.uid = uid;
        this.isVerified = isVerified;
        this.registrationNo = registrationNo;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getProgramme() {
        return programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public String getPassOutYear() {
        return passOutYear;
    }

    public void setPassOutYear(String passOutYear) {
        this.passOutYear = passOutYear;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
