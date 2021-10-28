package com.base.baselib.common.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.KeyguardManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.base.baselib.common.log.FLog;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/***
 * 变更：
 *  => 2021.2.20
 *   Android 10 及以上设备  注意 ： Manifest.permission.READ_PHONE_STATE  -> Manifest.permission.READ_PRECISE_PHONE_STATE
 *
 *
 */
public class DeviceUtil {


    /**
     * 获取手机唯一标识序列号
     *
     * @return
     */
    public static String getUniqueSerialNumber() {
        // Galaxy nexus 品牌类型
        String phoneName = Build.MODEL;
        //samsung 品牌
        String manuFacturer = Build.MANUFACTURER;
        Log.d("详细序列号", manuFacturer + "-" + phoneName + "-" + getSerialNumber());
        return manuFacturer + "-" + phoneName + "-" + getSerialNumber();
    }

    /**
     * IMEI （唯一标识序列号）
     * <p>需与{@link #isPhone(Context)}一起使用</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
     *
     * @param context 上下文
     * @return IMEI
     * <p>
     */
    @RequiresPermission(Manifest.permission.READ_PRECISE_PHONE_STATE)
//    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getIMEI(Context context) {
        String deviceId;
        if (isPhone(context)) {
            deviceId = getDeviceIdIMEI(context);
        } else {
            deviceId = getAndroidId(context);
        }
        return deviceId;
    }


    /**
     * 判断设备是否是手机
     *
     * @param context 上下文
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isPhone(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 获取设备的IMSI
     *
     * @param context
     * @return
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getIMSI(Context context) {
        return getSubscriberId(context);
    }

    /**
     * 获取设备的IMEI
     * 兼容android 10
     *
     * @param context
     * @return
     */
    @RequiresPermission(Manifest.permission.READ_PRECISE_PHONE_STATE)
    public static String getDeviceIdIMEI(Context context) {
        String id = null;
        //android.telephony.TelephonyManager
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            try {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                    id = tm.getImei();
                } else {
                    id = tm.getDeviceId();
                }
            } catch (Exception e) {
//          android10开始会出现异常
                e.printStackTrace();
            }
        }
        return id;
    }

    /***
     * 获取硬件wifi MAC
     * 获取mac地址需要声明ACCESS_WIFI_STATE权限，并且设备需要开启wifi。
     * 但是从Android 6.0开始，使用该方法获取到的mac地址都为02:00:00:00:00:00。
     * 替代方案是通过读取系统文件/sys/class/net/wlan0/address来获取mac地址，
     *
     * @return
     */
    public static String getMac(Context context) {
        String wifiMac = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            wifiMac = info.getMacAddress();
        }
        return wifiMac;
    }

    /**
     * 获取设备的软件版本号
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")

    public static String getDeviceSoftwareVersion(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceSoftwareVersion();
    }

    /**
     * 获取手机号
     *
     * @param context
     * @return
     */
    @RequiresPermission(anyOf = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_NUMBERS
    })
    public static String getLine1Number(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }

    /**
     * 获取ISO标准的国家码，即国际长途区号
     *
     * @param context
     * @return
     */
    public static String getNetworkCountryIso(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkCountryIso();
    }

    /**
     * 获取设备的 MCC + MNC
     *
     * @param context
     * @return
     */
    public static String getNetworkOperator(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkOperator();
    }

    /**
     * 获取(当前已注册的用户)的名字
     *
     * @param context
     * @return
     */
    public static String getNetworkOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkOperatorName();
    }

    /**
     * 获取当前使用的网络类型
     *
     * @param context
     * @return
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static int getNetworkType(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkType();
    }

    /**
     * 获取手机类型
     *
     * @param context
     * @return
     */
    public static int getPhoneType(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getPhoneType();
    }

    /**
     * 获取SIM卡的国家码
     *
     * @param context
     * @return
     */
    public static String getSimCountryIso(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimCountryIso();
    }

    /**
     * 获取SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字
     *
     * @param context
     * @return
     */
    public static String getSimOperator(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimOperator();
    }

    /**
     * 获取服务商名称
     *
     * @param context
     * @return
     */
    public static String getSimOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimOperatorName();
    }

    /**
     * 获取SIM卡的序列号
     *
     * @param context
     * @return
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getSimSerialNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimSerialNumber();
    }

    /**
     * 获取SIM的状态信息
     *
     * @param context
     * @return
     */
    public static int getSimState(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimState();
    }

    /**
     * 获取唯一的用户ID
     *
     * @param context
     * @return
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getSubscriberId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSubscriberId();
    }

    /**
     * 获取语音邮件号码
     *
     * @param context
     * @return
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getVoiceMailNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getVoiceMailNumber();
    }

    /**
     * 获取ANDROID ID
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取设备型号，如MI2SC
     *
     * @return 设备型号
     */
    public static String getBuildBrandModel() {
        return Build.MODEL;// Galaxy nexus 品牌类型
    }

    public static String getBuildBrand() {
        return Build.BRAND;//google
    }

    /**
     * 获取设备厂商，如Xiaomi
     *
     * @return 设备厂商
     */
    public static String getBuildMANUFACTURER() {
        return Build.MANUFACTURER;// samsung 品牌
    }

    /**
     * 序列号
     *
     * @return
     */
    public static String getSerialNumber() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }

    /**
     * 获取手机联系人
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_CONTACTS"/>}</p>
     *
     * @return 联系人链表
     */
    public static List<HashMap<String, String>> getAllContactInfo() {
        SystemClock.sleep(3000);
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        // 1.获取内容解析者
        ContentResolver resolver = Utils.getApp().getContentResolver();
        // 2.获取内容提供者的地址:com.android.contacts
        // raw_contacts 表的地址 :raw_contacts
        // view_data 表的地址 : data
        // 3.生成查询地址
        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri date_uri = Uri.parse("content://com.android.contacts/data");
        // 4.查询操作,先查询 raw_contacts,查询 contact_id
        // projection : 查询的字段
        Cursor cursor = resolver.query(raw_uri, new String[]{"contact_id"}, null, null, null);
        try {
            // 5.解析 cursor
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // 6.获取查询的数据
                    String contact_id = cursor.getString(0);
                    // cursor.getString(cursor.getColumnIndex("contact_id"));//getColumnIndex
                    // : 查询字段在 cursor 中索引值,一般都是用在查询字段比较多的时候
                    // 判断 contact_id 是否为空
                    if (!isEmpty(contact_id)) {//null   ""
                        // 7.根据 contact_id 查询 view_data 表中的数据
                        // selection : 查询条件
                        // selectionArgs :查询条件的参数
                        // sortOrder : 排序
                        // 空指针: 1.null.方法 2.参数为 null
                        Cursor c = resolver.query(date_uri, new String[]{"data1",
                                        "mimetype"}, "raw_contact_id=?",
                                new String[]{contact_id}, null);
                        HashMap<String, String> map = new HashMap<String, String>();
                        // 8.解析 c
                        if (c != null) {
                            while (c.moveToNext()) {
                                // 9.获取数据
                                String data1 = c.getString(0);
                                String mimetype = c.getString(1);
                                // 10.根据类型去判断获取的 data1 数据并保存
                                if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                                    // 电话
                                    map.put("phone", data1);
                                } else if (mimetype.equals("vnd.android.cursor.item/name")) {
                                    // 姓名
                                    map.put("name", data1);
                                }
                            }
                        }
                        // 11.添加到集合中数据
                        list.add(map);
                        // 12.关闭 cursor
                        if (c != null) {
                            c.close();
                        }
                    }
                }
            }
        } finally {
            // 12.关闭 cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }


    /**
     * https://www.jianshu.com/p/e8b6cafa91d5
     * </span></p>  * deviceID的组成为：渠道标志+识别符来源标志+hash后的终端识别符
     * <p>
     * 渠道标志为：
     * 1，andriod（a）
     * <p>
     * 识别符来源标志：
     * 1， wifi mac地址（wifi）；
     * 2， IMEI（imei）；
     * 3， 序列号（sn）；
     * 4， androidId
     * 5， id：随机码。若前面的都取不到时，则随机生成一个随机码，需要缓存。
     * <p>
     * <p>
     * todo wifi mac 说明( 6.0以前可用)
     * 获取mac地址需要声明ACCESS_WIFI_STATE权限，并且设备需要开启wifi。但是从Android 6.0开始，使用该方法获取到的mac地址都为02:00:00:00:00:00。替代方案是通过读取系统文件/sys/class/net/wlan0/address来获取mac地址
     * Android 7.0开始也行不通了，执行上面的代码会抛出java.io.FileNotFoundException: /sys/class/net/wlan0/address (Permission denied)异常，也就是说我们没有权限读取该文件
     *
     * <p>
     * todo imei sn 获取情况说明（6.0以前可用，6.0-8.0需要权限，8.0以上用getImei(),10.0 没法获取了）
     * 在Android 8.0（API Level 26）以下，可以通过TelephonyManager的getDeviceId()方法获取到设备的IMEI码（其实这里的说法不准确，该方法是会根据手机设备的制式（GSM或CDMA）返回相应的设备码（IMEI、MEID和ESN）），
     * 该方法在Android 8.0及之后的版本已经被废弃了，取而代之的是getImei()方法
     * ps :IMEI 说明不同版本情况说明
     * _Android 6.0以下：无需申请权限，可以通过getDeviceId()方法获取到IMEI码
     * _Android 6.0-Android 8.0：需要申请READ_PHONE_STATE权限，可以通过getDeviceId()方法获取到IMEI码，如果用户拒绝了权限，会抛出java.lang.SecurityException异常
     * _Android 8.0-Android 10：需要申请READ_PHONE_STATE权限，可以通过getImei()方法获取到IMEI码，如果用户拒绝了权限，会抛出java.lang.SecurityException异常
     * _Android 10及以上：分为以下两种情况：
     * __targetSdkVersion<29：没有申请权限的情况，通过getImei()方法获取IMEI码时抛出java.lang.SecurityException异常；申请了权限，通过getImei()方法获取到IMEI码为null
     * __targetSdkVersion=29：无论是否申请了权限，通过getImei()方法获取IMEI码时都会直接抛出java.lang.SecurityException异常
     * 不难看出，IMEI码在Android 10之后已经无法获取到了，而且甚至会直接抛出异常导致程序崩溃，在Android 10以下版本虽然可以获取到IMEI码，但是需要在应用获取到了READ_PHONE_STATE权限的前提下，我们依然无法保证这一点。
     * <p>
     * todo androidId 无需权限，唯一，但无法保证唯一性，root、刷机、恢复出厂都将导致变化
     *
     * @param context
     * @return
     */
    @RequiresPermission(Manifest.permission.READ_PRECISE_PHONE_STATE)
    public static String getDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
        // 渠道标志
        deviceId.append("a");
        try {
            //wifi mac地址
            String wifiMac = getMac(context);
            if (!TextUtils.isEmpty(wifiMac)) {
                deviceId.append("wifi");
                deviceId.append(wifiMac);
                return StringUtil.removeSpecialChar(deviceId.toString());
            }

            String imei = getDeviceIdIMEI(context);
            if (!TextUtils.isEmpty(imei)) {
                deviceId.append("imei");
                deviceId.append(imei);
                FLog.e("getDeviceId : " + StringUtil.removeSpecialChar(deviceId.toString()));
                return StringUtil.removeSpecialChar(deviceId.toString());
            }
            String androidId = getAndroidId(context);
            if (!TextUtils.isEmpty(androidId)) {
                deviceId.append(androidId);
                return StringUtil.removeSpecialChar(deviceId.toString());
            }
            //如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID(context);
            if (!TextUtils.isEmpty(uuid)) {
                deviceId.append("id");
                deviceId.append(uuid);
                FLog.e("getDeviceId : " + StringUtil.removeSpecialChar(deviceId.toString()));
                return StringUtil.removeSpecialChar(deviceId.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(getUUID(context));
        }
        String s = StringUtil.removeSpecialChar(deviceId.toString());
        FLog.e("getDeviceId : " + s);
        return s;
    }

    /**
     * 得到全局唯一UUID
     */
    public static String getUUID(Context context) {
        String uuid = null;
        SharedPreferences share = context.getSharedPreferences("UUID", Context.MODE_PRIVATE);
        if (share != null) {
            uuid = share.getString("uuid", "");
        }
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            share.edit().putString("uuid", uuid).commit();
        }
        FLog.e("getUUID", "getUUID : " + uuid);
        return uuid;
    }

    /***意图*/
    /**
     * 直接拨打电话
     * [sdk23需要检查权限]
     * <uses-permission android:name="android.permission.CALL_PHONE" />
     *
     * @param context  环境上下文Activity
     * @param phoneNum 号码
     */
    @RequiresPermission(Manifest.permission.CALL_PHONE)
    @SuppressLint("MissingPermission")
    public static void callPhone(Activity context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri uri = Uri.parse("tel:" + phoneNum);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 跳转到拨号界面
     *
     * @param context  环境上下文
     * @param phoneNum 号码
     */
    public static void dialPhone(Activity context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri uri = Uri.parse("tel:" + phoneNum);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 调用系统相机
     *
     * @param activity
     * @return 文件名
     */
    public static String systemCamera(Activity activity, int requestCode) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = new Date();
        String file_name = simpleDateFormat.format(date) + ".jpg";
        File file = new File(SdUtil.getCustomCachePath(activity, "photo"), file_name);
        Uri imageUri = null;
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(file);
        } else {
            String authority = activity.getPackageName() + ".fileProvider";
            imageUri = FileProvider.getUriForFile(activity, authority, file);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定照片保存路径（SD卡）
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, requestCode);
        return file.getAbsolutePath();
    }


    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public static void systemPicture(Object target, int requestCode) {
        //调用相册
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (target instanceof androidx.fragment.app.Fragment) {
            ((androidx.fragment.app.Fragment) target).startActivityForResult(intent, requestCode);
        } else if (target instanceof Activity) {
            ((Activity) target).startActivityForResult(intent, requestCode);
        }
    }

    /***
     * 处理相册的返回
     * @param context
     * @param data
     * @return
     */
    public static String systemPictrueForResult(Context context, Intent data) {
        Uri uri = data.getData();
        String imagePath = null;
        String scheme = uri.getScheme();


        imagePath = UriUtil.getFilePath(context, uri);
//        Log.d("DeviceUtil", "pic scheme: " + scheme + "\n toString():" + uri.toString());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
//            //如果是document类型的Uri,则通过document id处理
//            String docId = DocumentsContract.getDocumentId(uri);
//            Log.d("DeviceUtil", "uri :docment,id:" + docId);
//            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
//                //解析出数字格式的id
//                String id = docId.split(":")[1];
//                String selection = MediaStore.Images.Media._ID + "=" + id;
//                imagePath = getImagePath(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
//            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
//                imagePath = getImagePath(context, contentUri, null);
//            }
//        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            imagePath = getImagePath(context, uri, null);
//        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            imagePath = uri.getPath();
//        }
//        Log.d("DeviceUtil", "pic imagePath:" + imagePath);
        return imagePath;
    }

    public static void systemSetting(Context context) {
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        context.startActivity(intent);
    }

    /**
     * 安装APK
     * 安装apk需要系统允许 安装权限 ，判断是否打开
     * 1、 boolean haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
     * 2、 打开设置页面
     * **** Uri packageURI = Uri.parse("package:" + context.getPackageName());
     * **** Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
     * **** context.startActivity(intent);
     *
     * @param filePath 文件路径
     */
    public static void installAPK(Context context, String filePath) {
        try {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", new File(filePath));
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (!filePath.startsWith("file://")) {
                    filePath = "file://" + filePath;
                }
                intent.setDataAndType(Uri.parse(filePath), "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 安装APK
     *
     * @param filePath 文件路径
     */
    public static void installAPK(Activity context, int requestCode, String filePath) {
        try {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", new File(filePath));
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (!filePath.startsWith("file://")) {
                    filePath = "file://" + filePath;
                }
                intent.setDataAndType(Uri.parse(filePath), "application/vnd.android.package-archive");
            }
            context.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 系统录像
     * @param activity
     * @param requestCode
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static String systemRecordVideo(Fragment activity, int requestCode) {
        String videoPath = "";
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri fileUri = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "VID_" + timeStamp;
        String suffix = ".mp4";
        File file = new File(SdUtil.getCustomCachePath(activity.getContext(), "video"), imageFileName + suffix);
        videoPath = file.getAbsolutePath();
        if (Build.VERSION.SDK_INT < 24) {
            fileUri = Uri.fromFile(file); // create a file to saveInspectItemCodes the video
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            Uri uri = activity.getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        }
        // start the Video Capture Intent
        activity.startActivityForResult(intent, requestCode);
        return videoPath;
    }

    public static String systemRecordVideo(Activity activity, int requestCode) {
        String videoPath = "";
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri fileUri = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "VID_" + timeStamp;
        String suffix = ".mp4";
        File file = new File(SdUtil.getCustomCachePath(activity.getApplicationContext(), "video"), imageFileName + suffix);
        videoPath = file.getAbsolutePath();
        if (Build.VERSION.SDK_INT < 24) {
            fileUri = Uri.fromFile(file); // create a file to saveInspectItemCodes the video
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            Uri uri = activity.getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        }
        // start the Video Capture Intent
        activity.startActivityForResult(intent, requestCode);
        return videoPath;
    }

    /**
     * 调用系统视频播放器
     *
     * @param context
     * @param path
     */
    public static void systemPlayMp4(Context context, String path) {
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setDataAndType(Uri.parse(path), "video/mp4");
        context.startActivity(it);
    }


    /***
     * 进入应该系统管理界面
     * @param context
     */
    public static void gotoSystemAppManage(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 设置屏幕为全屏
     * <p>需在 {@code setContentView} 之前调用</p>
     *
     * @param activity activity
     */
    public static void setFullScreen(@NonNull final Activity activity) {
//        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 设置屏幕为横屏
     * <p>还有一种就是在 Activity 中加属性 android:screenOrientation="landscape"</p>
     * <p>不设置 Activity 的 android:configChanges 时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次</p>
     * <p>设置 Activity 的 android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次</p>
     * <p>设置 Activity 的 android:configChanges="orientation|keyboardHidden|screenSize"（4.0 以上必须带最后一个参数）时
     * 切屏不会重新调用各个生命周期，只会执行 onConfigurationChanged 方法</p>
     *
     * @param activity activity
     */
    public static void setLandscape(@NonNull final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 设置屏幕为竖屏
     *
     * @param activity activity
     */
    public static void setPortrait(@NonNull final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 判断是否横屏
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isLandscape() {
        return Utils.getApp().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 判断是否竖屏
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isPortrait() {
        return Utils.getApp().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 获取屏幕旋转角度
     *
     * @param activity activity
     * @return 屏幕旋转角度
     */
    public static int getScreenRotation(@NonNull final Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            default:
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
    }

    /**
     * 截屏
     *
     * @param activity activity
     * @return Bitmap
     */
    public static Bitmap screenShot(@NonNull final Activity activity) {
        return screenShot(activity, false);
    }

    /**
     * 截屏
     *
     * @param activity activity
     * @return Bitmap
     */
    public static Bitmap screenShot(@NonNull final Activity activity, boolean isDeleteStatusBar) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap bmp = decorView.getDrawingCache();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap ret;
        if (isDeleteStatusBar) {
            Resources resources = activity.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            int statusBarHeight = resources.getDimensionPixelSize(resourceId);
            ret = Bitmap.createBitmap(bmp, 0, statusBarHeight, dm.widthPixels, dm.heightPixels - statusBarHeight);
        } else {
            ret = Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
        }
        decorView.destroyDrawingCache();
        return ret;
    }

    /**
     * 判断是否锁屏
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isScreenLock() {
        KeyguardManager km = (KeyguardManager) Utils.getApp().getSystemService(Context.KEYGUARD_SERVICE);
        return km.inKeyguardRestrictedInputMode();
    }

    /**
     * 设置进入休眠时长
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.WRITE_SETTINGS" />}</p>
     *
     * @param duration 时长
     */
    public static void setSleepDuration(final int duration) {
        Settings.System.putInt(Utils.getApp().getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, duration);
    }

    /**
     * 获取进入休眠时长
     *
     * @return 进入休眠时长，报错返回-123
     */
    public static int getSleepDuration() {
        try {
            return Settings.System.getInt(Utils.getApp().getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return -123;
        }
    }


    /**
     * 判断是否是平板
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isTablet() {
        return (Utils.getApp().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static String getDeviceInfo(Context context) {
        StringBuilder sb = new StringBuilder();
        int screenWidth = ScreenUtil.getScreenWidth(context);
        int screenHeight = ScreenUtil.getScreenHeight(context);
//        像素
        String devicePx = screenWidth + "x" + screenHeight;
//        像素dp
        String deviceDp = ScreenUtil.px2dp(context, screenWidth) + "x" + ScreenUtil.px2dp(context, screenHeight);
//        像素密度
        float density = context.getResources().getDisplayMetrics().density;
        boolean tablet = isTablet();

        int calc = tablet ? screenHeight : screenWidth;

//      需要适配的文件夹名
        String filename;
        if (density >= 1.0 && density < 1.5) {
            filename = "屏幕级别：mdpi；屏幕密度：160；平板value命名：value-sw" + calc + "dp";
        } else if (density >= 1.5 && density < 2.0) {
            filename = "屏幕级别：hdpi；屏幕密度：240；平板value命名：value-sw" + calc / density + "dp";
        } else if (density >= 2.0 && density < 3) {
            filename = "屏幕级别：xhdpi；屏幕密度：320；平板value命名：value-sw" + calc / density + "dp";
        } else if (density >= 3 && density < 4) {
            filename = "屏幕级别：xxhdpi；屏幕密度：480；平板value命名：value-sw" + calc / density + "dp";
        } else if (density >= 4) {
            filename = "屏幕级别：xxxhdpi；屏幕密度：640；平板value命名：value-sw" + calc / density + "dp";
        } else {
            filename = "屏幕级别：未知；屏幕密度：未知；平板value命名：value-sw" + calc / density + "dp";
        }
//      是否为平板
        sb.append("屏幕像素：" + devicePx).append("\r\n");
        sb.append("屏幕DP：" + deviceDp).append("\r\n");
        sb.append("像素密度：" + density).append("\r\n");
        sb.append(filename).append("\r\n");
        sb.append("是否为平板：" + tablet).append("\r\n");
        sb.append("详细序列号：" + getUniqueSerialNumber()).append("\r\n");
        sb.append("是否存在sd卡：" + SdUtil.isSdExist()).append("\r\n");
//        sb.append("当前网络：" + NetWorkUtil.getNetWorkTypeName(context));
        return sb.toString();
    }

    private static String getImagePath(Context context, Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 判断字符串是否为 null 或长度为 0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(final CharSequence s) {
        return s == null || s.length() == 0;
    }


}
