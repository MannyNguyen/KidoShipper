package vn.kido.ship.Adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import vn.kido.ship.Bean.BeanGift;;
import vn.kido.ship.R;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.MyViewHolder> {
    View itemView;
    Fragment fragment;
    RecyclerView recyclerView;
    List<BeanGift> map;

    public GiftAdapter(Fragment fragment, RecyclerView recyclerView, List<BeanGift> map) {
        this.fragment = fragment;
        this.recyclerView = recyclerView;
        this.map = map;
    }

    @NonNull
    @Override
    public GiftAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_gift, parent, false);
        return new GiftAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GiftAdapter.MyViewHolder holder, final int position) {
        try {
            final BeanGift beanGift = map.get(position);
            if (beanGift != null) {
                holder.txvName.setText(beanGift.getName() + "");
                Picasso.get().load(beanGift.getImage()).placeholder(R.drawable.ic_error).resize(125, 125).into(holder.imvGift);
                holder.unit2.setText(String.valueOf(beanGift.getQuantity()));
                holder.txv2.setText(String.valueOf(beanGift.getQuantity()));
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

        TextView txvName, unit2, txv2;
        ImageView imvGift;

        public MyViewHolder(View itemView) {
            super(itemView);
            txvName = itemView.findViewById(R.id.name);
            imvGift = itemView.findViewById(R.id.thumbnail);
            unit2 = itemView.findViewById(R.id.unit2);
            txv2 = itemView.findViewById(R.id.txv2);

        }
    }
}