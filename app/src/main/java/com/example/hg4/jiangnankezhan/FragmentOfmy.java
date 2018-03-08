package com.example.hg4.jiangnankezhan;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVFriendship;
import com.avos.avoscloud.AVFriendshipQuery;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.callback.AVFriendshipCallback;
import com.example.hg4.jiangnankezhan.Adapter.MainPageAdapter;
import com.example.hg4.jiangnankezhan.Utils.PerferencesUtils;
import com.example.hg4.jiangnankezhan.Utils.Utilty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2017/9/10.
 */

public class FragmentOfmy extends Fragment {
    private List<AVObject> displayList=new ArrayList<>();
    private List<AVObject> myCmtList=new ArrayList<>();
    private AVUser user=AVUser.getCurrentUser();
    private Dialog dialog;
    private ImageView head;
    private DrawerLayout drawer;
    private int index;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup contain, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_my,contain,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        head=(ImageView) getView().findViewById(R.id.head);
        drawer = (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        sethead();
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        MainPageAdapter mainPageAdapter=new MainPageAdapter(displayList,1);
        final RecyclerFragment fragment=RecyclerFragment.newInstance(mainPageAdapter,displayList,new Bundle());
        FragmentManager fManager=getChildFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.add(R.id.myfollow_layout,fragment);
        fTransaction.commit();
        try{
            dialog= Utilty.createDiaglog(getContext(),"努力加载中...");
            if(!Utilty.isNetworkAvailable(getContext())){
                Utilty.dismissDiaglog(dialog,1000);
                Toast.makeText(getContext(),"网络连接不可用",Toast.LENGTH_SHORT).show();
            }
            AVFriendshipQuery query = AVUser.friendshipQuery(user.getObjectId(), AVUser.class);
            query.include("followee");
            query.getInBackground(new AVFriendshipCallback() {
                @Override
                public void done(AVFriendship friendship, AVException e) {
                    if(e==null){
                        List<AVUser> followees = friendship.getFollowees(); //获取关注列表
                        if(followees.size()==0){
                            Toast.makeText(getContext(),"您还没有关注任何人哦！",Toast.LENGTH_SHORT).show();
                            Utilty.dismissDiaglog(dialog,1000);
                        }
                    }else{
                        e.printStackTrace();
                    }
                }
            });

            AVQuery<AVUser> followeeQuery=user.followeeQuery(user.getObjectId(),AVUser.class);
            followeeQuery.findInBackground(new FindCallback<AVUser>() {
                @Override
                public void done(final List<AVUser> list0, AVException e) {
                    if(list0!=null){
                        final boolean[] ready=new boolean[list0.size()];
                        for(int i=0;i<list0.size();i++){
                            ready[i]=false;
                        }
                        for(final AVUser aimUser:list0){
                            AVQuery<AVObject> avQuery1=new AVQuery<>("Course_comment");
                            avQuery1.whereEqualTo("from",aimUser);
                            avQuery1.include("from");
                            avQuery1.findInBackground(new FindCallback<AVObject>() {
                                @Override
                                public void done(List<AVObject> list, AVException e) {
                                    if(e==null){
                                        if(list!=null){
                                            myCmtList.addAll(list);
                                        }
                                        AVQuery<AVObject> avQuery2=new AVQuery<>("Course_file");
                                        avQuery2.whereEqualTo("owner",aimUser);
                                        avQuery2.include("owner");
                                        avQuery2.findInBackground(new FindCallback<AVObject>() {
                                            @Override
                                            public void done(List<AVObject> list, AVException e) {
                                                if(list!=null){
                                                    myCmtList.addAll(list);
                                                    ready[index]=true;
                                                    index++;
                                                    boolean flag=true;
                                                    for(int i=0;i<ready.length;i++){
                                                        if(ready[i]==false){
                                                            flag=false;
                                                            break;
                                                        }
                                                    }
                                                    if(flag){
                                                        Collections.sort(myCmtList, new Comparator<AVObject>() {
                                                            @Override
                                                            public int compare(AVObject o1, AVObject o2) {
                                                                if(o1.getCreatedAt().getTime()>o2.getCreatedAt().getTime())
                                                                    return -1;
                                                                else return 1;
                                                            }
                                                        });
                                                        myCmtList=myCmtList.subList(0,100);
                                                        fragment.commentList=myCmtList;
                                                        fragment.loadMoreComment();
                                                        Utilty.dismissDiaglog(dialog,1500);
                                                    }
                                                }
                                                else e.printStackTrace();
                                            }
                                        });
                                    }
                                    else e.printStackTrace();
                                }
                            });
                        }


                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void sethead(){
        if (PerferencesUtils.getBitmapFromSharedPreferences(user.getObjectId(),getContext()) != null)
            head.setImageBitmap(PerferencesUtils.getBitmapFromSharedPreferences(user.getObjectId(),getContext()));
        else {
            AVQuery<AVObject> query = new AVQuery<>("_User");
            query.getInBackground(user.getObjectId(), new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (avObject != null) {
                        AVFile file = avObject.getAVFile("head");
                        if (file != null) {
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, AVException e) {
                                    if (e == null) {
                                        head.setImageBitmap(Utilty.Bytes2Bimap(bytes));
                                    } else e.printStackTrace();
                                }
                            });
                        }
                    }
                }
            });

        }
    }

}
