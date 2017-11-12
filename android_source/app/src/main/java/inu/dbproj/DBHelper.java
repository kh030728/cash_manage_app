package inu.dbproj;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 김광현 on 2017-11-11.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static String dbname = "inu.MoneyDB.db";
    SQLiteDatabase mdatabase;

    public DBHelper(Context context) {
        super(context, dbname, null, 2);
        mdatabase = this.getWritableDatabase();
    }

    public void renameGrp(String name,int GrpID) {
        mdatabase.execSQL("update GRP set GrpName ='"+name+"' where GrpID = "+GrpID+";");
    }

    public void insertGrp(String name) {
        mdatabase.execSQL("INSERT INTO GRP VALUES (null,'"+name+"');");
    }

    public String[][] getGRPAllMember() {
        Log.v("grp 조회", "시작");
        Cursor c = mdatabase.rawQuery("SELECT * FROM GRP", null);
        c.moveToFirst();
        int count = c.getCount();
        if(count != 0) {
            String Result[][] = new String[count][2];
            int i = 0;
            while (c.isAfterLast() == false) {
                Result[i][0] = ""+c.getString(0);
                Result[i][1] = ""+c.getString(1);
                i++;
                c.moveToNext();
            }
            Log.v("grp 조회", "조회 완료");
            return Result;

        } else {
            Log.v("grp 조회", "조회 완료 --- null 반환");
            return null;
        }


    }

    public boolean getGrpMemberFromGrpID(int GrpID,String[] Result) {
        Cursor c = mdatabase.rawQuery("select * from GRP where GrpID = "+GrpID,null);
        c.moveToFirst();
        if(c.getString(0)!=null) {
            Result[0] = c.getString(0);
            Result[1] = c.getString(1);
            Result[2] = getGRPCash(Integer.parseInt(Result[0]))+"";
            return true;
        } else return false;
    }

    public void deleteGRPMember(int id) {
        mdatabase.execSQL("delete from GRP where GrpID ="+id+";");
    }

    public int getGRPCash(int GrpId) {
        Cursor c = mdatabase.rawQuery("select SUM(ListCash) from LIST where GrpID ="+GrpId+" AND ListCheck = 0;",null);
        c.moveToFirst();
        if(c.getString(0) != null) {
            Log.v("캐쉬 값", c.getString(0) + "");

            return Integer.parseInt(c.getString(0));
        } else {
            return 0;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(getClass().getName(), "onCreate 실행");
        createTable(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        droptable(db);
        createTable(db);
    }

    public void createTable(SQLiteDatabase db) {
        Log.v(getClass().getName(), "GRP 테이블 생성......");

        db.execSQL("CREATE TABLE GRP(GrpID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "GrpName TEXT NOT NULL);");
        Log.v(getClass().getName(), "GRP 테이블 생성...... 완료");

        //User 테이블 생성
        Log.v(getClass().getName(), "USER 테이블 생성......");
        db.execSQL("CREATE TABLE USER(UId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UName TEXT NOT NULL);");
        Log.v(getClass().getName(), "USER 테이블 생성...... 완료");
        //RelationGU 테이블 생성
        Log.v(getClass().getName(), "RELATIONGU 테이블 생성......");
        db.execSQL("CREATE TABLE RELATIONGU(UId INTEGER REFERENCES USER(UId) ON UPDATE CASCADE, " +
                "GId INTEGER REFERENCES " +
                "GRP(GrpID) ON UPDATE CASCADE);");
        Log.v(getClass().getName(), "RELATIONGU 테이블 생성...... 완료");
        //List 테이블 생성
        Log.v(getClass().getName(), "LIST 테이블 생성......");
        db.execSQL("CREATE TABLE LIST(ListID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " GrpID INTEGER REFERENCES GRP(GrpID) ON UPDATE CASCADE," +
                "UId INTEGER REFERENCES USER(UId) ON UPDATE SET NULL," +
                "ListContext VARCHAR(30)," +
                "ListCash INTEGER NOT NULL," +
                "ListCheck INTEGER NOT NULL DEFAULT 0);");
        Log.v(getClass().getName(), "LIST 테이블 생성...... 완료");
    }

    public void droptable(SQLiteDatabase db) {
        Log.v(getClass().getName(), "테이블 삭제 실행");
        db.execSQL("DROP TABLE RelationGU;");
        db.execSQL("DROP TABLE List;");
        db.execSQL("DROP TABLE USER;");
        db.execSQL("DROP TABLE GRP;");
        Log.v(getClass().getName(), "테이블 삭제 완료");
    }
}