package vn.kido.ship.Service;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.kido.ship.Api.APIService;
import vn.kido.ship.Class.NotificationUtils;
import vn.kido.ship.Helper.StorageHelper;
import vn.kido.ship.MainActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private NotificationUtils notificationUtils;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        if (StorageHelper.get(StorageHelper.TOKEN).equals("")) {
            return;
        }
        updateDeviceToken(s);
    }

    private void updateDeviceToken(String refreshToken) {
        if (refreshToken != null) {
            APIService.getInstance().updateDeviceToken(refreshToken)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
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

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage == null)
            return;
        // Default
        if (remoteMessage.getNotification() != null) {
        }
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                Map<String, String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(params);
                handleDataMessage(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleDataMessage(JSONObject data) {
        try {
            String title = data.optString("title");
            String message = data.optString("body");
            String type = data.optString("deep_link_type");
            String mainPicture = data.optString("main_picture");
            String timestamp = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.putExtra("deep_link_type", type);
            resultIntent.putExtra("deep_link_id", data.optString("deep_link_id", "0"));
            //resultIntent.putExtra("subId", data.optString("sub_id", "0"));

            // check for image attachment
            if (TextUtils.isEmpty(mainPicture)) {
                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, mainPicture);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Showing notification with only text
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}