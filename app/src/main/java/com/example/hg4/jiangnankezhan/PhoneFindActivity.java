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
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.gyf.barlibrary.ImmersionBar;

public class PhoneFindActivity extends AppCompatActivity {
    private ImageButton back;
    private EditText phonenumberView;
    private Button sendidentify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonefind);
        ImmersionBar.with(this).transparentBar().init();
        back=(ImageButton)findViewById(R.id.back);
        phonenumberView=(EditText)findViewById(R.id.phonenumber);
        sendidentify=(Button)findViewById(R.id.sendidentify);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneFindActivity.this.finish();
            }
        });

        sendidentify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phonenumber=phonenumberView.getText().toString();
                if(phonenumber!=null) {
                    AVUser.requestPasswordResetBySmsCodeInBackground(phonenumber, new RequestMobileCodeCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e==null) {
                                            Intent intent=new Intent(PhoneFindActivity.this,InputPhoneCodeActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(PhoneFindActivity.this, "获取验证码失败/(ㄒoㄒ)/请检查一下哟", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }else{
                    Toast.makeText(PhoneFindActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
