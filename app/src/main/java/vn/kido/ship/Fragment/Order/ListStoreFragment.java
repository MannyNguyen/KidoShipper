package vn.kido.ship.Fragment.Order;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import vn.kido.ship.Adapter.StoreAdapter;
import vn.kido.ship.Api.APIService;
import vn.kido.ship.Api.ISubscriber;
import vn.kido.ship.Bean.BeanStore;
import vn.kido.ship.Fragment.BaseFragment;
import vn.kido.ship.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListStoreFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    SwipeRefreshLayout refresher;
    Spinner spinner;
    RecyclerView.OnScrollListener onScrollListener;
    public StoreAdapter adapter;
    public List<BeanStore> store;
    Subscription subscription;
    LinearLayoutManager layoutManager;

    public static ListStoreFragment newInstance() {
        Bundle args = new Bundle();
        ListStoreFragment fragment = new ListStoreFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public ListStoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_list_store, container, false);
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
                title.setText(getString(R.string.listshop));

                recyclerView = rootView.findViewById(R.id.recycler_store);
                refresher = rootView.findViewById(R.id.refresher_store);
                refresher.setOnRefreshListener(ListStoreFragment.this);
                spinner = rootView.findViewById(R.id.spinner_store);
                layoutManager = new LinearLayoutManager(getActivity());







                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addItemsOnSpinner();

                        recyclerView.setLayoutManager(layoutManager);
//                        recyclerView.setAdapter(adapter);

                        get();
                        onScrollListener = new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                if (layoutManager.findLastCompletelyVisibleItemPosition() == store.size() - 1 && store.size() >= 10000) {
                                    if (subscription == null) {
                                        return;
                                    }
                                    if (!subscription.isUnsubscribed()) {
                                        return;
                                    }
                                    //get();

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
        list.add("Quận");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.row_spinner, list);
        dataAdapter.setDropDownViewResource(R.layout.row_spinner);
        spinner.setAdapter(dataAdapter);

    }

    public void get() {
        refresher.setRefreshing(true);
        showProgress();
        subscription = APIService.getInstance().getstore()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ISubscriber() {
                    @Override
                    public void done() {
                        super.done();
                        refresher.setRefreshing(false);
                        hideProgress();
                    }

                    @Override
                    public void excute(JSONObject jsonObject) {
                        try {
                            JSONArray data = jsonObject.getJSONArray("data");
                            store = new ArrayList<>();
                            for (int i = 0; i < data.length(); i++) {

                                BeanStore beanStore = new Gson().fromJson(data.getString(i), BeanStore.class);
                                store.add(beanStore);
                            }
                            adapter = new StoreAdapter(ListStoreFragment.this, recyclerView, store);
                            recyclerView.setAdapter(adapter);
                            if (data.length() < 10) {
                                recyclerView.removeOnScrollListener(onScrollListener);
                            }
                            adapter.notifyDataSetChanged();




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
            store.clear();
            adapter.notifyDataSetChanged();
            refresher.setRefreshing(false);
            get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
