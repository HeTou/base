package com.zft.bluetooth;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.base.baselib.RxBaseTitleActivity;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.Constants.*;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.UUID;

public class CharacterActivity extends RxBaseTitleActivity implements View.OnClickListener {


    public static final int GATT_DEF_BLE_MTU_SIZE = 23;
    public static final int GATT_MAX_MTU_SIZE = 517;

    private String mMac;
    private UUID mService;
    private UUID mCharacter;

    private TextView mTvTitle;

    private Button mBtnRead;

    private Button mBtnWrite;
    private EditText mEtInput;

    private Button mBtnNotify;
    private Button mBtnUnnotify;
    private EditText mEtInputMtu;
    private Button mBtnRequestMtu;


    @Override
    public int bodyLayoutId() {
        return R.layout.activity_character;
    }

    @Override
    protected void initBodyUI() {
        super.initBodyUI();
        Intent intent = getIntent();
        mMac = intent.getStringExtra("mac");
        mService = (UUID) intent.getSerializableExtra("service");
        mCharacter = (UUID) intent.getSerializableExtra("character");

        mTvTitle = (TextView) findViewById(R.id.title);
        mTvTitle.setText(String.format("%s", mMac));

        mBtnRead = (Button) findViewById(R.id.read);

        mBtnWrite = (Button) findViewById(R.id.write);
        mEtInput = (EditText) findViewById(R.id.input);

        mBtnNotify = (Button) findViewById(R.id.notify);
        mBtnUnnotify = (Button) findViewById(R.id.unnotify);

        mEtInputMtu = (EditText) findViewById(R.id.et_input_mtu);
        mBtnRequestMtu = (Button) findViewById(R.id.btn_request_mtu);

        mBtnRead.setOnClickListener(this);
        mBtnWrite.setOnClickListener(this);

        mBtnNotify.setOnClickListener(this);
        mBtnNotify.setEnabled(true);

        mBtnUnnotify.setOnClickListener(this);
        mBtnUnnotify.setEnabled(false);

        mBtnRequestMtu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.read:
                ClientManager.getClient().read(mMac, mService, mCharacter, mReadRsp);
                break;
            case R.id.write:
                ClientManager.getClient().write(mMac, mService, mCharacter,
                        ByteUtils.stringToBytes(mEtInput.getText().toString()), mWriteRsp);
                break;
            case R.id.notify:
                ClientManager.getClient().notify(mMac, mService, mCharacter, mNotifyRsp);
                break;
            case R.id.unnotify:
                ClientManager.getClient().unnotify(mMac, mService, mCharacter, mUnnotifyRsp);
                break;
            case R.id.btn_request_mtu:
                String mtuStr = mEtInputMtu.getText().toString();
                if (TextUtils.isEmpty(mtuStr)) {
                    showLongToast("MTU不能为空");
                    return;
                }
                int mtu = Integer.parseInt(mtuStr);
                if (mtu < GATT_DEF_BLE_MTU_SIZE || mtu > GATT_MAX_MTU_SIZE) {
                    showLongToast("MTU不不在范围内");
                    return;
                }
//                ClientManager.getClient().requestMtu(mMac, mtu, mMtuResponse);
                break;
        }

    }


    private final BleReadResponse mReadRsp = new BleReadResponse() {
        @Override
        public void onResponse(int code, byte[] data) {
            if (code == REQUEST_SUCCESS) {
                mBtnRead.setText(String.format("read: %s", ByteUtils.byteToString(data)));
                showLongToast("success");
            } else {
                showLongToast("failed");
                mBtnRead.setText("read");
            }
        }
    };

    private final BleWriteResponse mWriteRsp = new BleWriteResponse() {
        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                showLongToast("success");
            } else {
                showLongToast("failed");
            }
        }
    };

    private final BleNotifyResponse mNotifyRsp = new BleNotifyResponse() {
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            if (service.equals(mService) && character.equals(mCharacter)) {
                mBtnNotify.setText(String.format("%s", ByteUtils.byteToString(value)));
            }
        }

        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                mBtnNotify.setEnabled(false);
                mBtnUnnotify.setEnabled(true);
                showLongToast("success");
            } else {
                showLongToast("failed");
            }
        }
    };

    private final BleUnnotifyResponse mUnnotifyRsp = new BleUnnotifyResponse() {
        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                showLongToast("success");
                mBtnNotify.setEnabled(true);
                mBtnUnnotify.setEnabled(false);
            } else {
                showLongToast("failed");
            }
        }
    };
//
//    private final BleMtuResponse mMtuResponse = new BleMtuResponse() {
//        @Override
//        public void onResponse(int code, Integer data) {
//            if (code == REQUEST_SUCCESS) {
//                showLongToast("request mtu success,mtu = " + data);
//            } else {
//                showLongToast("request mtu failed");
//            }
//        }
//    };
}
