package com.zft.bluetooth.gaomu;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.base.baselib.RxBaseTitleActivity;
import com.base.baselib.common.utils.VibratorUtil;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.zft.bluetooth.ClientManager;
import com.zft.bluetooth.DecodeUtils;
import com.zft.bluetooth.R;
import com.zft.bluetooth.widget.MeasureDashboardView;
import com.zft.bluetooth.widget.TemperatureView;

import java.util.UUID;

/***
 * gaomu额温枪
 */
public class GaoMUActivity extends RxBaseTitleActivity implements View.OnClickListener {

    /***服务   000018f0-0000-1000-8000-00805f9b34fb*/
    public static final UUID SERVICE_UUID = UUID.fromString("000018f0-0000-1000-8000-00805f9b34fb");
    /***特征   00002af0-0000-1000-8000-00805f9b34fb*/
    public static final UUID CHARACTER_UUID = UUID.fromString("00002af0-0000-1000-8000-00805f9b34fb");
    /***设备mac地址*/
    private String mDeviceMac;


    Button mBtnConnect;
    TextView mContent;
    TemperatureView mTempView;
    MeasureDashboardView mDashboradView;

    int isConnect;
    boolean isHandClose;

    @Override
    public void beforeInBodyUI() {
        super.beforeInBodyUI();
        Intent intent = getIntent();
        mDeviceMac = intent.getStringExtra("mac");
    }

    @Override
    public int bodyLayoutId() {
        return R.layout.activity_gaomu;
    }

    @Override
    protected void initBodyUI() {
        super.initBodyUI();
        setTitle("额温枪");
        mTempView = findViewById(R.id.temp_view);
        mTempView.setOnClickListener(this);
        mBtnConnect = findViewById(R.id.btn_connect);
        mBtnConnect.setOnClickListener(this);
        mDashboradView = findViewById(R.id.measure_dashboardview);
        mContent = findViewById(R.id.tv_content);
        ClientManager.getClient().registerConnectStatusListener(mDeviceMac, mBleConnectStatusListener);

        setRightText("连接", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnect == 0) {
                    connect();
                } else if (isConnect == 2) {
                    isHandClose = true;
                    ClientManager.getClient().disconnect(mDeviceMac);
                }
            }
        });
        connect();
    }

    BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == Constants.STATUS_CONNECTED) {
                isConnect = 2;
                setStatus();
            } else {
                isConnect = 0;
                setStatus();
                if (!isHandClose) {
                    connect();
                }
            }
        }
    };

    private void connect() {
        /***1、连接设备*/
        if (isConnect == 2) {
            return;
        }
        isConnect = 1;
        setStatus();
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)
                .setConnectTimeout(10000)
                .setServiceDiscoverRetry(3)
                .setServiceDiscoverTimeout(10000)
                .build();
        ClientManager.getClient().connect(mDeviceMac, options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                if (code == REQUEST_SUCCESS) {
                    isConnect = 2;
                    setStatus();
                } else {
                    isConnect = 0;
                    setStatus();
                }
                Log.d("GaoMUActivity", "connect - - onResponse() called with: code = [" + code + "], data = [" + data + "]");
                /***开始监听服务*/
                ClientManager.getClient().notify(mDeviceMac, SERVICE_UUID, CHARACTER_UUID, new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] value) {
                        Log.d("GaoMUActivity", "onNotify() called with: service = [" + service + "], character = [" + character + "], value = [" + value + "]");
                        if (service.equals(SERVICE_UUID) && character.equals(CHARACTER_UUID)) {
//                            mBtnNotify.setText(String.format("%s", ByteUtils.byteToString(value)));
                            double v = DecodeUtils.parseT(value);
                            ;
                            mContent.setText(v + "℃");
                            VibratorUtil.vibarte(GaoMUActivity.this);
                            mDashboradView.setValue((float) v);
                            mTempView.setValue((float) v);

                        }
                    }

                    @Override
                    public void onResponse(int code) {
                        Log.d("GaoMUActivity", "notify - -onResponse() called with: code = [" + code + "]");

                        if (code == REQUEST_SUCCESS) {
                            Log.d("GaoMUActivity", "onResponse: notify success");
                        } else {
                            Log.d("GaoMUActivity", "onResponse: notify fail");

                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_connect:
                if (isConnect == 0) {
                    connect();
                } else if (isConnect == 2) {
                    isHandClose = true;
                    ClientManager.getClient().disconnect(mDeviceMac);
                }
                break;
            case R.id.temp_view:
//                double random = Math.random();
//                double vvv = 35 + 6 * random;
//                Log.d("TAG",vvv+"");
//                mTempView.setValue((float) vvv);
                break;
        }
    }

    private void setStatus() {
        if (isConnect == 0) {
            mBtnConnect.setText("连接");
            setRightText("连接");

            mContent.setText("连接已断开");
        } else if (isConnect == 2) {
            mBtnConnect.setText("断开连接");
            setRightText("断开连接");
            isHandClose = false;
        } else if (isConnect == 1) {
            mBtnConnect.setText("连接中");
            setRightText("连接中");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClientManager.getClient().disconnect(mDeviceMac);
    }
}
