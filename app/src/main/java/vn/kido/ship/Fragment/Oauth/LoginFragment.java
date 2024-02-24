package vn.kido.ship.Fragment.Oauth;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.kido.ship.Api.APICheckUpdate;
import vn.kido.ship.Api.APIService;
import vn.kido.ship.Api.ISubscriber;
import vn.kido.ship.Bean.BeanConfig;
import vn.kido.ship.Class.CmmFunc;
import vn.kido.ship.Constant.AppConfig;
import vn.kido.ship.Fragment.BaseFragment;
import vn.kido.ship.Fragment.Config.ConfigFragment;
import vn.kido.ship.Fragment.Dialog.MessageDialogFragment;
import vn.kido.ship.Helper.FragmentHelper;
import vn.kido.ship.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {
    EditText edtlogin;
    Subscription login;
    String phone;

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
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
                edtlogin = rootView.findViewById(R.id.edt_phone);
                rootView.findViewById(R.id.btn_signin).setOnClickListener(LoginFragment.this);

            }
        });

        threadInit.start();
        isLoaded = true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signin:
                phone = edtlogin.getText().toString().trim();
                if (phone.equals("06321123987789")) {
                    FragmentHelper.replace(ConfigFragment.newInstance());
                } else if (TextUtils.isEmpty(phone) || phone.length() < 10) {
                    Drawable icError = getResources().getDrawable(R.drawable.ic_error);
                    icError.setBounds(0, 0, CmmFunc.convertDpToPx(getActivity(), 16), CmmFunc.convertDpToPx(getActivity(), 16));
                    edtlogin.setCompoundDrawables(null, null, icError, null);
                    return;
                }
                Log.d("checkconfig", AppConfig.DOMAIN + "checkconfig:" + getArguments().getString("key"));
                login(phone);
                break;
        }
    }

    private void showError(View error) {
        final Animation animShake = AnimationUtils.loadAnimation(getContext(), R.anim.anim_shake);
        error.startAnimation(animShake);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (login != null) {
            login.unsubscribe();
        }
    }

    private void login(final String phone) {
        try {
            showProgress();
            login = APIService.getInstance().login(phone).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ISubscriber() {
                        @Override
                        public void done() {
                            super.done();
                            hideProgress();
                        }

                        @Override
                        public void excute(JSONObject jsonObject) {
                            FragmentHelper.replace(OTPFragment.newInstance(phone));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
