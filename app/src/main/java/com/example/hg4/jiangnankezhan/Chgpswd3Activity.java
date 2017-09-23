package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Chgpswd3Activity extends AppCompatActivity {
         private ImageView back;
         private Button finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chgpswd3);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chgpswd3Activity.this.finish();
            }
        });
        finish=(Button)findViewById(R.id.button);
        finish.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          Intent intent=new Intent(Chgpswd3Activity.this,SettingActivity.class);
                                          startActivity(intent);
                                      }
                                  }
        );
    }
}
