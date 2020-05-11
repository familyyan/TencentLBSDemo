package com.tencent.mapsdk.vector.tencentdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMapOptions;
import com.tencent.tencentmap.mapsdk.maps.TencentMapResource;
import com.tencent.tencentmap.mapsdk.maps.TencentMapServiceProtocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestKeyActivity extends AppCompatActivity {
    private LinearLayout mLineraLayout;
    private MapView mMapView;
    TencentMapOptions mTencentMapOptions;
    private String test_key = "BJGBZ-QDR2F-QJFJO-J2L5O-MKRFJ-FQBBF";
    private EditText et_input;
    private String inputKey;
    private String lastKey;
    private SharedPreferences mSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_key);
        mSp = getSharedPreferences("test", MODE_PRIVATE);
        //读取上次存储的key
        lastKey = mSp.getString("test_key", "");
        mLineraLayout = findViewById(R.id.lineraLayout);
        et_input = findViewById(R.id.et_input_test);

        mTencentMapOptions = new TencentMapOptions();
        if (TextUtils.isEmpty(lastKey)) {
            et_input.setText("");
            mTencentMapOptions.setMapKey(test_key);
            mTencentMapOptions.setServiceProtocol(TencentMapResource.ResourceFrom.JSON, getTestServiceResource(test_key).toString());
        } else {
            et_input.setText(lastKey);
            mTencentMapOptions.setMapKey(lastKey);
            mTencentMapOptions.setServiceProtocol(TencentMapResource.ResourceFrom.JSON, getTestServiceResource(lastKey).toString());
        }
        mMapView = new MapView(getApplicationContext(), mTencentMapOptions);
        mMapView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mLineraLayout.addView(mMapView);
        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputKey = editable.toString();
                SharedPreferences.Editor editor = mSp.edit();
                if (!TextUtils.isEmpty(inputKey)) {
                    editor.putString("test_key", inputKey);
                    editor.commit();
                } else {
                    Log.i("test_key", inputKey);
                }

                mTencentMapOptions.setMapKey(inputKey);
                mTencentMapOptions.setServiceProtocol(TencentMapResource.ResourceFrom.JSON, getTestServiceResource(inputKey).toString());
            }
        });

    }

    /**
     * 测试环境  2
     *
     * @return
     */
    private JSONObject getTestServiceResource(String key) {
        Log.i("TAG", "getTestServiceResource()");
        JSONArray serviceArray = new JSONArray();
        //鉴权
        JSONObject auth = getService(TencentMapServiceProtocol.SERVICE_NAME_AUTHORIZATION,
                null, null, 2);
        if (auth != null) {
            serviceArray.put(auth);
        }

        JSONObject serviceResource = new JSONObject();
        try {
            serviceResource.put("services", serviceArray);
            serviceResource.put("sdk_protocol", 1);
            serviceResource.put("_private_partner", key);//MAP_KEY
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return serviceResource;
    }

    /**
     * 构建服务配置 json 对象
     *
     * @param name     服务名称，全部定义在 {@link TencentMapServiceProtocol}
     * @param host     重新设置 name 对应的域名
     * @param testHost 重新设置 name 对应的测试域名
     * @param status   0， 关闭服务；1，使用正式域名；2，使用测试域名
     * @return
     */
    private JSONObject getService(String name, String host, String testHost, int status) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        JSONObject serviceObject = new JSONObject();
        try {
            serviceObject.put("name", name);
            if (!TextUtils.isEmpty(host)) {
                serviceObject.put("host", host);
            }
            if (!TextUtils.isEmpty(testHost)) {
                serviceObject.put("host_test", testHost);
            }
            serviceObject.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return serviceObject;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMapView != null) {
            mMapView.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

}
