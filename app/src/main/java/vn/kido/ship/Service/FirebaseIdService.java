package vn.kido.ship.Service;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.iid.InstanceIdResult;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.kido.ship.Api.APIService;
import vn.kido.ship.Class.GlobalClass;
import vn.kido.ship.HomeActivity;

public class FirebaseIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        updateDeviceToken(refreshedToken);
    }

    private void updateDeviceToken(String refreshedToken) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                if ((token != null && GlobalClass.getActivity() instanceof HomeActivity)) {
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
