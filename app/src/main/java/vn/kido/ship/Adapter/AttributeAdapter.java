package vn.kido.ship.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.kido.ship.Bean.BeanAttribute;
import vn.kido.ship.Bean.BeanProduct;
import vn.kido.ship.Fragment.Order.ReQuantityFragment;
import vn.kido.ship.Helper.FragmentHelper;
import vn.kido.ship.R;

import java.util.List;

import static vn.kido.ship.Class.GlobalClass.getActivity;


public class AttributeAdapter extends RecyclerView.Adapter<AttributeAdapter.MyViewHolder> {
    View itemView;
    List<BeanAttribute> map;
    List<BeanAttribute> attributes;
    BeanProduct Product;


    public AttributeAdapter(BeanProduct beanProduct, List<BeanAttribute> map, List<BeanAttribute> attributes) {
        this.Product = beanProduct;
        this.map = map;
        this.attributes = attributes;
    }

    @NonNull
    @Override
    public AttributeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_attribute, parent, false);
        return new AttributeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        try {
            final BeanAttribute beanAttribute = map.get(position);
            final BeanAttribute beanAttributeNew= attributes.get(position);
            if (beanAttribute != null) {
                //holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(recyclerView.getLayoutManager().w / 4, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.name.setText(beanAttribute.getName() + "");
                holder.value.setText(beanAttributeNew.getValue() + "");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ReQuantityFragment reQuantityFragment = ReQuantityFragment.newInstance(Product, beanAttribute, map, position);
                        reQuantityFragment.quantityListener = new ReQuantityFragment.QuantityListener() {
                            @Override
                            public void excute(int position, final int value) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            beanAttributeNew.setValue(value);
                                            notifyDataSetChanged();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        };
                        FragmentHelper.add(reQuantityFragment);
//                        FragmentHelper.add(ReQuantityFragment.newInstance(Product, beanAttribute, map,position));
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

