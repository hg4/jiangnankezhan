package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.gyf.barlibrary.ImmersionBar;


public class NewpasswordOKActivity extends AppCompatActivity {
    private Button enter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpassword_ok);
        ImmersionBar.with(this).transparentBar().init();
        enter=(Button)findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String password = getIntent().getStringExtra("newpswd");
                final String username=getIntent().getStringExtra("number");
                AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if (e == null) {
                            startActivity(new Intent(NewpasswordOKActivity.this, MainActivity.class));
                            NewpasswordOKActivity.this.finish();
                        } else {
                            e.printStackTrace();
                        }
                        NewpasswordOKActivity.this.finish();
                    }
                });
            }
        });
    }
    }

