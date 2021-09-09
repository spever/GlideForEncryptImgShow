package com.redluo.decipherimag;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Request;

public class DeEncryptedImageFetcher implements DataFetcher<InputStream> {

    private EncryptedSrcDataBean enImageBean;
    // 检查是否取消任务的标识
    private volatile boolean mIsCanceled;

    private Call mFetchStreamCall;
    InputStream isBm = null;

    public DeEncryptedImageFetcher(EncryptedSrcDataBean bean) {
        this.enImageBean = bean;

    }


    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {

        if (mIsCanceled) {
            return;
        }
        try {
            Request.Builder request = new Request.Builder().url(enImageBean.getUrl());
            mFetchStreamCall = OkHttpGlideModule.UnsafeOkHttpClient.getUnsafeOkHttpClient().newCall(request.build());

            byte[] src = mFetchStreamCall.execute().body().bytes();
            if (src == null || src.length <= 0) {
                Log.d("TestOut", "图片加载错误！");
                return;
            }
            /*************************************************************/
            /*************************************************************/
            /******************    此处进行解密操作    *********************/
            /************************************************************/
            /************************************************************/
            /*************** 返回解密后的数据流*****************************/

//            byte[] data1 = EZGlobalSDK.getInstance().decryptData(src, enImageBean.getPassword());
//            if (data1 == null || data1.length <= 0) {
//                LogUtil.d("TestOut", "verifyCodeError！");
//                /*************** 验证码错误 ,此处回调是在子线程中，处理UI需调回到主线程****************/
//
//            } else
//                isBm = new ByteArrayInputStream(data1);



        } catch (IOException e) {
            e.printStackTrace();
        }

        callback.onDataReady(isBm);
    }


    @Override
    public void cleanup() {
        if (isBm != null) {
            try {
                isBm.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                isBm = null;
            }
        }

    }

    @Override
    public void cancel() {
        mIsCanceled = true;
        if (mFetchStreamCall != null) {
            mFetchStreamCall.cancel();
        }

    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }


    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }


    /**
     * 此判断方式只能判断获取报警信息列表获取到的报警图片url
     *
     * @param url
     * @return 图片url解析得到图片是否加密
     */
    private static boolean isEncrypt(String url) {
        int ret = 0;
        try {
            Uri uri = Uri.parse(url);
            ret = Integer.parseInt(uri.getQueryParameter("isEncrypted"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret == 1;
    }
}
