package com.zft.vr;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

import java.io.IOException;

public class VrVideoActivity extends AppCompatActivity {
    final String TAG = "TAG";

    VrVideoView mVrVideo;
    private boolean isPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr_video);
        mVrVideo = findViewById(R.id.vr_video);
        mVrVideo.setEventListener(new MyVrVideoEventListener() {

        });
        VrVideoView.Options options = new VrVideoView.Options();
        options.inputType = VrVideoView.Options.TYPE_STEREO_OVER_UNDER;
        try {
            mVrVideo.loadVideoFromAsset("congo.mp4", options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        // Prevent the view from rendering continuously when in the background.
        mVrVideo.pauseRendering();
        // If the video was playing when onPause() is called, the default behavior will be to pause
        // the video and keep it paused when onResume() is called.
        isPaused = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Resume the 3D rendering.
        mVrVideo.resumeRendering();
        // Update the text to account for the paused video in onPause().
        updateStatusText();
    }

    @Override
    public void onDestroy() {
        // Destroy the widget and free memory.
        mVrVideo.shutdown();
        super.onDestroy();
    }


    private void updateStatusText() {

    }


    public class MyVrVideoEventListener extends VrVideoEventListener {
        @Override
        public void onCompletion() {
            super.onCompletion();
            Log.d(TAG, "onCompletion() called");
            mVrVideo.seekTo(0);
        }

        @Override
        public void onNewFrame() {
            super.onNewFrame();
            Log.d(TAG, "onNewFrame() called");
        }

        @Override
        public void onLoadSuccess() {
            super.onLoadSuccess();
            Log.d(TAG, "onLoadSuccess() called");
        }

        @Override
        public void onLoadError(String errorMessage) {
            super.onLoadError(errorMessage);
            Log.d(TAG, "onLoadError() called with: errorMessage = [" + errorMessage + "]");
        }

        @Override
        public void onClick() {
            super.onClick();
            Log.d(TAG, "onClick() called");
        }

        @Override
        public void onDisplayModeChanged(int newDisplayMode) {
            super.onDisplayModeChanged(newDisplayMode);
            Log.d(TAG, "onDisplayModeChanged() called with: newDisplayMode = [" + newDisplayMode + "]");
        }
    }
}