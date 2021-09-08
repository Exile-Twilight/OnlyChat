package com.example.onlychat.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Util {
    public static Bitmap base64ToBitmap(String codeBase64)
    {
        byte[] bytes = Base64.decode(codeBase64,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        return bitmap;
    }

    public static String bitmapTobase64(Bitmap imageBitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream =  new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String base64 = Base64.encodeToString(bytes,Base64.DEFAULT);
        return base64;
    }
}
