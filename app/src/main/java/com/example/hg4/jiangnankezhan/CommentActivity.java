package com.example.hg4.jiangnankezhan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentActivity extends AppCompatActivity {
    private ImageView back;
    private EditText comment;
    private TextView textnum;
    private int maxNum = 115;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentActivity.this.finish();
            }
        });
        comment=(EditText)findViewById(R.id.textcomment);
        textnum=(TextView)findViewById(R.id.textnum);
        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String S = String.valueOf(maxNum-s.length());
                textnum.setText(S);
            }

        });
    }
}
