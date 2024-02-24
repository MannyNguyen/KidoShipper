package vn.kido.ship.Helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Icon;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vn.kido.ship.Class.CmmFunc;
import vn.kido.ship.Class.GlobalClass;
import vn.kido.ship.HomeActivity;
import vn.kido.ship.R;

public class MapHelper {
    //Tạo URL giữa 2 điểm
    private static String getUrlRoutes(LatLng origin, LatLng dest) {
        String url = null;
        try {
            String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
            String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
            String sensor = "sensor=false";
            String key = "key=" + GlobalClass.getActivity().getString(R.string.google_map_key);
            String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + key;
            String output = "json";
            url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    //region Nhận Json Object trả về danh sách LatLog
    public static PolylineOptions getPolylineOptions(LatLng origin, LatLng dest) {
        String response = HttpHelper.get(getUrlRoutes(origin, dest));
        List<List<HashMap<String, String>>> routes = new ArrayList<>();
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;
        PolylineOptions lineOptions = null;
        try {
            JSONObject jObject = new JSONObject(response);
            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<>();

                /** Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("lat", Double.toString((list.get(l)).latitude));
                            hm.put("lng", Double.toString((list.get(l)).longitude));
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }

            ArrayList<LatLng> latLngs;

            // Traversing through all the routes
            for (int j = 0; j < routes.size(); j++) {
                latLngs = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> p = routes.get(j);

                // Fetching all the points in i-th route
                for (int t = 0; t < p.size(); t++) {
                    HashMap<String, String> point = p.get(t);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    latLngs.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(latLngs);
                lineOptions.width(8);
                lineOptions.color(ContextCompat.getColor(GlobalClass.getActivity(), R.color.backgroundseekbar));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lineOptions;
    }

    private static List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        try {
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }
        } catch (Exception e) {
            //Log.e(getClass().getName(), "ViMT - decodePoly: " + e.getMessage());
            e.printStackTrace();
        }
        return poly;
    }

    public static Bitmap createMarker(int idView, String index) {
        View markerView = ((LayoutInflater) GlobalClass.getActivity()
                .getSystemService(GlobalClass.getActivity().LAYOUT_INFLATER_SERVICE))
                .inflate(idView, null);

        switch (idView) {
            case R.layout.marker_shipper:

                break;
            case R.layout.marker_inactive:
            case R.layout.marker_active:
                TextView title = markerView.findViewById(R.id.index);
                if (title != null) {
                    title.setText(index);
                    break;
                }
        }

        return createDrawableFromView(GlobalClass.getActivity(), markerView);

    }

    private static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels,
                displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static void animateCenter(GoogleMap map, LatLng latLng) {
        try {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng) // Sets the center of the map to
                    .zoom(16f)                   // Sets the zoom
                    .bearing(0) // Sets the orientation of the camera to east
                    //.tilt(30)    // Sets the tilt of the camera to 30 degrees
                    .build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            map.animateCamera(cameraUpdate);
        } catch (Exception e) {

        }
    }

    public static void bound(GoogleMap map, List<LatLng> latLngs) {
        try {
            if (latLngs.size() == 1) {
                animateCenter(map, latLngs.get(0));
                return;
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng latLng : latLngs) {
                builder.include(latLng);
            }
            LatLngBounds bounds = builder.build();
            int padding = CmmFunc.convertDpToPx(GlobalClass.getActivity(), 50);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, HomeActivity.WINDOW_WIDTH, HomeActivity.WINDOW_WIDTH, padding);
            map.animateCamera(cameraUpdate);
        } catch (Exception e) {
        }
    }

}


