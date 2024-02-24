package vn.kido.ship.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import vn.kido.ship.Bean.BeanAttribute;
import vn.kido.ship.Bean.BeanProduct;
import vn.kido.ship.Class.CmmFunc;
import vn.kido.ship.Fragment.Order.ChangeQuantityFragment;
import vn.kido.ship.Fragment.Order.PromotionGiftFragment;
import vn.kido.ship.Fragment.Order.ReQuantityFragment;
import vn.kido.ship.Helper.FragmentHelper;
import vn.kido.ship.R;

import static vn.kido.ship.Class.GlobalClass.getActivity;

public class PromotionGiftAdapter extends RecyclerView.Adapter<PromotionGiftAdapter.MyViewHolder> {


    public interface ProductAttributteClickListener {
        void onProductAttributteClick(int position, BeanProduct product, BeanAttribute beanAttribute, int a, BeanProduct beanProduct);
    }


    List<BeanProduct> map;
    int status;
    Context context;
    PromotionGiftAdapter.ProductAttributteClickListener mClickListener;


    public PromotionGiftAdapter() {
        map = new ArrayList<>();
    }

    public void setData(List<BeanProduct> list, int status, Context context) {
        this.map = list;
        this.status = status;
        this.context = context;
        notifyDataSetChanged();
    }

    public void setProductAttributteClickListener(PromotionGiftAdapter.ProductAttributteClickListener listener) {
        mClickListener = listener;
    }


    @NonNull
    @Override
    public PromotionGiftAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_requantity_gift, parent, false);
        return new PromotionGiftAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PromotionGiftAdapter.MyViewHolder holder, final int position) {
        final BeanProduct beanProduct;
        final BeanProduct product;

        try {
            beanProduct = map.get(position);
            product = new BeanProduct(beanProduct);
            /*AttributeAdapter attributeAdapterReal = new AttributeAdapter(product, map.get(position).getAttribute(), map.get(position).getAttribute_received());
            holder.rcvAttributeReal.setAdapter(attributeAdapterReal);*/
            // View
            String quantity = String.valueOf(product.getQuantity());
            String name = String.valueOf(product.getProduct_name());
            String minunit = String.valueOf(product.getMin_unit());
            String price = String.valueOf(product.getPrice());

            holder.txvQuantity.setText(quantity);
            holder.txvNameGift.setText(name);
            holder.txvPriceGift.setText(CmmFunc.formatMoneyNew(price, true));
            holder.txvMinUnit.setText(minunit);
            holder.txvUnit.setText(minunit);
            holder.lnRequantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChangeQuantityFragment reQuantityFragment = ChangeQuantityFragment.newInstance(beanProduct, position);
                    reQuantityFragment.quantityListener = new ChangeQuantityFragment.QuantityListener() {
                        @Override
                        public void excute(int position, final int value) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        beanProduct.setQuantity(value);
                                        notifyDataSetChanged();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    };
                    FragmentHelper.add(reQuantityFragment);
                }
            });


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
        //ánh xạ
//        RecyclerView rcvAttributeReal;
        TextView txvQuantity, txvNameGift, txvMinUnit, txvPriceGift, txvUnit, txvMaxUnit, txvMoney;
        LinearLayout lnRequantity;

        public MyViewHolder(View itemView) {
            super(itemView);
            txvQuantity = itemView.findViewById(R.id.unit_gift_quantity);
            txvNameGift = itemView.findViewById(R.id.namegift);
            txvMinUnit = itemView.findViewById(R.id.minunit);
            txvPriceGift = itemView.findViewById(R.id.price_gift);
            txvUnit = itemView.findViewById(R.id.unit_gift);
            txvMaxUnit = itemView.findViewById(R.id.max_unit);
            txvMoney = itemView.findViewById(R.id.money);
            lnRequantity = itemView.findViewById(R.id.ln_requantity);
//            rcvAttributeReal = itemView.findViewById(R.id.rcv_attribute_real);
//            rcvAttributeReal.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));


        }


    }


}