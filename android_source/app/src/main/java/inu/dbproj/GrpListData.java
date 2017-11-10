package inu.dbproj;


/**
 * Created by 김광현 on 2017-11-08.
 * 리스트에 담아 변동할(값이 바뀌는) 데이터 형식이다
 * 현재는 그룹 네임만 바꿀 것이기 때문에 String grpName을 넣엇고
 * 기본적으로 구분하기 위한 Id를 부여하기 위하여 getResId를 집어 넣었다.
 * 해당 Id는 나중에 기본키로 대체 될지도??
 */

public class GrpListData {
    private String grpName;
    private int resId;
    private int grpcash;

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) { this.grpName = grpName;}

    public int getGrpCash() {return grpcash;}

    public void setGrpCash(int grpcash) { this.grpcash = grpcash;}

}
