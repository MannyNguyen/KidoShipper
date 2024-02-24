package vn.kido.ship.Api;

import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import vn.kido.ship.Class.GlobalClass;
import vn.kido.ship.Constant.AppConfig;
import vn.kido.ship.Helper.StorageHelper;

public class APIService {
    private interface GateAPI {
        @POST("shipperservice/shipper/login")
        @Headers({"Content-Type: application/json;charset=UTF-8"})
        Observable<String> login(@Body String data);

        @POST("shipperservice/shipper/send_otp")
        @Headers({"Content-Type: application/json;charset=UTF-8"})
        Observable<String> resendOTP(@Body String data);

        @POST("shipperservice/shipper/verify")
        @Headers({"Content-Type: application/json;charset=UTF-8"})
        Observable<String> verifyOTP(@Body String data);


        @POST("shipperservice/shipper/shipper_confirm_upload")
        @Headers({"Content-Type: application/json;charset=UTF-8"})
        Observable<String> upload(@Body String data);

        @POST("shipperservice/shipper/confirm_order")
        @Headers({"Content-Type: application/json;charset=UTF-8"})
        Observable<String> confirmorder(@Body String data);

        @POST("shipperservice/shipper/send_noti_delivery")
        @Headers({"Content-Type: application/json;charset=UTF-8"})
        Observable<String> notiDelivery(@Body String data);

        @GET("shipperservice/shipper/get_orders")
        Observable<String> getOrder(@Query("username") String username,
                                    @Query("deviceid") String deviceId,
                                    @Query("token") String token,
                                    @Query("type") int type,
                                    @Query("sort_type") int sort_type,
                                    @Query("lat") double lat,
                                    @Query("lng") double lng);

        @GET("shipperservice/shipper/get_shops")
        Observable<String> getStore(@Query("username") String username,
                                    @Query("deviceid") String deviceId,
                                    @Query("token") String token);

        //@Query("sup_id") String sub_id
        @GET("shipperservice/shipper/get_reason")
        Observable<String> getReason(@Query("username") String username,
                                     @Query("deviceid") String deviceId,
                                     @Query("token") String token);

        @GET("shipperservice/shipper/get_order_detail")
        Observable<String> getDetailOrder(@Query("username") String username,
                                          @Query("token") String token,
                                          @Query("deviceid") String deviceid,
                                          @Query("order_code") String id);

        @POST("shipperservice/shipper/update_devicetoken")
        @Headers({"Content-Type: application/json;charset=UTF-8"})
        Observable<String> updateDeviceToken(@Body String data);

        @POST("shipperservice/shipper/cancel_order")
        @Headers({"Content-Type: application/json;charset=UTF-8"})
        Observable<String> cancelOrder(@Body String data);

        @GET("shipperservice/shipper/get_items_received")
        Observable<String> getTotal(@Query("username") String username,
                                    @Query("deviceid") String deviceid,
                                    @Query("token") String token,
                                    @Query("order_code") String id,
                                    @Query("items") String items);

        @GET("shipperservice/shipper/get_list_order_history")
        Observable<String> getHistory(@Query("username") String username,
                                      @Query("deviceid") String deviceid,
                                      @Query("token") String token,
                                      @Query("sort_type") int sort_type,
                                      @Query("lat") double lat,
                                      @Query("lng") double lng,
                                      @Query("type") int type);
        @GET("shipperservice/shipper/get_profile")
        Observable<String> getProfile(@Query("username") String username,
                                      @Query("deviceid") String deviceid,
                                      @Query("token") String token);
        @GET("shipperservice/shipper/get_location")
        Observable<String> getLocation(@Query("username") String username,
                                      @Query("deviceid") String deviceid,
                                      @Query("token") String token,
                                       @Query("order_code") String order_code);
    }

    private GateAPI gateAPI;
    private static APIService instance;

    public static APIService getInstance() {
        if (instance == null) {
            instance = new APIService();
        }
        return instance;
    }

    public static void setInstance(APIService value) {
        instance = value;
    }

