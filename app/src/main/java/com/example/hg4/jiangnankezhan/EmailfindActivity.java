package com.example.hg4.jiangnankezhan;

import android.app.Activity;
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
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.gyf.barlibrary.ImmersionBar;

public class EmailfindActivity extends AppCompatActivity {
      private ImageButton back;
      private EditText emailView;
      private Button sendEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailfind);
        ImmersionBar.with(this).transparentBar().init();
        back=(ImageButton)findViewById(R.id.back);
        emailView=(EditText)findViewById(R.id.email);
        sendEmail=(Button)findViewById(R.id.sendEmail);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               EmailfindActivity.this.finish();
            }
        });
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email=emailView.getText().toString();
                AVUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                    public void done(AVException e) {
                        if (e == null) {
                            Toast.makeText(EmailfindActivity.this, "请通过您收到的邮件进行密码重置", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(EmailfindActivity.this,LoginActivity.class);
                            startActivity(intent);
                            // 已发送一份重置密码的指令到用户的邮箱
                        } else {
                            Toast.makeText(EmailfindActivity.this, "重置密码失败", Toast.LENGTH_SHORT).show();
                            // 重置密码出错。
                        }
                    }
                });

            }
        });
    }
}
