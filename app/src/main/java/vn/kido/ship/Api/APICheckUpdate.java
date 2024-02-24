package vn.kido.ship.Api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import rx.Observable;
import vn.kido.ship.Constant.AppConfig;

public class APICheckUpdate {
    private interface GateAPI {
        @GET("config/android/shipper_info.json")
        //@Headers({"Content-Type: application/json;charset=UTF-8"})
        Observable<String> check();
    }

    private GateAPI gateAPI;
    private static APICheckUpdate instance;

    public static APICheckUpdate getInstance() {
        if (instance == null) {
            instance = new APICheckUpdate();
        }
        return instance;
    }

    public APICheckUpdate() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.UPDATE)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        gateAPI = retrofit.create(GateAPI.class);
    }
    public Observable<String> checkUpdate() {
        return gateAPI.check();
    }

}
