package vn.kido.ship.Fragment.Order;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import vn.kido.ship.Bean.BeanAttribute;
import vn.kido.ship.Bean.BeanGift;
import vn.kido.ship.Bean.BeanItem;
import vn.kido.ship.Bean.BeanProduct;
import vn.kido.ship.Class.CmmFunc;
import vn.kido.ship.Fragment.BaseFragment;
import vn.kido.ship.Helper.FragmentHelper;
import vn.kido.ship.R;

/*

public class ReQuantityFragment extends BaseFragment implements View.OnClickListener {
    TextView unitproduct, unittotal, txtAttibute;
    NumberPicker np;
    String price;
    String tto;
    public static int attribute;
    public static int quantity;
    BeanItem beanItem = new BeanItem();
    BeanAttribute beanAttributeUpdate = new BeanAttribute();
    int totalorder;
    public OnValueChangeListener onValueChangeListener;
    public OnValueGiftChangeListener onValueGiftChangeListener;

    //    public static List<BeanItem> itemss = new ArrayList<>();
    BeanProduct params, product;
    BeanGift beanGift;


    public static ReQuantityFragment newInstance(int position, BeanGift beanGift) {
        Bundle args = new Bundle();
        ReQuantityFragment fragment = new ReQuantityFragment();
        fragment.setArguments(args);
        args.putInt("position", position);
        args.putString("beanGift", new Gson().toJson(beanGift));
        return fragment;
    }

    public static ReQuantityFragment newInstance(int idProduct, BeanAttribute current, List<BeanAttribute> attributes) {
        Bundle args = new Bundle();
        args.putInt("id_product", idProduct);
        args.putString("current", new Gson().toJson(current));
        args.putString("attributes", new Gson().toJson(attributes));
        ReQuantityFragment fragment = new ReQuantityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ReQuantityFragment newInstance(int position, BeanProduct product, BeanProduct beanProduct, BeanAttribute attribute, int a) {
        Bundle args = new Bundle();
        ReQuantityFragment fragment = new ReQuantityFragment();
        fragment.setArguments(args);
        args.putInt("position", position);
        args.putString("dataproduct", new Gson().toJson(product));
        args.putString("data", new Gson().toJson(beanProduct));
        args.putInt("attribute", attribute.getId());
        args.putInt("a", a);
        fragment.beanAttributeUpdate = attribute;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_requantity, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isLoaded) {
            return;
        }
        beanGift = new Gson().fromJson(getArguments().getString("beanGift"), BeanGift.class);
        rootView.findViewById(R.id.submit).setOnClickListener(ReQuantityFragment.this);
        unitproduct = rootView.findViewById(R.id.unitproduct);
        unittotal = rootView.findViewById(R.id.unittotal);
        np = (NumberPicker) rootView.findViewById(R.id.numberpicker);
        txtAttibute = (TextView) rootView.findViewById(R.id.txvattribute);


        params = new Gson().fromJson(getArguments().getString("data"), BeanProduct.class);
        product = new Gson().fromJson(getArguments().getString("dataproduct"), BeanProduct.class);
        maxNumberPicker();
        np.setValue(beanAttributeUpdate.getValue());
        totalorder = params.getPrice() * beanAttributeUpdate.getQuantity();
        quantity = beanAttributeUpdate.getQuantity();
        attribute = beanAttributeUpdate.getMoney() * 1;
        tto = String.valueOf(np.getValue() * attribute);
        unittotal.setText(CmmFunc.formatMoney(tto, true));
        */
