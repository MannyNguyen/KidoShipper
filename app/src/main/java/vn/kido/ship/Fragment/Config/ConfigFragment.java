package vn.kido.ship.Fragment.Config;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.kido.ship.Api.APICheckUpdate;
import vn.kido.ship.Api.ISubscriber;
import vn.kido.ship.Bean.BeanConfig;
import vn.kido.ship.Constant.AppConfig;
import vn.kido.ship.Fragment.BaseFragment;
import vn.kido.ship.Fragment.Dialog.MessageDialogFragment;
import vn.kido.ship.Fragment.Oauth.LoginFragment;
import vn.kido.ship.Helper.FragmentHelper;
import vn.kido.ship.Helper.StorageHelper;
import vn.kido.ship.MainActivity;
import vn.kido.ship.OauthActivity;
import vn.kido.ship.R;


/*public class ConfigFragment extends BaseFragment {
    Button btnDev, btnTest;

    public static ConfigFragment newInstance() {
        Bundle args = new Bundle();
        ConfigFragment fragment = new ConfigFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public ConfigFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_config, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isLoaded) {
            return;
        }
        btnDev = getView().findViewById(R.id.btndev);
        btnTest = getView().findViewById(R.id.btntest);
        btnDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHelper.replace(LoginFragment.newInstance("dev"));
            }
        });
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHelper.replace(LoginFragment.newInstance("test"));
            }
        });
        isLoaded = true;
    }
}*/
public class ConfigFragment  extends BaseFragment implements View.OnClickListener {
    public static final String DEV = "server_dev";
    public static final String TEST = "server_test";

    Button btnDev, btnTest;

    public ConfigFragment() {
        // Required empty public constructor
    }

    public static ConfigFragment newInstance() {
        Bundle args = new Bundle();
        ConfigFragment fragment = new ConfigFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_config, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isLoaded) {
            return;
        }
        threadInit = new Thread(new Runnable() {
            @Override
            public void run() {
                btnDev = rootView.findViewById(R.id.btndev);
                btnTest = rootView.findViewById(R.id.btntest);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnDev.setOnClickListener(ConfigFragment.this);
                        btnTest.setOnClickListener(ConfigFragment.this);
                    }
                });
            }
        });
        threadInit.start();
        isLoaded = true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btndev:
                StorageHelper.set(StorageHelper.SERVER, DEV);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btntest:
                StorageHelper.set(StorageHelper.SERVER, TEST);
                Intent intent2 = new Intent(getActivity(), MainActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
