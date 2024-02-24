package vn.kido.ship.Fragment.Common;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.kido.ship.Api.APIService;
import vn.kido.ship.Api.ISubscriber;
import vn.kido.ship.Fragment.BaseFragment;
import vn.kido.ship.Fragment.Dialog.ConfirmDialogFragment;
import vn.kido.ship.Fragment.Order.ListHistoryFragment;
import vn.kido.ship.Fragment.Order.ListOrderFragment;
import vn.kido.ship.Fragment.Order.ListStoreFragment;
import vn.kido.ship.Helper.FragmentHelper;
import vn.kido.ship.Helper.NotificationRouteHelper;
import vn.kido.ship.R;

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    ImageView ivAvatar;
    TextView txt_name, txt_customer_level;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
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
                ivAvatar = rootView.findViewById(R.id.iv_avatar);
                txt_name = rootView.findViewById(R.id.txt_name);
                txt_customer_level = rootView.findViewById(R.id.txt_customer_level);
                rootView.findViewById(R.id.ln_order).setOnClickListener(HomeFragment.this);
                rootView.findViewById(R.id.ln_store).setOnClickListener(HomeFragment.this);
                rootView.findViewById(R.id.ln_history).setOnClickListener(HomeFragment.this);


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getProfile();
                    }
                });
            }
        });
        threadInit.start();
        isLoaded = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            Bundle bundle = getActivity().getIntent().getExtras();
            if (bundle != null) {
                if (bundle.containsKey("deep_link_type")) {
                    new NotificationRouteHelper().route(getActivity(), bundle);
                }
            }

            LocationManager mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            boolean enabled;
            if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                enabled = false;
            } else {
                enabled = true;
            }

            if (!enabled) {
                ConfirmDialogFragment confirmDialogFragment = ConfirmDialogFragment.newInstance();
                confirmDialogFragment.setMessage(getString(R.string.confirm_dialog));
                confirmDialogFragment.setTitle(getString(R.string.mess));
                confirmDialogFragment.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        startActivity( new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                });
                confirmDialogFragment.show(getActivity().getSupportFragmentManager(), confirmDialogFragment.getClass().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ln_order:
                FragmentHelper.add(ListOrderFragment.newInstance());
                break;
            case R.id.ln_store:
                FragmentHelper.add(ListStoreFragment.newInstance());
                break;
            case R.id.ln_history:
                FragmentHelper.add(ListHistoryFragment.newInstance());
                break;
        }
    }

    public void getProfile() {
        showProgress();
        APIService.getInstance().getProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ISubscriber() {
                    @Override
                    public void done() {
                        super.done();
                        hideProgress();
                    }

                    @Override
                    public void excute(JSONObject jsonObject) {
                        try {
                            JSONObject data = jsonObject.getJSONObject("data");
                            if (!TextUtils.isEmpty(data.getString("avatar"))) {
                                Picasso.get().load(data.getString("avatar")).placeholder(R.drawable.left_avatar).resize(240, 240).centerInside().into(ivAvatar);
                            }
                            txt_name.setText(data.getString("fullname") + "");
                            txt_customer_level.setText(data.getString("supplier_name") + "");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
