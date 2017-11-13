package inu.dbproj;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GrpListActivity extends AppCompatActivity {
    String[] grpValues = new String[3];
    ExpandableListView exList = null;
    DBHelper dbhelper;
    AlertDialog.Builder diaalog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grp_list);
        Log.v("init","시작");
        init();
        drawList();
        ImageButton imgbtn = (ImageButton)findViewById(R.id.imgBtnUserAdd);
        imgbtn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRelGUDialog();
            }
        });
    }

    private void showRelGUDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(android.R.layout.list_content,null);
        final Dialog dialog = new Dialog(this);
        final String data[][] = dbhelper.getUserWithoutGroup(Integer.parseInt(grpValues[0]));
        Button btn;
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1);
        ListView lv;
        if(data == null) {
            Log.v("데이터","널이에요");
            String[] temp = {"등록된 인원이 없습니다."};
            adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,temp);
            lv = (ListView)v.findViewById(android.R.id.list);
            lv.setAdapter(adapter);
            View vv = inflater.inflate(R.layout.activity_grpheader,null);
            btn = (Button)vv.findViewById(R.id.grpaddbutton);
            btn.setText("인원 생성 하기");
            lv.addFooterView(vv);
            dialog.setTitle("인원 추가");
            dialog.setContentView(v);
            //dialog.setNegativeButton("취소", null);
            lv.setOnItemClickListener(null);
            dialog.show();
        } else {
            Log.v("데이터","널아니에요");
            int count = data.length;
            for(int i = 0 ; i < count ; i++) {
                Log.v("왠지...",""+i);
                adapter.add(""+data[i][1]);
                Log.v("데이터",i+"이름 : "+data[i][0]);
            }
            lv = (ListView)v.findViewById(android.R.id.list);
            View vv = inflater.inflate(R.layout.activity_grpheader,null);
            btn = (Button)vv.findViewById(R.id.grpaddbutton);
            btn.setText("인원 생성 하기");
            lv.setAdapter(adapter);
            lv.addFooterView(vv);
            dialog.setTitle("인원 추가");
            dialog.setContentView(v);
            //dialog.setNegativeButton("취소", null);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dbhelper.insertRelationGU(Integer.parseInt(grpValues[0]),Integer.parseInt(data[position][0]));
                    drawList();
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAddDialog();
                dialog.dismiss();
            }
        });
    }
    private void userAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_groupadd,null);
        builder.setTitle("인원 생성");
        final EditText editText = (EditText)v.findViewById(R.id.ET_grpName);
        editText.setHint("인원 이름 (2글자 이상)");

        builder.setView(v);
        builder.setPositiveButton("생성", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String grpName = editText.getText().toString();
                if(grpName.length() < 2) {
                    Toast.makeText(getApplicationContext(),"인원의 이름을 두글자 이상으로 입력해주세요.",Toast.LENGTH_SHORT).show();
                }else {
                    dbhelper.insertUSER(grpName);
                    Toast.makeText(getApplicationContext(),"인원이 추가되었습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("취소",null);
        builder.show();

    }
    private void drawList() {
        ArrayList<UserData> Groups = setGroups(Integer.parseInt(grpValues[0]));

        if(Groups == null) {
            /*
            String empty[] = {"그룹이 없습니다. 그룹을 추가하세요"};
            ArrayAdapter emptyAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, empty);
            exList.setAdapter(emptyAdapter);
            exList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    return false;
                }
            });*/
        } else {
            final UserDataAdapter adapter = new UserDataAdapter(this, Groups);
            exList.setAdapter(adapter);
            exList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    if(adapter.mUserData.get(groupPosition).cashListData==null) {

                    }else {
                        if (parent.isGroupExpanded(groupPosition)) {
                            parent.collapseGroup(groupPosition);
                        } else {
                            parent.expandGroup(groupPosition);
                        }
                    }

                    return true;
                }
            });
        }
    }

    private ArrayList<UserCashListData> setChildsData(int uid, int gid) {
        ArrayList<UserCashListData> Childs = new ArrayList<>();
        UserCashListData Childtemp;
        String[][] temp = dbhelper.getListaDataForGroup(uid, gid);

        if (temp == null) {
            return null;
        } else {
            int count = temp.length;
            for (int i = 0; i < count; i++) {
                boolean check;
                if (Integer.parseInt(temp[i][5]) == 0) check = false;
                else check = true;
                Childtemp = new UserCashListData(Integer.parseInt(temp[i][0]), Integer.parseInt(temp[i][2]), Integer.parseInt(temp[i][1]), temp[i][3], Integer.parseInt(temp[i][4]), check);
                Childs.add(Childtemp);
            }
            return Childs;
        }
    }

    private UserData setGroupData(int uid, int gid) {
        Log.v("setGroupData","시작");
        String temp[] = dbhelper.getUserData(uid);
        Log.v("getUserData","완료");
        Log.v("temp",temp+" ");
        ArrayList<UserCashListData> ChildsTemp = setChildsData(uid,gid);
        UserData Group = new UserData(uid,temp[1],ChildsTemp);
        return Group;
    }
    private ArrayList<UserData> setGroups(int gid) {
        int[] temp = dbhelper.getRelationGU(gid);
        if(temp == null) {
            return null;
        } else {
            ArrayList<UserData> grpsTemp = new ArrayList<>();
            int count = temp.length;
            for(int i = 0; i < count ; i++) {
                grpsTemp.add(setGroupData(temp[i],gid));
            }
            return grpsTemp;
        }
    }

    private void init() {
        Intent intentFromMainActivity = new Intent(getIntent());
        dbhelper = new DBHelper(this);
        dbhelper.getGrpMemberFromGrpID(intentFromMainActivity.getIntExtra("GrpID", 0), grpValues);
        setTitle(grpValues[1]);
        TextView grpCash = (TextView) findViewById(R.id.textview_cash_activity_grp_list);
        grpCash.setText(dbhelper.getGRPCash(Integer.parseInt(grpValues[0])) + " 원");
        exList  = (ExpandableListView) findViewById(R.id.exlist_userlist_activity_grp_list);
        Log.v("init","완료");
    }

    //액션바 매뉴구성과 클릭
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_grpNameEdit:
                AlertDialog.Builder builderGrpNameEdit = new AlertDialog.Builder(this);
                LayoutInflater grpnameinflater = getLayoutInflater();
                final View grpNameEditView = grpnameinflater.inflate(R.layout.dialog_groupadd, null);
                final EditText temp = (EditText) grpNameEditView.findViewById(R.id.ET_grpName);
                temp.setText(grpValues[1]);
                builderGrpNameEdit.setTitle("모임 이름 수정");
                builderGrpNameEdit.setView(grpNameEditView);
                builderGrpNameEdit.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nametemp = temp.getText().toString();
                        if (nametemp.length() < 2) {
                            Toast.makeText(getApplicationContext(), "모임 이름을 두글자 이상으로 입력해 주세요", Toast.LENGTH_SHORT).show();
                        } else {
                            dbhelper.renameGrp(nametemp, Integer.parseInt(grpValues[0]));
                            init();
                        }
                    }
                });
                builderGrpNameEdit.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builderGrpNameEdit.show();
                break;
            case R.id.action_grpDelete:
                AlertDialog.Builder builderGrpDelete = new AlertDialog.Builder(this);
                builderGrpDelete.setTitle("모임 삭제");
                builderGrpDelete.setMessage("정말로 모임을 삭제하시겠습니까?\n\n삭제된 모임은 복구할 수 없습니다.");
                builderGrpDelete.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbhelper.deleteGRPMember(Integer.parseInt(grpValues[0]));
                        finish();
                    }
                });
                builderGrpDelete.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builderGrpDelete.show();
                break;
            default:
                break;
        }
        return true;
    }

}
