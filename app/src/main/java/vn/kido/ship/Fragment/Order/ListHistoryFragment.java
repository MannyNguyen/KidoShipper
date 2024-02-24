package vn.kido.ship.Fragment.Order;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import vn.kido.ship.Adapter.ViewPagerAdapter;
import vn.kido.ship.Fragment.BaseFragment;
import vn.kido.ship.Helper.PermissionHelper;
import vn.kido.ship.R;


public class ListHistoryFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks {
    TabLayout tab;
    ViewPager pager;
    ViewPagerAdapter adapter;
    GoogleApiClient mGoogleApiClient;
    ImageView ic_support;
    public LatLng latLng;
    PermissionHelper permissionHelper;
    final int LATLONG_REQUEST_CODE = 17;

    public static ListHistoryFragment newInstance() {
        Bundle args = new Bundle();
        ListHistoryFragment fragment = new ListHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ListHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_list_history, container, false);
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
                mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(ListHistoryFragment.this)
                        .build();
                mGoogleApiClient.connect();
                permissionHelper = new PermissionHelper(ListHistoryFragment.this);
                tab = rootView.findViewById(R.id.tab);
                pager = rootView.findViewById(R.id.pager);
                final TextView title = rootView.findViewById(R.id.title);
                ic_support = rootView.findViewById(R.id.ic_support);
                adapter = new ViewPagerAdapter(getChildFragmentManager());
                final String[] titles = {getString(R.string.completed), getString(R.string.cancelled)};
                for (int i = 0; i < titles.length; i++) {
                    adapter.addFrag(ChildListHistoryOrderFragment.newInstance(i + 1), titles[i]);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        title.setText(R.string.history);
                        pager.setAdapter(adapter);
                        pager.setOffscreenPageLimit(6);
                        tab.setupWithViewPager(pager);
                        UITab(titles);
                        ic_support.setVisibility(View.GONE);
                        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {
                                View row = tab.getCustomView();
                                TextView name = row.findViewById(R.id.name);
                                name.setTextColor(getResources().getColor(R.color.main));
                            }

                            @Override
                            public void onTabUnselected(TabLayout.Tab tab) {
                                View row = tab.getCustomView();
                                TextView name = row.findViewById(R.id.name);
                                name.setTextColor(getResources().getColor(R.color.gray_700));
                            }

                            @Override
                            public void onTabReselected(TabLayout.Tab tab) {

                            }

                        });
                    }
                });


            }
        });
        threadInit.start();
        isLoaded = true;
    }

    private void UITab(String[] titles) {
        try {
            for (int i = 0; i < titles.length; i++) {
                View row = getActivity().getLayoutInflater().inflate(R.layout.tab_layout, null);
                TextView name = row.findViewById(R.id.name);
                name.setText(titles[i]);
                tab.getTabAt(i).setCustomView(row);
            }
            View row = tab.getTabAt(0).getCustomView();
            TextView name = row.findViewById(R.id.name);
            name.setTextColor(getResources().getColor(R.color.main));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case LATLONG_REQUEST_CODE:
                requestPermission(LATLONG_REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void manuResume() {
        super.manuResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        for (int i = 0; i < adapter.getCount(); i++) {
            Fragment fragment = adapter.getItem(i);
            if (fragment instanceof ChildListHistoryOrderFragment) {
                ChildListHistoryOrderFragment childFragment = (ChildListHistoryOrderFragment) fragment;
                childFragment.onRefresh();
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case LATLONG_REQUEST_CODE:
                boolean isPerpermissionForAllGranted = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        isPerpermissionForAllGranted = false;
                    }
                }
                if (isPerpermissionForAllGranted) {
                    requestPermission(LATLONG_REQUEST_CODE);
                }
                break;
        }
    }

    private void requestPermission(int requestCode) {
        if (permissionHelper.requestPermission(requestCode, Manifest.permission.ACCESS_FINE_LOCATION, "Xin cấp quyền lấy vị trí cho Kido!")) {
            getLastLocation();
        }

    }

    private void getLastLocation() {
        LocationRequest locationRequest = LocationRequest.create()
                .setNumUpdates(1)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(0);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestPermission(LATLONG_REQUEST_CODE);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

}
