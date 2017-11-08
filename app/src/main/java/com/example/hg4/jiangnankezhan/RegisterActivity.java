package com.example.hg4.jiangnankezhan;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVSMS;
import com.avos.avoscloud.AVSMSOption;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SignUpCallback;
import com.gyf.barlibrary.ImmersionBar;


public class RegisterActivity extends AppCompatActivity {
	private EditText phoneView;
	private EditText passwordView;
	private EditText identifyInput;
	private Button identifyView;
	private Button register;
	private PhoneCountDownTimer timer;
	private ProgressDialog progressDialog;
	private final long TIME = 60 * 1000L;
	private final long INTERVAL = 1000L;
	private boolean cancel = false;
	private View focusView = null;
	private ImageButton back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ImmersionBar.with(this).transparentBar().init();
		phoneView=(EditText)findViewById(R.id.phone);
		passwordView=(EditText)findViewById(R.id.password);
		identifyInput=(EditText)findViewById(R.id.identifyinput);
		identifyView=(Button) findViewById(R.id.sendidentify);
		register=(Button)findViewById(R.id.register);
		back=(ImageButton)findViewById(R.id.back_icon);
		progressDialog=new ProgressDialog(this);
		phoneView.requestFocus();
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RegisterActivity.this.finish();
			}
		});
		passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_NULL) {
					attemptRegister();
					return true;
				}
				return false;
			}
		});
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				attemptRegister();
			}
		});
		identifyView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String phone=phoneView.getText().toString();
				AVSMSOption option = new AVSMSOption();
				option.setTtl(10);                     // 验证码有效时间为 10 分钟
				option.setApplicationName("江南课栈");
				option.setOperation("注册");
				if(phone!=null) {
					AVSMS.requestSMSCodeInBackground(phone, option, new RequestMobileCodeCallback() {
						@Override
						public void done(AVException e) {
							if (null == e) {
								if (timer == null) {
									timer = new PhoneCountDownTimer(TIME, INTERVAL);
								}
								timer.start();
							} else {
								Toast.makeText(RegisterActivity.this, "获取验证码失败/(ㄒoㄒ)/请检查一下哟", Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			}
		});
	}

	private void attemptRegister() {
		String phone = phoneView.getText().toString();
		final String identify = identifyInput.getText().toString();
		final String password = passwordView.getText().toString();


		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(this, "客官手机号不能为空", Toast.LENGTH_SHORT).show();
			focusView = phoneView;
			cancel = true;
		} else if (TextUtils.isEmpty(identify)) {
			Toast.makeText(this, "客官验证码不能为空", Toast.LENGTH_SHORT).show();
			focusView = identifyView;
			cancel = true;
		} else {
			AVSMS.verifySMSCodeInBackground(identify,phone, new AVMobilePhoneVerifyCallback() {
				@Override
				public void done(AVException e) {
					if (null == e) {
						if (TextUtils.isEmpty(password)) {
							Toast.makeText(RegisterActivity.this, "客官密码不能为空", Toast.LENGTH_SHORT).show();
							focusView = phoneView;
							cancel = true;
						}
   /* 请求成功 */
						else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
							Toast.makeText(RegisterActivity.this, "客官密码长度请设置在4-16位之间", Toast.LENGTH_SHORT).show();
							focusView = passwordView;
							cancel = true;
						}
					} else {
   /* 请求失败 */
						cancel = true;
						focusView = identifyInput;
						Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
			if (cancel) {
					focusView.requestFocus();
				} else {
					showProgress(true);

					AVUser user = new AVUser();// 新建 AVUser 对象实例
					user.setUsername(phone);// 设置用户名
					user.setPassword(password);// 设置密码

					user.signUpInBackground(new SignUpCallback() {
						@Override
						public void done(AVException e) {
							if (e == null) {
								// 注册成功，把用户对象赋值给当前用户 AVUser.getCurrentUser()
								progressDialog.dismiss();
								startActivity(new Intent(RegisterActivity.this, RegisterOKActivity.class));
								RegisterActivity.this.finish();
							} else {
								// 失败的原因可能有多种，常见的是用户名已经存在。
								showProgress(false);
								Toast.makeText(RegisterActivity.this, "这个手机已经被注册过了哎~", Toast.LENGTH_SHORT).show();
							}
						}
					});
			}
	}
		@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
		private void showProgress(final boolean show) {
			if(show){
				progressDialog.setMessage("努力注册中...");
				progressDialog.setCancelable(false);
				progressDialog.show();
			}
			else if(progressDialog.isShowing()){
				progressDialog.dismiss();
			}
		}
		private boolean isPasswordValid(String password) {
		//TODO: Replace this with your own logic
		return password.length() >= 4&&password.length()<=16;
	}
	public class PhoneCountDownTimer extends CountDownTimer {
		public PhoneCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			long time=millisUntilFinished/1000;
			identifyView.setText("("+time+")");
		}

		@Override
		public void onFinish() {
			identifyView.setText("发送验证码");
			identifyView.setTextSize(12);
			identifyView.setTextColor(getResources().getColor(R.color.colorgrey));
			if(timer!=null)
				timer.cancel();
			timer=null;
			if(!identifyView.isClickable()){
				identifyView.setClickable(true);
			}
		}
	}



}
