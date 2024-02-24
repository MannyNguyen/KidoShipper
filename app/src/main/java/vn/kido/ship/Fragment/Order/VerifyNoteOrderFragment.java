package vn.kido.ship.Fragment.Order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.kido.ship.Api.APIService;
import vn.kido.ship.Api.ISubscriber;
import vn.kido.ship.Fragment.BaseFragment;
import vn.kido.ship.Helper.FragmentHelper;
import vn.kido.ship.R;

import static android.view.View.GONE;


public class VerifyNoteOrderFragment extends BaseFragment implements View.OnClickListener {
    EditText edtnote;
    ImageView imv_nonote, imv_note;
    int highlight = 0;
    View lnNote;
    View submit;

    public static VerifyNoteOrderFragment newInstance(String order_code, int id, String code, String base64, int type, String items) {
        Bundle args = new Bundle();
        VerifyNoteOrderFragment fragment = new VerifyNoteOrderFragment();
        fragment.setArguments(args);
        args.putString("order_code", order_code);
        args.putInt("id", id);
        args.putString("code", code);
        args.putString("base64", base64);
        args.putInt("type", type);
        args.putString("items", items);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_verify_note_order, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isLoaded) {
            return;
        }
        edtnote = rootView.findViewById(R.id.edt_note);
        submit = rootView.findViewById(R.id.submit);
        imv_nonote = rootView.findViewById(R.id.imv_nonote);
        imv_note = rootView.findViewById(R.id.imv_note);
        lnNote = rootView.findViewById(R.id.lnnote);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                lnNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (imv_note.getVisibility() == GONE) {
                            highlight = 1;
                            imv_note.setVisibility(View.VISIBLE);
                            imv_nonote.setVisibility(View.GONE);
                        } else {
                            highlight = 0;
                            imv_note.setVisibility(View.GONE);
                            imv_nonote.setVisibility(View.VISIBLE);
                        }
                        Log.d("aaaaaaa", "" + highlight);
                    }
                });

                submit.setOnClickListener(VerifyNoteOrderFragment.this);

            }
        });
        isLoaded = true;
    }

    private void upload(String order_id, int type, String items, String verifycode, String base64, String note) {
        try {
            showProgress();
            APIService.getInstance().upload(order_id, type, items, verifycode, base64, note, highlight).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ISubscriber() {
                        @Override
                        public void done() {
                            super.done();
                            hideProgress();
                            submit.setOnClickListener(VerifyNoteOrderFragment.this);
                        }

                        @Override
                        public void excute(JSONObject jsonObject) {
                            try {
//                                itemss.clear();
                                FragmentHelper.pop(getActivity(), ListOrderFragment.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                submit.setOnClickListener(null);
                String note = edtnote.getText().toString().trim();
                Toast.makeText(getContext(), "uploading !!", Toast.LENGTH_SHORT).show();
                String order_code = getArguments().getString("order_code");
                String code = getArguments().getString("code");
                String base64 = getArguments().getString("base64");
                int type = getArguments().getInt("type");
                String items = getArguments().getString("items");

                Log.d("!!!!!", type + "|||||" + note + "||||" + order_code + "||||" + code + "||||" + items);
                try {

                    upload(order_code, type, items, code, base64, note);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }
}
