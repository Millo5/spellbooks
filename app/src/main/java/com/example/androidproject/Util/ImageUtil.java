package com.example.androidproject.Util;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageUtil {


    public static String copyUriToInternalStorage(Context context, Uri uri, String fileName) throws IOException {
        File file = new File(context.getFilesDir(), fileName);
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(file)) {

            if (inputStream == null)  throw new IOException("Unable to open input stream for URI: " + uri);

            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }
        return file.getAbsolutePath();
    }

    public static Uri getUriFromPath(String path) {
        return Uri.fromFile(new File(path));
    }

}
