package com.redluo.decipherimag;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


public class GlideUtil {


    public static void loadImage(Context context, int placeholder, Object url, ImageView view) {
        Glide.with(context).load(url).apply(new RequestOptions().placeholder(placeholder).error(placeholder)).into(view);
    }


    /**
     * glide家在加密图片，首先下载数据流，然后解密，
     * @param context
     * @param bean
     * @param view
     */
    public static void loadImage(Context context, EncryptedSrcDataBean bean, ImageView view) {
        Glide.with(context).load(bean).apply(new RequestOptions()).into(view);
    }


}
