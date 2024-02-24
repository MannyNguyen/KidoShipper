package vn.kido.ship.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import vn.kido.ship.Bean.BeanAttribute;
import vn.kido.ship.Bean.BeanGift;
import vn.kido.ship.R;

public class GiftAdapterTest extends RecyclerView.Adapter<GiftAdapterTest.MyViewHolder> {

    public interface GiftAttributteClickListener {
        void onGiftAttributteClick(int position, List<BeanGift> beanGiftList, BeanGift gift);
    }


    List<BeanGift> map;
    GiftAdapterTest.GiftAttributteClickListener mClickListener;


    public GiftAdapterTest() {
        map = new ArrayList<>();
    }

    public void setData(List<BeanGift> list) {
        this.map = list;
        notifyDataSetChanged();
    }

    public void setGiftAttributteClickListener(GiftAdapterTest.GiftAttributteClickListener listener) {
        mClickListener = listener;
    }


    @NonNull
    @Override
    public GiftAdapterTest.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_gift, parent, false);
        return new GiftAdapterTest.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GiftAdapterTest.MyViewHolder holder, final int position) {
        BeanGift beanGift;
        List<BeanGift> beanGiftList = map;
        try {

            beanGift = map.get(position);
            holder.bindGift(beanGift);
            holder.bindAttributes(position, beanGiftList, beanGift, mClickListener);


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

        TextView txvName, txv1, txv3, unit1, unit3, unit2, txv2;
        ImageView imvGift;

        public MyViewHolder(View itemView) {
            super(itemView);
            txvName = itemView.findViewById(R.id.name);
            imvGift = itemView.findViewById(R.id.thumbnail);
            unit2 = itemView.findViewById(R.id.unit2);
            txv2 = itemView.findViewById(R.id.txv2);
            unit1 = itemView.findViewById(R.id.unit1);
            txv1 = itemView.findViewById(R.id.txv1);
            unit3 = itemView.findViewById(R.id.unit3);
            txv3 = itemView.findViewById(R.id.txv3);

        }


        public void bindGift(BeanGift gift) {
            txvName.setText(gift.getName() + "");
            Picasso.get().load(gift.getImage()).placeholder(R.drawable.ic_error).resize(125, 125).into(imvGift);
            unit2.setText(String.valueOf(gift.getQuantity()));
            txv2.setText(String.valueOf(gift.getQuantity()));

        }

        public void bindAttributes(int position, List<BeanGift> beanGiftList, BeanGift beanGift, GiftAdapterTest.GiftAttributteClickListener listener) {
            for (int i = 0; i < beanGift.getAttribute().size(); i++) {
                BeanAttribute att = beanGift.getAttribute().get(i);
                if (i == 0) {
                    txv1.setText(att.getName() + "");
                    unit1.setText(att.getValue() + "");
                } else if (i == 1) {
                    txv2.setText(att.getName() + "");
                    unit2.setText(att.getValue() + "");
                } else if (i == 2) {
                    txv3.setText(att.getName() + "");
                    unit3.setText(att.getValue() + "");
                }
            }
        }


        public void bindAttributeClick(View view, final int position, final List<BeanGift> beanGiftList, final BeanGift beanGift, final GiftAdapterTest.GiftAttributteClickListener listener) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onGiftAttributteClick(position, beanGiftList, beanGift);
                }
            });

        }


    }


}