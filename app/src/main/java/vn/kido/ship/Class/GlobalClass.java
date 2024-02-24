package vn.kido.ship.Class;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.FragmentActivity;

public class GlobalClass extends MultiDexApplication {
    private static Context mContext;
    private static FragmentActivity activity;


    public static FragmentActivity getActivity() {
        return activity;
    }

    public static void setActivity(FragmentActivity activity) {
        GlobalClass.activity = activity;
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        GlobalClass.mContext = mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        setContext(getApplicationContext());
    }


}