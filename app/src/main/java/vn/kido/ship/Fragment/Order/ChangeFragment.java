package vn.kido.ship.Fragment.Order;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.kido.ship.Api.APIService;
import vn.kido.ship.Api.ISubscriber;
import vn.kido.ship.Fragment.BaseFragment;
import vn.kido.ship.Helper.FragmentHelper;
import vn.kido.ship.R;

import static android.view.View.GONE;


public class ChangeFragment extends BaseFragment implements View.OnClickListener {
    Button btnConfirmChange;
    EditText edtNoteChange;
    TextView txvReason1, txvReason2, txvReason3;
    View lnReason1, lnReason2, lnReason3;
    ImageView imvNote1, imvNoNote1, imvNote2, imvNoNote2, imvNote3, imvNoNote3;
    String textCheckReason = "";

    public static ChangeFragment newInstance(String order_code) {
        Bundle args = new Bundle();
        ChangeFragment fragment = new ChangeFragment();
        fragment.setArguments(args);
        args.putString("order_code", order_code);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_change, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isLoaded) {
            return;
        }
        threadInit = new Thread(new Runnable() {
            @Override
            public void run() {
                btnConfirmChange = (Button) rootView.findViewById(R.id.btn_confirm_change);
                edtNoteChange = (EditText) rootView.findViewById(R.id.edt_note_change);
                txvReason1 = (TextView) rootView.findViewById(R.id.txvreason1);
                txvReason2 = (TextView) rootView.findViewById(R.id.txvreason2);
                txvReason3 = (TextView) rootView.findViewById(R.id.txvreason3);
                lnReason1 = (LinearLayout) rootView.findViewById(R.id.lnreason1);
                lnReason2 = (LinearLayout) rootView.findViewById(R.id.lnreason2);
                lnReason3 = (LinearLayout) rootView.findViewById(R.id.lnreason3);
                imvNoNote1 = rootView.findViewById(R.id.imv_nonote1);
                imvNote1 = rootView.findViewById(R.id.imv_note1);
                imvNoNote2 = rootView.findViewById(R.id.imv_nonote2);
                imvNote2 = rootView.findViewById(R.id.imv_note2);
                imvNoNote3 = rootView.findViewById(R.id.imv_nonote3);
                imvNote3 = rootView.findViewById(R.id.imv_note3);
                btnConfirmChange.setOnClickListener(ChangeFragment.this);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getreason();
                        lnReason1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (imvNote1.getVisibility() == GONE) {

                                    imvNote1.setVisibility(View.VISIBLE);
                                    imvNoNote1.setVisibility(View.GONE);
                                    imvNote2.setVisibility(View.GONE);
                                    imvNoNote2.setVisibility(View.VISIBLE);
                                    imvNote3.setVisibility(View.GONE);
                                    imvNoNote3.setVisibility(View.VISIBLE);
                                    textCheckReason = txvReason1.getText().toString();
                                } else {
                                    textCheckReason = "";
                                    imvNote1.setVisibility(View.GONE);
                                    imvNoNote1.setVisibility(View.VISIBLE);
                                }

                            }
                        });
                        lnReason2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (imvNote2.getVisibility() == GONE) {
                                    imvNote2.setVisibility(View.VISIBLE);
                                    imvNoNote2.setVisibility(View.GONE);
                                    imvNote1.setVisibility(View.GONE);
                                    imvNoNote1.setVisibility(View.VISIBLE);
                                    imvNote3.setVisibility(View.GONE);
                                    imvNoNote3.setVisibility(View.VISIBLE);
                                    textCheckReason = txvReason1.getText().toString();
                                } else {
                                    textCheckReason = "";
                                    imvNote2.setVisibility(View.GONE);
                                    imvNoNote2.setVisibility(View.VISIBLE);
                                }

                            }
                        });
                        lnReason3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (imvNote3.getVisibility() == GONE) {
                                    imvNote3.setVisibility(View.VISIBLE);
                                    imvNoNote3.setVisibility(View.GONE);
                                    imvNote1.setVisibility(View.GONE);
                                    imvNoNote1.setVisibility(View.VISIBLE);
                                    imvNote2.setVisibility(View.GONE);
                                    imvNoNote2.setVisibility(View.VISIBLE);
                                    textCheckReason = txvReason1.getText().toString();
                                } else {
                                    textCheckReason = "";
                                    imvNote3.setVisibility(View.GONE);
                                    imvNoNote3.setVisibility(View.VISIBLE);
                                }

                            }
                        });

                    }
                });
            }
        });

        threadInit.start();
        isLoaded = true;
    }

    private void cancleOrder(String order_id) {
        String noteChangeOrder = textCheckReason + "\n" + edtNoteChange.getText().toString();
        if (!textCheckReason.equals("")) {
            try {
                showProgress();
                Log.d("wwwwwwwwww", "" + noteChangeOrder);
                APIService.getInstance().cancelOrder(order_id, noteChangeOrder).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ISubscriber() {
                            @Override
                            public void done() {
                                super.done();
                                hideProgress();
                            }

                            @Override
                            public void excute(JSONObject jsonObject) {
                                try {
                                    FragmentHelper.pop(getActivity(), ListOrderFragment.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String textreason="Vui lòng chọn và nhập lý do trả hàng";
            Toast toast= Toast.makeText(getContext(), textreason, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 200);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            v.setTextColor(Color.RED);
            v.setTextSize(14);
            toast.show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm_change:
                cancleOrder(getArguments().getString("order_code"));
                break;
        }
    }

    public void getreason() {
        showProgress();
        APIService.getInstance().getreason()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ISubscriber() {
                    @Override
                    public void done() {
                        super.done();
                        hideProgress();
                    }

                    @Override
                    public void excute(JSONObject jsonObject) {
                        try {
                            JSONArray data = jsonObject.getJSONArray("data");
                            txvReason1.setText(data.get(0).toString());
                            txvReason2.setText(data.get(1).toString());
                            txvReason3.setText(data.get(2).toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
