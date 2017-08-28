package com.example.hg4.jiangnankezhan;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.usernameView)EditText usernameView;
    @BindView(R.id.passwordView)EditText passwordView;
    @BindView(R.id.login)Button login;
    @BindView(R.id.register)Button register;
    @BindView(R.id.forget)Button forget;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImmersionBar.with(this).transparentBar().init();
        ButterKnife.bind(this);
        progressDialog=new ProgressDialog(this);
        if (AVUser.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            LoginActivity.this.finish();
        }
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        usernameView.requestFocus();
    }

    private void attemptLogin() {
        final String username = usernameView.getText().toString();
        final String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            Toast.makeText(this,"客官密码长度应在4-16位之间",Toast.LENGTH_SHORT).show();
            focusView = passwordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this,"客官你忘了输用户名",Toast.LENGTH_SHORT).show();
            focusView = usernameView;
            cancel = true;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"客官你忘了输密码",Toast.LENGTH_SHORT).show();
            focusView=passwordView;
            cancel=true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if (e == null) {
                        progressDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        LoginActivity.this.finish();
                    } else {

                        showProgress(false);
                        Toast.makeText(LoginActivity.this, "登录失败╮(╯▽╰)╭请检查账号密码", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showProgress(final boolean show) {
       if(show){
           progressDialog.setMessage("努力登录中...");
           progressDialog.setCancelable(false);
           progressDialog.show();
       }
       else if(progressDialog.isShowing()){
           progressDialog.dismiss();
       }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4&&password.length()<16;
    }
}
