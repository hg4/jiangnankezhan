package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.gyf.barlibrary.ImmersionBar;

import static com.example.hg4.jiangnankezhan.R.id.register;

public class ForgetpasswordActivity extends AppCompatActivity {
    private ImageButton back;
    private Button phonefind;
    private Button emailfind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        ImmersionBar.with(this).transparentBar().init();
        back=(ImageButton)findViewById(R.id.back);
        phonefind=(Button)findViewById(R.id.phonefind);
        emailfind=(Button)findViewById(R.id.emailfind);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetpasswordActivity.this.finish();
            }
        });
        phonefind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(ForgetpasswordActivity.this,PhoneFindActivity.class);
                startActivity(intent);

            }
        });
        emailfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(ForgetpasswordActivity.this,EmailfindActivity.class);
                startActivity(intent);
            }
        });

    }
}
