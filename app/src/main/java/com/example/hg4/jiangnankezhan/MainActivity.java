package com.example.hg4.jiangnankezhan;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.example.hg4.jiangnankezhan.Utils.PerferencesUtils;
import com.example.hg4.jiangnankezhan.Utils.Utilty;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity
		implements NavigationView.OnNavigationItemSelectedListener {
	static final String[] PERMISSION = new String[]{
			Manifest.permission.READ_CONTACTS,// 写入权限
			Manifest.permission.READ_EXTERNAL_STORAGE,  //读取权限
			Manifest.permission.WRITE_EXTERNAL_STORAGE      //读取设备信息
	};
	private ImageButton logout;
	private ImageButton setting;
	private AlertDialog dialog;
	private NavigationView navigationView;
	private TextView username;
	private String getusername=new String();
	private ImageView mainheadView;
    private RadioGroup rg_tab_bar;
    private RadioButton home;
	private ArrayList<String> replyList=new ArrayList<>();
    //Fragment Object
    private FragmentOfhomepage fg1;
    private FragmentOfschedule fg2;
    private FragmentOfmessage fg3;
    private FragmentOfmy fg4;
    private FragmentManager fManager;
	private DrawerLayout drawer;
	private FrameLayout director;
	private TextView directorNumber;
	private ReplyService.ReplyBinder replyBinder;
	private ServiceConnection connection=new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			replyBinder=(ReplyService.ReplyBinder) service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	};
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!PerferencesUtils.getPermission(this,id)){
			setPermissions();
		}
		setContentView(R.layout.activity_main);
        ifnewVersion();
		savePhone();
		director=(FrameLayout)findViewById(R.id.director);
		directorNumber=(TextView)findViewById(R.id.director_number);
		Intent intent=new Intent(this, ReplyService.class);
		startService(intent);
		bindService(intent,connection,BIND_AUTO_CREATE);
		fg2 = new FragmentOfschedule();
        fManager=getSupportFragmentManager();
		FragmentTransaction fTransaction = fManager.beginTransaction();
		fTransaction.replace(R.id.ly_content,fg2);
        fTransaction.commit();
        /*rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rg_tab_bar.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                FragmentTransaction fTransaction = fManager.beginTransaction();
                hideAllFragment(fTransaction);
                switch (i){
                    case R.id.home:
                        if(fg1 == null){
                            fg1 = new FragmentOfhomepage();
                            fTransaction.add(R.id.ly_content,fg1);
                        }else{
                            fTransaction.show(fg1);
                        }
                        break;
                    case R.id.schedule:
                        if(fg2 == null){
                            fg2 = new FragmentOfschedule();
                            fTransaction.add(R.id.ly_content,fg2);
                        }else{
                            fTransaction.show(fg2);
                        }
                        break;
                    case R.id.message:
                        if(fg3 == null){
                            fg3 = new FragmentOfmessage();
                            fTransaction.add(R.id.ly_content,fg3);
                        }else{
                            fTransaction.show(fg3);
                        }
                        break;
                    case R.id.my:
                        if(fg4 == null){
                            fg4 = new FragmentOfmy();
                            fTransaction.add(R.id.ly_content,fg4);
                        }else{
                            fTransaction.show(fg4);
                        }
                        break;
                }
                fTransaction.commit();
            }
        });
        home = (RadioButton) findViewById(R.id.home);
        home.setChecked(true);
        */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//		drawer.setDrawerListener(toggle);
