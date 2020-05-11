package com.tencent.mapsdk.vector.tencentdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class SettingActivity extends AppCompatActivity {
    private EditText editText;
    Handler handler = new Handler();
    Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        editText = findViewById(R.id.et_input);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                String text=  charSequence.toString();
//                Log.i("TAG",text+"///////////////");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                String text=  charSequence.toString();
//                Log.i("TAG",text+"///////////////");
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                    Log.v("tag", "---" + s.toString());
                }
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        String text = s.toString();
                        Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                        intent.putExtra("Key", text);
                        setResult(RESULT_OK, intent);//设置返回结果，
                        Log.v("tag", "跳转======" + s.toString());
                        finish();
                    }
                };
                Log.v("tag", "(((((" + s.toString());
                handler.postDelayed(runnable, 3000);


            }
        });
    }
}
