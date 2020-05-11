package com.tencent.mapsdk.vector.tencentdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMapOptions;
import com.tencent.tencentmap.mapsdk.maps.TencentMapResource;
import com.tencent.tencentmap.mapsdk.maps.TencentMapServiceProtocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FormalKeyActivity extends AppCompatActivity {
    private LinearLayout mLineraLayout;
    private MapView mMapView;
    private EditText editText;
    TencentMapOptions mTencentMapOptions;
    private String formal_key = "WMMBZ-DQ5WP-F6ND5-VYPDR-36Z36-4OBKX";
    private String inputKey;
    private SharedPreferences mSp;
    private String lastKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formal);
        mSp = getSharedPreferences("formal", MODE_PRIVATE);
        //读取上次存储的key
        lastKey = mSp.getString("formal_key", "");

        editText = findViewById(R.id.et_input_formal);
        mLineraLayout = findViewById(R.id.ll_formal);
        mTencentMapOptions = new TencentMapOptions();

        if (TextUtils.isEmpty(lastKey)) {
            editText.setText("");
            mTencentMapOptions.setMapKey(formal_key);
            mTencentMapOptions.setServiceProtocol(TencentMapResource.ResourceFrom.JSON, getFormalServiceResource(formal_key).toString());
        } else {
            editText.setText(lastKey);
            mTencentMapOptions.setMapKey(lastKey);
            mTencentMapOptions.setServiceProtocol(TencentMapResource.ResourceFrom.JSON, getFormalServiceResource(lastKey).toString());
        }

        mMapView = new MapView(getApplicationContext(), mTencentMapOptions);
        mMapView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mLineraLayout.addView(mMapView);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputKey = editable.toString();
                Log.i("formal_key", inputKey);
                SharedPreferences.Editor editor = mSp.edit();
                if (!TextUtils.isEmpty(inputKey)) {
                    editor.putString("formal_key", inputKey);
                    editor.commit();
                }
                mTencentMapOptions.setMapKey(inputKey);
                mTencentMapOptions.setServiceProtocol(TencentMapResource.ResourceFrom.JSON, getFormalServiceResource(inputKey).toString());

            }
        });
    }

    /**
     * 正式环境   1
     *
     * @return
     */
    private JSONObject getFormalServiceResource(String key) {
        Log.i("TAG", "getFormalServiceResource()");
        JSONArray serviceArray = new JSONArray();
        //鉴权
        JSONObject auth = getService(TencentMapServiceProtocol.SERVICE_NAME_AUTHORIZATION,
                null, null, 1);
        if (auth != null) {
            serviceArray.put(auth);
        }

        JSONObject style = getService(TencentMapServiceProtocol.SERVICE_NAME_MAP_STYLE, null, null, 1);
        if (style != null) {
            serviceArray.put(style);
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
