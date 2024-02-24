package vn.kido.ship.Class;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class CmmFunc {
    public static void hideKeyboard(Activity activity) {
        try {
            View v = GlobalClass.getActivity().getCurrentFocus();
            InputMethodManager imm = (InputMethodManager) GlobalClass.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (v != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int convertDpToPx(Activity activity, int dp) {
        try {
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, activity.getResources().getDisplayMetrics()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dp;
    }

    public static String formatMoney(Object value, boolean isPrefix) {
        String str = value + "";
        try {
            for (int i = str.length() - 3; i > 0; i -= 3) {
                str = new StringBuilder(str).insert(i, ",").toString();
            }
            if (isPrefix) {
                str = str + " VNĐ";
            }
            return str;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String formatDiscount(Object value, boolean isPrefix) {
        String str = value + "";
        try {
            if (isPrefix) {
                str ="("+ str + " % )";
            }
            return str;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String formatMoneyNew(Object value, boolean isPrefix) {
        String str = value + "";
        try {
            for (int i = str.length() - 3; i > 0; i -= 3) {
                str = new StringBuilder(str).insert(i, ",").toString();
            }
//            if (isPrefix) {
//                str = str + " " + GlobalClass.getActivity().getString(R.string.vnd);
//            }
            return str;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatKPoint(Object value) {
        return "+ " + value + " Kpoint";
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int maxSize) {
        if (bitmap == null)
            return null;
        try {
            int actualWidth = bitmap.getWidth();
            int actualHeight = bitmap.getHeight();
            float rate = 1;
            if (actualHeight > maxSize || actualWidth > maxSize) {
                if (actualWidth > actualHeight) {
                    rate = (float) maxSize / actualWidth;
                } else {
                    rate = (float) maxSize / actualHeight;
                }
                bitmap = Bitmap.createScaledBitmap(bitmap, Math.round(bitmap.getWidth() * rate), Math.round(bitmap.getHeight() * rate), false);
                return bitmap;
            } else {
                return bitmap;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
