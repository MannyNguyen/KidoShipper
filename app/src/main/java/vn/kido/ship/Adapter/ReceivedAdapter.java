package vn.kido.ship.Adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import vn.kido.ship.Bean.BeanAttribute;
import vn.kido.ship.Bean.BeanProduct;
import vn.kido.ship.Class.CmmFunc;
import vn.kido.ship.R;

public class ReceivedAdapter extends RecyclerView.Adapter<ReceivedAdapter.MyViewHolder> {
    View itemView;
    Fragment fragment;
    List<BeanProduct> map;

    public ReceivedAdapter(Fragment fragment,  List<BeanProduct> map) {
        this.fragment = fragment;
        this.map = map;
    }

    @NonNull
    @Override
    public ReceivedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_received, parent, false);
        return new ReceivedAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReceivedAdapter.MyViewHolder holder, final int position) {
        try {
            final BeanProduct beanProduct = map.get(position);
            if (beanProduct != null) {
                String txtMaxunit = beanProduct.getPrice_max_unit();
                holder.txvName.setText(beanProduct.getProduct_name() + "");
                holder.txtPrice.setText(CmmFunc.formatMoneyNew(beanProduct.getPrice(), true) + "");
                holder.txtMinUnit.setText(beanProduct.getMin_unit() + "");
                holder.txtUnit.setText(beanProduct.getMax_unit() + "");
                holder.txtPriceMaxUnit.setText(CmmFunc.formatMoneyNew(txtMaxunit, true));
                holder.txtTotalProduct.setText(CmmFunc.formatMoneyNew(beanProduct.getTotal_product_price(), true) + "");
                holder.txtDiscount.setText(CmmFunc.formatMoneyNew(beanProduct.getTotal_pay(), true) + "");
                holder.txtTotalReal.setText(CmmFunc.formatMoneyNew(beanProduct.getTotal_price(), true) + "");
                if (!TextUtils.isEmpty(beanProduct.getProduct_image())) {
                    Picasso.get().load(beanProduct.getProduct_image()).resize(240, 240).centerInside().into(holder.ivProduct);
                }
                if (beanProduct.getTotal_percent_discount() == 0) {
                    holder.lnDiscount.setVisibility(View.GONE);
                }
                if (beanProduct.getAttribute().size() == 1) {
                    holder.lnrequantity0.setVisibility(View.INVISIBLE);
                    holder.lnrequantity2.setVisibility(View.INVISIBLE);
                    holder.lnrequantity3.setVisibility(View.GONE);
                    holder.lnquantity0.setVisibility(View.INVISIBLE);
                    holder.lnquantity2.setVisibility(View.INVISIBLE);
                    holder.lnquantity3.setVisibility(View.GONE);
                } else if (beanProduct.getAttribute().size() == 2) {
                    holder.lnrequantity0.setVisibility(View.INVISIBLE);
                    holder.lnrequantity3.setVisibility(View.GONE);
                    holder.lnquantity0.setVisibility(View.INVISIBLE);
                    holder.lnquantity3.setVisibility(View.GONE);
                } else {
                    holder.lnrequantity0.setVisibility(View.GONE);
                    holder.lnquantity0.setVisibility(View.GONE);
                }
                for (int i = 0; i < beanProduct.getAttribute().size(); i++) {
                    BeanAttribute att = beanProduct.getAttribute().get(i);
                    if (i == 0) {
                        holder.txtQuantity1Old.setText(att.getValue() + "");
                        holder.txtNameQuantityOld1.setText(att.getName() + "");
                    } else if (i == 1) {
                        holder.txtQuantity2Old.setText(att.getValue() + "");
                        holder.txtNameQuantityOld2.setText(att.getName() + "");
                    } else if (i == 2) {
                        holder.txtQuantity3Old.setText(att.getValue() + "");
                        holder.txtNameQuantityOld3.setText(att.getName() + "");
                    }
                }
                for (int i = 0; i < beanProduct.getAttribute_received().size(); i++) {
                    BeanAttribute attReceived = beanProduct.getAttribute_received().get(i);
                    if (i == 0) {
                        holder.txtQuantity1New.setText(attReceived.getValue() + "");
                        holder.txtNameQuantityNew1.setText(attReceived.getName() + "");
                    } else if (i == 1) {
                        holder.txtQuantity2New.setText(attReceived.getValue() + "");
                        holder.txtNameQuantityNew2.setText(attReceived.getName() + "");
                    } else if (i == 2) {
                        holder.txtQuantity3New.setText(attReceived.getValue() + "");
                        holder.txtNameQuantityNew3.setText(attReceived.getName() + "");
                    }
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        FragmentHelper.add(ReQuantityFragment.newInstance(position,beanGift));

                    }
                });
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

        TextView txvName, txtPrice, txtUnit, txtMinUnit, txtTotalProduct,
                txtTotalReal,
                txtDiscount, txtQuantity1Old, txtQuantity2Old, txtQuantity3Old,
                txtQuantity1New, txtQuantity2New, txtQuantity3New,
                txtNameQuantityOld1, txtNameQuantityOld2, txtNameQuantityOld3,
                txtNameQuantityNew1, txtNameQuantityNew2, txtNameQuantityNew3,
                txtPriceMaxUnit;
        ImageView ivProduct;
        View lnrequantity0, lnrequantity1, lnrequantity2, lnrequantity3, lnquantity0, lnquantity1, lnquantity2, lnquantity3,
                lnDiscount;

        public MyViewHolder(View itemView) {
            super(itemView);
            txvName = itemView.findViewById(R.id.nameproduct);
            ivProduct = itemView.findViewById(R.id.ivproduct);
            txtPrice = itemView.findViewById(R.id.priceproduct);
            txtUnit = itemView.findViewById(R.id.unit);
            txtMinUnit = itemView.findViewById(R.id.minunit);
            txtTotalProduct = itemView.findViewById(R.id.total_product);
            txtDiscount = itemView.findViewById(R.id.discount);
            txtTotalReal = itemView.findViewById(R.id.total_real);
            txtQuantity1Old = itemView.findViewById(R.id.quantity1old);
            txtQuantity2Old = itemView.findViewById(R.id.quantity2old);
            txtQuantity3Old = itemView.findViewById(R.id.quantity3old);
            txtQuantity1New = itemView.findViewById(R.id.quantity1new);
            txtQuantity2New = itemView.findViewById(R.id.quantity2new);
            txtQuantity3New = itemView.findViewById(R.id.quantity3new);
            txtNameQuantityOld1 = itemView.findViewById(R.id.namequantityold1);
            txtNameQuantityOld2 = itemView.findViewById(R.id.namequantityold2);
            txtNameQuantityOld3 = itemView.findViewById(R.id.namequantityold3);
            txtNameQuantityNew1 = itemView.findViewById(R.id.namequantitynew1);
            txtNameQuantityNew2 = itemView.findViewById(R.id.namequantitynew2);
            txtNameQuantityNew3 = itemView.findViewById(R.id.namequantitynew3);
            txtPriceMaxUnit = itemView.findViewById(R.id.price_max_unit);

            lnDiscount = itemView.findViewById(R.id.lndiscount);

            lnrequantity0 = itemView.findViewById(R.id.lnrequantity0);
            lnrequantity1 = itemView.findViewById(R.id.lnrequantity1);
            lnrequantity2 = itemView.findViewById(R.id.lnrequantity2);
            lnrequantity3 = itemView.findViewById(R.id.lnrequantity3);

            lnquantity0 = itemView.findViewById(R.id.lnquantity0);
            lnquantity1 = itemView.findViewById(R.id.lnquantity1);
            lnquantity2 = itemView.findViewById(R.id.lnquantity2);
            lnquantity3 = itemView.findViewById(R.id.lnquantity3);

        }
    }
}