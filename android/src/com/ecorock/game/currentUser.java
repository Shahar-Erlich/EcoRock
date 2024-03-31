package com.ecorock.game;

public class currentUser {
    private static String mail="",name="",password="";
    private static int icon=0,level=0;

    public static String getMail() {
        return mail;
    }

    public static void setMail(String mail) {
        currentUser.mail = mail;
    }

    public static int getLevel() {
        return level;
    }

    public static void setLevel(int level) {
        currentUser.level = level;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        currentUser.name = name;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        currentUser.password = password;
    }

    public static int getIcon() {
        return icon;
    }

    public static void setIcon(int icon) {
        currentUser.icon = icon;
    }
}
