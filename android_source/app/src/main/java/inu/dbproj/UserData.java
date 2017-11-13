package inu.dbproj;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by 김광현 on 2017-11-12.
 */

public class UserData {
    int uid = 0;
    String uName ="";
    ArrayList<UserCashListData> cashListData;
    public UserData(int uid, String uName, ArrayList<UserCashListData> data) {
        this.uid = uid;
        this.uName = uName;
        cashListData = data;
    }
    public void setUid(int uid) {this.uid=uid;}
    public void setuName(String uName) {this.uName=uName;}
    public int getUid() {return this.uid;}
    public String getuName() {return this.uName;}


}
