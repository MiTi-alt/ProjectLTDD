package com.example.myapplication.Domain;

public class UserDomain {
    private int id;
    private String email;
    private String userName;

    private String pass;
    private String pic;

    public UserDomain(int id, String email, String userName, String pass, String pic) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.pass = pass;
        this.pic = pic;
    }

    public UserDomain(String email, String userName, String pass, String pic) {
        this.email = email;
        this.userName = userName;
        this.pass = pass;
        this.pic = pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
