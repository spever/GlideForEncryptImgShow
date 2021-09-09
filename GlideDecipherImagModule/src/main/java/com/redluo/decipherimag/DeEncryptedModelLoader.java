package com.redluo.decipherimag;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;

import java.io.InputStream;

public class DeEncryptedModelLoader implements ModelLoader<EncryptedSrcDataBean, InputStream> {


    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(@NonNull EncryptedSrcDataBean bean, int width, int height, @NonNull Options options) {
        return new LoadData<>(new ObjectKey(bean), new DeEncryptedImageFetcher(bean));
    }

    @Override
    public boolean handles(@NonNull EncryptedSrcDataBean bean) {
        return true;
    }


    public static final class Factory implements ModelLoaderFactory<EncryptedSrcDataBean, InputStream> {
        @Override
        public ModelLoader<EncryptedSrcDataBean, InputStream> build(MultiModelLoaderFactory multiFactory) {
            return new DeEncryptedModelLoader();
        }

        @Override
        public void teardown() {
        }
    }
}
