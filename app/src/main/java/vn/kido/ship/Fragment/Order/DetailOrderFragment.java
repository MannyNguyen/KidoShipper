
package vn.kido.ship.Fragment.Order;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.kido.ship.Adapter.AttributeAdapter;
import vn.kido.ship.Adapter.ExchangeAdapter;
import vn.kido.ship.Adapter.GiftAdapter;
import vn.kido.ship.Adapter.GiftAdapterTest;
import vn.kido.ship.Adapter.IntactAdapter;
import vn.kido.ship.Adapter.ProductAdapter;
import vn.kido.ship.Adapter.ReceivedAdapter;
import vn.kido.ship.Adapter.ReturnAdapter;
import vn.kido.ship.Api.APIService;
import vn.kido.ship.Api.ISubscriber;
import vn.kido.ship.Bean.BeanAttribute;
import vn.kido.ship.Bean.BeanGift;
import vn.kido.ship.Bean.BeanItem;
import vn.kido.ship.Bean.BeanProduct;
import vn.kido.ship.Class.CmmFunc;
import vn.kido.ship.Fragment.BaseFragment;
import vn.kido.ship.Helper.FragmentHelper;
import vn.kido.ship.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailOrderFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "DetailOrderFragment";
    RecyclerView recyclerView, recycler_gift;
    //RecyclerView.OnScrollListener onScrollListener;
    LinearLayoutManager layoutManager, layoutManagerGift;
    public ProductAdapter adapter;
    public ReceivedAdapter receivedAdapter;
    public ExchangeAdapter exchangeAdapter;
    public IntactAdapter intactAdapter;
    public ReturnAdapter returnAdapter;
    public AttributeAdapter attributeAdapter;
    public GiftAdapter giftAdapter;
    public GiftAdapterTest giftAdapterTest;
    public List<BeanProduct> products;
    public List<BeanAttribute> attributes;
    public List<BeanProduct> listproduct;
    public List<BeanItem> listproductupdate;
    public List<BeanGift> listgift;
    List<BeanProduct> tempList = new ArrayList<>();
    String u, uu;
    int type;
    int checktype = 0;
    Subscription subscription;
    public int check = 0;
    JSONArray jsonArray;
    Gson gson;
    public int status;

    int id;
    String phone, order_code;
    View lngift, lnfinish, lnsaleorder, lnsale, lntotalorder, lntotal, lnreason, lnnote;
    SeekBar seek;
    LinearLayout lnone, lntwo, lnButton;
    Button btnStart, btnExchange, btnFinish;
    ImageView imv_receive, imv_receive_processing, imv_delivering, imv_delivering_processing, imv_delivered, imv_delivering_waiting, imv_delivered_waiting, ivMap;
    TextView txvcodeorder, txvnoteorder, txvnameshop, txvphone, txvaddress, txvproductcount, txvtotal, txtnote,
            txv_sale_order, txv_sale, txv_total, txv_order_type_name, txv_reason, txv_status;


    public static DetailOrderFragment newInstance(String order_code) {
        Bundle args = new Bundle();
        args.putString("order_code", order_code);
        DetailOrderFragment fragment = new DetailOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public DetailOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_detail_order, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = rootView.findViewById(R.id.recycler_product);
        recycler_gift = rootView.findViewById(R.id.recycler_gift);
        txv_order_type_name = rootView.findViewById(R.id.typeorder);
        txvaddress = rootView.findViewById(R.id.txv_address_order);
        txvcodeorder = rootView.findViewById(R.id.txv_code_order);
        txvnameshop = rootView.findViewById(R.id.txv_nameshop_order);
        txvnoteorder = rootView.findViewById(R.id.txv_note_order);
        txvphone = rootView.findViewById(R.id.txv_phone_order);
        txvproductcount = rootView.findViewById(R.id.txv_productcount_order);
        txvtotal = rootView.findViewById(R.id.txv_total_order);
        txtnote = rootView.findViewById(R.id.txtnote);
        txv_total = rootView.findViewById(R.id.txv_total);
        txv_sale = rootView.findViewById(R.id.txv_sale);
        txv_sale_order = rootView.findViewById(R.id.txv_sale_order);
        txv_reason = rootView.findViewById(R.id.txv_reason);
        txv_status = rootView.findViewById(R.id.txv_status);
        rootView.findViewById(R.id.sale_gift).setOnClickListener(DetailOrderFragment.this);

        imv_delivered = rootView.findViewById(R.id.imv_delivered);
        imv_delivered_waiting = rootView.findViewById(R.id.imv_delivered_waiting);
        imv_receive = rootView.findViewById(R.id.imv_receive);
        imv_receive_processing = rootView.findViewById(R.id.imv_receive_processing);
        imv_delivering = rootView.findViewById(R.id.imv_delivering);
        imv_delivering_processing = rootView.findViewById(R.id.imv_delivering_processing);
        imv_delivering_waiting = rootView.findViewById(R.id.imv_delivering_waiting);
//        lnone = rootView.findViewById(R.id.lnone);
        lntwo = rootView.findViewById(R.id.lntwo);
        lnButton = rootView.findViewById(R.id.delivery_container);
        lntotalorder = rootView.findViewById(R.id.ln_total_order);
        lnsale = rootView.findViewById(R.id.ln_sale);
        lnsaleorder = rootView.findViewById(R.id.ln_sale_order);
        lntotal = rootView.findViewById(R.id.ln_total);
        lnreason = rootView.findViewById(R.id.lnreason);
        lnnote = rootView.findViewById(R.id.lnnote);
        rootView.findViewById(R.id.call_user).setOnClickListener(DetailOrderFragment.this);
        rootView.findViewById(R.id.btn_exchange).setOnClickListener(DetailOrderFragment.this);
        rootView.findViewById(R.id.btn_callsupport).setOnClickListener(DetailOrderFragment.this);
        /*btn_finish = rootView.findViewById(R.id.finished);
        btn_finished = rootView.findViewById(R.id.btnfinish);*/
        btnStart = rootView.findViewById(R.id.btn_cancel);
        btnExchange = rootView.findViewById(R.id.btn_exchange);
        btnFinish = rootView.findViewById(R.id.btn_finish);
        seek = rootView.findViewById(R.id.seek);
        ivMap = rootView.findViewById(R.id.iv_map);
        rootView.findViewById(R.id.sms_user).setOnClickListener(this);

        adapter = new ProductAdapter();
//        adapter.setProductAttributteClickListener(this);
        recyclerView.setAdapter(adapter);
        lngift = rootView.findViewById(R.id.lngift);
//        lnfinish = rootView.findViewById(R.id.lnfinish);
        giftAdapterTest = new GiftAdapterTest();
//        giftAdapterTest.setGiftAttributteClickListener(this);
        recycler_gift.setAdapter(giftAdapterTest);

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
                title.setText(getString(R.string.detailorder));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        layoutManager = new LinearLayoutManager(getContext());
                        layoutManagerGift = new LinearLayoutManager(getContext());
                        gson = new Gson();
                        products = new ArrayList<>();
                        attributes = new ArrayList<>();
                        listproduct = new ArrayList<>();
                        listproductupdate = new ArrayList<>();
                        listgift = new ArrayList<>();
//                        adapter = new ProductAdapter(DetailOrderFragment.this, recyclerView, products);

//                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(layoutManager);
                        recycler_gift.setLayoutManager(layoutManagerGift);

                        getList();

                        ivMap.setOnClickListener(DetailOrderFragment.this);
                    }
                });
            }
        });
        threadInit.start();
        isLoaded = true;
    }

    @Override
    public void manuResume() {
        super.manuResume();
        //update update item call api update totalprice

//
//        GsonBuilder builderr = new GsonBuilder();
//        Gson gsonn = builderr.create();
//        uu = gsonn.toJson(listproduct);
//        Log.i("newwwwwwwww", uu + "#####");
//
//
//        // send list product edit data
//        adapter.notifyDataSetChanged();
//        giftAdapterTest.notifyDataSetChanged();
////        giftAdapter.notifyDataSetChanged();
////        List<BeanItem> items;
////        items = itemss;
////
////
////        GsonBuilder builder = new GsonBuilder();
////        Gson gson = builder.create();
////        u = gson.toJson(items);
//        Log.i("OBJJJJ", u + "#####");

        //CALL API
//        BeanItem updateItem = getUpdateItem(p);
//        newArr();
//        checkForUpdate(updateItem);
//        // GsonBuilder builder = new GsonBuilder();
//        Gson gson = new Gson();
//        u = gson.toJson(listproductupdate);
//        Log.d("zzz", uu + "\n" + u);
//        gettotal(order_code, u);
        for (BeanProduct origin : products) {
            for (BeanProduct update : tempList) {
                if (origin.getId() != update.getId()) {
                    continue;
                }

                for (int i = 0; i < update.getAttribute_received().size(); i++) {
                    int quantitynew = 0;
                    for (BeanAttribute beanAttribute : update.getAttribute_received()) {

                        quantitynew += beanAttribute.getValue() * beanAttribute.getQuantity();
                    }
                    update.setTotal_money(quantitynew * update.getPrice());

                }
            }
        }
        adapter.notifyDataSetChanged();
        recyclerView.getAdapter();
        if (type == 1 && getItems().size() > 0) {
            gettotal(order_code, new Gson().toJson(getItems()));
        }
       /* if (getItems().size() > 0) {
            gettotal(order_code, new Gson().toJson(getItems()));
        }*/


    }

    private List<BeanItem> getItems() {
        List<BeanItem> items = new ArrayList<>();
        for (BeanProduct origin : products) {
            for (BeanProduct tail : tempList) {
                if (origin.getId() != tail.getId()) {
                    continue;
                }
                BeanItem item = new BeanItem();
                int quantity = 0;
                item.setId(tail.getId());
                item.setType(tail.getType());
                for (int i = 0; i < tail.getAttribute_received().size(); i++) {
                    BeanAttribute beanAttribute = tail.getAttribute_received().get(i);
                    quantity += beanAttribute.getValue() * beanAttribute.getQuantity();
                }
                item.setQuantity(quantity);
                items.add(item);
            }
        }
        return items;
    }

    private List<BeanProduct> getTempList() {
        for (BeanProduct origin : products) {
            for (BeanProduct tail : tempList) {
                if (origin.getId() != tail.getId()) {
                    continue;
                }

                int quantity = 0;
                for (int i = 0; i < tail.getAttribute_received().size(); i++) {
                    BeanAttribute beanAttribute = tail.getAttribute_received().get(i);
                    quantity += beanAttribute.getValue() * beanAttribute.getQuantity();
                }
                if (origin.getId() == tail.getId()) {
                    tail.setQuantity(quantity);
                }
            }
        }
        return tempList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    private void getList() {
        tempList.clear();
        listgift.clear();
        showProgress();
        subscription = APIService.getInstance().getDetailOrder(getArguments().getString("order_code") + "")
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

                            hideProgress();
                            JSONObject data = jsonObject.getJSONObject("data");
                            String totalorder = (data.getString("total_money") + "");
                            String txvsale = (data.getString("tong_km_san_pham") + "");
                            String txvsaleorder = (data.getString("tong_km_don") + "");
                            String txvtotalend = (data.getString("tong_thanh_toan") + "");

                            type = data.getInt("type");
                            txv_total.setText(CmmFunc.formatMoney(txvtotalend, true) + "");
                            txvtotal.setText(CmmFunc.formatMoney(totalorder, true) + "");
                            txv_sale.setText(CmmFunc.formatMoney(txvsale, true) + "");
                            txv_sale_order.setText(CmmFunc.formatMoney(txvsaleorder, true) + "");
                            txvaddress.setText(data.getString("address_delivery") + "");
                            txvcodeorder.setText(data.getString("order_code") + "");
//                            txv_order_type_name.setText(data.getString("order_type_name") + "");

                            txvnameshop.setText(data.getString("fullname") + "");
                            phone = data.getString("phone");
                            txvphone.setText(phone);
                            txvproductcount.setText(data.getString("product_count") + "");
//                            txvtotal.setText(data.getString("status") + "");


                            status = data.getInt("status");
                            id = data.getInt("id");
                            order_code = data.getString("order_code");

                            seek.setEnabled(false);
                            if (status == 4) {
//                                lnfinish.setVisibility(View.GONE);
                                imv_receive.setVisibility(View.GONE);
                                imv_receive_processing.setVisibility(View.VISIBLE);
                               /* btn_finish.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        confirmorder(order_code);
                                    }
                                });*/
                                btnStart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        confirmorder(order_code);
                                    }
                                });
                                btnFinish.setVisibility(View.GONE);
                            } else if (status == 5 && type != 1) {
//                                lnfinish.setVisibility(View.VISIBLE);
//                                confirmorder(id);
                                seek.setProgress(50);
                                imv_receive.setVisibility(View.VISIBLE);
                                imv_receive_processing.setVisibility(View.GONE);
                                imv_delivering.setVisibility(View.GONE);
                                imv_delivering_processing.setVisibility(View.VISIBLE);
                                /*btn_finish.setText(R.string.cancle_order);
                                btn_finish.setTextColor(getResources().getColor(R.color.main));
                                btn_finish.setBackground(getResources().getDrawable(R.drawable.red_border_box));
                                btn_finish.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        checktype = 2;
                                        FragmentHelper.add(VerifyOrderFragment.newInstance(order_code + "", checktype, ""));
                                        notiDelivery(order_code);
                                    }
                                });*/
                                btnStart.setText(R.string.cancle_order);
                                btnStart.setTextColor(getResources().getColor(R.color.main));
                                btnStart.setBackground(getResources().getDrawable(R.drawable.red_border_box));
                                btnStart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        checktype = 2;
                                        FragmentHelper.add(VerifyOrderFragment.newInstance(order_code + "", checktype, ""));
                                        notiDelivery(order_code);
                                    }
                                });

                                /*btn_finished.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        u = getItems().toString();
                                        uu = gson.toJson(getItems());
                                        if (uu == null) {
                                            uu = "";
                                        }
                                        if (uu.equals("")) {
                                            checktype = 0;
                                        } else {
                                            checktype = 1;
                                        }
                                        FragmentHelper.add(VerifyOrderFragment.newInstance(order_code + "", checktype, uu));
                                        notiDelivery(order_code);
                                    }
                                });*/
                                btnFinish.setVisibility(View.VISIBLE);
                                btnFinish.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        u = getItems().toString();
                                        uu = gson.toJson(getItems());
                                        String uuu = new Gson().toJson(products);
                                        String uuuu = new Gson().toJson(getTempList());
                                        if (uu == null) {
                                            uu = "";
                                        }

                                        for (BeanProduct origin : products) {
                                            for (BeanProduct tail : getTempList()) {
                                                if (origin.getId() == tail.getId() && origin.getQuantity() == tail.getQuantity()) {
                                                    checktype = 0;
                                                } else {
                                                    checktype = 1;
                                                }

                                            }
                                        }
                                        /*if (uuu.equals(uuuu)) {
                                            checktype = 0;
                                        } else {
                                            checktype = 1;
                                        }*/

                                       /* if (uu.equals("")) {
                                            checktype = 0;
                                        } else {
                                            checktype = 1;
                                        }*/
                                        Log.d("checktype", "" + checktype);
                                        FragmentHelper.add(VerifyOrderFragment.newInstance(order_code + "", checktype, uu));
                                        notiDelivery(order_code);
                                    }
                                });
                            } else if (status == 5 && type == 1) {
//                                confirmorder(id);
                                seek.setProgress(50);
                                imv_receive.setVisibility(View.VISIBLE);
                                imv_receive_processing.setVisibility(View.GONE);
                                imv_delivering.setVisibility(View.GONE);
                                imv_delivering_processing.setVisibility(View.VISIBLE);
//                                btn_finish.setText(R.string.finished);
                                btnStart.setText(R.string.finished);
                                btnStart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        u = getItems().toString();
                                        uu = gson.toJson(getItems());
                                        String uuuu = new Gson().toJson(getTempList());
                                        String uuu = new Gson().toJson(products);
                                        if (uu == null) {
                                            uu = "";
                                        }
                                        for (BeanProduct origin : products) {
                                            for (BeanProduct tail : getTempList()) {
                                                if (origin.getId() == tail.getId() && origin.getQuantity() == tail.getQuantity()) {
                                                    checktype = 0;
                                                } else {
                                                    checktype = 1;
                                                }

                                            }
                                        }
                                        /*if (uuu.equals(uuuu)) {
                                            checktype = 0;
                                        } else {
                                            checktype = 1;
                                        }*/
                                        /*if (uu.equals("")) {
                                            checktype = 0;
                                        } else {
                                            checktype = 1;
                                        }*/
                                        Log.d("checktype", "" + checktype);
                                        FragmentHelper.add(VerifyOrderFragment.newInstance(order_code + "", checktype, uu));
                                        notiDelivery(order_code);
                                    }
                                });

                                /*btn_finish.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        u = getItems().toString();
                                        uu = gson.toJson(getItems());
                                        if (uu == null) {
                                            uu = "";
                                        }
                                        if (uu.equals("")) {
                                            checktype = 0;
                                        } else {
                                            checktype = 1;
                                        }
                                        FragmentHelper.add(VerifyOrderFragment.newInstance(order_code + "", checktype, uu));
                                        notiDelivery(order_code);
                                    }
                                });*/

