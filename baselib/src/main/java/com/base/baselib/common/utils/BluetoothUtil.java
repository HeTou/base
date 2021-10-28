package com.base.baselib.common.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresPermission;

/***
 * 蓝牙工具
 */
public class BluetoothUtil {
    /***
     * 是否支持蓝牙
     * @param context
     * @return
     */
    public static boolean isBleSupported(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
                && context != null
                && context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE);
    }

    /***
     * 蓝牙是否开启
     * @return
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public static boolean isBluetoothEnabled() {
        return getBluetoothState() == BluetoothAdapter.STATE_ON;
    }


    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public static int getBluetoothState() {
        BluetoothAdapter adapter = getBluetoothAdapter();
        return adapter != null ? adapter.getState() : 0;
    }

    /***
     * 开启蓝牙
     * @return
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public static boolean openBluetooth() {
        BluetoothAdapter adapter = getBluetoothAdapter();
        if (adapter != null) {
            return adapter.enable();
        }
        return false;
    }

    /***
     * 关闭蓝牙
     * @return
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public static boolean closeBluetooth() {
        BluetoothAdapter adapter = getBluetoothAdapter();
        if (adapter != null) {
            return adapter.disable();
        }
        return false;
    }

    /***
     * 获取蓝牙适配器
     */
    public static BluetoothAdapter getBluetoothAdapter() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter;
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public static void registerBluetooth(Context context, final BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
//      手机蓝牙开启关闭时发送
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//      蓝牙设备配对和解除配对时发送
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                            BluetoothAdapter.ERROR);
                    switch (state) {
                        case BluetoothAdapter.STATE_OFF:
                            Log.d("aaa", "STATE_OFF 手机蓝牙关闭");
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            Log.d("aaa", "STATE_TURNING_OFF 手机蓝牙正在关闭");
                            break;
                        case BluetoothAdapter.STATE_ON:
                            Log.d("aaa", "STATE_ON 手机蓝牙开启");
                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:
                            Log.d("aaa", "STATE_TURNING_ON 手机蓝牙正在开启");
                            break;
                    }
                } else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    @SuppressLint("MissingPermission")
                    String name = device.getName();
                    Log.d("aaa", "device name: " + name);
                    int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
                    switch (state) {
                        case BluetoothDevice.BOND_NONE:
                            Log.d("aaa", "BOND_NONE 删除配对");
                            break;
                        case BluetoothDevice.BOND_BONDING:
                            Log.d("aaa", "BOND_BONDING 正在配对");
                            break;
                        case BluetoothDevice.BOND_BONDED:
                            Log.d("aaa", "BOND_BONDED 配对成功");
                            break;
                    }
                }
                if (receiver != null) {
                    receiver.onReceive(context, intent);
                }
            }
        };
        context.registerReceiver(broadcastReceiver, intentFilter);
    }
}