    public APIService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.DOMAIN)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        gateAPI = retrofit.create(GateAPI.class);
    }

    String deviceId;

    private String getDeviceId() {
        if (deviceId == null) {
            deviceId = Settings.Secure.getString(GlobalClass.getActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
//            Log.d("token:",""+StorageHelper.get(StorageHelper.TOKEN));
//            Log.d("deviceid:",""+deviceId);
        }
        return deviceId;
    }

    public Observable<String> login(String username) {
        Map map = new HashMap();
        map.put("username", username);
        map.put("deviceid", getDeviceId());
        map.put("os", "ANDROID");
        String data = new Gson().toJson(map);
        return gateAPI.login(data);
    }

    public Observable<String> resendOTP(String username) {
        Map map = new HashMap();
        map.put("username", username);
        String data = new Gson().toJson(map);
        return gateAPI.resendOTP(data);
    }

    public Observable<String> verifyOTP(String username, String otp) {
        Map map = new HashMap();
        map.put("username", username);
        map.put("otp", otp);
        map.put("deviceid", getDeviceId());
        String data = new Gson().toJson(map);
        return gateAPI.verifyOTP(data);
    }

    public Observable<String> upload(String order_id, int type, String items, String verify_code, String image, String note, int highlight) {
        Map map = new HashMap();
        map.put("username", StorageHelper.get(StorageHelper.USERNAME));
        map.put("deviceid", getDeviceId());
        map.put("token", StorageHelper.get(StorageHelper.TOKEN));
        map.put("order_code", order_id);
        map.put("type", type);
        map.put("items", items);
        map.put("verify_code", verify_code);
        map.put("image", image);
        map.put("note", note);
        map.put("highlight", highlight);
        Log.d("aaaaaaa", StorageHelper.get(StorageHelper.USERNAME) + "||||||" + getDeviceId() + "|||||||" + StorageHelper.get(StorageHelper.TOKEN));


        String data = new Gson().toJson(map);
        return gateAPI.upload(data);
    }

    public Observable<String> confirmorder(String order_id) {
        Map map = new HashMap();
        map.put("username", StorageHelper.get(StorageHelper.USERNAME));
        map.put("deviceid", getDeviceId());
        map.put("token", StorageHelper.get(StorageHelper.TOKEN));
        map.put("order_code", order_id);
        String data = new Gson().toJson(map);
        return gateAPI.confirmorder(data);
    }
    public Observable<String> notiDelivery(String order_code) {
        Map map = new HashMap();
        map.put("username", StorageHelper.get(StorageHelper.USERNAME));
        map.put("deviceid", getDeviceId());
        map.put("token", StorageHelper.get(StorageHelper.TOKEN));
        map.put("order_code", order_code);
        String data = new Gson().toJson(map);
        return gateAPI.notiDelivery(data);
    }

    public Observable<String> getorder(int type, int sort_type, double lat, double lng) {
        return gateAPI.getOrder(StorageHelper.get(StorageHelper.USERNAME), getDeviceId(), StorageHelper.get(StorageHelper.TOKEN), type, sort_type, lat, lng);
    }

    public Observable<String> getstore() {
        return gateAPI.getStore(StorageHelper.get(StorageHelper.USERNAME), getDeviceId(), StorageHelper.get(StorageHelper.TOKEN));
    }

    public Observable<String> getreason() {
        return gateAPI.getReason(StorageHelper.get(StorageHelper.USERNAME), getDeviceId(), StorageHelper.get(StorageHelper.TOKEN));

    }

    public Observable<String> getDetailOrder(String id) {
        return gateAPI.getDetailOrder(StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN), getDeviceId(), id);
    }

    public Observable<String> getTotal(String id, String items) {
        /*try {
            items = URLEncoder.encode(items, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        Observable<String> observable = gateAPI.getTotal(StorageHelper.get(StorageHelper.USERNAME), getDeviceId(), StorageHelper.get(StorageHelper.TOKEN), id, items);
        return observable;
    }

    public Observable<String> getHistory(int sorttype, double lat, double lng, int type) {
        Observable<String> observable = gateAPI.getHistory(StorageHelper.get(StorageHelper.USERNAME), getDeviceId(), StorageHelper.get(StorageHelper.TOKEN), sorttype, lat, lng, type);
        return observable;
    }
    public Observable<String> getProfile(){
        Observable<String> observable = gateAPI.getProfile(StorageHelper.get(StorageHelper.USERNAME), getDeviceId(), StorageHelper.get(StorageHelper.TOKEN));
        return observable;
    }
    public Observable<String> getLocation(String ordercode){
        Observable<String> observable = gateAPI.getLocation(StorageHelper.get(StorageHelper.USERNAME), getDeviceId(), StorageHelper.get(StorageHelper.TOKEN),ordercode);
        return observable;
    }

    public Observable<String> updateDeviceToken(String firebaseDevicetoken) {
        Map map = new HashMap();
        map.put("username", StorageHelper.get(StorageHelper.USERNAME));
        map.put("deviceid", getDeviceId());
        map.put("token", StorageHelper.get(StorageHelper.TOKEN));
        map.put("firebase_devicetoken", firebaseDevicetoken);
        String data = new Gson().toJson(map);
        return gateAPI.updateDeviceToken(data);
    }

    public Observable<String> cancelOrder(String ordercode, String note) {
        Map map = new HashMap();
        map.put("username", StorageHelper.get(StorageHelper.USERNAME));
        map.put("deviceid", getDeviceId());
        map.put("token", StorageHelper.get(StorageHelper.TOKEN));
        map.put("order_code", ordercode);
        map.put("note", note);
        String data = new Gson().toJson(map);
        return gateAPI.cancelOrder(data);
    }
}
