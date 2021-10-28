package com.zft.bluetooth;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.base.baselib.RxBaseTitleActivity;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.inuker.bluetooth.library.utils.BluetoothUtils;

import java.util.UUID;

public class DeviceDetailActivity extends RxBaseTitleActivity {

    private TextView mTvTitle;
    private ProgressBar mPbar;

    private DeviceDetailAdapter mAdapter;

    private SearchResult mResult;

    private BluetoothDevice mDevice;

    private boolean mConnected;
    private String mac;
    private RecyclerView rlv;

    @Override
    public int bodyLayoutId() {
        return R.layout.activity_device_detail;
    }

    @Override
    protected void initBodyUI() {
        super.initBodyUI();
        Intent intent = getIntent();
        mac = intent.getStringExtra("mac");
        mResult = intent.getParcelableExtra("device");

        mDevice = BluetoothUtils.getRemoteDevice(mac);
        rlv = findViewById(R.id.listview);
        mTvTitle = findViewById(R.id.title);
        mPbar = findViewById(R.id.pbar);
        rlv.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new DeviceDetailAdapter(this, null);
        mAdapter.setOnItemClickedListener(new DeviceDetailAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(View view, int position) {
                if (!mConnected) {
                    return;
                }
                DetailItem item = (DetailItem) mAdapter.getItem(position);
                if (item.type == DetailItem.TYPE_CHARACTER) {
                    BluetoothLog.v(String.format("click service = %s, character = %s", item.service, item.uuid));
                    startCharacterActivity(item.service, item.uuid);
                }
            }
        });
        rlv.setAdapter(mAdapter);

//      开始连接设备
//      连接状态的监听
        ClientManager.getClient().registerConnectStatusListener(mac, mBleConnectStatusListener);
        connectDevice();
    }

    private void startCharacterActivity(UUID service, UUID character) {
        Intent intent = new Intent(this, CharacterActivity.class);
        intent.putExtra("mac", mDevice.getAddress());
        intent.putExtra("service", service);
        intent.putExtra("character", character);
        startActivity(intent);
    }
    /***
     * 连接
     */
    private void connectDevice() {
        mTvTitle.setText(String.format("%s%s", "正在连接", mDevice.getAddress()));
        mPbar.setVisibility(View.VISIBLE);
        rlv.setVisibility(View.GONE);
        BleConnectOptions option = new BleConnectOptions.Builder()
                .setConnectRetry(3)   // 连接如果失败重试3次
                .setConnectTimeout(30000)   // 连接超时30s
                .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                .setServiceDiscoverTimeout(20000)  // 发现服务超时20s
                .build();
        ClientManager.getClient().connect(mac, option, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                BluetoothLog.d("onResponse() called with: code = [" + code + "], data = [" + data + "]");

                BluetoothLog.v(String.format("profile:\n%s", data));
                mTvTitle.setText(String.format("%s", mDevice.getAddress()));
                mPbar.setVisibility(View.GONE);
                rlv.setVisibility(View.VISIBLE);
                if (code == REQUEST_SUCCESS) {
                    mAdapter.setGattProfile(data);
                }
            }
        });
    }

    BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {

            BluetoothLog.v(String.format("DeviceDetailActivity onConnectStatusChanged %d in %s",
                    status, Thread.currentThread().getName()));

            mConnected = (status == STATUS_CONNECTED);
            connectDeviceIfNeeded();

            BluetoothLog.v("onConnectStatusChanged() called with: mac = [" + mac + "], status = [" + status + "]");
            if (status == Constants.STATUS_CONNECTED) {

            } else if (status == Constants.STATUS_DISCONNECTED) {

            }
        }
    };

    @Override
    protected void onDestroy() {
        ClientManager.getClient().disconnect(mDevice.getAddress());
        ClientManager.getClient().unregisterConnectStatusListener(mDevice.getAddress(), mBleConnectStatusListener);

        super.onDestroy();

    }

    private void connectDeviceIfNeeded() {
        if (!mConnected) {
            connectDevice();
        }
    }
}
