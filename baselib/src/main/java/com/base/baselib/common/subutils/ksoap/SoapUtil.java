package com.base.baselib.common.subutils.ksoap;




import com.base.baselib.common.subutils.ksoap.network.Callback;
import com.base.baselib.common.subutils.ksoap.network.SoapClient;
import com.base.baselib.common.subutils.ksoap.network.SoapRequest;

import org.ksoap2.SoapEnvelope;

/**
 * Created by LiuShuai on 2017/3/6.
 */

public class SoapUtil {
    private static final String TAG = "SoapUtil";
    private static SoapUtil mInstance;
    private SoapClient mSoapClient;

    public static final String mWeatherEndPoint = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx";

    public static final String mNameSpace = "http://WebXml.com.cn/";
    public int mSOAPVersion = SoapEnvelope.VER11;

    private SoapUtil() {
        mSoapClient = new SoapClient();
        //设置是否是调试模式
        mSoapClient.setDebug(true);
    }

    public static synchronized SoapUtil getInstance() {
        if (mInstance == null) {
            mInstance = new SoapUtil();
        }
        return mInstance;
    }


    /**
     * 异步调用
     *
     * @param cityName
     * @param callback
     */
    public void getSupportCity(String cityName, Callback callback) {
        SoapRequest request = new SoapRequest.Builder()
                .endPoint(mWeatherEndPoint)
                .methodName("getSupportCity")
                .soapAction(mNameSpace + "getSupportCity")
                .addParam("byProvinceName", cityName)
                .nameSpace(mNameSpace)
                .setVersion(mSOAPVersion)
                .setDotNet(true)
                .build();
        mSoapClient.newCall(request).enqueue(callback);
    }

    /**
     * 同步调用
     *
     * @param cityName
     * @return
     */
    public SoapEnvelope getSupportCity(String cityName) {
        SoapRequest request = new SoapRequest.Builder().endPoint(mWeatherEndPoint)
                .methodName("getSupportCity")
                .soapAction(mNameSpace + "getSupportCity")
                .addParam("byProvinceName", cityName)
                .nameSpace(mNameSpace)
                .setVersion(mSOAPVersion)
                .setDotNet(true)
                .build();
        return mSoapClient.newCall(request).execute();
    }


    public SoapEnvelope getVeinTemplates(String endPoint, String nameSpace, String idNum) {
        SoapRequest request = new SoapRequest.Builder()
                .endPoint("http://222.85.128.68:8080/znitPlatform_pre/services/Vein?wsdl")
                .soapAction("")
                .methodName("getVeinTemplates")
                .nameSpace("http://znitech.service.com/")
                .addParam("dealingId","856A57346256945CA5863321461A3F0A")
                .addParam("xmlBody","<datas><data><idCard>522122198212146030</idCard></data></datas>")
                .build();
        return mSoapClient.newCall(request).execute();
    }

}
