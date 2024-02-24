package vn.kido.ship.Adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.List;

import vn.kido.ship.Bean.BeanOrder;
import vn.kido.ship.Class.CmmFunc;
import vn.kido.ship.Fragment.Order.DetailOrderFragment;
import vn.kido.ship.Helper.FragmentHelper;
import vn.kido.ship.R;


public class HistoryOrderAdapter extends RecyclerView.Adapter<HistoryOrderAdapter.MyViewHolder> {
    Fragment fragment;
    RecyclerView recyclerView;
    List<BeanOrder> map;
    public  int typetab;
    public  int typeorder = -1;

    public HistoryOrderAdapter(Fragment fragment, RecyclerView recyclerView, List<BeanOrder> map, int type) {
        this.fragment = fragment;
        this.recyclerView = recyclerView;
        this.map = map;
        this.typetab = type;
    }

    @NonNull
    @Override
    public HistoryOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order, parent, false);
        return new HistoryOrderAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryOrderAdapter.MyViewHolder holder, int position) {
        try {
            final BeanOrder beanOrder = map.get(position);
            if (beanOrder != null) {
                typeorder = beanOrder.getType();
                if (typeorder == 1) {
                    holder.backgroundTypeOrder.setBackgroundResource(R.drawable.background_type_order_red);
                    holder.txvTypeOrder.setTextColor(Color.WHITE);
                    holder.txvTypeOrder.setText(R.string.ordernew);
                } else if (typeorder == 2) {
                    holder.backgroundTypeOrder.setBackgroundResource(R.drawable.background_type_order_gray);
                    holder.txvTypeOrder.setTextColor(Color.GRAY);
                    holder.txvTypeOrder.setText(R.string.intact);
                } else if (typeorder == 3) {
                    holder.backgroundTypeOrder.setBackgroundResource(R.drawable.background_type_order_gray);
                    holder.txvTypeOrder.setTextColor(Color.GRAY);
                    holder.txvTypeOrder.setText(R.string.changeodd);
                } else if (typeorder == 4) {
                    holder.backgroundTypeOrder.setBackgroundResource(R.drawable.background_type_order_gray);
                    holder.txvTypeOrder.setTextColor(Color.GRAY);
                    holder.txvTypeOrder.setText(R.string.returnodd);
                } else if (typeorder == 0) {
                    holder.backgroundTypeOrder.setBackgroundResource(R.drawable.background_type_order_red);
                    holder.txvTypeOrder.setTextColor(Color.WHITE);
                    holder.txvTypeOrder.setText(R.string.ordernew);
                }
                holder.txtSuggestTime.setText(new DateTime(beanOrder.getSuggest_time()).toString("dd/MM/yyyy"));

                holder.txtCreateDay.setText(new DateTime(beanOrder.getCreate_date()).toString("dd/MM/yyyy"));
                holder.txtQuantity.setText(beanOrder.getQuantity() + "");
                holder.txtOrderCode.setText(beanOrder.getOrder_code() + "");


                holder.txtDeliveryTime.setText(new DateTime(beanOrder.getDelivered_date()).toString("dd/MM/yyyy"));

//                holder.txtDeliveryTime.setText(new DateTime(beanOrder.getDelivered_date()).toString("EEEE, dd/MM/yyyy", new Locale("vi", "VN")));
                String total = String.valueOf(beanOrder.getTotal_money() + "");
                holder.txtTotal.setText(CmmFunc.formatMoney(total, true));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.itemView.setOnClickListener(null);
                        Log.d("123456789", "" + beanOrder.getDelivered_date());
                        FragmentHelper.add(DetailOrderFragment.newInstance(beanOrder.getOrder_code()));

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return map.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtOrderCode, txtQuantity, txtCreateDay, txtSuggestTime, txtTotal, txtDeliveryTime, txvTextDate, txvTypeOrder;
        View backgroundTypeOrder;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtOrderCode = itemView.findViewById(R.id.order_code);
            txtQuantity = itemView.findViewById(R.id.quantity);
            txtCreateDay = itemView.findViewById(R.id.create_date);
            txtSuggestTime = itemView.findViewById(R.id.suggest_time);
            txtTotal = itemView.findViewById(R.id.total);
            txtDeliveryTime = itemView.findViewById(R.id.delivery_date);
            txvTextDate = itemView.findViewById(R.id.txv_text_date);
            txvTypeOrder = itemView.findViewById(R.id.typeorder);
            backgroundTypeOrder = itemView.findViewById(R.id.backgroundtypeorder);
            if (typetab < 3) {
                txtSuggestTime.setVisibility(View.GONE);
                txvTextDate.setText(R.string.datedelivered);
            } else {
                txtDeliveryTime.setVisibility(View.GONE);
            }
        }
    }
}
