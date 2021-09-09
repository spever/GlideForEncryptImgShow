package com.redluo.decipherimag;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Key;

import java.security.MessageDigest;


/**
 *
 * 构造加密属性对象
 *
 */

public class EncryptedSrcDataBean implements Key {


    @Nullable
    private volatile byte[] cacheKeyBytes;
    private int hashCode;


    @Nullable
    private String stringUrl;
    private String password;

    public String getUrl() {
        return stringUrl;
    }

    public void setUrl(String url) {
        this.stringUrl = url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(getCacheKeyBytes());
    }

    private byte[] getCacheKeyBytes() {
        if (cacheKeyBytes == null) {
            cacheKeyBytes = getCacheKey().getBytes(CHARSET);
        }
        return cacheKeyBytes;
    }

    @SuppressWarnings("WeakerAccess")
    public String getCacheKey() {
        return stringUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EncryptedSrcDataBean) {
            EncryptedSrcDataBean other = (EncryptedSrcDataBean) o;
            return getCacheKey().equals(other.getCacheKey());
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = getCacheKey().hashCode() * 31;
        }
        return hashCode;
    }
}
