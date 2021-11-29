package com.zft.vr;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class ImageLoaderTask extends AsyncTask<AssetManager, Void, Bitmap> {
    private static final String TAG = "ImageLoaderTask";
    private final String assetName;
    //因为加载图像时可能会破坏试图，通过弱引用，可以立即回收垃圾，而不必等到异步任务被销毁
    private final WeakReference<VrPanoramaView> viewReference;
    private final VrPanoramaView.Options viewOptions;
    //为了避免设备旋转时重新加载图像，我们缓存最后加载的图像，我们通过加载资产的名称以及生成的位图来做到
    private static WeakReference<Bitmap> lastBitmap = new WeakReference<>(null);
    private static String lastName ;
    //通过构造函数传参
    public ImageLoaderTask(VrPanoramaView view, VrPanoramaView.Options viewOptions, String assetName) {
        viewReference = new WeakReference<>(view);
        this.viewOptions = viewOptions;
        this.assetName = assetName;
    }
    @Override
    protected Bitmap doInBackground(AssetManager... assetManagers) {
        //通过AssetManager 来获取InputStream图像，
        //将输入流传给BitmapFactory以加载图片并返回到主线程中
        AssetManager assetManager = assetManagers[0];
        //打开流之前检查最后加载的图像，以节省内存
        if (assetName.equals(lastName) && lastBitmap.get() != null) {
            return lastBitmap.get();
        }

        try(InputStream istr = assetManager.open(assetName)) {
            Bitmap b = BitmapFactory.decodeStream(istr);
            lastBitmap = new WeakReference<>(b);
            lastName = assetName;
            return b;
        } catch (IOException e) {
            Log.e(TAG, "Could not decode default bitmap: " + e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        final VrPanoramaView vw = viewReference.get();
        if (vw != null && bitmap != null) {
            vw.loadImageFromBitmap(bitmap, viewOptions);
        }
    }
}