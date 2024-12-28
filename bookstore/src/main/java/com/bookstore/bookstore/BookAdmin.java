package com.bookstore.bookstore;

public record BookAdmin(String username, String password) {

    private static final String USERNAME = "Uncle_Bob_1337";
    private static final String PASSWORD = "TomCruiseIsUnder170cm";

    public static boolean isAdmin(String username, String password) {
        return USERNAME.equals(username) && PASSWORD.equals(password);
    }

}