//		toggle.syncState();
		drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				checkNew();
			}

			@Override
			public void onDrawerOpened(View drawerView) {

			}

			@Override
			public void onDrawerClosed(View drawerView) {

			}

			@Override
			public void onDrawerStateChanged(int newState) {

			}
		});
		logout=(ImageButton)findViewById(R.id.logout);
		setting=(ImageButton)findViewById(R.id.setting);
		navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		Resources resource=(Resources)getBaseContext().getResources();
		ColorStateList csl1=(ColorStateList)resource.getColorStateList(R.color.navigation_menu_item_color);
		navigationView.setItemIconTintList(csl1);
		navigationView.setItemTextColor(csl1);
		navigationView.getMenu().getItem(0).setChecked(true);
		logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showAlertDialog();
			}
		});
		username=(TextView) navigationView.getHeaderView(0).findViewById(R.id.main_username);
		mainheadView = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.image);
		getusername();
		getmainhead();
		setting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent=new Intent(MainActivity.this,SettingActivity.class);
				startActivity(intent);
			}
		});



	}

	@Override
	public void onBackPressed() {
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}


	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id1 = item.getItemId();
		switch (id1){
			case R.id.nav_home:

				break;
			case R.id.nav_display:
				startActivity(new Intent(MainActivity.this,MyCommentActivity.class));
				break;
			case R.id.nav_collect:
				Toast.makeText(this,"功能待开发中...",Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_info:
				startActivity(new Intent(MainActivity.this,PersonInfoActivity.class));
				break;
			case R.id.nav_message:
				Intent intent=new Intent(MainActivity.this,ReplyActivity.class);
		//		intent.putStringArrayListExtra("reply",replyList);
				PerferencesUtils.saveUserIntData(this,id,"newcount",0);
				if(director.getVisibility()==View.VISIBLE)
					director.setVisibility(View.INVISIBLE);
				startActivity(intent);
				break;
		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	private void showAlertDialog(){
		LinearLayout dialogForm=(LinearLayout)getLayoutInflater().inflate(R.layout.dialogform,null);
		final AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setView(dialogForm)
				.setPositiveButton("注销", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						AVUser.getCurrentUser().logOut();
						finish();
						startActivity(new Intent(MainActivity.this,LoginActivity.class));
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		builder.create().show();
	}
    private void ifnewVersion(){
        try {
            PackageManager packageManager = getPackageManager();
            final PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            AVQuery<AVObject> query = new AVQuery<>("AppVersion");
			query.orderByDescending("createdAt");
            query.getFirstInBackground(new GetCallback<AVObject>() {
                @Override
                public void done(final AVObject avObject, AVException e) {
                    {
                        if (e == null) {
                            if (avObject!=null){
                                if(packageInfo.versionCode<avObject.getNumber("VersionCode").intValue()){
                                    LinearLayout dialogForm=(LinearLayout)getLayoutInflater().inflate(R.layout.version_dialog,null);
                                    final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                                    builder.setView(dialogForm)
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Uri uri = Uri.parse(avObject.getAVFile("Appapk").getUrl());
                                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                    startActivity(intent);
                                                }
                                            })
                                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            });
                                    builder.create().show();
                                }
                            }
                        } else {
                            e.printStackTrace();
                        }

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
	private void getusername(){
		SharedPreferences pref=getSharedPreferences(id+"userdata",MODE_PRIVATE);
		getusername=pref.getString("昵称","");
		if(!getusername.equals("")){
			username.setText(getusername);
		}
		else {
			AVQuery<AVObject> query=new AVQuery<>("_User");
			query.getInBackground(id, new GetCallback<AVObject>() {
				@Override
				public void done(AVObject avObject, AVException e) {
					if(e==null){
						getusername=avObject.getString("nickname");
						if(getusername!=null){
							username.setText(getusername);
							PerferencesUtils.saveUserStringData(MainActivity.this,id,"昵称",getusername);
						}

					}


				}
			});
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		navigationView.getMenu().getItem(0).setChecked(true);
		getusername();
		getmainhead();

	}
	private Bitmap getBitmapFromSharedPreferences(){
		SharedPreferences sharedPreferences=getSharedPreferences(id+"userdata", MODE_PRIVATE);
		String imageString=sharedPreferences.getString("image", "");
		byte[] byteArray= Base64.decode(imageString, Base64.DEFAULT);
		ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArray);
		Bitmap bitmap= BitmapFactory.decodeStream(byteArrayInputStream);
		return bitmap;
	}
	private void getmainhead(){
		if(getBitmapFromSharedPreferences()!=null)
			mainheadView.setImageBitmap(getBitmapFromSharedPreferences());
		else {
			AVQuery<AVObject> query=new AVQuery<>("_User");
			query.getInBackground(id, new GetCallback<AVObject>() {
				@Override
				public void done(AVObject avObject, AVException e) {
					if(avObject!=null){
						AVFile file=avObject.getAVFile("head");
						if(file!=null){
						file.getDataInBackground(new GetDataCallback() {
							@Override
							public void done(byte[] bytes, AVException e) {
								if(e==null){
									mainheadView.setImageBitmap(Utilty.Bytes2Bimap(bytes));
								}
								else e.printStackTrace();
							}
						});
						}
					}
				}
			});

		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		replyBinder.executeFind();
		//findReplyNew();
	}

	private void checkNew(){
		int newCount=PerferencesUtils.getUserIntData(this,id,"newcount");
		Log.e("new",newCount+"");
		if(newCount>0){
			director.setVisibility(View.VISIBLE);
			if(newCount<=99){
				directorNumber.setText(""+newCount);
			}
			else directorNumber.setText("99+");
		}
		else director.setVisibility(View.INVISIBLE);
	}
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(fg1 != null)fragmentTransaction.hide(fg1);
        if(fg2 != null)fragmentTransaction.hide(fg2);
        if(fg3 != null)fragmentTransaction.hide(fg3);
        if(fg4 != null)fragmentTransaction.hide(fg4);
    }

	private void savePhone(){
		AVQuery<AVUser> query = new AVQuery<>("_User");
		query.getInBackground(id, new GetCallback<AVUser>() {
			@Override
			public void done(AVUser avUser, AVException e) {
				if(e==null){
					if ("".equals(avUser.getMobilePhoneNumber())) {
						avUser.setMobilePhoneNumber(avUser.getUsername());
						avUser.saveInBackground();
					}
				}else{
					e.printStackTrace();
				}

			}
		});

	}
	/**
	 * 设置Android6.0的权限申请
	 */
	private void setPermissions() {
		if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
			//Android 6.0申请权限
			ActivityCompat.requestPermissions(this,PERMISSION,1);
			PerferencesUtils.savePermission(this,id,true);
		}else{
			Log.i("tag","权限申请ok");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(connection);
		stopService(new Intent(this,ReplyService.class));
	}
}
