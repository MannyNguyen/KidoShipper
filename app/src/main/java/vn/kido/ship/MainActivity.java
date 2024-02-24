package vn.kido.ship;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.kido.ship.Api.APICheckUpdate;
import vn.kido.ship.Api.APIService;
import vn.kido.ship.Api.ISubscriber;
import vn.kido.ship.Bean.BeanConfig;
import vn.kido.ship.Class.GlobalClass;
import vn.kido.ship.Constant.AppConfig;
import vn.kido.ship.Fragment.Config.ConfigFragment;
import vn.kido.ship.Fragment.Dialog.MessageDialogFragment;
import vn.kido.ship.Helper.StorageHelper;

public class MainActivity extends FragmentActivity {
    public List<BeanConfig> configs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalClass.setActivity(MainActivity.this);
        StorageHelper.init(getApplicationContext());
//        Check();
        checkUpdate();


    }

    private void route() {
        if (TextUtils.isEmpty(StorageHelper.get(StorageHelper.TOKEN))) {
            Intent intent = new Intent(MainActivity.this, OauthActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        //Đã Login
        try {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            Bundle bundle = MainActivity.this.getIntent().getExtras();
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            Uri data = getIntent().getData();
            if (data != null) {
                intent.setData(data);
            }
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void checkUpdate() {
        APIService.setInstance(null);
        APICheckUpdate.getInstance().checkUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ISubscriber() {
                    @Override
                    public void done() {
                        super.done();
                    }

                    @Override
                    public void excute(JSONObject jsonObject) {
                        try {
                            if (jsonObject == null) {
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(Object response) {
                        try {
                            super.onNext(response);
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray ja = jsonObject.getJSONArray("config");
                            configs = new ArrayList<>();
                            for (int i = 0; i < ja.length(); i++) {
                                ja.get(i).toString();
                                final BeanConfig beanConfig = new Gson().fromJson(ja.get(i).toString(), BeanConfig.class);
                                if (AppConfig.VERSION.equals(beanConfig.getVersion())) {
/*                                    if (beanConfig.getUpdate_status().equals("0")) {
                                        beanConfig.setUpdate_status("1");
                                    }*/
                                    AppConfig.DOMAIN = beanConfig.getRestful_ip();
                                    if (StorageHelper.get(StorageHelper.SERVER).equals("SERVER")) {
                                        AppConfig.DOMAIN = beanConfig.getRestful_ip();
                                    }
                                    if (StorageHelper.get(StorageHelper.SERVER).equals(ConfigFragment.DEV)) {
                                        AppConfig.DOMAIN = beanConfig.getRestful_ip_dev();
                                    }
                                    if (StorageHelper.get(StorageHelper.SERVER).equals(ConfigFragment.TEST)) {
                                        AppConfig.DOMAIN = beanConfig.getRestful_ip_test();
                                    }
                                    if (beanConfig.getUpdate_status().equals("0")) {

                                        route();
                                    } else if (beanConfig.getUpdate_status().equals("1")) {
                                        MessageDialogFragment messageDialogFragment = MessageDialogFragment.newInstance();
                                        messageDialogFragment.setTitle(getString(R.string.update));
                                        messageDialogFragment.setMessage(getString(R.string.textupdate));
                                        messageDialogFragment.setCancelable(false);
/*                                        messageDialogFragment.setCancle(new Runnable() {
                                            @Override
                                            public void run() {
                                                AppConfig.DOMAIN = beanConfig.getRestful_ip();
                                                route();
                                            }
                                        });*/

                                        messageDialogFragment.setRunnable(new Runnable() {
                                            @Override
                                            public void run() {
//                                                AppConfig.DOMAIN = beanConfig.getRestful_ip();
//                                                Log.d("DDDDDDDDD",beanConfig.getRestful_ip());
//                                                route();
                                                finishAffinity();
//                                                String url = beanConfig.getAndroid_url();
                                                String url = "https://play.google.com/store/apps/details?id=" + "vn.kido.ship";
                                                if (!url.startsWith("http://") && !url.startsWith("https://"))
                                                    url = "http://" + url;
                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                                startActivity(browserIntent);
                                            }
                                        });
                                        messageDialogFragment.show(getSupportFragmentManager(), messageDialogFragment.getClass().getName());

                                        Toast.makeText(MainActivity.this, "Đã có bản update mới", Toast.LENGTH_SHORT).show();
                                    } else if (beanConfig.getUpdate_status().equals("2")) {
                                        MessageDialogFragment messageDialogFragment = MessageDialogFragment.newInstance();
                                        messageDialogFragment.setTitle(getString(R.string.update));
                                        messageDialogFragment.setMessage(getString(R.string.textupdate));
                                        messageDialogFragment.setCancelable(false);
                                        messageDialogFragment.setRunnable(new Runnable() {
                                            @Override
                                            public void run() {
//                                                AppConfig.DOMAIN = beanConfig.getRestful_ip();
//                                                route();
                                                finishAffinity();
//                                                String url = beanConfig.getAndroid_url();
                                                String url = "https://play.google.com/store/apps/details?id=" + "vn.kido.ship";
                                                if (!url.startsWith("http://") && !url.startsWith("https://"))
                                                    url = "http://" + url;
                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                                startActivity(browserIntent);
                                            }
                                        });
                                        messageDialogFragment.show(getSupportFragmentManager(), messageDialogFragment.getClass().getName());
                                        Toast.makeText(MainActivity.this, "Bạn phải cập nhật bản mới nhất", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                }
                                configs.add(beanConfig);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
