package vn.kido.ship.Helper;

import android.content.Context;
import android.content.SharedPreferences;


import vn.kido.ship.Class.GlobalClass;

import static android.content.Context.MODE_PRIVATE;

public class StorageHelper {

    public static final String TOKEN = "TOKEN";
    public static final String USERNAME = "USERNAME";
    public static final String SUP_ID = "SUP_ID";
    public static final String SERVER = "SERVER";

    private static SharedPreferences preferences;

    public static SharedPreferences getPreferences() {
        if (preferences == null) {
            init(GlobalClass.getActivity());
        }
        return preferences;
    }

    public static void init(Context context) {
        String PREF_FILE_NAME = "KIDO_SHIP_STORAGE";
        preferences = context.getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
    }

    public static void set(String key, String value) {
        try {
            SharedPreferences.Editor editor = getPreferences().edit();
            editor.putString(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return getPreferences().getString(key, "");
    }
}
