package vn.kido.ship.Helper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.InputStream;

import vn.kido.ship.Class.GlobalClass;


public class ImageHelper {
    public static Bitmap dataToBitmap(Intent data) {
        Bitmap bmp = null;
        try {
            Uri uri = Uri.parse(data.getDataString());
            if (uri != null) {
                String realPath = PathHelper.getPathFromUri(GlobalClass.getActivity(), uri);
                if (realPath != null) {
                    ExifInterface exif = new ExifInterface(realPath);
                    int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    switch (rotation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotation = 90;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotation = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotation = 270;
                            break;
                        default:
                            rotation = 0;
                            break;
                    }
                    Matrix matrix = new Matrix();
                    if (rotation != 0f) {
                        matrix.preRotate(rotation);
                    }

                    bmp = MediaStore.Images.Media.getBitmap(GlobalClass.getActivity().getContentResolver(), uri);
                    bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                } else {
                    if (data.getData() == null) {
                        bmp = (Bitmap) data.getExtras().get("data");
                    } else {
                        InputStream inputStream = GlobalClass.getActivity().getContentResolver().openInputStream(data.getData());
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        bmp = BitmapFactory.decodeStream(bufferedInputStream);
                    }
                }
            } else {
                if (data.getData() == null) {
                    bmp = (Bitmap) data.getExtras().get("data");
                } else {
                    InputStream inputStream = GlobalClass.getActivity().getContentResolver().openInputStream(data.getData());
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    bmp = BitmapFactory.decodeStream(bufferedInputStream);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public static Bitmap uriToBitmap(Uri uri) {
        Bitmap bmp = null;
        try {
            String realPath = PathHelper.getPathFromUri(GlobalClass.getActivity(), uri);
            if (realPath != null) {
                ExifInterface exif = new ExifInterface(realPath);
                int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (rotation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotation = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotation = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotation = 270;
                        break;
                    default:
                        rotation = 0;
                        break;
                }
                Matrix matrix = new Matrix();
                if (rotation != 0f) {
                    matrix.preRotate(rotation);
                }

                bmp = MediaStore.Images.Media.getBitmap(GlobalClass.getActivity().getContentResolver(), uri);
                bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
            } else {
                bmp = MediaStore.Images.Media.getBitmap(GlobalClass.getActivity().getContentResolver(), uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
