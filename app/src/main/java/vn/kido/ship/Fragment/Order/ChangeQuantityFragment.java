package vn.kido.ship.Fragment.Order;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import vn.kido.ship.Bean.BeanAttribute;
import vn.kido.ship.Bean.BeanProduct;
import vn.kido.ship.Class.CmmFunc;
import vn.kido.ship.Fragment.BaseFragment;
import vn.kido.ship.Helper.FragmentHelper;
import vn.kido.ship.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeQuantityFragment extends BaseFragment implements View.OnClickListener {
    TextView unitproduct, unittotal, txtAttibute;
    NumberPicker np;
    String price;
    String tto;
    int totalorder;
    public static int attribute;
    public static int quantity;
    public QuantityListener quantityListener;
    BeanProduct params= new BeanProduct();


    public static ChangeQuantityFragment newInstance(BeanProduct beanProduct,int position) {
        Bundle args = new Bundle();
        args.putString("params", new Gson().toJson(beanProduct));
        args.getInt("position", position);
        ChangeQuantityFragment fragment = new ChangeQuantityFragment();
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
        rootView = inflater.inflate(R.layout.fragment_change_quantity, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isLoaded) {
            return;
        }
        rootView.findViewById(R.id.submit).setOnClickListener(ChangeQuantityFragment.this);
        unitproduct = rootView.findViewById(R.id.unitproduct);
        unittotal = rootView.findViewById(R.id.unittotal);
        np = (NumberPicker) rootView.findViewById(R.id.numberpicker);
        txtAttibute = (TextView) rootView.findViewById(R.id.txvattribute);


        params = new Gson().fromJson(getArguments().getString("params"), BeanProduct.class);
        np.setMaxValue(params.getQuantity());
        txtAttibute.setText(params.getProduct_name());
        totalorder = params.getPrice() * params.getQuantity();
        quantity = params.getQuantity();
        attribute = params.getPrice() * 1;
        tto = String.valueOf(np.getValue() * attribute);
        unittotal.setText(CmmFunc.formatMoney(tto, true));


        np.setMinValue(0);
        np.setWrapSelectorWheel(false);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                price = params.getPrice() + "";
                String unit = params.getPrice() + "";
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
