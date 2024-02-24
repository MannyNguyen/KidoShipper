package vn.kido.ship.Adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vn.kido.ship.Bean.BeanStore;
import vn.kido.ship.R;

public class ProgramSaleAdapter extends RecyclerView.Adapter<ProgramSaleAdapter.MyViewHolder> {
    View itemView;
    Fragment fragment;
    RecyclerView recyclerView;
    List<BeanStore> map;

    public ProgramSaleAdapter(Fragment fragment, RecyclerView recyclerView, List<BeanStore> map) {
        this.fragment = fragment;
        this.recyclerView = recyclerView;
        this.map = map;
    }

    @NonNull
    @Override
    public ProgramSaleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_store, parent, false);
        return new ProgramSaleAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProgramSaleAdapter.MyViewHolder holder, int position) {
        try {
            final BeanStore beanStore = map.get(position);
            if (beanStore != null) {

                holder.txtProgram.setText(beanStore.getId() + "");

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        FragmentHelper.add(DetailOrderFragment.newInstance(1));
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
        TextView txtProgram;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtProgram = itemView.findViewById(R.id.active);

        }
    }


}