package vn.kido.ship;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.kido.ship.Api.APIService;
import vn.kido.ship.Class.GlobalClass;
import vn.kido.ship.Fragment.Common.HomeFragment;
import vn.kido.ship.Fragment.Map.MapOrderFragment;
import vn.kido.ship.Fragment.Order.ChildListOrderFragment;
import vn.kido.ship.Fragment.Order.DetailOrderFragment;
import vn.kido.ship.Fragment.Order.VerifyOrderFragment;
import vn.kido.ship.Helper.FragmentHelper;

public class HomeActivity extends FragmentActivity {

    public static int WINDOW_HEIGHT;
    public static int WINDOW_WIDTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        GlobalClass.setActivity(HomeActivity.this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        WINDOW_HEIGHT = displayMetrics.heightPixels;
        WINDOW_WIDTH = displayMetrics.widthPixels;

        FragmentHelper.add(HomeFragment.newInstance());
        updateDeviceToken();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        Fragment fragment = FragmentHelper.getActiveFragment(HomeActivity.this);
        if (fragment instanceof HomeFragment) {
            finishAffinity();
            return;
        }

        if (fragment instanceof MapOrderFragment) {
            FragmentHelper.pop(HomeActivity.this, HomeFragment.class);
            return;
        }

        FragmentHelper.pop(HomeActivity.this);
    }

    private void updateDeviceToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                if (token != null) {
                    APIService.getInstance().updateDeviceToken(token)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(String s) {

                        }
                    });
                }
            }
        });
    }
}
