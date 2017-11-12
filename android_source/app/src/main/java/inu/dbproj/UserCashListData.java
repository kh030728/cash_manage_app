package inu.dbproj;

/**
 * Created by 김광현 on 2017-11-12.
 */

public class UserCashListData {
    int listId;
    int uid;
    int grpid;
    String listContext;
    int price;
    boolean listCheck;
    public UserCashListData(int listId, int uid, int grpid, String listContext, int price, boolean listCheck) {
        this.listId = listId;
        this.uid = uid;
        this.grpid = grpid;
        this.listContext = listContext;
        this.price = price;
        this.listCheck = listCheck;
    }








    /*
    아래는 getter & setter
     */
    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getGrpid() {
        return grpid;
    }

    public void setGrpid(int grpid) {
        this.grpid = grpid;
    }

    public String getListContext() {
        return listContext;
    }

    public void setListContext(String listContext) {
        this.listContext = listContext;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isListCheck() {
        return listCheck;
    }

    public void setListCheck(boolean listCheck) {
        this.listCheck = listCheck;
    }
}
