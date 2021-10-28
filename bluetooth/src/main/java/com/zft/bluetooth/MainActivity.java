package com.zft.bluetooth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.base.baselib.common.utils.BluetoothUtil;
import com.base.baselib.common.utils.GpsUtil;
import com.base.baselib.common.utils.ToastUtils;
import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.tbruyelle.rxpermissions3.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.functions.Consumer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnScan;
    private RecyclerView rlv;


    private List<SearchResult> mDevices = new ArrayList<>();
    MainAdapter mainAdapter;
    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnScan = findViewById(R.id.btn_scan);
        rlv = findViewById(R.id.rlv);
        rlv.setLayoutManager(new LinearLayoutManager(this));
        mainAdapter = new MainAdapter(this, mDevices);
        rlv.setAdapter(mainAdapter);
        mBtnScan.setOnClickListener(this);
        rxPermissions = new RxPermissions(this);
//        BluetoothUtil.registerBluetooth(this, new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String action = intent.getAction();
//                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
//                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
//                            BluetoothAdapter.ERROR);
//                    switch (state) {
//                        case BluetoothAdapter.STATE_OFF:
//                            break;
//                        case BluetoothAdapter.STATE_TURNING_OFF:
//                            break;
//                        case BluetoothAdapter.STATE_ON:
//                            Log.d("aaa", "STATE_ON 手机蓝牙开启");
//                            searchDevice();
//                            break;
//                        case BluetoothAdapter.STATE_TURNING_ON:
//                            break;
//                    }
//                }
//            }
//        });
//      判断蓝牙是否支持蓝牙
        boolean bleSupported = ClientManager.getClient().isBleSupported();
        BluetoothLog.d("bleSupported:" + bleSupported);
        if (bleSupported) {
//          是否开启蓝牙
            boolean bluetoothOpened = ClientManager.getClient().isBluetoothOpened();
            BluetoothLog.d("bluetoothOpened:" + bluetoothOpened);
            if (bluetoothOpened) {
                boolean gpsOpen = GpsUtil.isGpsOpen(this);
                if (gpsOpen) {
                    rxPermissions.request("android.permission.ACCESS_COARSE_LOCATION").subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Throwable {
                            if (aBoolean) {
                                searchDevice();
                            } else {
                                ToastUtils.showLongMsg(getApplication(), "请给定位权限");
                            }
                        }
                    });
                } else {
                    ToastUtils.showLongMsg(getApplication(), "请开启gps");
                }
            } else {
//              开启蓝牙
                ClientManager.getClient().openBluetooth();
                BluetoothLog.d("开启蓝牙");

            }
        }
    }

    /***
     * 开始搜索
     */
    public void searchDevice() {

        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)   // 先扫BLE设备3次，每次3s
//                .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
//                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();

        ClientManager.getClient().search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {
//              开始搜索
                BluetoothLog.d("开始搜索");

            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                Beacon beacon = new Beacon(device.scanRecord);
                BluetoothLog.d(String.format("beacon for %s\n%s", device.getAddress(), beacon.toString()));
                if (!mDevices.contains(device) &&"BF4030".equals(device.getName()) ) {
                    for (SearchResult mDevice : mDevices) {
                        if (mDevice.getAddress().equals(device.device.getAddress())) {
                            BluetoothLog.e("出现相同地址了");
                        }
                    }
                    mDevices.add(device);
                    mainAdapter.notifyDataSetChanged();
//                    mAdapter.setDataList(mDevices);

//                Beacon beacon = new Beacon(device.scanRecord);
//                BluetoothLog.v(String.format("beacon for %s\n%s", device.getAddress(), beacon.toString()));

//                BeaconItem beaconItem = null;
//                BeaconParser beaconParser = new BeaconParser(beaconItem);
//                int firstByte = beaconParser.readByte(); // 读取第1个字节
//                int secondByte = beaconParser.readByte(); // 读取第2个字节
//                int productId = beaconParser.readShort(); // 读取第3,4个字节
//                boolean bit1 = beaconParser.getBit(firstByte, 0); // 获取第1字节的第1bit
//                boolean bit2 = beaconParser.getBit(firstByte, 1); // 获取第1字节的第2bit
//                beaconParser.setPosition(0); // 将读取起点设置到第1字节处
                }

                if (mDevices.size() > 0) {

                }
            }

            @Override
            public void onSearchStopped() {
//              停止搜索(
                BluetoothLog.d("停止搜索");
            }

            @Override
            public void onSearchCanceled() {
//              搜素取消
                BluetoothLog.d("取消搜索");

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                searchDevice();
                break;
        }
    }

}