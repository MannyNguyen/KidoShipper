package vn.kido.ship.Helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;

import vn.kido.ship.Class.NotificationUtils;
import vn.kido.ship.Fragment.Order.DetailOrderFragment;


public class NotificationRouteHelper {
    public static final String HOME_SCREEN = "";
    public static final String ORDER = "Order";
    public static final String EVENT = "Event";

    Context context;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public NotificationRouteHelper() {
        //Nothing
    }

    public void register() {
        if (mRegistrationBroadcastReceiver == null) {
            return;
        }
        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(NotificationUtils.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(NotificationUtils.PUSH_NOTIFICATION));
        //NotificationUtils.clearNotifications(context);
    }

    public void route(FragmentActivity activity, Bundle bundle) {
        try {
            String type = bundle.getString("deep_link_type");
            switch (type) {
                case HOME_SCREEN:
                    break;
                case ORDER:
//                    if(bundle.getString("sub_deep_link_id").equals("HISTORY")){
//                        String id = bundle.getString("deep_link_id");
//
//                    } else{
//
//                    }
                    String id = bundle.getString("deep_link_id");
                    FragmentHelper.add(DetailOrderFragment.newInstance(id));
                    break;
            }
            activity.getIntent().removeExtra("deep_link_type");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
