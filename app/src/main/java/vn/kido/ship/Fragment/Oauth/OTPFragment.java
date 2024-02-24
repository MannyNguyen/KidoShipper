package vn.kido.ship.Fragment.Oauth;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.kido.ship.Api.APIService;
import vn.kido.ship.Api.ISubscriber;
import vn.kido.ship.Fragment.BaseFragment;
import vn.kido.ship.Fragment.Common.HomeFragment;
import vn.kido.ship.Helper.FragmentHelper;
import vn.kido.ship.Helper.StorageHelper;
import vn.kido.ship.HomeActivity;
import vn.kido.ship.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OTPFragment extends BaseFragment {
    Subscription verify, login;
    EditText edtOTP;
    Button resend;
    ImageView imvback;

    public static OTPFragment newInstance(String username) {
        Bundle args = new Bundle();
        OTPFragment fragment = new OTPFragment();
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }


    public OTPFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_otp, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isLoaded) {
            return;
        }
        try {
            resend = getView().findViewById(R.id.btn_resend);
            edtOTP = getView().findViewById(R.id.edt_otp);
            imvback = getView().findViewById(R.id.back);
            edtOTP.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().length() > 3) {
                        String value = s.toString().substring(0, 4);
                        verifyOTP(getArguments().getString("username"), value);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            imvback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentHelper.pop(getActivity());
                }
            });

            resend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (login != null && login.isUnsubscribed()) {
                        login.unsubscribe();
                    }
                    resendOTP(getArguments().getString("username"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        isLoaded = true;
    }

    private void login(final String phone) {
        try {
            login = (Subscription) APIService.getInstance().login(phone).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ISubscriber() {
                        @Override
                        public void excute(JSONObject jsonObject) {
                            try {
                                if (jsonObject == null) {
                                    return;
                                }
                                int code = jsonObject.getInt("code");
                                if (code == 1) {
//                                    FragmentHelper.add(OTPFragment.newInstance(phone));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void resendOTP(final String phone) {
        try {
            login = (Subscription) APIService.getInstance().resendOTP(phone).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ISubscriber() {
                        @Override
                        public void excute(JSONObject jsonObject) {
                            try {
                                if (jsonObject == null) {
                                    return;
                                }
                                int code = jsonObject.getInt("code");
                                if (code == 1) {
//                                    FragmentHelper.add(OTPFragment.newInstance(phone));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void verifyOTP(final String phone, String otp) {
        try {

            verify = APIService.getInstance().verifyOTP(phone, otp).subscribeOn(Schedulers.io())
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
                                int code = jsonObject.getInt("code");
                                if (code == 1) {
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    StorageHelper.set(StorageHelper.TOKEN, data.getString("token"));
                                    StorageHelper.set(StorageHelper.USERNAME, phone);
                                    StorageHelper.set(StorageHelper.SUP_ID, data.getString("sup_id"));
                                    Fragment fragment = FragmentHelper.findFragmentByTag(LoginFragment.class);
                                    if (fragment != null) {
                                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
//                                        FragmentHelper.add(HomeFragment.newInstance());
                                    } else {
                                        /*Intent intent = new Intent(getActivity(), HomeActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();*/
                                        Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
