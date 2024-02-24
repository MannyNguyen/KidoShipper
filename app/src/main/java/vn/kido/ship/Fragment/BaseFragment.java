package vn.kido.ship.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;

import vn.kido.ship.Class.CmmFunc;
import vn.kido.ship.Helper.FragmentHelper;
import vn.kido.ship.R;

public class BaseFragment extends Fragment {

    public View rootView;
    public boolean isLoaded;
    public Thread threadInit;

    View layoutProgress;

    public BaseFragment() {
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (rootView == null) {
            return;
        }
        rootView.setClickable(true);
        layoutProgress = rootView.findViewById(R.id.layout_progress);
        View back = rootView.findViewById(R.id.back);
        View callsupport= rootView.findViewById(R.id.call);
        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentHelper.pop(getActivity());
                }
            });
        }
        if (callsupport != null) {
            callsupport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + "+" + getString(R.string.phone_support)));
                    startActivity(intent);
                }
            });
        }

    }

    public void manuResume() {

        View cart = rootView.findViewById(R.id.cart);
        if (cart != null) {

        }
        CmmFunc.hideKeyboard(getActivity());
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (threadInit != null) {
            threadInit.interrupt();
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        CmmFunc.hideKeyboard(getActivity());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


    }
    public void showProgress() {
        try {
            CmmFunc.hideKeyboard(getActivity());
            if (layoutProgress == null) {
                return;
            }
            if (layoutProgress.getVisibility() == View.VISIBLE) {
                return;
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    layoutProgress.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideProgress() {
        try {
            if (layoutProgress == null) {
                return;
            }
            if (layoutProgress.getVisibility() == View.GONE) {
                return;
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    layoutProgress.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
