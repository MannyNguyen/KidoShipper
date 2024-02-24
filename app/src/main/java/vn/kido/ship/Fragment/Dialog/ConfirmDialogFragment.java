package vn.kido.ship.Fragment.Dialog;


import android.content.Intent;
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
public class ConfirmDialogFragment extends BaseDialogFragment {

    private String message = "";
    private Runnable runnable;
    private String title = "";

    public ConfirmDialogFragment() {
        // Required empty public constructor
    }

    public static ConfirmDialogFragment newInstance() {

        Bundle args = new Bundle();
        ConfirmDialogFragment fragment = new ConfirmDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_dialog, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            this.setCancelable(false);
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
                    ConfirmDialogFragment.this.dismissAllowingStateLoss();
                    if (runnable != null) {
                        runnable.run();
                    }
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
