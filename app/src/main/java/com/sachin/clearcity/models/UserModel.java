package com.sachin.clearcity.models;

public class UserModel {

    private String name,email,password,profile,referCode,redeemStatus;

    private int coin;

    private String uId;

    public UserModel(String name, String email, String password, String profile, String referCode, String redeemStatus, int coin) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.referCode = referCode;
        this.redeemStatus = redeemStatus;
        this.coin = coin;
    }

    public UserModel() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public String getRedeemStatus() {
        return redeemStatus;
    }

    public void setRedeemStatus(String redeemStatus) {
        this.redeemStatus = redeemStatus;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
