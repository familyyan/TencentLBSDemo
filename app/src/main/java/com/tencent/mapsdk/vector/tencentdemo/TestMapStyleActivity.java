package com.tencent.mapsdk.vector.tencentdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapOptions;
import com.tencent.tencentmap.mapsdk.maps.TencentMapResource;
import com.tencent.tencentmap.mapsdk.maps.TencentMapServiceProtocol;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestMapStyleActivity extends AppCompatActivity {
    private MapView mMapView;
    private TencentMap mTencentMap;
    private Spinner mSpinner;
    private LinearLayout linearLayout;
    private TencentMapOptions mTencentMapOptions;
    private EditText editText;
    private String lastKey;
    private SharedPreferences mSp;
    private String test_key = "BJGBZ-QDR2F-QJFJO-J2L5O-MKRFJ-FQBBF";
    private String[] styles = new String[]{"style1", "style2", "style3", "style4", "style5",
            "normal", "traffic_navi", "traffic_navi_night", "satellite", "night", "navi", "night", "eagle_day", "eagle_night"};
    private String inputKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_map_style);
        linearLayout = findViewById(R.id.ll_map_style);
        editText = findViewById(R.id.et_input_test_style);
        mSp = getSharedPreferences("testMapStyle", MODE_PRIVATE);
        //读取上次存储的key
        lastKey = mSp.getString("test_style", "");

        mTencentMapOptions = new TencentMapOptions();
        if (TextUtils.isEmpty(lastKey)) {
            editText.setText("");
            mTencentMapOptions.setMapKey(test_key);
            mTencentMapOptions.setServiceProtocol(TencentMapResource.ResourceFrom.JSON, getTestServiceResource(test_key).toString());
        } else {
            editText.setText(lastKey);
            mTencentMapOptions.setMapKey(lastKey);
            mTencentMapOptions.setServiceProtocol(TencentMapResource.ResourceFrom.JSON, getTestServiceResource(lastKey).toString());
        }

        mMapView = new MapView(this, mTencentMapOptions);
        linearLayout.addView(mMapView);
        mTencentMap = mMapView.getMap();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(39.901268, 116.403854), 11f, 0f, 0f));
        mTencentMap.moveCamera(cameraUpdate);
        //mTencentMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(39.901268, 116.403854))); //移动地图
        // mTencentMap.setOnCameraChangeListener(this);
        mSpinner = findViewById(R.id.sp_style);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, styles);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < 5) {
                    mTencentMap.setMapStyle(position + 1);
                }
                switch (position) {
                    case 5:
                        mTencentMap.setMapStyle(TencentMap.MAP_TYPE_NORMAL);
                        break;
                    case 6:
                        mTencentMap.setMapStyle(TencentMap.MAP_TYPE_TRAFFIC_NAVI);
                        break;
                    case 7:
                        mTencentMap.setMapStyle(TencentMap.MAP_TYPE_TRAFFIC_NIGHT);
                        break;
                    case 8:
                        mTencentMap.setMapStyle(TencentMap.MAP_TYPE_SATELLITE);
                        break;
                    case 9:
                        mTencentMap.setMapStyle(TencentMap.MAP_TYPE_NIGHT);
                        break;
                    case 10:
                        mTencentMap.setMapStyle(TencentMap.MAP_TYPE_NAVI);
                        break;
                    case 11:
                        mTencentMap.setMapStyle(13 + 1000);
                        break;
                    case 12:
                        mTencentMap.setMapStyle(14 + 1000);
                        break;
                    case 13:
                        mTencentMap.setMapStyle(15 + 1000);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RadioGroup rg = findViewById(R.id.rg_map_type);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_normal_type:
                        if (mTencentMap != null) {
                            mTencentMap.setMapType(TencentMap.MAP_TYPE_NORMAL);
                        }
                        break;
                    case R.id.rb_dark_type:
                        if (mTencentMap != null) {
                            mTencentMap.setMapType(TencentMap.MAP_TYPE_DARK);
                        }
                        break;
                    case R.id.rb_sagellite_type:
                        if (mTencentMap != null) {
                            mTencentMap.setMapType(TencentMap.MAP_TYPE_SATELLITE);
                        }
                        break;
                }
            }
        });


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
                SharedPreferences.Editor editor = mSp.edit();
                if (!TextUtils.isEmpty(inputKey)) {
                    editor.putString("test_style", inputKey);
                    editor.commit();
                } else {
                    Log.i("test_style", inputKey);
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

        //样式更新
        JSONObject style = getService(TencentMapServiceProtocol.SERVICE_NAME_MAP_STYLE,
                null, null, 2);
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