//                                lnfinish.setVisibility(View.GONE);
                                btnFinish.setVisibility(View.GONE);
                            } else if (status >= 6) {
                                if (status > 6) {
                                    rootView.findViewById(R.id.seek_container).setVisibility(View.GONE);
                                } else {
                                    rootView.findViewById(R.id.seek_container).setVisibility(View.VISIBLE);
                                    seek.setProgress(100);
                                }

                                imv_receive.setVisibility(View.VISIBLE);
                                imv_receive_processing.setVisibility(View.GONE);
                                imv_delivering.setVisibility(View.VISIBLE);
                                imv_delivering_processing.setVisibility(View.GONE);
                                imv_delivered.setVisibility(View.VISIBLE);
                                imv_delivered_waiting.setVisibility(View.GONE);
//                                lntwo.setVisibility(View.GONE);
                                lnButton.setVisibility(View.GONE);
//                                lnone.setVisibility(View.VISIBLE);
                            }
                            JSONArray datachild = data.getJSONArray("products");
                            for (int i = 0; i < datachild.length(); i++) {
                                BeanProduct dataorder = gson.fromJson(datachild.getString(i), BeanProduct.class);
                                BeanProduct datalist = gson.fromJson(datachild.getString(i), BeanProduct.class);
                                products.add(dataorder);
                                listproduct.add(datalist);
                                tempList.add(datalist);
                            }

                            for (BeanProduct beanProduct : tempList) {
                                beanProduct.setOriginAttribute(beanProduct.getAttribute());
//                                beanProduct.setOrigin_total_money(beanProduct.getTotal_price());
                                beanProduct.setOrigin_total_money(beanProduct.getTotal_money());
                            }

                            adapter.setData(tempList, status, getActivity());
                            txv_order_type_name.setText(data.getString("order_type_name") + "");
                            if (status >= 6) {
                                receivedAdapter = new ReceivedAdapter(DetailOrderFragment.this, tempList);
                                receivedAdapter.notifyDataSetChanged();
                                recyclerView.setAdapter(receivedAdapter);
                            } else if (type == 2) {
                                intactAdapter = new IntactAdapter(DetailOrderFragment.this, tempList);
                                intactAdapter.notifyDataSetChanged();
                                txv_order_type_name.setText(R.string.intact);
                                recyclerView.setAdapter(intactAdapter);
                            } else if (type == 3) {
                                lnsale.setVisibility(View.GONE);
                                lnsaleorder.setVisibility(View.GONE);
                                lntotalorder.setVisibility(View.GONE);
                                txv_order_type_name.setText(R.string.changeodd);
//                                lntotal.setVisibility(View.GONE);
                                exchangeAdapter = new ExchangeAdapter(DetailOrderFragment.this, tempList);
                                exchangeAdapter.notifyDataSetChanged();
                                recyclerView.setAdapter(exchangeAdapter);
                            } else if (type == 4) {
                                lnsale.setVisibility(View.GONE);
                                lnsaleorder.setVisibility(View.GONE);
                                lntotalorder.setVisibility(View.GONE);
                                txv_order_type_name.setText(R.string.returnodd);
//                                lntotal.setVisibility(View.GONE);
                                returnAdapter = new ReturnAdapter(DetailOrderFragment.this, tempList);
                                returnAdapter.notifyDataSetChanged();
                                recyclerView.setAdapter(returnAdapter);
                            }


                            JSONArray datagift = data.getJSONArray("gifts");
                            List<BeanGift> gifts = new ArrayList<>();
                            for (int i = 0; i < datagift.length(); i++) {
                                BeanGift datagifts = gson.fromJson(datagift.getString(i), BeanGift.class);
                                listgift.add(datagifts);
                                gifts.add(datagifts);
                                Log.d("aaaaa", "" + listgift.get(i).getQuantity());
                            }
                            /*giftAdapter = new GiftAdapter(DetailOrderFragment.this, recycler_gift, listgift);
                            giftAdapter.notifyDataSetChanged();
                            recycler_gift.setAdapter(giftAdapter);*/
                            if (gifts.size() != 0) {
                                lngift.setVisibility(View.VISIBLE);
                            } else {
                                lngift.setVisibility(View.GONE);
                            }
                            giftAdapterTest.setData(gifts);
                            recycler_gift.getAdapter().notifyDataSetChanged();
                            recyclerView.getAdapter().notifyDataSetChanged();
                            String reason = (data.getString("reason") + "");
                            if (reason.equals("")) {
                                lnreason.setVisibility(View.GONE);
                            } else if (!reason.equals("")) {
                                lnreason.setVisibility(View.VISIBLE);
                                txv_reason.setText(reason);
                            }
                            if (data.optString("note_delivery", "").equals("")) {
                                lnnote.setVisibility(View.GONE);
                            } else {
                                lnnote.setVisibility(View.VISIBLE);
                                txtnote.setText(R.string.note);
                                txvnoteorder.setText(data.getString("note_delivery") + "");
                            }
                            txv_status.setText(data.getString("status_name") + "");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
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
                                    getList();
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

    public void gettotal(String id, String items) {
        showProgress();
        APIService.getInstance().getTotal(id, items)
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
                            String totalorder = data.getString("total_money");
                            String total = data.getString("tong_thanh_toan");
                            String sale = data.getString("tong_km_san_pham");
                            String saleorder = data.getString("tong_km_don_hang");
                            Log.d("cccc", "" + totalorder);
                            txvtotal.setText(CmmFunc.formatMoney(totalorder, true));
                            txv_total.setText(CmmFunc.formatMoney(total, true));
                            txv_sale.setText(CmmFunc.formatMoney(sale, true));
                            txv_sale_order.setText(CmmFunc.formatMoney(saleorder, true));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_callsupport:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "+" + getString(R.string.phone_support)));
                startActivity(intent);
                break;
            case R.id.btn_exchange:
                FragmentHelper.add(ChangeFragment.newInstance(order_code));
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
            case R.id.iv_map:
                break;
            case R.id.sale_gift:
                FragmentHelper.add(PromotionGiftFragment.newInstance(products));
                break;
        }
    }

