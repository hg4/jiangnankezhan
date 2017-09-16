package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVUser;

public class Chgpswd1ctivity extends AppCompatActivity {
    private Button cancel;
    private Button back;
    private Button confirm;
    private EditText oldpswd;
    private String pswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chgpswd1);
        back=(Button)findViewById(R.id.back);
        cancel=(Button)findViewById(R.id.cancel);
        confirm=(Button)findViewById(R.id.confirm);
        oldpswd=(EditText)findViewById(R.id.oldpswd);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chgpswd1ctivity.this.finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chgpswd1ctivity.this.finish();}
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   pswd=oldpswd.getText().toString();
                    Intent intent=new Intent(Chgpswd1ctivity.this,Chgpswd2Activity.class);
                    intent.putExtra("string_data",pswd);
                    startActivity(intent);
            }
        });
    }
}
