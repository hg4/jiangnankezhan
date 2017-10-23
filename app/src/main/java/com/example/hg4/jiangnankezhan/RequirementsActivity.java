package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hg4.jiangnankezhan.Adapter.AdapterFragment;

import java.util.ArrayList;

public class RequirementsActivity extends BaseActivity implements View.OnClickListener {
    private String courseName;
    private String teacher;
    private ImageView back;
    private ViewPager viewPager;
    private ArrayList<Fragment> FragmentList;
    private TextView rollcall;
    private TextView homework;
    private TextView examinetype;
    private ImageView scrollbar;
    private Button comment;
    private  AdapterFragment adapterFragment;
    private int currIndex=0;
    private int one;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirements);
        back=(ImageView)findViewById(R.id.back);
        comment=(Button)findViewById(R.id.addcmt_comment);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        rollcall = (TextView)findViewById(R.id.rollcall);
        homework = (TextView)findViewById(R.id.homework);
        examinetype = (TextView)findViewById(R.id.examinetype);
        scrollbar = (ImageView)findViewById(R.id.scrollbar);
        courseName=getIntent().getStringExtra("courseName");
        teacher=getIntent().getStringExtra("teacher");
        if(getIntent().getIntExtra("type",0)!=0){
            currIndex=getIntent().getIntExtra("type",0);
        }
        FragmentList=new ArrayList<>();
        for(int i=0;i<3;i++){
            CommentFragment fragment=createFragment(i);
            FragmentList.add(fragment);
        }
        adapterFragment=new AdapterFragment(getSupportFragmentManager(),FragmentList);
        viewPager.setAdapter(adapterFragment);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        viewPager.setCurrentItem(currIndex);
        rollcall.setOnClickListener(this);
        homework.setOnClickListener(this);
        examinetype.setOnClickListener(this);
        back.setOnClickListener(this);
        comment.setOnClickListener(this);
        rollcall.performClick();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenW = displayMetrics.widthPixels;
        one = screenW/3;
        Matrix matrix = new Matrix();
        matrix.postTranslate(0,0);
        scrollbar.setImageMatrix(matrix);
            }


            public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

                @Override
                public void onPageSelected(int arg0) {
                    Animation animation = null;
                    switch (arg0) {
                        case 0:
                            /**
                             * TranslateAnimation的四个属性分别为
                             * float fromXDelta 动画开始的点离当前View X坐标上的差值
                             * float toXDelta 动画结束的点离当前View X坐标上的差值
                             * float fromYDelta 动画开始的点离当前View Y坐标上的差值
                             * float toYDelta 动画开始的点离当前View Y坐标上的差值
                             **/
                            animation = new TranslateAnimation(one, 0, 0, 0);
                            setSelected();
                            rollcall.setSelected(true);
                            break;
                        case 1:
                            if (currIndex==0){
                            animation = new TranslateAnimation(0, one, 0, 0);
                        }else{
                                animation = new TranslateAnimation(2*one, one, 0, 0);
                            }
                            setSelected();
                            homework.setSelected(true);
                            break;
                        case 2:
                            animation = new TranslateAnimation(one, 2*one, 0, 0);
                            setSelected();
                            examinetype.setSelected(true);
                            break;
                    }
                    currIndex = arg0;
                    animation.setFillAfter(true);
                    animation.setDuration(200);
                    scrollbar.startAnimation(animation);
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }
            }

            @Override
            public void onClick(View view){
                switch (view.getId()){
                    case R.id.rollcall:
                        setSelected();
                        rollcall.setSelected(true);
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.homework:
                        setSelected();
                        homework.setSelected(true);
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.examinetype:
                        setSelected();
                        examinetype.setSelected(true);
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.back:
                       RequirementsActivity.this.finish();
                        break;
                    case R.id.addcmt_comment:
                        Intent intent=new Intent(RequirementsActivity.this,CommentActivity.class);
                        intent.putExtra("type",viewPager.getCurrentItem());
                        intent.putExtra("courseName",courseName);
                        intent.putExtra("teacher",teacher);
                        intent.putExtra("from",1);
                        currIndex=viewPager.getCurrentItem();
                        startActivityForResult(intent,1);
                        break;
                }
            }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=0){
            switch (requestCode){
                case 1:
                    CommentFragment fragment=(CommentFragment) adapterFragment.getItem(currIndex);
                    fragment.autoRefresh();
                    break;
            }
        }
    }

    private CommentFragment createFragment(int type){
        CommentFragment fragment=new CommentFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("type",type);
        bundle.putString("courseName",courseName);
        bundle.putString("teacher",teacher);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void setSelected(){
        rollcall.setSelected(false);
        homework.setSelected(false);
        examinetype.setSelected(false);
    }

}


