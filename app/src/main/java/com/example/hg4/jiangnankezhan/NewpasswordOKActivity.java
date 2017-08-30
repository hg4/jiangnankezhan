package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                Intent intent=new Intent(NewpasswordOKActivity.this,MainActivity.class);
                startActivity(intent);
                NewpasswordOKActivity.this.finish();
            }
        });
    }
}
