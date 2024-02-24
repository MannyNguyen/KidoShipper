package vn.kido.ship.Fragment.Order;


import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.kido.ship.Api.APIService;
import vn.kido.ship.Api.ISubscriber;
import vn.kido.ship.Class.CmmFunc;
import vn.kido.ship.Fragment.BaseFragment;
import vn.kido.ship.Fragment.Common.HomeFragment;
import vn.kido.ship.Fragment.Map.MapOrderFragment;
import vn.kido.ship.Helper.FragmentHelper;
import vn.kido.ship.Helper.ImageHelper;
import vn.kido.ship.Helper.PermissionHelper;
import vn.kido.ship.R;

import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import static android.view.View.GONE;

public class VerifyOrderFragment extends BaseFragment implements View.OnClickListener {
    final int GALLERY_REQUEST_CODE = 11;
    final int CAPTURE_REQUEST_CODE = 12;
    Uri captureURI;
    PermissionHelper permissionHelper;
    ImageView takePhoto, uploadPhoto;
    Button btnFinish;
    EditText edt;
    String base64 = "";
    EditText edtnote;
    ImageView imv_nonote, imv_note;
    int highlight = 0;
    View lnNote;
    View submit;
    TextView TxvMessNoti;

    public VerifyOrderFragment() {
        // Required empty public constructor
    }

    public static VerifyOrderFragment newInstance(String order_code, int type, String items) {
        Bundle args = new Bundle();
        VerifyOrderFragment fragment = new VerifyOrderFragment();
        fragment.setArguments(args);
        args.putString("order", order_code);
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
        rootView = inflater.inflate(R.layout.fragment_verify_order, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isLoaded) {
            return;
        }
        permissionHelper = new PermissionHelper(VerifyOrderFragment.this);
        threadInit = new Thread(new Runnable() {
            @Override
            public void run() {
                takePhoto = (ImageView) rootView.findViewById(R.id.iv_verify_photo);
                edt = (EditText) rootView.findViewById(R.id.edt_code);
//                uploadPhoto= rootView.findViewById(R.id.btn_next);
                btnFinish = (Button) rootView.findViewById(R.id.btn_next);
//                uploadPhoto.setOnClickListener(VerifyOrderFragment.this);
                takePhoto.setOnClickListener(VerifyOrderFragment.this);
                btnFinish.setOnClickListener(VerifyOrderFragment.this);
                edtnote = rootView.findViewById(R.id.edt_note);
//                submit = rootView.findViewById(R.id.submit);
                imv_nonote = rootView.findViewById(R.id.imv_nonote);
                imv_note = rootView.findViewById(R.id.imv_note);
                lnNote = rootView.findViewById(R.id.lnnote);
                TxvMessNoti = rootView.findViewById(R.id.txt_messnotify);
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

//                        submit.setOnClickListener(VerifyOrderFragment.this);

                    }
                });
            }
        });
        threadInit.start();
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
                            btnFinish.setOnClickListener(VerifyOrderFragment.this);
                        }

                        @Override
                        public void excute(JSONObject jsonObject) {
                            try {
                                Fragment mFragment = FragmentHelper.findFragmentByTag(MapOrderFragment.class);
                                if (mFragment != null) {
                                    MapOrderFragment mapOrderFragment = (MapOrderFragment) mFragment;
                                    mapOrderFragment.isLoaded = false;
                                    FragmentHelper.pop(getActivity(), MapOrderFragment.class);
                                    return;
                                }

                                Fragment lFragment = FragmentHelper.findFragmentByTag(ListOrderFragment.class);
                                if (lFragment != null) {
                                    FragmentHelper.pop(getActivity(), ListOrderFragment.class);
                                    return;
                                }

                                FragmentHelper.pop(getActivity(), HomeFragment.class);
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
    public void onClick(View v) {
        switch (v.getId()) {
/*            case R.id.upload_photo:
                requestPermission(GALLERY_REQUEST_CODE);
                break;*/
            case R.id.iv_verify_photo:
                requestPermission(CAPTURE_REQUEST_CODE);
                break;
            case R.id.btn_next:
                btnFinish.setOnClickListener(null);
                if (TxvMessNoti.getVisibility() == View.VISIBLE) {
                    TxvMessNoti.setVisibility(View.GONE);
                }
                String code = edt.getText().toString().trim();
                if (base64.equals("") || (TextUtils.isEmpty(code))) {
                    btnFinish.setOnClickListener(VerifyOrderFragment.this);
                    TxvMessNoti.setVisibility(View.VISIBLE);
                    return;
                }


                String order = getArguments().getString("order");
                int id = getArguments().getInt("id");
                int type = getArguments().getInt("type");
                String item = getArguments().getString("items");
                String note = edtnote.getText().toString().trim();
                upload(order, type, item, code, base64, note);

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void manuResume() {
        super.manuResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case GALLERY_REQUEST_CODE:
                if (data == null) {
                    return;
                }
                parseDataImage(data);
                break;
            case CAPTURE_REQUEST_CODE:
                parseDataImage(captureURI);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isPerpermissionForAllGranted = true;
        switch (requestCode) {
            case GALLERY_REQUEST_CODE:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        isPerpermissionForAllGranted = false;
                    }
                }
                if (isPerpermissionForAllGranted) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY_REQUEST_CODE);
                }
                break;
            case CAPTURE_REQUEST_CODE:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        isPerpermissionForAllGranted = false;
                    }
                }
                if (isPerpermissionForAllGranted) {
                    requestPermission(CAPTURE_REQUEST_CODE);
                }
                break;
        }
    }

    private void requestPermission(int requestCode) {
        if (permissionHelper.requestPermission(requestCode, Manifest.permission.WRITE_EXTERNAL_STORAGE, "Xin cấp quyền truy cập bộ nhớ máy cho Kido!")) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, requestCode);
            }
            if (requestCode == CAPTURE_REQUEST_CODE) {
                if (permissionHelper.requestPermission(requestCode, Manifest.permission.CAMERA, "Xin cấp quyền CAMERA cho Kido!")) {
                    openCamera();
                }
            }

        }
    }

    //region Methods

    private void openCamera() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "KIDO" + System.currentTimeMillis());
                values.put(MediaStore.Images.Media.DESCRIPTION, "KIDO" + System.currentTimeMillis());
                captureURI = getActivity().getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, captureURI);
                startActivityForResult(intent, CAPTURE_REQUEST_CODE);
            }
        }).start();
    }

    private void parseDataImage(final Object data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bmp = null;
                    if (data instanceof Intent) {
                        bmp = ImageHelper.dataToBitmap((Intent) data);
                    } else if (data instanceof Uri) {
                        bmp = ImageHelper.uriToBitmap((Uri) data);
                    }
                    if (bmp == null) {
                        return;
                    }
                    bmp = CmmFunc.resizeBitmap(bmp, 1280);
//                    takePhoto.setImageBitmap(bmp);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                    byte[] arr = stream.toByteArray();
                    base64 = Base64.encodeToString(arr, Base64.DEFAULT);
                    Log.d("base64", "" + base64);
                    final Bitmap finalBmp = bmp;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                takePhoto.setImageBitmap(finalBmp);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
    //endregion


}
