package com.dhananjay.imageuploadapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionManager {

    private static PermissionManager sInstance;

    private PermissionManager() {
    }

    public static PermissionManager getInstance() {
        if (sInstance == null) {
            sInstance = new PermissionManager();
        }

        return sInstance;
    }

    public boolean hasGrantedPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void requestPermission(Activity activity, String permission, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
    }
}