/*    @Override
    public void onProductAttributteClick(int position, final BeanProduct product, BeanAttribute beanAttribute, int a, BeanProduct beanProduct) {
        if (status == 5) {

            ReQuantityFragment fragment = ReQuantityFragment.newInstance(position, products.get(position), product, beanAttribute, a);
            fragment.setOnValueChangeListener(new ReQuantityFragment.OnValueChangeListener() {
                @Override
                public void onChange(BeanItem beanItem, BeanAttribute beanAttribute) {

                    BeanProduct p = product;
                    if (null == p.getAttribute()) return;
                    if (p.getAttribute().size() == 0) {
                        p.getAttribute().add(beanAttribute);
                    }
                    for (int i = 0; i < p.getAttribute().size(); i++) {
                        if (p.getAttribute().get(i).getId() == beanAttribute.getId()) {
                            p.getAttribute().set(i, beanAttribute);
                            product.getAttribute().set(i, beanAttribute);
                        }
                    }
                    BeanItem updateItem = getUpdateItem(p);
                    newArr();
                    checkForUpdate(updateItem);
                    // GsonBuilder builder = new GsonBuilder();
                    Gson gson = new Gson();
                    u = gson.toJson(listproductupdate);
                    Log.d("zzz", uu + "\n" + u);
                    gettotal(order_code, u);

                }
            });
            FragmentHelper.add(fragment);

        }

    }*/


    private BeanItem getUpdateItem(BeanProduct p) {
        BeanItem updateItem = new BeanItem();
        int sum = 0;
        List<BeanAttribute> sumList = p.getAttribute();
        for (int s = 0; s < sumList.size(); s++) {
            sum += sumList.get(s).getQuantity() * sumList.get(s).getValue();
        }
        updateItem.setId(p.getId());
        updateItem.setQuantity(sum);
        p.setTotal_money(sum * p.getPrice());
        updateItem.setType(p.getType());
        return updateItem;
    }

    public void newArr() {
        if (check == 0) {
            for (int i = 0; i < listproduct.size(); i++) {
                BeanItem item = new BeanItem();
                item.setId(listproduct.get(i).getId());
                item.setType(listproduct.get(i).getType());
                item.setQuantity(listproduct.get(i).getQuantity());
                listproductupdate.add(item);
            }
            check = 1;
        }
    }

    private void checkForUpdate(BeanItem item) {


        int indexOfExist = -1;
        for (int i = 0; i < listproductupdate.size(); i++) {
            BeanItem temp = listproductupdate.get(i);
            if (temp.getId() == item.getId()) {
                indexOfExist = i;
                break;
            }
        }
        if (indexOfExist == -1) {
//            listproductupdate.add(item);
        } else {
            listproductupdate.set(indexOfExist, item);
            recyclerView.getAdapter().notifyDataSetChanged();
        }

    }


/*    @Override
    public void onGiftAttributteClick(int position, final List<BeanGift> beanGiftList, final BeanGift gift) {
        if (status == 5) {
            ReQuantityFragment fragment = ReQuantityFragment.newInstance(position, gift);
            fragment.setOnValueGiftChangeListener(new ReQuantityFragment.OnValueGiftChangeListener() {
                @Override
                public void onGiftChange(BeanItem beanItem, BeanGift beanGift) {
                    List<BeanGift> list = beanGiftList;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getId() == gift.getId()) {
                            list.set(i, gift);
                            beanGiftList.set(i, gift);
                        }
                    }
//                    BeanItem updateItem = getUpdateItem(list);
                    newArr();
//                    checkForUpdate(updateItem);
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    u = gson.toJson(listproductupdate);
                    Log.d("zzz", uu + "\n" + u);
                    gettotal(order_code, u);

                }
            });
            FragmentHelper.add(fragment);

        }
    }*/
}
