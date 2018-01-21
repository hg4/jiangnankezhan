package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.avos.avoscloud.SaveCallback;
import com.example.hg4.jiangnankezhan.Adapter.UniCmtAdapter;
import com.example.hg4.jiangnankezhan.Utils.TimeUtils;
import com.example.hg4.jiangnankezhan.Utils.Utilty;

import java.util.ArrayList;
import java.util.List;


public class DownloadActivity extends BaseActivity {
    private TextView materialname;
    private TextView owner;
    private TextView date;
    private TextView content;
    private ImageView head;
    private ImageView back;
    private Button download;
    private ImageView like;
    private ImageView comment;
    private TextView likenumber;
    private TextView commentnumber;
	private RecyclerFragment recyclerFragment;
	private TextView downNumber;
	private TextView coinNumber;
	private AVObject fileObject;
    private AVObject user;
    private AVObject getOwner;
    private String courseName;
    private String teacher;
	private int userPoints;
	private int needPoints;
	private int ownerPoints;
	private AVObject userPointObject;
	private AVObject ownerPointsObject;
	private LinearLayout fragHolder;
	private List<AVObject> displayList=new ArrayList<>();
	private List<AVObject> datalist=new ArrayList<>();
	private UniCmtAdapter cmtAdapter=new UniCmtAdapter(displayList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        materialname = (TextView) findViewById(R.id.materialname);
        back = (ImageView) findViewById(R.id.back);
        owner = (TextView) findViewById(R.id.owner);
        date = (TextView) findViewById(R.id.date);
        content = (TextView) findViewById(R.id.content);
        head = (ImageView) findViewById(R.id.head);
        download = (Button) findViewById(R.id.download);
        like=(ImageView)findViewById(R.id.like);
        likenumber=(TextView)findViewById(R.id.likenumber);
        comment=(ImageView)findViewById(R.id.comment);
        commentnumber=(TextView)findViewById(R.id.commentnumber);
		fragHolder=(LinearLayout)findViewById(R.id.materielcomment);
		courseName=getIntent().getStringExtra("courseName");
		teacher=getIntent().getStringExtra("teacher");
		downNumber=(TextView)findViewById(R.id.downloadnumber);
		coinNumber=(TextView)findViewById(R.id.coinnumber);
		Bundle bundle=new Bundle();
		bundle.putInt("close",0);
		recyclerFragment=RecyclerFragment.newInstance(cmtAdapter,displayList,bundle);
		FragmentManager fManager=getSupportFragmentManager();
		FragmentTransaction fTransaction = fManager.beginTransaction();
		fTransaction.add(R.id.materielcomment,recyclerFragment);
		fTransaction.commit();
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like.setImageResource(R.drawable.comment_like);
                int newlikenumber=Integer.parseInt(likenumber.getText().toString())+1;
                likenumber.setText(String.valueOf(newlikenumber));
                AVQuery<AVObject> query = new AVQuery<>("Course_file");
                query.whereEqualTo("Title", getIntent().getStringExtra("content"));
                query.getFirstInBackground(new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        {
                            if (e == null) {
                                avObject.put("likeCount",Integer.parseInt(likenumber.getText().toString()));
                                avObject.saveInBackground();
                                like.setClickable(false);
                            } else {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadActivity.this.finish();
            }
        });
        materialname.setText(getIntent().getStringExtra("content"));
        AVQuery<AVObject> query = new AVQuery<>("Course_file");
        query.whereEqualTo("Title", getIntent().getStringExtra("content"));
        query.include("owner");
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                {
                    if (e == null) {
						if(avObject!=null){
							fileObject=avObject;
                        if(!"".equals(avObject.getString("Introduce"))){
                            content.setText(avObject.getString("Introduce"));
                        }
                        date.setText(TimeUtils.dateToString(avObject.getCreatedAt()));
                        getOwner = avObject.getAVObject("owner");
						if(!getOwner.getString("nickname").equals("（请填写）")){
                            owner.setText(getOwner.getString("nickname"));
                        }else{
                            owner.setText("匿名用户");
                        }
                        needPoints=avObject.getInt("points");
						coinNumber.setText(avObject.getInt("points")+"积分");
						downNumber.setText(avObject.getInt("download")+"");
                        AVFile file = getOwner.getAVFile("head");
                        likenumber.setText(avObject.getNumber("likeCount").toString());
                        commentnumber.setText(avObject.getNumber("commentCount").toString());
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, AVException e) {
                                if (e == null && bytes != null) {
                                    Bitmap head1 = Utilty.Bytes2Bimap(bytes);
                                    head.setImageBitmap(head1);
                                } else e.printStackTrace();
                            }
                        });
							AVQuery<AVObject> avQuery=new AVQuery<AVObject>("UserPoints");
							avQuery.whereEqualTo("User",getOwner);
							avQuery.getFirstInBackground(new GetCallback<AVObject>() {
								@Override
								public void done(AVObject avObject, AVException e) {
									if(avObject!=null){
										ownerPointsObject=avObject;
										ownerPoints=avObject.getInt("points");
									}
									else {
										AVObject pointsObject=new AVObject("UserPoints");
										pointsObject.put("User",getOwner);
										pointsObject.saveInBackground();
									}
								}
							});
							AVQuery<AVObject> avQuery1=new AVQuery<>("UserPoints");
							avQuery1.whereEqualTo("User", AVUser.getCurrentUser());
							avQuery1.getFirstInBackground(new GetCallback<AVObject>() {
								@Override
								public void done(AVObject avObject, AVException e) {
									if(avObject!=null){
										userPointObject=avObject;
										userPoints=avObject.getInt("points");
									}
								}
							});
							findData();
						}
                    } else {
                        e.printStackTrace();
                        Toast.makeText(DownloadActivity.this, "查找失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
		comment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(DownloadActivity.this,CommentActivity.class);
				intent.putExtra("material",fileObject.toString());
				intent.putExtra("to_User",getOwner.toString());
				intent.putExtra("from",5);
				startActivityForResult(intent,1);
			}
		});
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				AVQuery<AVObject> avQuery=new AVQuery<>("_User");
				avQuery.getInBackground(id, new GetCallback<AVObject>() {
					@Override
					public void done(AVObject avObject, AVException e) {
						if(avObject!=null){
							user=avObject;
						if(user!=null){
							AVQuery<AVObject> query = new AVQuery<>("Course_file");
							query.whereEqualTo("Title", getIntent().getStringExtra("content"));
							query.getFirstInBackground(new GetCallback<AVObject>() {
								@Override
								public void done(final AVObject avObject, AVException e) {
									{
										if (e == null) {
											AVQuery<AVObject> avQuery=new AVQuery<>("UserDownloadMap");
											avQuery.whereEqualTo("User",user);
											avQuery.whereEqualTo("Course_file",avObject);
											avQuery.findInBackground(new FindCallback<AVObject>() {
												@Override
												public void done(List<AVObject> list, AVException e) {
													boolean flag=false;
													if(!list.isEmpty()){
														flag=true;
													}
													if(flag||user.getObjectId().equals(avObject.getAVObject("owner").getObjectId())||isPointEnough(avObject)){
														avObject.increment("download");
														avObject.saveInBackground(new SaveCallback() {
															@Override
															public void done(AVException e) {
																if(e==null){
																	Uri uri = Uri.parse(avObject.getAVFile("resource").getUrl());
																	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
																	startActivity(intent);
																}
															}
														});

													}
													else {
														final FrameLayout downLimit=(FrameLayout) getLayoutInflater().inflate(R.layout.limit_dialog, null);
														AlertDialog.Builder limitBuilder=new AlertDialog.Builder(DownloadActivity.this);
														limitBuilder.setView(downLimit);
														final AlertDialog limitDialog=limitBuilder.create();
														limitDialog.show();
														Button jump=(Button)downLimit.findViewById(R.id.jump);
														jump.setOnClickListener(new View.OnClickListener() {
															@Override
															public void onClick(View v) {
																Intent intent=new Intent(DownloadActivity.this,RequirementsActivity.class);
																intent.putExtra("teacher",teacher);
																intent.putExtra("courseName",courseName);
																limitDialog.dismiss();
																startActivity(intent);
															}
														});
														Button cancel=(Button)downLimit.findViewById(R.id.cancel);
														cancel.setOnClickListener(new View.OnClickListener() {
															@Override
															public void onClick(View v) {
																limitDialog.dismiss();
															}
														});
													}
												}
											});

										} else {
											e.printStackTrace();
										}

									}
								}
							});

							}
						}
					}
				});
            }
        });


    }
    private void findData(){
		AVQuery<AVObject> avQuery=new AVQuery<>("Material_comment");
		avQuery.whereEqualTo("material",fileObject);
		avQuery.include("from_User");
		avQuery.include("material");
		avQuery.addAscendingOrder("createdAt");
		avQuery.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> list, AVException e) {
				if(list!=null){
					if(list.size()!=0){
						datalist.addAll(list);
						recyclerFragment.commentList=datalist;
						recyclerFragment.loadMoreComment();
						commentnumber.setText(list.size()+"");
					}
				}
			}
		});
	}
	private boolean isPointEnough(final AVObject avObject){
		needPoints=avObject.getInt("points");
		if(userPoints>=needPoints){
			userPoints=userPoints-needPoints;
			userPointObject.put("points",userPoints);
			userPointObject.saveInBackground(new SaveCallback() {
				@Override
				public void done(AVException e) {
					if(e==null){
						Toast.makeText(DownloadActivity.this,"积分-"+needPoints,Toast.LENGTH_SHORT);
						AVObject downloadMap=new AVObject("UserDownloadMap");
						downloadMap.put("User",user);
						downloadMap.put("Course_file",avObject);
						downloadMap.saveInBackground(new SaveCallback() {
							@Override
							public void done(AVException e) {
								ownerPointsObject.increment("points",needPoints);
								ownerPointsObject.saveInBackground();
							}
						});
					}
				}
			});
			return true;
		}
		else return false;
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=0){
            switch (requestCode){
                case 1:
                	try{
						AVObject newcmt=AVObject.parseAVObject(data.getStringExtra("newcmt"));
						recyclerFragment.commentList.add(newcmt);
						recyclerFragment.autoRefresh();
						commentnumber.setText(recyclerFragment.commentList.size()+"");

					}
					catch (Exception e){
						e.printStackTrace();
					}
                    break;
                case 2:
                    try{
                        recyclerFragment.autoRefresh();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try{
						recyclerFragment.autoRefresh();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
