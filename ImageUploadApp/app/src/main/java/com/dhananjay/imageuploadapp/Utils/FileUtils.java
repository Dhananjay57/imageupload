package com.dhananjay.imageuploadapp.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.dhananjay.imageuploadapp.Utils.FileType;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtils {

    public static final String DIRECTORY_PROFILE_IMAGE = "Profile Pic";

    /**
     * Returns true if bitmap was saved as jpeg otherwise false
     *
     * @param context
     * @param filename
     * @param fileType
     * @param bitmap
     * @return
     */
    public static boolean storeImageToStorage(Context context, String filename, FileType fileType, Bitmap bitmap) {
        File file;
        if (fileType == FileType.PROFILE_IMAGE) {
            File parentDirectory = getParentDirectory(context, fileType);
            parentDirectory.mkdirs();
            file = new File(parentDirectory, filename + ".jpg");
        } else {
            return false;
        }
        if (file.exists())
            file.delete();
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 65, outputStream);
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Important note : clear disk cache and memory of glide in case where image path is same
     * but content may change
     *
     * saves the provided bitmap in location set for profile images
     * @param context
     * @param bitmap
     * @return
     */
    public static boolean storeProfileImageToStorage(Context context, Bitmap bitmap) {
        return storeImageToStorage(context, "profile_pic", FileType.PROFILE_IMAGE, bitmap);
    }

    public static Uri getProfileImageFromStorage(Context context) {
        File parentDirectory = getParentDirectory(context, FileType.PROFILE_IMAGE);
        File file = new File(parentDirectory, "profile_pic.jpg");
        if (file.exists()) {
            return Uri.fromFile(file);
        } else {
            return Uri.EMPTY;
        }
    }

    public static File getProfileImageFile(Context context) {
        File parentDirectory = getParentDirectory(context, FileType.PROFILE_IMAGE);
        File file = new File(parentDirectory, "profile_pic.jpg");
        return file;
    }

    private static File getParentDirectory(Context context, FileType fileType) {
        if (fileType == FileType.PROFILE_IMAGE)
            return new File(context.getFilesDir(), DIRECTORY_PROFILE_IMAGE);
        else
            return context.getFilesDir();
    }

}