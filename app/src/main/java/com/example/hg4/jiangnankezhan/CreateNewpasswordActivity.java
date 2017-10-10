package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.gyf.barlibrary.ImmersionBar;

public class CreateNewpasswordActivity extends AppCompatActivity {
    private ImageButton back;
    private EditText newpassword;
    private Button confirm;
    private String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_newpassword);
        ImmersionBar.with(this).transparentBar().init();
        back=(ImageButton)findViewById(R.id.back);
        newpassword=(EditText)findViewById(R.id.newpassword);
        confirm=(Button)findViewById(R.id.confirm);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewpasswordActivity.this.finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code=getIntent().getStringExtra("string_code");
                if (TextUtils.isEmpty(newpassword.getText().toString())) {
                    Toast.makeText(CreateNewpasswordActivity.this, "客官密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(newpassword.getText().toString()) &&
                        !isPasswordValid(newpassword.getText().toString())) {
                    Toast.makeText(CreateNewpasswordActivity.this, "客官密码长度请设置在4-16位之间", Toast.LENGTH_SHORT).show();
                } else {
                                AVUser.resetPasswordBySmsCodeInBackground(code, newpassword.getText().toString(), new UpdatePasswordCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e==null){
                                            Intent intent = new Intent(CreateNewpasswordActivity.this, NewpasswordOKActivity.class);
                                            intent.putExtra("newpswd",newpassword.getText().toString());
                                            intent.putExtra("number",getIntent().getStringExtra("number"));
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(CreateNewpasswordActivity.this, "验证码错误",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                        }

                    });
                }


        private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4&&password.length()<16;
    }

        }
