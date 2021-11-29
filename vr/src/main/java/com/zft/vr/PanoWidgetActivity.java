package com.zft.vr;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

public class PanoWidgetActivity extends AppCompatActivity {

    final String TAG = "TAG";
    VrPanoramaView paNormalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pano_widget);
        paNormalView = findViewById(R.id.panorama);
        loadPanoImage();
    }


    @Override
    public void onPause() {
        super.onPause();
        paNormalView.pauseRendering();
    }

    @Override
    public void onResume() {
        super.onResume();
        paNormalView.resumeRendering();
    }

    @Override
    protected void onDestroy() {
        paNormalView.shutdown();
        super.onDestroy();

    }

    ImageLoaderTask backgroundImageLoaderTask;

    private synchronized void loadPanoImage() {
        ImageLoaderTask task = backgroundImageLoaderTask;
        if (task != null && task.isCancelled()) {
            task.cancel(true);
        }
        //从assets中获取图片的名称，然后加载它
        VrPanoramaView.Options viewOption = new VrPanoramaView.Options();

        /***
         *
         * public static final int TYPE_MONO = 1;
         * 图像被预期以覆盖沿着其水平轴360度，而垂直范围是根据图像的宽高比来计算。例如，如果一个1000x250像素的图像，给出所述全景将覆盖360x90度与垂直范围是-45至+45度。
         *
         * public static final int TYPE_STEREO_OVER_UNDER = 2;
         * 包含两个大小相等的投影 全景图垂直叠加。顶部图像被显示给左眼、底部图像被显示给右眼。//看下图你就懂了
         *
         */
        viewOption.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
//        paNormalView.setFullscreenButtonEnabled (false); //隐藏全屏模式按钮
//        paNormalView.setInfoButtonEnabled(false); //设置隐藏最左边信息的按钮
//        paNormalView.setStereoModeButtonEnabled(false); //设置隐藏立体模型的按钮
        paNormalView.setEventListener(new ActivityEventListener()); //设置监听
        //加载本地的图片源
//        paNormalView.loadImageFromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.andes), paNormalOptions);

        //Using the name of the image int the assets/directory
//        String picName = "sample_converted.jpg";
        String picName = "andes.jpg";
        //创建任务
        task = new ImageLoaderTask(paNormalView, viewOption, picName);
        task.execute(getAssets());
        task = backgroundImageLoaderTask;
    }


    private class ActivityEventListener extends VrPanoramaEventListener {
        @Override
        public void onLoadSuccess() {//图片加载成功
            Log.i(TAG, "onLoadSuccess------------>");
        }


        @Override
        public void onLoadError(String errorMessage) {//图片加载失败
            Log.i(TAG, "Error loading pano:------------> " + errorMessage);
        }

        @Override
        public void onClick() {//当我们点击了VrPanoramaView 时候出发
            super.onClick();
            Log.i(TAG, "onClick------------>");
        }

        @Override
        public void onDisplayModeChanged(int newDisplayMode) {
            //改变显示模式时候出发（全屏模式和纸板模式）
            super.onDisplayModeChanged(newDisplayMode);
            Log.i(TAG, "onDisplayModeChanged------------>" + newDisplayMode);
        }
    }
}