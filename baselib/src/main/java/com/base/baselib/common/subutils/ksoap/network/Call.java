package com.base.baselib.common.subutils.ksoap.network;

import org.ksoap2.SoapEnvelope;

/**
 * Created by LiuShuai on 2017/3/6.
 */

public interface Call {
    SoapEnvelope execute();

    void enqueue(Callback callback);
}
