package com.example.hg4.jiangnankezhan;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.hg4.jiangnankezhan.Utils.PerferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HG4 on 2017/11/6.
 */

public class ReplyService extends Service {
	private ReplyBinder mBinder=new ReplyBinder();
	private int datalen;
	private int getNumber;
	private int count;
	private String id=AVUser.getCurrentUser().getObjectId();
	private long saveRecentTime;
	private ArrayList<String> mReplyList=new ArrayList<>();
	class ReplyBinder extends Binder{
		public void executeFind(){
			findReplyNew();
		}
		public int getNewNumber(){
			return count;
		}
		public List<String> getReplyList(){
			return mReplyList;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		saveRecentTime=PerferencesUtils.getUserLongData(this,id,"recenttime");
	}

	@Override
	public int onStartCommand(Intent intent,int flags, int startId) {
		findReplyNew();
		AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
		int delayTime=3*60*1000;
		long triggerAtTime= SystemClock.elapsedRealtime()+delayTime;
		Intent i=new Intent(this,ReplyService.class);
		PendingIntent pi=PendingIntent.getService(this,0,i,0);
		manager.cancel(pi);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
		return super.onStartCommand(intent, flags, startId);
	}
	private void findReplyNew(){
		AVQuery<AVObject> avQuery=new AVQuery<>("cscmt_commentlist");
		avQuery.whereEqualTo("targetUser", AVUser.getCurrentUser());
		avQuery.orderByDescending("createdAt");
		avQuery.whereNotEqualTo("from",AVUser.getCurrentUser());
		avQuery.include("to");
		avQuery.include("from");
		avQuery.include("targetUser");
		avQuery.setLimit(20);
		avQuery.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> list, AVException e) {
				if(list!=null){
					mReplyList.clear();
					PerferencesUtils.saveDataList(ReplyService.this,id,"replylist",mReplyList);
					if(list.size()!=0){
						datalen=list.size();
						Log.e("test",datalen+"");
						long newTime=list.get(0).getCreatedAt().getTime();
						Log.e("test2",""+list.get(0).getCreatedAt().getTime());
						count=0;
						for(AVObject avObject:list){
							mReplyList.add(avObject.toString());
							if(avObject.getCreatedAt().getTime()>saveRecentTime){
								count++;
							}
						}
						PerferencesUtils.saveDataList(ReplyService.this,id,"replylist",mReplyList);
						if(newTime>saveRecentTime)
							saveRecentTime=newTime;
						if(count!=0)
							PerferencesUtils.saveUserIntData(ReplyService.this,AVUser.getCurrentUser().getObjectId(),"newcount",count);
						PerferencesUtils.saveUserLongData(ReplyService.this,id,"recenttime",saveRecentTime);
					}

				}

			}
		});
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
}
