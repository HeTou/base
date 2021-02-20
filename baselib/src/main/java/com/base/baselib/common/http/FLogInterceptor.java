package com.base.baselib.common.http;


import android.text.TextUtils;


import com.base.baselib.common.log.FLog;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * zft
 * 2017/4/21.
 */

/**
 * zft
 * 2017/4/21.
 */

public class FLogInterceptor implements Interceptor {
    private static final String N = "\n";
    private static final String T = "\t";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String DOUBLE_SEPARATOR = LINE_SEPARATOR + LINE_SEPARATOR;
    private static final String OMITTED_RESPONSE = "Omitted response body";
    private static final String OMITTED_REQUEST = "Omitted request body";
    private static final String BODY_TAG = "Body:";
    private static final String URL_TAG = "URL: ";
    private static final String METHOD_TAG = "Method: @";
    private static final String HEADERS_TAG = "Headers:";
    private static final String STATUS_CODE_TAG = "Status Code: ";
    private static final String RECEIVED_TAG = "Received in: ";
    private static final String CORNER_UP = "┌ ";
    private static final String CORNER_BOTTOM = "└ ";
    private static final String CENTER_LINE = "├ ";
    private static final String DEFAULT_LINE = "│ ";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        logRequest(request);
        long t1 = System.nanoTime();
        Response response = chain.proceed(request);
        logResponse(request, response, t1);
        return response;
    }


    private void logResponse(Request request, Response response, long t1) {
        long t2 = System.nanoTime();
        ResponseBody responseBody = response.body();
        //打印响应结果
        String bodyString = null;
        if (responseBody != null && isParseable(responseBody.contentType())) {
            bodyString = printResult(response, true);
        }

        final List<String> segmentList = request.url().encodedPathSegments();
        final String header = response.headers().toString();
        final int code = response.code();
        final boolean isSuccessful = response.isSuccessful();
        final String message = response.message();
        final String url = response.request().url().toString();
        long chainMs= TimeUnit.NANOSECONDS.toMillis(t2 - t1);
        if (responseBody != null && isParseable(responseBody.contentType())) {
            FLog.json(URL_TAG + url,getResponse(header, chainMs, code, isSuccessful, segmentList, message),"body:",bodyString);
        }else{
            FLog.json(URL_TAG + url,getResponse(header, chainMs, code, isSuccessful, segmentList, message), OMITTED_REQUEST);
        }
    }


    private void logRequest(Request request) {
        if (request.body() != null && isParseable(request.body().contentType())) {
            final String requestBody = parseParams(request);
            FLog.json(URL_TAG + request.url(), getRequest(request), "body:", requestBody);
        } else {
            FLog.json(URL_TAG + request.url(), getRequest(request), OMITTED_REQUEST);
        }
    }

    private static String getResponse(String header, long tookMs, int code, boolean isSuccessful,
                                      List<String> segments, String message) {
        String log;
        String segmentString = slashSegments(segments);
        log = ((!TextUtils.isEmpty(segmentString) ? segmentString + " - " : "") + "is success : "
                + isSuccessful + " - " + RECEIVED_TAG + tookMs + "ms" + DOUBLE_SEPARATOR + STATUS_CODE_TAG +
                code + " / " + message + DOUBLE_SEPARATOR + (isEmpty(header) ? "" : HEADERS_TAG + LINE_SEPARATOR +
                dotHeaders(header)));
        return log;
    }

    private static String slashSegments(List<String> segments) {
        StringBuilder segmentString = new StringBuilder();
        for (String segment : segments) {
            segmentString.append("/").append(segment);
        }
        return segmentString.toString();
    }

    private static String getRequest(Request request) {
        String log;
        String header = request.headers().toString();
        log = METHOD_TAG + request.method() + DOUBLE_SEPARATOR +
                (isEmpty(header) ? "" : HEADERS_TAG + LINE_SEPARATOR + dotHeaders(header));
        return log;
    }


    /**
     * 打印响应结果
     *
     * @param response
     * @param logResponse
     * @return
     * @throws IOException
     */
    private String printResult(Response response, boolean logResponse) {
        try {
            //读取服务器返回的结果
            ResponseBody responseBody = response.newBuilder().build().body();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            //获取content的压缩类型
            String encoding = response
                    .headers()
                    .get("Content-Encoding");

            Buffer clone = buffer.clone();

            //解析response content
            return parseContent(responseBody, encoding, clone);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }


    /**
     * 解析服务器响应的内容
     *
     * @param responseBody
     * @param encoding
     * @param clone
     * @return
     */
    private String parseContent(ResponseBody responseBody, String encoding, Buffer clone) {
        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(charset);
        }
//        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {//content使用gzip压缩
//            return ZipUtil.decompressForGzip(clone.readByteArray(), convertCharset(charset));//解压
//        } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) {//content使用zlib压缩
//            return ZipUtil.decompressToStringForZlib(clone.readByteArray(), convertCharset(charset));//解压
//        } else {//content没有被压缩
            return clone.readString(charset);
//        }
    }

    /**
     * 解析请求服务器的请求参数
     *
     * @param request
     * @return
     */
    public static String parseParams(Request request) {
        try {
            RequestBody body = request.newBuilder().build().body();
            if (body == null) return "";
            Buffer requestbuffer = new Buffer();
            body.writeTo(requestbuffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            return URLDecoder.decode(requestbuffer.readString(charset), convertCharset(charset));
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    private static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || N.equals(line) || T.equals(line) || TextUtils.isEmpty(line.trim());
    }

    /**
     * 对 {@code header} 按规定的格式进行处理
     *
     * @param header
     * @return
     */
    private static String dotHeaders(String header) {
        String[] headers = header.split(LINE_SEPARATOR);
        StringBuilder builder = new StringBuilder();
        String tag = "─ ";
        if (headers.length > 1) {
            for (int i = 0; i < headers.length; i++) {
                if (i == 0) {
                    tag = CORNER_UP;
                } else if (i == headers.length - 1) {
                    tag = CORNER_BOTTOM;
                } else {
                    tag = CENTER_LINE;
                }
                builder.append(tag).append(headers[i]).append("\n");
            }
        } else {
            for (String item : headers) {
                builder.append(tag).append(item).append("\n");
            }
        }
        return builder.toString();
    }

    /**
     * 是否可以解析
     *
     * @param mediaType
     * @return
     */
    public static boolean isParseable(MediaType mediaType) {
        return isText(mediaType) || isPlain(mediaType)
                || isJson(mediaType) || isForm(mediaType)
                || isHtml(mediaType) || isXml(mediaType);
    }

    public static boolean isText(MediaType mediaType) {
        if (mediaType == null || mediaType.type() == null) return false;
        return mediaType.type().equals("text");
    }

    public static boolean isPlain(MediaType mediaType) {
        if (mediaType == null || mediaType.subtype() == null) return false;
        return mediaType.subtype().toLowerCase().contains("plain");
    }

    public static boolean isJson(MediaType mediaType) {
        if (mediaType == null || mediaType.subtype() == null) return false;
        return mediaType.subtype().toLowerCase().contains("json");
    }

    public static boolean isXml(MediaType mediaType) {
        if (mediaType == null || mediaType.subtype() == null) return false;
        return mediaType.subtype().toLowerCase().contains("xml");
    }

    public static boolean isHtml(MediaType mediaType) {
        if (mediaType == null || mediaType.subtype() == null) return false;
        return mediaType.subtype().toLowerCase().contains("html");
    }

    public static boolean isForm(MediaType mediaType) {
        if (mediaType == null || mediaType.subtype() == null) return false;
        return mediaType.subtype().toLowerCase().contains("x-www-form-urlencoded");
    }

    public static String convertCharset(Charset charset) {
        String s = charset.toString();
        int i = s.indexOf("[");
        if (i == -1)
            return s;
        return s.substring(i + 1, s.length() - 1);
    }


}

