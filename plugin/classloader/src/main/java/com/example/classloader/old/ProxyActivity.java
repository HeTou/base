package com.example.classloader.old;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.plugina2.PayInterface;

import java.lang.reflect.Constructor;


/***
 * 我这里试过用 AppcompatActivtity 但是会报错
 *  setWindowCallback(android.view.Window$Callback)' on a null object reference
 *
 *  改回来activity就可以了
 */
public class ProxyActivity extends Activity {

    private PayInterface payInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String className = getIntent().getStringExtra("className");

        try {
            Class<?> activityClass = getClassLoader().loadClass(className);
            Constructor<?> constructor = activityClass.getConstructor(new Class[]{});

            Object instance = constructor.newInstance(new Class[]{});
            payInterface = (PayInterface) instance;
            payInterface.attach(this);
            Bundle bundle = new Bundle();
            payInterface.onCreate(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance().getDexClassLoader();
    }

    @Override
    public Resources getResources() {
        return PluginManager.getInstance().getResources();
    }

    @Override
    protected void onResume() {
        super.onResume();
        payInterface.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        payInterface.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        payInterface.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        payInterface.onStop();
    }
}
