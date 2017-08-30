package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVSMS;
import com.gyf.barlibrary.ImmersionBar;

public class InputPhoneCodeActivity extends AppCompatActivity {
     private ImageButton back;
     private EditText  vertifycode;
     private  Button   confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_phone_code);
        ImmersionBar.with(this).transparentBar().init();
        back=(ImageButton)findViewById(R.id.back);
        vertifycode=(EditText) findViewById(R.id.code);
        confirm=(Button)findViewById(R.id.confirm);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputPhoneCodeActivity.this.finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String code = vertifycode.getText().toString();
   /* 验证成功 */            Intent intent=new Intent(InputPhoneCodeActivity.this,CreateNewpasswordActivity.class);
                             intent.putExtra("string_code",code);
                             startActivity(intent);
            }
                });
            }
    }
