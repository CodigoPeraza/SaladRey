package com.mr21082pm18077.saladrey.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static SharedPreferences prefs;

    public static void init(Context ctx) {
        if (prefs == null)
            prefs = ctx.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    public static void save(String mail, boolean isAdmin) {
        prefs.edit()
                .putString("email", mail)
                .putBoolean("isAdmin", isAdmin)
                .apply();
    }

    public static String getEmail() {
        return prefs.getString("email", "");
    }


    public static boolean isLogged() {
        return prefs.contains("email");
    }

    public static boolean isAdmin() {
        return prefs.getBoolean("isAdmin", false);
    }

    public static void logout() {
        prefs.edit().clear().apply();
    }
}
