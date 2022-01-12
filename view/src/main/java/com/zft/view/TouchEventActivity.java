package com.zft.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zft.view.touchevent.ChlidrenView;
import com.zft.view.touchevent.MyView;
import com.zft.view.touchevent.ParentView;

public class TouchEventActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private CheckBox ckParentIntercept;
    private CheckBox ckParentDispatch;
    private CheckBox ckParentTouch;
    private CheckBox ckChildrenIntercept;
    private CheckBox ckChildrenDispatch;
    private CheckBox ckChildrenTouch;
    private CheckBox ckView;
    private ParentView viewParent;
    private ChlidrenView viewChlidren;
    private MyView view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_event);
        initView();
    }

    private void initView() {
        ckParentIntercept = (CheckBox) findViewById(R.id.ck_parent_intercept);
        ckParentDispatch = (CheckBox) findViewById(R.id.ck_parent_dispatch);
        ckParentTouch = (CheckBox) findViewById(R.id.ck_parent_touch);
        ckChildrenIntercept = (CheckBox) findViewById(R.id.ck_children_intercept);
        ckChildrenDispatch = (CheckBox) findViewById(R.id.ck_children_dispatch);
        ckChildrenTouch = (CheckBox) findViewById(R.id.ck_children_touch);
        ckView = (CheckBox) findViewById(R.id.ck_view);
        viewParent = (ParentView) findViewById(R.id.view_parent);
        viewChlidren = (ChlidrenView) findViewById(R.id.view_chlidren);
        view = (MyView) findViewById(R.id.view);

        ckParentIntercept.setOnCheckedChangeListener(this);
        ckParentDispatch.setOnCheckedChangeListener(this);
        ckParentTouch.setOnCheckedChangeListener(this);
        ckChildrenIntercept.setOnCheckedChangeListener(this);
        ckChildrenDispatch.setOnCheckedChangeListener(this);
        ckChildrenTouch.setOnCheckedChangeListener(this);
        ckView.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.ck_parent_intercept:
                viewParent.setIntercept(isChecked);
                break;
            case R.id.ck_parent_dispatch:
                viewParent.setDispatch(isChecked);
                break;
            case R.id.ck_parent_touch:
                viewParent.setTouch(isChecked);
                break;
            case R.id.ck_children_intercept:
                viewChlidren.setIntercept(isChecked);
                break;
            case R.id.ck_children_dispatch:
                viewChlidren.setDispatch(isChecked);
                break;
            case R.id.ck_children_touch:
                viewChlidren.setTouch(isChecked);
                break;
            case R.id.ck_view:
                view.setTouch(isChecked);
                break;
        }
    }

    public String TAG = "Event1";

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.v(TAG, "-->dispatchTouchEvent() "+"["+MotionEvent.actionToString(ev.getAction())+"]");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v(TAG, "-->onTouchEvent() "+"["+MotionEvent.actionToString(event.getAction())+"]");
        return super.onTouchEvent(event);
    }
}
