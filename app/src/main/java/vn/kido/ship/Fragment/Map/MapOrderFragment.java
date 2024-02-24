package vn.kido.ship.Fragment.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.kido.ship.Adapter.OrderAdapter;
import vn.kido.ship.Api.APIService;
import vn.kido.ship.Api.ISubscriber;
import vn.kido.ship.Bean.BeanOrder;
import vn.kido.ship.Class.CmmFunc;
import vn.kido.ship.Fragment.BaseFragment;
import vn.kido.ship.Fragment.Common.HomeFragment;
import vn.kido.ship.Fragment.Order.ChangeFragment;
import vn.kido.ship.Fragment.Order.DetailOrderFragment;
import vn.kido.ship.Fragment.Order.ListOrderFragment;
import vn.kido.ship.Fragment.Order.VerifyOrderFragment;
import vn.kido.ship.Helper.FragmentHelper;
import vn.kido.ship.Helper.MapHelper;
import vn.kido.ship.Helper.PermissionHelper;
import vn.kido.ship.R;


public class MapOrderFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, View.OnClickListener {

    GoogleApiClient mGoogleApiClient;
    GoogleMap map;
    MarkerOptions markerShipper;
    ImageButton ic_support, ic_list;
    private LatLng latLng;
    PermissionHelper permissionHelper;
    final int LATLONG_REQUEST_CODE = 17;
    TextView btnDelivered;
    TextView btnNewOrder;
    TextView btnOrder;
    TextView txvOrderCode;
    TextView txvAddressOrder;
    TextView txvReason;
    TextView txvNote;
    TextView txvNameShop;
    RecyclerView rcvDelivered;
    View fmMap;
    int currentTab;
    String orderCode, phone, order_type_name;
    int order_step, type;
    ImageView dropdown;
    LinearLayout lnThreeButton, lnTwoButton, lnNote, lnReason;
    Button btnFinish, btnExchange, btnStart;
    String tag;
    TextView txvIndex;
    FrameLayout bottom;
    BottomSheetBehavior bottomSheetBehavior;


    //
    Polyline polylineActive;
    Marker markerActive;


    Subscription subscriptionOrders;
    List<Marker> markers;
    List<BeanOrder> orders;