/*//*
/xét lấy quantity theo product

        if (getArguments().getInt("a") == 0 || getArguments().getInt("a") == 1 || getArguments().getInt("a") == 2) {
            params = new Gson().fromJson(getArguments().getString("data"), BeanProduct.class);
            product = new Gson().fromJson(getArguments().getString("dataproduct"), BeanProduct.class);
            maxNumberPicker();
            np.setValue(beanAttributeUpdate.getValue());
            totalorder = params.getPrice() * beanAttributeUpdate.getQuantity();
            quantity = beanAttributeUpdate.getQuantity();
            attribute = beanAttributeUpdate.getMoney() * 1;
            tto = String.valueOf(np.getValue() * attribute);
            unittotal.setText(CmmFunc.formatMoney(tto, true));
        }
        //xét lấy quantity theo gift
        else if (getArguments().getInt("position") != -1) {
            beanGift = new Gson().fromJson(getArguments().getString("beanGift"), BeanGift.class);
            np.setValue(beanGift.getQuantity());
            np.setMaxValue(beanGift.getQuantity());
        }*//*



        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
//        np.setOnValueChangedListener(new ReQuantityFragment());
//        np.setOnValueChangedListener();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                price = params.getPrice() + "";
                String unit = beanAttributeUpdate.getMoney() + "";
                unitproduct.setText(CmmFunc.formatMoney(unit, true));


                PickerListener listener = new PickerListener();
                np.setOnScrollListener(listener);
                np.setOnValueChangedListener(listener);
            }
        });
        isLoaded = true;
    }

    public void maxNumberPicker() {
        int number = getArguments().getInt("attribute");
        if (product.getAttribute().size() == 1) {
            np.setMaxValue(product.getAttribute().get(0).getValue());
        }

        if (product.getAttribute().size() == 2) {
            if (getArguments().getInt("a") == 0) {
                int quantity1 = params.getAttribute().get(1).getQuantity() * params.getAttribute().get(1).getValue();
                int max = (product.getQuantity() - quantity1) / product.getAttribute().get(0).getQuantity();
                if (max > 0) {
                    np.setMaxValue(max);
                    txtAttibute.setText(params.getAttribute().get(0).getName());

                } else {
                    np.setMaxValue(0);
                    txtAttibute.setText(params.getAttribute().get(0).getName());
                }

            }
            if (getArguments().getInt("a") == 1) {
                int quantity0 = params.getAttribute().get(0).getQuantity() * params.getAttribute().get(0).getValue();
//            int quantity2 = params.getAttribute().get(2).getQuantity() * params.getAttribute().get(2).getValue();
                int max = params.getQuantity() - quantity0;
                if (max > params.getAttribute().get(1).getValue()) {
                    np.setMaxValue(product.getAttribute().get(1).getValue());
                    txtAttibute.setText(params.getAttribute().get(1).getName());
                } else if (max == params.getAttribute().get(1).getValue()) {
                    np.setMaxValue(product.getAttribute().get(1).getValue());
                    txtAttibute.setText(params.getAttribute().get(1).getName());
                } else {
                    np.setMaxValue(product.getAttribute().get(0).getQuantity() - 1);
                    txtAttibute.setText(params.getAttribute().get(1).getName());
                }
            }
        }
        if (product.getAttribute().size() == 3) {
            if (getArguments().getInt("a") == 0) {
                int quantity1 = params.getAttribute().get(1).getQuantity() * params.getAttribute().get(1).getValue();
                int quatity2 = params.getAttribute().get(2).getQuantity() * params.getAttribute().get(2).getValue();
                int max = (product.getQuantity() - quantity1 - quatity2) / product.getAttribute().get(0).getQuantity();
                if (max > 0) {
                    np.setMaxValue(max);
                    txtAttibute.setText(params.getAttribute().get(0).getName());

                } else {
                    np.setMaxValue(0);
                    txtAttibute.setText(params.getAttribute().get(0).getName());
                }

            }
            if (getArguments().getInt("a") == 1) {
                int maxValue = params.getAttribute().get(0).getQuantity() / params.getAttribute().get(1).getQuantity();
                int quantity0 = params.getAttribute().get(0).getQuantity() * params.getAttribute().get(0).getValue();
                int quantity2 = params.getAttribute().get(2).getQuantity() * params.getAttribute().get(2).getValue();
                int max = (params.getQuantity() - quantity0 - quantity2) / params.getAttribute().get(1).getQuantity();
                if (max >= maxValue) {
                    np.setMaxValue(maxValue - 1);
                    txtAttibute.setText(params.getAttribute().get(1).getName());
                } else if (max < maxValue && max != 0) {
                    np.setMaxValue(max);
                } else {
                    np.setMaxValue(0);
                    txtAttibute.setText(params.getAttribute().get(1).getName());
                }
            }
            if (getArguments().getInt("a") == 2) {
                int quantity0 = params.getAttribute().get(0).getQuantity() * params.getAttribute().get(0).getValue();
                int quantity1 = params.getAttribute().get(1).getQuantity() * params.getAttribute().get(1).getValue();
                int max = (params.getQuantity() - quantity0 - quantity1) / params.getAttribute().get(2).getQuantity();
                if (max >= params.getAttribute().get(1).getQuantity()) {
                    np.setMaxValue(product.getAttribute().get(1).getQuantity() - 1);
                    txtAttibute.setText(params.getAttribute().get(2).getName());
                } else {
                    np.setMaxValue(max);
                    txtAttibute.setText(params.getAttribute().get(2).getName());
                }
            }
        }
       */
