package com.example.glideforencryptimgshow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.redluo.decipherimag.EncryptedSrcDataBean;
import com.redluo.decipherimag.GlideUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.iv_pic);

        EncryptedSrcDataBean bean = new EncryptedSrcDataBean();
        bean.setUrl("sss");
        bean.setPassword("1234");

        GlideUtil.loadImage(this, bean, imageView);

    }
}