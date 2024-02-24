package vn.kido.ship.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import vn.kido.ship.Bean.BeanAttribute;
import vn.kido.ship.Bean.BeanItem;
import vn.kido.ship.Bean.BeanProduct;
import vn.kido.ship.Class.CmmFunc;
import vn.kido.ship.Fragment.Order.ReQuantityFragment;
import vn.kido.ship.Helper.FragmentHelper;
import vn.kido.ship.R;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {


    public interface ProductAttributteClickListener {
        void onProductAttributteClick(int position, BeanProduct product, BeanAttribute beanAttribute, int a, BeanProduct beanProduct);
    }


    List<BeanProduct> map;
    int status;
    Context context;
    ProductAttributteClickListener mClickListener;
    public int numberAtt = -1;


    public ProductAdapter() {
        map = new ArrayList<>();
    }

    public void setData(List<BeanProduct> list, int status, Context context) {
        this.map = list;
        this.status = status;
        this.context = context;
        notifyDataSetChanged();
    }

    public void setProductAttributteClickListener(ProductAttributteClickListener listener) {
        mClickListener = listener;
    }


    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product, parent, false);
        return new ProductAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductAdapter.MyViewHolder holder, final int position) {
        BeanProduct beanProduct;
        BeanProduct product;

        try {
            beanProduct = map.get(position);
            product = new BeanProduct(beanProduct);
            AttributeOrderAdapter attributeAdapter = new AttributeOrderAdapter(product.getId(),map.get(position).getOriginAttribute());
            holder.rcvAttribute.setAdapter(attributeAdapter);
            AttributeAdapter attributeAdapterReal= new AttributeAdapter(product,map.get(position).getAttribute(),map.get(position).getAttribute_received());
            holder.rcvAttributeReal.setAdapter(attributeAdapterReal);
            // BIND PRODUCT VIEW
            holder.bindProduct(beanProduct, product);
            // BIND ATTRIBUTES
            holder.bindAttributes(beanProduct.getAttribute_received(), position, beanProduct, mClickListener, product);
            //Product gốc
            for (int i = 0; i < product.getAttribute().size(); i++) {
                BeanAttribute att = product.getAttribute().get(i);
                if (i == 0) {
                    holder.txtNameQuantityOld1.setText(att.getName() + "");
                    holder.txtQuantityOld1.setText(beanProduct.getOriginAttribute().get(0).getValue() + "");
                } else if (i == 1) {
                    holder.txtNameQuantityOld2.setText(att.getName() + "");
                    holder.txtQuantityOld2.setText(beanProduct.getOriginAttribute().get(1).getValue() + "");
                } else if (i == 2) {
                    holder.txtNameQuantityOld3.setText(att.getName() + "");
                    holder.txtQuantityOld3.setText(beanProduct.getOriginAttribute().get(2).getValue() + "");
                }
            }


        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return map == null ? 0 : map.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtQuantity, txtPrice, txtUnit, txtMinUnit, txtUnitf, txtUnits, txtUnitt,
                txtQuantityOld1, txtQuantityOld2, txtQuantityOld3, txtQuantityNew1, txtQuantityNew2, txtQuantityNew3,
                txtNameQuantityOld1, txtNameQuantityOld2, txtNameQuantityOld3,
                txtNameQuantityNew1, txtNameQuantityNew2, txtNameQuantityNew3,
                txtQuantityReal, txtPriceMaxUnit, txtDiscount, txtPercent;
        ImageView ivProduct;
        TextView btn1, btn2, btn3;
        View lnOne, lnTwo, lnThree, lnrequantity3, lnrequantity2, lnrequantity1, lnrequantity0, lnrealreceived,
                lnquantity3, lnquantity2, lnquantity1, lnquantity0,
                lntotal_real, lnDiscount;
        RecyclerView rcvAttribute,rcvAttributeReal;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.nameproduct);
            txtQuantity = itemView.findViewById(R.id.quantityproduct);
            txtPrice = itemView.findViewById(R.id.priceproduct);
            txtUnit = itemView.findViewById(R.id.unit);
            txtMinUnit = itemView.findViewById(R.id.minunit);
            txtUnitf = itemView.findViewById(R.id.unit1);
            txtUnits = itemView.findViewById(R.id.unit2);
            txtUnitt = itemView.findViewById(R.id.unit3);
            txtPriceMaxUnit = itemView.findViewById(R.id.price_max_unit);
            ivProduct = itemView.findViewById(R.id.ivproduct);
            txtQuantityReal = itemView.findViewById(R.id.quantityproduct_real);
            rcvAttribute = itemView.findViewById(R.id.rcv_attribute);
            rcvAttributeReal = itemView.findViewById(R.id.rcv_attribute_real);
            txtPercent = itemView.findViewById(R.id.percent);

            lnDiscount = itemView.findViewById(R.id.lndiscount);
            lnrealreceived = itemView.findViewById(R.id.lnrealreceived);
            lntotal_real = itemView.findViewById(R.id.lntotal_real);
            rcvAttribute.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            rcvAttributeReal.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

            btn1 = itemView.findViewById(R.id.btn1);
            btn2 = itemView.findViewById(R.id.btn2);
            btn3 = itemView.findViewById(R.id.btn3);
            lnOne = itemView.findViewById(R.id.lnone);
            lnTwo = itemView.findViewById(R.id.lntwo);
            lnThree = itemView.findViewById(R.id.lnthree);

            txtDiscount = itemView.findViewById(R.id.discount);

            lnrequantity0 = itemView.findViewById(R.id.lnrequantity0);
            lnrequantity1 = itemView.findViewById(R.id.lnrequantity1);
            lnrequantity2 = itemView.findViewById(R.id.lnrequantity2);
            lnrequantity3 = itemView.findViewById(R.id.lnrequantity3);

            lnquantity0 = itemView.findViewById(R.id.lnquantity0);
            lnquantity1 = itemView.findViewById(R.id.lnquantity1);
            lnquantity2 = itemView.findViewById(R.id.lnquantity2);
            lnquantity3 = itemView.findViewById(R.id.lnquantity3);

            txtQuantityOld1 = itemView.findViewById(R.id.quantity1old);
            txtQuantityOld2 = itemView.findViewById(R.id.quantity2old);
            txtQuantityOld3 = itemView.findViewById(R.id.quantity3old);

            txtNameQuantityOld1 = itemView.findViewById(R.id.namequantityold1);
            txtNameQuantityOld2 = itemView.findViewById(R.id.namequantityold2);
            txtNameQuantityOld3 = itemView.findViewById(R.id.namequantityold3);

            txtQuantityNew1 = itemView.findViewById(R.id.quantity1new);
            txtQuantityNew2 = itemView.findViewById(R.id.quantity2new);
            txtQuantityNew3 = itemView.findViewById(R.id.quantity3new);

            txtNameQuantityNew1 = itemView.findViewById(R.id.namequantitynew1);
            txtNameQuantityNew2 = itemView.findViewById(R.id.namequantitynew2);
            txtNameQuantityNew3 = itemView.findViewById(R.id.namequantitynew3);
        }


        public void bindProduct(BeanProduct product, BeanProduct beanProduct) {
            String price = String.valueOf(product.getPrice());
            String txtPricemaxunit = String.valueOf(product.getPrice_max_unit());
            String total = String.valueOf(product.getOrigin_total_money());
            String totalreal = String.valueOf(product.getTotal_money());
//            String totalreal = String.valueOf(product.getTotal_price());
            String totalpay = String.valueOf(product.getTotal_pay());
            String percent = String.valueOf(product.getTotal_percent_discount());

            txtPrice.setText(CmmFunc.formatMoneyNew(price, true));
            txtUnit.setText(product.getMax_unit() + "");
            txtPriceMaxUnit.setText(CmmFunc.formatMoneyNew(txtPricemaxunit, true) + "");
            txtQuantity.setText(CmmFunc.formatMoneyNew(total, true));

            txtQuantityReal.setText(CmmFunc.formatMoneyNew(totalreal, true));
            txtMinUnit.setText(product.getMin_unit() + "");
            txtName.setText(String.valueOf(product.getProduct_name() + ""));
            txtDiscount.setText(CmmFunc.formatMoneyNew(totalpay, true) + "");
            txtPercent.setText(CmmFunc.formatDiscount(percent, true));
            if (!TextUtils.isEmpty(product.getProduct_image())) {
                Picasso.get().load(product.getProduct_image()).resize(240, 240).centerInside().into(ivProduct);
            }
            if (product.getTotal_percent_discount() == 0) {
                lnDiscount.setVisibility(View.GONE);
            }

        }

        public void bindAttributes(List<BeanAttribute> attribute, int position, BeanProduct product, ProductAdapter.ProductAttributteClickListener listener, BeanProduct beanProduct) {
            if (status == 4) {
                lnrealreceived.setVisibility(View.GONE);
                lntotal_real.setVisibility(View.GONE);
            }
            if (status == 5) {
                lnrealreceived.setVisibility(View.VISIBLE);
                lntotal_real.setVisibility(View.VISIBLE);
            }
            if (attribute.size() == 1) {
                lnrequantity0.setVisibility(View.INVISIBLE);
                lnrequantity2.setVisibility(View.INVISIBLE);
                lnrequantity3.setVisibility(View.GONE);
                lnquantity0.setVisibility(View.INVISIBLE);
                lnquantity2.setVisibility(View.INVISIBLE);
                lnquantity3.setVisibility(View.GONE);
            } else if (attribute.size() == 2) {
                lnrequantity0.setVisibility(View.INVISIBLE);
                lnrequantity3.setVisibility(View.GONE);
                lnquantity0.setVisibility(View.INVISIBLE);
                lnquantity3.setVisibility(View.GONE);
            } else {
                lnrequantity0.setVisibility(View.GONE);
                lnquantity0.setVisibility(View.GONE);
            }
            for (int i = 0; i < attribute.size(); i++) {
                BeanAttribute att = attribute.get(i);
                if (i == 0) {
                    txtUnitf.setText(att.getName() + "");
                    btn1.setText(att.getValue() + "");
                    txtNameQuantityNew1.setText(att.getName() + "");
                    txtQuantityNew1.setText(att.getValue() + "");
                    numberAtt = 0;
                    bindAttributeClick(lnrequantity1, position, product, att, listener, numberAtt, beanProduct);
                } else if (i == 1) {
                    txtUnits.setText(att.getName() + "");
                    btn2.setText(att.getValue() + "");
                    txtNameQuantityNew2.setText(att.getName() + "");
                    txtQuantityNew2.setText(att.getValue() + "");
                    numberAtt = 1;
                    bindAttributeClick(lnrequantity2, position, product, att, listener, numberAtt, beanProduct);
                } else if (i == 2) {
                    txtUnitt.setText(att.getName() + "");
                    btn3.setText(att.getValue() + "");
                    txtNameQuantityNew3.setText(att.getName() + "");
                    txtQuantityNew3.setText(att.getValue() + "");
                    numberAtt = 2;
                    bindAttributeClick(lnrequantity3, position, product, att, listener, numberAtt, beanProduct);
                }
            }


        }


        public void bindAttributeClick(View view, final int position, final BeanProduct beanProduct, final BeanAttribute beanAttribute, final ProductAdapter.ProductAttributteClickListener listener, final int a, final BeanProduct product) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onProductAttributteClick(position, beanProduct, beanAttribute, a, product);
                }
            });

        }


    }


}