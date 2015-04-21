package com.Ramdanak.ramdank;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created on 4/21/15.
 */
public abstract class BitmapHelper {
    public static byte[] bitmapToBytes (Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap BytesToBitmap(byte [] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
