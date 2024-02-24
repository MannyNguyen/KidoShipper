package vn.kido.ship.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.kido.ship.Bean.BeanClaim;
import vn.kido.ship.Fragment.Order.PromotionGiftFragment;
import vn.kido.ship.R;

public class ClaimAdapter extends RecyclerView.Adapter<ClaimAdapter.MenuViewHolder> {
    PromotionGiftFragment fragment;
    RecyclerView recyclerView;
    List<BeanClaim> claims;

    public ClaimAdapter(PromotionGiftFragment fragment, RecyclerView recycler, List<BeanClaim> claims) {
        this.fragment = fragment;
        this.recyclerView = recycler;
        this.claims = claims;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_requantity_gift, parent, false);
        return new MenuViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        try {
            final BeanClaim item = claims.get(position);
            if (item != null) {
                holder.title.setText(item.getTitle() + "");
                if (item.isCanClaim()) {
                    holder.active.setVisibility(View.VISIBLE);
                } else {
                    holder.active.setVisibility(View.GONE);
                }
                if (item.isActive()) {
                    holder.container.setBackgroundResource(R.drawable.background_btn);
                } else {
                    holder.container.setBackgroundResource(R.drawable.background_seekbar);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BeanClaim beanActive = BeanClaim.getActive(claims);
                        if (item.getId() == beanActive.getId()) {
                            return;
                        }
                        for (BeanClaim beanClaim : claims) {
                            beanClaim.setActive(false);
                        }
                        item.setActive(true);
                        notifyDataSetChanged();
                        fragment.getRewards(item.getId());
                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return claims.size();
    }


    public class MenuViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private View active;
        private View container;

        public MenuViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            active = itemView.findViewById(R.id.active);
            container = itemView.findViewById(R.id.container);
        }
    }
}
