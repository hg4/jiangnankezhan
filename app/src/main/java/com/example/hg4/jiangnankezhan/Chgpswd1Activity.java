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

public class Chgpswd1Activity extends BaseActivity {
    private Button cancel;
    private ImageView back;
    private Button confirm;
    private EditText oldpswd;
    private String pswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chgpswd1);
        back=(ImageView)findViewById(R.id.back);
        cancel=(Button)findViewById(R.id.cancel);
        confirm=(Button)findViewById(R.id.confirm);
        oldpswd=(EditText)findViewById(R.id.oldpswd);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chgpswd1Activity.this.finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chgpswd1Activity.this.finish();}
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(oldpswd.getText().toString())) {
                    Toast.makeText(Chgpswd1Activity.this, "客官您没有填原密码哟", Toast.LENGTH_SHORT).show();}
                else {
                    finish();
                    pswd = oldpswd.getText().toString();
                    Intent intent = new Intent(Chgpswd1Activity.this, Chgpswd2Activity.class);
                    intent.putExtra("string_data", pswd);
                    startActivity(intent);
                }
            }
        });
    }
}
