package com.example.hg4.jiangnankezhan;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCChatProfileProvider;
import cn.leancloud.chatkit.LCChatProfilesCallBack;

/**
 * Created by Administrator on 2018/2/1.
 */

public class CustomUserProvider implements LCChatProfileProvider {

    private static CustomUserProvider customUserProvider;

    public synchronized static CustomUserProvider getInstance() {
        if (null == customUserProvider) {
            customUserProvider = new CustomUserProvider();
        }
        return customUserProvider;
    }

    private CustomUserProvider() {
    }

    private static List<LCChatKitUser> partUsers = new ArrayList<LCChatKitUser>();

    static {
        AVQuery<AVObject> query = new AVQuery<>("_User");
        query.limit(1000);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for(int i=0;i<list.size();i++){
                    AVObject user= list.get(i);
                    if(user.getAVFile("head")!=null&&user.getAVFile("head").getUrl()!=null){
                        partUsers.add(new LCChatKitUser(user.getObjectId(),user.getString("nickname"),user.getAVFile("head").getUrl()));
                    }

                }
            }
        });
    }

    @Override
    public void fetchProfiles(List<String> list, LCChatProfilesCallBack callBack) {
        List<LCChatKitUser> userList = new ArrayList<LCChatKitUser>();
        for (String userId : list) {
            for (LCChatKitUser user : partUsers) {
                if (user.getUserId().equals(userId)) {
                    userList.add(user);
                    break;
                }
            }
        }
        callBack.done(userList, null);
    }

    public List<LCChatKitUser> getAllUsers() {
        return partUsers;
    }
}