    public static MapOrderFragment newInstance(int tabPosition) {
        Bundle args = new Bundle();
        args.putInt("tab_position", tabPosition);
        MapOrderFragment fragment = new MapOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MapOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_map_order, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (isLoaded) {
            return;
        }
        rootView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHelper.pop(getActivity(), HomeFragment.class);
            }
        });
        currentTab = getArguments().getInt("tab_position");
        threadInit = new Thread(new Runnable() {
            @Override
            public void run() {
                mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(MapOrderFragment.this)
                        .build();
                mGoogleApiClient.connect();
                permissionHelper = new PermissionHelper(MapOrderFragment.this);
                final TextView title = rootView.findViewById(R.id.title);
                ic_support = rootView.findViewById(R.id.ic_support);
                ic_list = rootView.findViewById(R.id.ic_list);
                btnDelivered = rootView.findViewById(R.id.btn_delivered_order);
                btnDelivered.setOnClickListener(MapOrderFragment.this);
                btnNewOrder = rootView.findViewById(R.id.btn_new_order);
                btnNewOrder.setOnClickListener(MapOrderFragment.this);
                btnOrder = rootView.findViewById(R.id.btn_order);
                txvOrderCode = rootView.findViewById(R.id.order_code);
                txvAddressOrder = rootView.findViewById(R.id.txv_address_order);
                txvNameShop = rootView.findViewById(R.id.txv_nameshop_order);
                txvNote = rootView.findViewById(R.id.txv_note);
                txvReason = rootView.findViewById(R.id.txv_reason);
                rcvDelivered = rootView.findViewById(R.id.rcv_delivered);
                fmMap = rootView.findViewById(R.id.fm_map);
                rootView.findViewById(R.id.call_user).setOnClickListener(MapOrderFragment.this);
                rootView.findViewById(R.id.ic_detail_order).setOnClickListener(MapOrderFragment.this);
                rootView.findViewById(R.id.sms_user).setOnClickListener(MapOrderFragment.this);
                rootView.findViewById(R.id.btn_exchange).setOnClickListener(MapOrderFragment.this);
                dropdown = rootView.findViewById(R.id.drop_down_up);
                lnThreeButton = rootView.findViewById(R.id.delivery_container);
                lnTwoButton = rootView.findViewById(R.id.ln_two_button);
                lnNote = rootView.findViewById(R.id.ln_note);
                lnReason = rootView.findViewById(R.id.ln_reason);
                btnFinish = rootView.findViewById(R.id.btnfinish);
                btnExchange = rootView.findViewById(R.id.btn_exchange);
                btnStart = rootView.findViewById(R.id.btn_cancel);
                txvIndex = rootView.findViewById(R.id.index);
                bottom = rootView.findViewById(R.id.bottom);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ic_support.setOnClickListener(MapOrderFragment.this);
                            ic_list.setOnClickListener(MapOrderFragment.this);
                            changeLayout(currentTab);
                            title.setText(R.string.road);
                            ic_support.setVisibility(View.VISIBLE);
                            showProgress();

                            btnNewOrder.setOnClickListener(MapOrderFragment.this);
                            btnNewOrder.setOnClickListener(MapOrderFragment.this);
                            btnOrder.setOnClickListener(MapOrderFragment.this);
                            rcvDelivered.setLayoutManager(new LinearLayoutManager(getActivity()));
                            rootView.findViewById(R.id.my_location).setOnClickListener(MapOrderFragment.this);

                            dropdown.setOnClickListener(MapOrderFragment.this);

                            bottomSheetBehavior = BottomSheetBehavior.from(bottom);
                            /*int sizePeek = CmmFunc.convertDpToPx(getActivity(), 194);
                            bottomSheetBehavior.setPeekHeight(sizePeek);*/
                            bottomSheetBehavior.setHideable(true);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
        threadInit.start();
        isLoaded = true;
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
    public void manuResume() {
        super.manuResume();
        try {
            if (isLoaded) {
                return;
            }
            changeLayout(currentTab);
            getOrders(currentTab);
            isLoaded = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        final LocationRequest locationRequest = LocationRequest.create()
                .setNumUpdates(1)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(0);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {
                    try {
                        hideProgress();
                        setLatLng(new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()));
                        changeLayout(currentTab);
                        getOrders(currentTab);
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, null);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getMap();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        requestPermission(LATLONG_REQUEST_CODE);
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (BeanOrder beanOrder : orders) {
                    if (beanOrder.getOrder_code().equals(marker.getSnippet())) {
                        txvOrderCode.setText(beanOrder.getOrder_code());
                        txvAddressOrder.setText(beanOrder.getAddress_delivery());
                        txvNameShop.setText(beanOrder.getShop_name());
                        if (beanOrder.getNote().equals("")) {
                            lnNote.setVisibility(View.GONE);
                        } else {
                            lnNote.setVisibility(View.VISIBLE);
                            txvNote.setText(beanOrder.getNote());
                        }
                        if (beanOrder.getReason().equals("")) {
                            lnReason.setVisibility(View.GONE);
                        } else {
                            lnReason.setVisibility(View.VISIBLE);
                            txvReason.setText(beanOrder.getReason());
                        }

                        orderCode = beanOrder.getOrder_code();
                        phone = beanOrder.getPhone();
                        order_step = beanOrder.getOrder_step();
                        order_type_name = beanOrder.getOrder_type_name();
                        type = beanOrder.getType();
                        statusOrder();

                        //clear & change ui
                        if (polylineActive != null) {
                            polylineActive.remove();
                        }
                        if (markerActive != null) {
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(MapHelper.createMarker(R.layout.marker_inactive, markerActive.getTag() + ""));
                            tag = markerActive.getTag() + "";
                            markerActive.setIcon(icon);
                            //markerActive.setAnchor(0.5f, 1);
                        }
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(MapHelper.createMarker(R.layout.marker_active, marker.getTag() + ""));
                        marker.setIcon(icon);
                        //marker.setAnchor(0.5f, 0.5f);
                        markerActive = marker;

                        txvIndex.setText(marker.getSnippet());
                        draw(getLatLng(), new LatLng(beanOrder.getLat(), beanOrder.getLng()));

                        gonePopup(true);
                    }
                }
                return true;
            }
        });
    }

    private void getMap() {
        try {
            final SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
            android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.add(R.id.map_view, supportMapFragment).commit();
            supportMapFragment.getMapAsync(MapOrderFragment.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMarkerShipper(LatLng latLng) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(MapHelper.createMarker(R.layout.marker_shipper, ""));
        markerShipper = new MarkerOptions().position(latLng)
                .flat(false)
                .anchor(0.5f, 0.5f)
                .icon(icon);
        map.addMarker(markerShipper);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_new_order:
                currentTab = 1;
                if (fmMap.getVisibility() == View.GONE) {
                    fmMap.setVisibility(View.VISIBLE);
                    rcvDelivered.setVisibility(View.GONE);
                }
                changeLayout(currentTab);
                getOrders(currentTab);
                addMarkerShipper(getLatLng());
                break;
            case R.id.btn_delivered_order:
                currentTab = 2;
                if (fmMap.getVisibility() == View.GONE) {
                    fmMap.setVisibility(View.VISIBLE);
                    rcvDelivered.setVisibility(View.GONE);
                }
                changeLayout(currentTab);
                getOrders(currentTab);
                addMarkerShipper(getLatLng());
                break;
            case R.id.btn_order:
                currentTab = 3;
                if (rcvDelivered.getVisibility() == View.GONE) {
                    fmMap.setVisibility(View.GONE);
                    rcvDelivered.setVisibility(View.VISIBLE);
                }
                changeLayout(currentTab);
                getOrders(currentTab);
                addMarkerShipper(getLatLng());
                break;
            case R.id.drop_down_up:
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    gonePopup(false);
                } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    gonePopup(true);
                }

                break;
            case R.id.call_user:
                Intent it = new Intent(Intent.ACTION_DIAL);
                it.setData(Uri.parse("tel:" + "+" + phone));
                startActivity(it);

                break;
            case R.id.sms_user:
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setType("vnd.android-dir/mms-sms");
                sendIntent.putExtra("address", "" + phone);
                startActivity(sendIntent);
                break;

            case R.id.my_location:
                if (latLng == null) {
                    return;
                }
                MapHelper.animateCenter(map, getLatLng());
                break;
            case R.id.ic_detail_order:
                if (!orderCode.equals("")) {
                    FragmentHelper.add(DetailOrderFragment.newInstance(orderCode));
                    return;
                }
                break;
            case R.id.btn_exchange:
                FragmentHelper.add(ChangeFragment.newInstance(orderCode));
                break;
            case R.id.ic_support:
                Intent itsp = new Intent(Intent.ACTION_DIAL);
                itsp.setData(Uri.parse("tel:" + "+" + R.string.phone_support));
                startActivity(itsp);

                break;
            case R.id.ic_list:
                try {
                    FragmentHelper.pop(getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    private void gonePopup(boolean gone) {
        if (gone == false) {
            txvIndex.setText(tag);
            rootView.findViewById(R.id.top_container).setVisibility(View.INVISIBLE);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            dropdown.setImageResource(R.drawable.ic_open_up);
        } else {
            txvIndex.setText(tag);
            rootView.findViewById(R.id.top_container).setVisibility(View.VISIBLE);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            dropdown.setImageResource(R.drawable.ic_dropdown);
        }
    }

    private void changeLayout(int tab) {
        btnDelivered.setTextColor(getResources().getColor(R.color.gray_700));
        btnDelivered.setBackground(getResources().getDrawable(R.drawable.background_tab_black));
        btnNewOrder.setTextColor(getResources().getColor(R.color.gray_700));
        btnNewOrder.setBackground(getResources().getDrawable(R.drawable.background_tab_black));
        btnOrder.setTextColor(getResources().getColor(R.color.gray_700));
        btnOrder.setBackground(getResources().getDrawable(R.drawable.background_tab_black));
        switch (tab) {
            case 1:
                btnNewOrder.setTextColor(getResources().getColor(R.color.main));
                btnNewOrder.setBackground(getResources().getDrawable(R.drawable.background_tab));
                break;
            case 2:
                btnDelivered.setTextColor(getResources().getColor(R.color.main));
                btnDelivered.setBackground(getResources().getDrawable(R.drawable.background_tab));
                break;
            case 3:
                btnOrder.setTextColor(getResources().getColor(R.color.main));
                btnOrder.setBackground(getResources().getDrawable(R.drawable.background_tab));
                if (rcvDelivered.getVisibility() == View.GONE) {
                    fmMap.setVisibility(View.GONE);
                    rcvDelivered.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    private void notiDelivery(final String code) {
        try {
            showProgress();
            APIService.getInstance().notiDelivery(code).subscribeOn(Schedulers.io())
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
                                if (jsonObject == null) {
                                    return;
                                }
                                int code = jsonObject.getInt("code");
                                if (code == 1) {
                                    Log.d("Success", "success!");
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

    private void confirmorder(final String id) {
        try {
            showProgress();
            APIService.getInstance().confirmorder(id).subscribeOn(Schedulers.io())
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
                                if (jsonObject == null) {
                                    return;
                                }
                                int code = jsonObject.getInt("code");
                                if (code == 1) {
                                    currentTab = 2;
                                    if (fmMap.getVisibility() == View.GONE) {
                                        fmMap.setVisibility(View.VISIBLE);
                                        rcvDelivered.setVisibility(View.GONE);
                                    }
                                    changeLayout(currentTab);
                                    getOrders(currentTab);
                                    addMarkerShipper(getLatLng());
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

    private void statusOrder() {
        if (order_step == 1) {
            lnThreeButton.setVisibility(View.VISIBLE);
            btnStart.setText(R.string.start);
            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmorder(orderCode);
                }
            });
            btnFinish.setVisibility(View.GONE);
            int sizePeek = CmmFunc.convertDpToPx(getActivity(), 230);
            bottomSheetBehavior.setPeekHeight(sizePeek);
        } else if (order_step == 2 && type != 1) {
            lnThreeButton.setVisibility(View.VISIBLE);
            btnStart.setText(R.string.cancle_order);
            btnStart.setTextColor(getResources().getColor(R.color.main));
            btnStart.setBackground(getResources().getDrawable(R.drawable.red_border_box));
            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FragmentHelper.add(VerifyOrderFragment.newInstance(orderCode + "", 0, ""));
                    notiDelivery(orderCode);
                }
            });
            btnFinish.setVisibility(View.VISIBLE);
            btnFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentHelper.add(VerifyOrderFragment.newInstance(orderCode + "", 0, ""));
                    notiDelivery(orderCode);
                }
            });
            int sizePeek = CmmFunc.convertDpToPx(getActivity(), 134);
            bottomSheetBehavior.setPeekHeight(sizePeek);
        } else if (order_step == 2 && type == 1) {
            lnThreeButton.setVisibility(View.VISIBLE);
            btnStart.setText(R.string.finished);
            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentHelper.add(VerifyOrderFragment.newInstance(orderCode + "", 0, ""));
                    notiDelivery(orderCode);
                }
            });
            btnFinish.setVisibility(View.GONE);
            int sizePeek = CmmFunc.convertDpToPx(getActivity(), 230);
            bottomSheetBehavior.setPeekHeight(sizePeek);
        } else if (order_step >= 3) {
            lnThreeButton.setVisibility(View.VISIBLE);
            if (order_step > 3) {
                rootView.findViewById(R.id.seek_container).setVisibility(View.GONE);
            } else {
                rootView.findViewById(R.id.seek_container).setVisibility(View.VISIBLE);
            }

            lnThreeButton.setVisibility(View.GONE);
        }
    }

    public void getOrders(final int tabmap) {
        showProgress();
        clearMap();
        addMarkerShipper(getLatLng());
        subscriptionOrders = APIService.getInstance().getorder(tabmap, 2, getLatLng().latitude, getLatLng().longitude)
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
                            JSONArray data = jsonObject.getJSONArray("data");
                            orders = new ArrayList<>();

                            if (tabmap == 3) {
                                //show list
                                for (int i = 0; i < data.length(); i++) {
                                    BeanOrder beanOrder = new Gson().fromJson(data.getString(i), BeanOrder.class);
                                    orders.add(beanOrder);

                                }
                                OrderAdapter orderAdapter = new OrderAdapter(MapOrderFragment.this, rcvDelivered, orders, 3);
                                orderAdapter.notifyDataSetChanged();
                                rcvDelivered.setAdapter(orderAdapter);
                                return;
                            }

                            markers = new ArrayList<>();
                            List<LatLng> bounds = new ArrayList<>();
                            for (int i = 0; i < data.length(); i++) {
                                BeanOrder beanOrder = new Gson().fromJson(data.getString(i), BeanOrder.class);
                                orders.add(beanOrder);
                                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(MapHelper.createMarker(R.layout.marker_inactive, (i + 1) + ""));

                                beanOrder.setLat(10.80294 + i);
                                beanOrder.setLng(107.714642);
                                MarkerOptions mo = new MarkerOptions().position(new LatLng(beanOrder.getLat(), beanOrder.getLng()))
                                        .flat(false)
                                        .anchor(0.5f, 0.5f)
                                        .snippet(beanOrder.getOrder_code())
                                        .icon(icon);
                                Marker marker = map.addMarker(mo);
                                marker.setTag(i + 1);
                                markers.add(marker);
                                bounds.add(marker.getPosition());
                            }

                            bounds.add(getLatLng());
                            MapHelper.bound(map, bounds);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void draw(final LatLng origin, final LatLng dest) {
        if (origin.latitude == 0) {
            return;
        }
        if (dest.latitude == 0) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final PolylineOptions polylineOptions = MapHelper.getPolylineOptions(origin, dest);
                    if (polylineOptions == null) {
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            polylineActive = map.addPolyline(polylineOptions);
                            List<LatLng> latLngs = new ArrayList<>();
                            latLngs.add(origin);
                            latLngs.add(dest);
                            MapHelper.bound(map, latLngs);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    public LatLng getLatLng() {
        if (latLng == null) {
            latLng = new LatLng(0, 0);
        }
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    private void clearMap() {
        try {
            if (map == null) {
                return;
            }
            markerActive = null;
            polylineActive = null;
            txvOrderCode.setText("");
            txvAddressOrder.setText("");
            txvNameShop.setText("");
            txvNote.setText("");
            txvReason.setText("");
            if (txvNote.getText().toString().equals("")) {
                lnNote.setVisibility(View.GONE);
            }
            if (txvReason.getText().toString().equals("")) {
                lnReason.setVisibility(View.GONE);
            }
            rootView.findViewById(R.id.top_container).setVisibility(View.INVISIBLE);
            bottomSheetBehavior.setHideable(true);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            map.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
