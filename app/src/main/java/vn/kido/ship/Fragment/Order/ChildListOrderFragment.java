package vn.kido.ship.Fragment.Order;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.kido.ship.Adapter.OrderAdapter;
import vn.kido.ship.Api.APIService;
import vn.kido.ship.Api.ISubscriber;
import vn.kido.ship.Bean.BeanOrder;
import vn.kido.ship.Fragment.BaseFragment;
import vn.kido.ship.Helper.PermissionHelper;
import vn.kido.ship.R;


public class ChildListOrderFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    PermissionHelper permissionHelper;
    RecyclerView recyclerView;
    Spinner spinner;
    RecyclerView.OnScrollListener onScrollListener;
     OrderAdapter adapter;
    SwipeRefreshLayout refresher;
    public List<BeanOrder> order;
    Subscription subscription;
    double lat, lng;
    int sorttype;
    TextView txtinfo;


    public ChildListOrderFragment() {
        // Required empty public constructor
    }

    public static ChildListOrderFragment newInstance(int id) {
        Bundle args = new Bundle();
        ChildListOrderFragment fragment = new ChildListOrderFragment();
        fragment.setArguments(args);
        args.putInt("id", id);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_child_list_order, container, false);
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
                if (getParentFragment() instanceof ListOrderFragment) {
                    ListOrderFragment listOrderFragment = (ListOrderFragment) getParentFragment();
                    if (listOrderFragment.latLng != null) {
                        lat = listOrderFragment.latLng.latitude;
                        lng = listOrderFragment.latLng.latitude;
                    }

                }

                spinner = rootView.findViewById(R.id.spinner);
                txtinfo = rootView.findViewById(R.id.infolistorder);
                recyclerView = rootView.findViewById(R.id.recycler_order);
                refresher = rootView.findViewById(R.id.refresher_order);
                permissionHelper = new PermissionHelper(ChildListOrderFragment.this);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addItemsOnSpinner();
                        refresher.setOnRefreshListener(ChildListOrderFragment.this);
                        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);

                        onScrollListener = new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                if (layoutManager.findLastCompletelyVisibleItemPosition() == order.size() - 1 && order.size() >= 10000) {
                                    if (subscription == null) {
                                        return;
                                    }
                                    if (!subscription.isUnsubscribed()) {
                                        return;
                                    }
                                    addItemsOnSpinner();
                                }
                            }
                        };
                        recyclerView.addOnScrollListener(onScrollListener);


                    }
                });
            }
        });
        threadInit.start();
        isLoaded = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public void addItemsOnSpinner() {

        List<String> list = new ArrayList<String>();
        list.add("Thời gian");
        list.add("Khoảng cách");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.row_spinner, list);
        dataAdapter.setDropDownViewResource(R.layout.row_spinner);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                // Object item = parentView.getItemAtPosition(position);

                ChildListOrderFragment.this.sorttype = spinner
                        .getSelectedItemPosition() + 1;
//                Log.d("sorttypeeeee", "" + sorttype);
                get(sorttype, lat, lng);

            }

            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
            }

        });

    }

    @Override
    public void manuResume() {
        super.manuResume();
        get(sorttype, lat, lng);

    }

    public void get(int sorttype, double lat, double lng) {
        showProgress();
        subscription = APIService.getInstance().getorder(getArguments().getInt("id"), sorttype, lat, lng)
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
                            if (jsonObject == null) {
                                return;
                            }
                            int code = jsonObject.getInt("code");
                            if (code != 1) {
                                return;
                            }
                            JSONArray data = jsonObject.getJSONArray("data");
                            order = new ArrayList<>();
                            if (data.length() == 0) {
                                txtinfo.setVisibility(View.VISIBLE);
                            }else {
                                txtinfo.setVisibility(View.GONE);
                            }
                            for (int i = 0; i < data.length(); i++) {
                                BeanOrder beanOrder = new Gson().fromJson(data.getString(i), BeanOrder.class);
                                order.add(beanOrder);
                            }
                            adapter = new OrderAdapter(ChildListOrderFragment.this, recyclerView, order, getArguments().getInt("id"));
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        try {
            if (!subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
            recyclerView.removeOnScrollListener(onScrollListener);
            recyclerView.addOnScrollListener(onScrollListener);
            order.clear();
            adapter.notifyDataSetChanged();
            refresher.setRefreshing(false);
            addItemsOnSpinner();
            get(sorttype, lat, lng);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
