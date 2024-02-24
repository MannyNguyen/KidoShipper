package vn.kido.ship.Fragment.Map;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import vn.kido.ship.Fragment.BaseFragment;
import vn.kido.ship.Helper.FragmentHelper;
import vn.kido.ship.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment implements OnMapReadyCallback {
    private GoogleMap map;
    Button btnMap;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        Bundle args = new Bundle();
        MapFragment fragment = new MapFragment();
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
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
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
                TextView title = rootView.findViewById(R.id.title);
                btnMap=rootView.findViewById(R.id.btnmap);
                title.setText(getString(R.string.road));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getMap();
                        btnMap.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
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
                    ft.add(R.id.map_container, supportMapFragment).commit();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            supportMapFragment.getMapAsync(MapFragment.this);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
