package vn.kido.ship.Fragment.Order;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import java.util.List;

import vn.kido.ship.Adapter.ViewPagerAdapter;
import vn.kido.ship.Fragment.BaseFragment;
import vn.kido.ship.Fragment.Map.MapOrderFragment;
import vn.kido.ship.Helper.FragmentHelper;
import vn.kido.ship.Helper.PermissionHelper;
import vn.kido.ship.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListOrderFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, View.OnClickListener {
    TabLayout tab;
    ViewPager pager;
    ViewPagerAdapter adapter;
    GoogleApiClient mGoogleApiClient;
    View icMap, icSuport;
    public LatLng latLng;
    PermissionHelper permissionHelper;
    final int LATLONG_REQUEST_CODE = 17;

    public static ListOrderFragment newInstance() {
        Bundle args = new Bundle();
        ListOrderFragment fragment = new ListOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ListOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_list_order, container, false);
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
                        .addConnectionCallbacks(ListOrderFragment.this)
                        .build();
                mGoogleApiClient.connect();
                permissionHelper = new PermissionHelper(ListOrderFragment.this);
                tab = rootView.findViewById(R.id.tab);
                pager = rootView.findViewById(R.id.pager);
                final TextView title = rootView.findViewById(R.id.title);
                icMap = rootView.findViewById(R.id.ic_map);
                icSuport = rootView.findViewById(R.id.ic_support);
                adapter = new ViewPagerAdapter(getChildFragmentManager());
                final String[] titles = {getString(R.string.ordernew), getString(R.string.delivering), getString(R.string.delivered)};
                for (int i = 0; i < titles.length; i++) {
                    adapter.addFrag(ChildListOrderFragment.newInstance(i + 1), titles[i]);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        title.setText(R.string.listorder);
                        pager.setAdapter(adapter);
                        pager.setOffscreenPageLimit(6);
                        tab.setupWithViewPager(pager);
                        UITab(titles);
                        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {
                                View row = tab.getCustomView();
                                TextView name = row.findViewById(R.id.name);
                                name.setTextColor(getResources().getColor(R.color.main));
                                Fragment fragment = adapter.getItem(tab.getPosition());
                                if (fragment instanceof ChildListOrderFragment) {
                                    ChildListOrderFragment childNewsFragment = (ChildListOrderFragment) fragment;
                                    childNewsFragment.onRefresh();
                                }
                                /*if (tab.getPosition() !=-1) {
                                    Fragment fragmentlistorderchild = adapter.getItem(tab.getPosition());
                                    if (fragment instanceof ChildListOrderFragment) {
                                        ChildListOrderFragment childFragment = (ChildListOrderFragment) fragmentlistorderchild;
                                        childFragment.onRefresh();
                                    }
                                }*/
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
                        icMap.setOnClickListener(ListOrderFragment.this);
                        icSuport.setOnClickListener(ListOrderFragment.this);
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
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            Fragment fragment = adapter.getItem(tab.getSelectedTabPosition());
            if (fragment instanceof ChildListOrderFragment) {
                ChildListOrderFragment childNewsFragment = (ChildListOrderFragment) fragment;
                childNewsFragment.onRefresh();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
//                            Log.d("listorder", latLng+"");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_map:
                try {
                    if (tab.getSelectedTabPosition() == 3) {
                        return;
                    }
                    Fragment fragment = MapOrderFragment.newInstance(tab.getSelectedTabPosition() + 1);
                    String tag = fragment.getClass().getName();
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    manager.beginTransaction()
                            .add(R.id.home_content, fragment, tag)
                            .addToBackStack(tag)
                            .commitAllowingStateLoss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ic_support:
                Intent itsp = new Intent(Intent.ACTION_DIAL);
                itsp.setData(Uri.parse("tel:" + "+" + R.string.phone_support));
                startActivity(itsp);

                break;
        }
    }
}
