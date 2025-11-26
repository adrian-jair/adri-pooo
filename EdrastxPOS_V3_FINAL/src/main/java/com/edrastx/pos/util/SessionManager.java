package com.edrastx.pos.util;

public class SessionManager {
    private static String currentUser;
    private static String currentRole;

    public static void setUser(String user, String role) {
        currentUser = user;
        currentRole = role;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static String getCurrentRole() {
        return currentRole;
    }
}
