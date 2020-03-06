package com.example.bookingmobile;

public class CUser {

    private String loginName;
    private String hashPassword;
    private String email;

    public CUser(String loginName, String hashPassword, String email) {
        this.loginName = loginName;
        this.hashPassword = hashPassword;
        this.email = email;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
