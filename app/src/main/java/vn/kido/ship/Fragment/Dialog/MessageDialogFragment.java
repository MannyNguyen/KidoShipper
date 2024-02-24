package vn.kido.ship.Fragment.Dialog;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import vn.kido.ship.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageDialogFragment extends BaseDialogFragment {

    private String message = "";
    private Runnable runnable;
    private Runnable cancle;
    private String title = "";
    private boolean isTouchOutSide;

    public MessageDialogFragment() {
        // Required empty public constructor
    }

    public static MessageDialogFragment newInstance() {

        Bundle args = new Bundle();
        MessageDialogFragment fragment = new MessageDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_dialog, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            TextView message = getView().findViewById(R.id.message);
            TextView title = getView().findViewById(R.id.title);
            View submit = getView().findViewById(R.id.submit);
            message.setText(getMessage());
            if (!TextUtils.isEmpty(getTitle())) {
                title.setText(getTitle());
            }
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MessageDialogFragment.this.dismissAllowingStateLoss();
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
            getView().findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isTouchOutSide() == true) {
                        MessageDialogFragment.this.dismissAllowingStateLoss();
                        if (cancle != null) {
                            cancle.run();
                        }
                    } else if (isTouchOutSide() == false) {
                        Toast.makeText(getContext(), "Bắt buộc!", Toast.LENGTH_SHORT).show();
                    }
//                    MessageDialogFragment.this.setTouchOutSide(isTouchOutSide());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public Runnable getCancle() {
        return cancle;
    }

    public void setCancle(Runnable cancle) {
        this.cancle = cancle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isTouchOutSide() {
        return isTouchOutSide;
    }

    public void setTouchOutSide(boolean touchOutSide) {
        isTouchOutSide = touchOutSide;
    }
}
