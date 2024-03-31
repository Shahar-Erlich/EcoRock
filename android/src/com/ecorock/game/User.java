package com.ecorock.game;

public class User {
    private String name,pass,mail;
    private int prof,level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public User(String name, String mail, String pass) {
        this.name = name;
        this.pass = pass;
        this.mail = mail;
    }

    public User(String pass, String mail) {
        this.pass = pass;
        this.mail = mail;
    }
    public User() {

    }

    public int getProf() {
        return prof;
    }

    public void setProf(int prof) {
        this.prof = prof;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
