package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.UpdatePasswordCallback;

import static com.avos.avoscloud.LogUtil.avlog.i;
import static com.example.hg4.jiangnankezhan.R.id.newpassword;

public class Chgpswd2Activity extends AppCompatActivity {
    private Button cancel;
    private ImageView back;
    private Button confirm;
    private EditText newpswd1;
    private EditText newpswd2;
    private AVUser user=AVUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chgpswd2);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chgpswd2Activity.this.finish();
            }
        });
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chgpswd2Activity.this.finish();
            }
        });
        confirm = (Button) findViewById(R.id.confirm);
        newpswd1 = (EditText) findViewById(R.id.editText);
        newpswd2 = (EditText) findViewById(R.id.editText1);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(newpswd1.getText().toString())) {
                    Toast.makeText(Chgpswd2Activity.this, "客官密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(newpswd1.getText().toString()) &&
                        !isPasswordValid(newpswd1.getText().toString())) {
                    Toast.makeText(Chgpswd2Activity.this, "客官密码长度请设置在4-16位之间", Toast.LENGTH_SHORT).show();
                } else if(newpswd1.getText().toString().equals(newpswd2.getText().toString())) {
                    String oldPassword = getIntent().getStringExtra("string_data");


                        user.updatePasswordInBackground(oldPassword,
                                newpswd1.getText().toString(),
                                new UpdatePasswordCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            finish();
                                            Intent intent = new Intent(Chgpswd2Activity.this, Chgpswd3Activity.class);
                                            startActivity(intent);
                                        }
                                        else{
                                            e.printStackTrace();
                                            Toast.makeText(Chgpswd2Activity.this, "原密码错误", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }

        });
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4 && password.length() < 16;
    }
}

