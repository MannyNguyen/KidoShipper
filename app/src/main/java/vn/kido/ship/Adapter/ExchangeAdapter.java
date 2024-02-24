package vn.kido.ship.Adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vn.kido.ship.Bean.BeanProduct;
import vn.kido.ship.Class.CmmFunc;
import vn.kido.ship.R;

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.MyViewHolder> {
    View itemView;
    Fragment fragment;
    List<BeanProduct> map;

    public ExchangeAdapter(Fragment fragment, List<BeanProduct> map) {
        this.fragment = fragment;
        this.map = map;
    }

    @NonNull
    @Override
    public ExchangeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_exchange_order, parent, false);
        return new ExchangeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExchangeAdapter.MyViewHolder holder, final int position) {
        try {
            final BeanProduct beanProduct = map.get(position);
            if (beanProduct != null) {
                holder.txvReason.setText(beanProduct.getReason() + "");
                String txtMaxunit = beanProduct.getPrice_max_unit();
                holder.txtName.setText(beanProduct.getProduct_name() + "");
                holder.txtPrice.setText(CmmFunc.formatMoneyNew(beanProduct.getPrice(), true) + "");
                holder.txtMinUnit.setText(beanProduct.getMin_unit() + "");
                holder.txtUnit.setText(beanProduct.getMax_unit() + "");
                holder.txtPriceMaxUnit.setText(CmmFunc.formatMoneyNew(txtMaxunit, true));
                holder.txtQuantity.setText(beanProduct.getQuantity() + "");
                holder.txtNameQuantity.setText(beanProduct.getMin_unit() + "");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        FragmentHelper.add(ReQuantityFragment.newInstance(position,beanGift));

                    }
                });
                AttributeOrderAdapter attributeAdapter = new AttributeOrderAdapter(beanProduct.getId(),map.get(position).getAttribute());
                holder.rcvAttribute.setAdapter(attributeAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (map != null) {
            return map.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txvReason, txtName, txtPrice, txtUnit, txtMinUnit, txtPriceMaxUnit, txtQuantity, txtNameQuantity;
        ImageView ivProduct;
        RecyclerView rcvAttribute;


        public MyViewHolder(View itemView) {
            super(itemView);
            txvReason = itemView.findViewById(R.id.txvreason);
            txtName = itemView.findViewById(R.id.nameproduct);
            txtPrice = itemView.findViewById(R.id.priceproduct);
            txtUnit = itemView.findViewById(R.id.unit);
            txtMinUnit = itemView.findViewById(R.id.minunit);
            txtPriceMaxUnit = itemView.findViewById(R.id.price_max_unit);
            txtQuantity = itemView.findViewById(R.id.quantity);
            txtNameQuantity = itemView.findViewById(R.id.namequantity);
            ivProduct = itemView.findViewById(R.id.ivproduct);
            rcvAttribute= itemView.findViewById(R.id.rcv_attribute);
            rcvAttribute.setLayoutManager(new LinearLayoutManager(fragment.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
    }
}