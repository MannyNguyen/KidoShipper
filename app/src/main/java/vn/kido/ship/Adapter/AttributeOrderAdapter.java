package vn.kido.ship.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.kido.ship.Bean.BeanAttribute;
import vn.kido.ship.Fragment.Order.ReQuantityFragment;
import vn.kido.ship.Helper.FragmentHelper;
import vn.kido.ship.R;

public class AttributeOrderAdapter extends RecyclerView.Adapter<AttributeOrderAdapter.MyViewHolder> {
    View itemView;
    List<BeanAttribute> map;
    int idProduct;


    public AttributeOrderAdapter(int idProduct, List<BeanAttribute> map) {
        this.idProduct = idProduct;
        this.map = map;
    }

    @NonNull
    @Override
    public AttributeOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_attribute_order, parent, false);
        return new AttributeOrderAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AttributeOrderAdapter.MyViewHolder holder, int position) {
        try {
            final BeanAttribute beanAttribute = map.get(position);
            if (beanAttribute != null) {
                //holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(recyclerView.getLayoutManager().w / 4, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.name.setText(beanAttribute.getName() + "");
                holder.value.setText(beanAttribute.getValue() + "");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        FragmentHelper.add(ReQuantityFragment.newInstance(idProduct, beanAttribute, map));
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
        TextView name, value;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameattribute);
            value = itemView.findViewById(R.id.valueattribute);
        }
    }
}