/* if (getArguments().getInt("a") == 0) {
            np.setMaxValue(product.getAttribute().get(0).getValue());
            txtAttibute.setText(product.getAttribute().get(0).getName());
        }
        if (getArguments().getInt("a") == 1) {
            np.setMaxValue(product.getAttribute().get(1).getValue());
            txtAttibute.setText(product.getAttribute().get(1).getName());
        }
        if (getArguments().getInt("a") == 2) {
            np.setMaxValue(product.getAttribute().get(2).getValue());
            txtAttibute.setText(product.getAttribute().get(2).getName());
        }*//*

    }


    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        this.onValueChangeListener = onValueChangeListener;
    }

    public void setOnValueGiftChangeListener(OnValueGiftChangeListener onValueGiftChangeListener) {
        this.onValueGiftChangeListener = onValueGiftChangeListener;
    }

    private class PickerListener implements NumberPicker.OnScrollListener, NumberPicker.OnValueChangeListener {
        private int scrollState = 0;

        @Override
        public void onScrollStateChange(NumberPicker view, int scrollState) {
            this.scrollState = scrollState;
            if (scrollState == SCROLL_STATE_IDLE) {
            }
        }

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            Log.d("newnew", "" + newVal);
            if (params != null) {
                tto = String.valueOf(np.getValue() * attribute);
                Log.d("vvvvv", "" + tto);
                unittotal.setText(CmmFunc.formatMoney(tto, true));
            }
            if (scrollState == 0) {

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                if (params != null) {
                    int id = params.getId();
                    int type = params.getType();
                    beanItem.setId(id);
                    beanItem.setType(type);
                    int test = attribute;
                    Log.d("123456789", "" + test);
                    beanItem.setQuantity(np.getValue() * quantity);
                    beanAttributeUpdate.setValue(np.getValue());
                    onValueChangeListener.onChange(beanItem, beanAttributeUpdate);
                    FragmentHelper.pop(getActivity());
                    break;
                }
                if (beanGift != null) {
                    beanGift.setQuantity(np.getValue());
                    beanItem.setId(beanGift.getId());
                    beanItem.setType(3);
                    beanItem.setQuantity(np.getValue());
                    onValueGiftChangeListener.onGiftChange(beanItem, beanGift);
                    FragmentHelper.pop(getActivity());
                    break;
                }
        }
    }


    public interface OnValueChangeListener {
        void onChange(BeanItem beanItem, BeanAttribute beanAttributeUpdate);
    }

    public interface OnValueGiftChangeListener {
        void onGiftChange(BeanItem beanItem, BeanGift beanGift);
    }

}
*/

public class ReQuantityFragment extends BaseFragment implements View.OnClickListener {
    TextView unitproduct, unittotal, txtAttibute;
    NumberPicker np;
    String price;
    String tto;
    BeanAttribute beanAttribute= new BeanAttribute();
    int totalorder;
    public static int attribute;
    public static int quantity;
    public QuantityListener quantityListener;
    BeanProduct params= new BeanProduct();


    public static ReQuantityFragment newInstance(BeanProduct beanProduct, BeanAttribute current, List<BeanAttribute> attributes, int position) {
        Bundle args = new Bundle();
        args.putString("params", new Gson().toJson(beanProduct));
        args.putString("current", new Gson().toJson(current));
        args.putString("attributes", new Gson().toJson(attributes));
        args.getInt("position", position);
        ReQuantityFragment fragment = new ReQuantityFragment();
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
        rootView = inflater.inflate(R.layout.fragment_requantity, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isLoaded) {
            return;
        }
        rootView.findViewById(R.id.submit).setOnClickListener(ReQuantityFragment.this);
        unitproduct = rootView.findViewById(R.id.unitproduct);
        unittotal = rootView.findViewById(R.id.unittotal);
        np = (NumberPicker) rootView.findViewById(R.id.numberpicker);
        txtAttibute = (TextView) rootView.findViewById(R.id.txvattribute);


        params = new Gson().fromJson(getArguments().getString("params"), BeanProduct.class);
        beanAttribute = new Gson().fromJson(getArguments().getString("current"), BeanAttribute.class);
        np.setMaxValue(beanAttribute.getValue());
        txtAttibute.setText(beanAttribute.getName());
        totalorder = params.getPrice() * beanAttribute.getQuantity();
        quantity = beanAttribute.getQuantity();
        attribute = beanAttribute.getMoney() * 1;
        tto = String.valueOf(np.getValue() * attribute);
        unittotal.setText(CmmFunc.formatMoney(tto, true));


        np.setMinValue(0);
        np.setWrapSelectorWheel(false);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                price = params.getPrice() + "";
                String unit = beanAttribute.getMoney() + "";
                unitproduct.setText(CmmFunc.formatMoney(unit, true));


                PickerListener listener = new PickerListener();
                np.setOnScrollListener(listener);
                np.setOnValueChangedListener(listener);
            }
        });
        isLoaded = true;
    }


    private class PickerListener implements NumberPicker.OnScrollListener, NumberPicker.OnValueChangeListener {
        private int scrollState = 0;

        @Override
        public void onScrollStateChange(NumberPicker view, int scrollState) {
            this.scrollState = scrollState;
            if (scrollState == SCROLL_STATE_IDLE) {
            }
        }

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            Log.d("newnew", "" + newVal);
            if (params != null) {
                tto = String.valueOf(np.getValue() * attribute);
                Log.d("vvvvv", "" + tto);
                unittotal.setText(CmmFunc.formatMoney(tto, true));
            }
            if (scrollState == 0) {

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                try {
                    int value = np.getValue();
                    quantityListener.excute(getArguments().getInt("position"), value);
                    FragmentHelper.pop(getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    public interface QuantityListener {
        void excute(int position, int value);
    }

}
