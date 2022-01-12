package com.example.plugin2;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.lang.reflect.Method;

public class LoadUtil {
    private static Resources mResources;
    private static String apkPath = "apk路径";

    public static Resources getResources(Context context) {
        if (mResources == null) {
            mResources = loadResource(context);
        }
        return mResources;
    }

    private static Resources loadResource(Context context) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();

            Method addAssetPathMethod = assetManager.getClass().getDeclaredMethod("addAssetPath",String.class);
            addAssetPathMethod.setAccessible(true);
            addAssetPathMethod.invoke(assetManager, apkPath);

            Resources resources = new Resources(assetManager,
                    context.getResources().getDisplayMetrics(),
                    context.getResources().getConfiguration());

            return resources;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
