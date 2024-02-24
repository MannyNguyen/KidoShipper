package vn.kido.ship.Fragment.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.kido.ship.Api.APIService;
import vn.kido.ship.Api.ISubscriber;
import vn.kido.ship.Fragment.BaseFragment;
import vn.kido.ship.Helper.PermissionHelper;
import vn.kido.ship.R;


public class DetailMapFragment extends BaseFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    GoogleMap mGoogleMap;
    DateTime now = new DateTime();
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LatLng currentLatLng, newLatLng;
    Object dataTransfer[] = new Object[2];
    double lat, lng, currentLat, currentLng;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    final int LATLONG_REQUEST_CODE = 17;
    private static final int overview = 0;
    PermissionHelper permissionHelper;

    public DetailMapFragment() {
        // Required empty public constructor
    }

    public static DetailMapFragment newInstance() {
        Bundle args = new Bundle();
        DetailMapFragment fragment = new DetailMapFragment();
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
        rootView = inflater.inflate(R.layout.fragment_detail_map, container, false);
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
                permissionHelper = new PermissionHelper(DetailMapFragment.this);
                TextView title = rootView.findViewById(R.id.title);

                title.setText(getString(R.string.road));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getMap();
                        getLocation("TACTV26100006");
                    }
                });
            }
        });
        threadInit.start();
        isLoaded = true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getMap() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    final SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
                    android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.detail_map_container, supportMapFragment).commit();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            supportMapFragment.getMapAsync(DetailMapFragment.this);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Hàm xin quyền google
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
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

    private void requestPermission(int requestCode) {
        if (permissionHelper.requestPermission(requestCode, Manifest.permission.ACCESS_FINE_LOCATION, "Xin cấp quyền lấy vị trí cho Kido!")) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (ContextCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
//        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(false);
            } else {
                //Request Location Permission
//                checkLocationPermission();
                requestPermission(LATLONG_REQUEST_CODE);
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(false);
        }
//        getLocation("TACTV26100006");
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        currentLat = location.getLatitude();
        currentLng = location.getLongitude();
        currentLatLng = new LatLng(currentLat, currentLng);
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker


    }

    public void getLocation(String ordercode) {
        showProgress();
        APIService.getInstance().getLocation(ordercode)
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
//                            lat = data.getDouble("lat");
//                            lng = data.getDouble("lng");
                            lat = 10.802943;
                            lng = 106.714642;
                            newLatLng = new LatLng(lat, lng);
                            if (currentLat == 0 && currentLng == 0) {
                                return;
                            }
                            String origin = currentLat + "," + currentLng;
                            String destination = lat + "," + lng;
                            DirectionsResult results = getDirectionsDetails(origin, destination, TravelMode.DRIVING);
                            if (results != null) {
                                addPolyline(results, mGoogleMap);
                                positionCamera(results.routes[overview], mGoogleMap);
                                addMarkersToMap(results, mGoogleMap);
                            }

                            //move map camera
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 4));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().color(getContext().getResources().getColor(R.color.backgroundseekbar)).addAll(decodedPath));
    }

    private void addMarkersToMap(DirectionsResult results, GoogleMap mMap) {
//        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[overview].legs[overview].startLocation.lat,results.routes[overview].legs[overview].startLocation.lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.location)).title(results.routes[overview].legs[overview].startAddress));
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[overview].legs[overview].startLocation.lat, results.routes[overview].legs[overview].startLocation.lng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(results.routes[overview].legs[overview].startAddress));
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[overview].legs[overview].endLocation.lat, results.routes[overview].legs[overview].endLocation.lng)).title(results.routes[overview].legs[overview].startAddress).snippet(getEndLocationTitle(results)));
    }

    private void positionCamera(DirectionsRoute route, GoogleMap mMap) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(route.legs[overview].startLocation.lat, route.legs[overview].startLocation.lng), 12));
    }

    private String getEndLocationTitle(DirectionsResult results) {
        return "Time :" + results.routes[overview].legs[overview].duration.humanReadable + " Distance :" + results.routes[overview].legs[overview].distance.humanReadable;
    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3).setApiKey("AIzaSyAfiotVN8sVh9f471Q0SwpYhCHFcuvXS7Q").setConnectTimeout(5, TimeUnit.SECONDS).setReadTimeout(5, TimeUnit.SECONDS).setWriteTimeout(1, TimeUnit.SECONDS);
    }

    private DirectionsResult getDirectionsDetails(String origin, String destination, TravelMode mode) {
        DateTime now = new DateTime();
        try {
            return DirectionsApi.newRequest(getGeoContext())
                    .mode(mode)
                    .origin(origin)
                    .destination(destination)
                    .departureTime(now)
                    .await();
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
