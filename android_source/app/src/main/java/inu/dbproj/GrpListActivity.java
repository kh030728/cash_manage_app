package inu.dbproj;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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
import java.util.zip.Inflater;

public class GrpListActivity extends AppCompatActivity {
    String[] grpValues = new String[3];
    ExpandableListView exList = null;
    DBHelper dbhelper;
    AlertDialog.Builder diaalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grp_list);
        Log.v("init", "시작");
        init();
        drawList();
        //exList.deferNotifyDataSetChanged();
        ImageButton imgbtn = (ImageButton) findViewById(R.id.imgBtnUserAdd);
        imgbtn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRelGUDialog();
            }
        });
        ImageButton removeImgBtn = (ImageButton) findViewById(R.id.imgBtnRemoveUser);
        removeImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdelDialog();
            }
        });
        ImageButton imgbtn2 = (ImageButton) findViewById(R.id.addList);
        imgbtn2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                showaddListDialog();
            }
        });
        ImageButton imgbtn3 = (ImageButton) findViewById(R.id.imgBtnDeleUser);
        imgbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showuserdeletedialog();
            }
        });

    }

    private void showdelDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(android.R.layout.list_content, null);
        final Dialog dialog = new Dialog(this);
        final String data[][] = dbhelper.getUserWithGroup(Integer.parseInt(grpValues[0]));
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        ListView lv;
        if (data == null) {
            AlertDialog.Builder nulldialog = new AlertDialog.Builder(this);
            nulldialog.setTitle("인원 내보내기");
            nulldialog.setMessage("\n모임에서 내보낼 인원이 없습니다.");
            nulldialog.show();
            drawList();
        } else {
            int count = data.length;
            for (int i = 0; i < count; i++) {
                adapter.add("" + data[i][1]);
                Log.v("데이터", i + "이름 : " + data[i][0]);
            }
            v = inflater.inflate(R.layout.item_list_and_text, null);
            lv = (ListView) v.findViewById(R.id.item_list_ListView);
            TextView tv = (TextView) v.findViewById(R.id.item_list_TextView);
            tv.setText("인원 내보내기");
            lv.setAdapter(adapter);
            dialog.setContentView(v);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dbhelper.deleteRelationGU(Integer.parseInt(grpValues[0]), Integer.parseInt(data[position][0]));
                    dbhelper.deleteList(Integer.parseInt(grpValues[0]), Integer.parseInt(data[position][0]));
                    dialog.dismiss();
                    drawList();
                }
            });

            dialog.show();
        }

    }

    private void showuserdeletedialog() {
        final Dialog dialoog = new Dialog(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.item_list_and_text,null);
        TextView tv= (TextView)v.findViewById(R.id.item_list_TextView);
        ListView lv= (ListView)v.findViewById(R.id.item_list_ListView);
        tv.setText("인원 삭제하기");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        final String data[][] = dbhelper.getAllUser();
        if(data == null) {
            adapter.add("등록된 인원이 없습니다.");
            lv.setOnItemClickListener(null);
        } else {
            int count = data.length;
            for(int i = 0 ; i < count; i++) {
                adapter.add(data[i][1]);
            }
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder aa = new AlertDialog.Builder(parent.getContext());
                    aa.setTitle("인원 삭제하기");
                    aa.setMessage("정말로 삭제하시겠습니까?\n삭제된 인원은 복구할 수 없습니다.");
                    aa.setNegativeButton("취소",null);
                    aa.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbhelper.deleteUser(Integer.parseInt(data[position][0]));
                            dbhelper.deleteRelationGUall(Integer.parseInt(data[position][0]));
                            drawList();
                            dialoog.dismiss();
                        }
                    });
                    aa.show();
                }

            });
        }
        //데이터 주입 완료
        lv.setAdapter(adapter);
        dialoog.setContentView(v);
        dialoog.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        drawList();
    }

    private void showaddListDialog() {
        final String data[][] = dbhelper.getUserWithGroup(Integer.parseInt(grpValues[0]));
        Log.v("으아", "" + data);
        if (data == null) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("내역 추가");
            dialog.setMessage("모임에 속한 인원이 없습니다.");
            dialog.setPositiveButton("확인", null);
            dialog.show();
        } else {
            final int count = data.length;
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("내역 일괄 추가");
            LayoutInflater inflate = getLayoutInflater();
            final View v = inflate.inflate(R.layout.dialog_listadd, null);
            dialog.setView(v);
            dialog.setMessage("나누어지는 금액은 백의 자리로 올림합니다.");
            dialog.setNegativeButton("취소", null);
            dialog.setPositiveButton("결정", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText etCash = (EditText) v.findViewById(R.id.ET_addListOnGroup);
                    EditText etContext = (EditText) v.findViewById(R.id.ET_addListContextOnGroup);
                    int total_cash = Integer.parseInt(etCash.getText().toString());
                    int cash = total_cash / count;
                    int celi_cash = cash % 100;
                    if (celi_cash > 0) {
                        cash -= celi_cash;
                        cash += 100;
                    }
                    /*여기에 유저한테 cash만큼 리스트 부여*/
                    for (int i = 0; i < count; i++) {
                        dbhelper.insertList(Integer.parseInt(grpValues[0]), Integer.parseInt(data[i][0]), etContext.getText().toString(), cash);

                    }
                    drawList();
                }
            });
            dialog.show();

        }
    }

    private void showRelGUDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.item_list_and_text, null);
        final Dialog dialog = new Dialog(this);
        final String data[][] = dbhelper.getUserWithoutGroup(Integer.parseInt(grpValues[0]));
        Button btn;
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        ListView lv = (ListView) v.findViewById(R.id.item_list_ListView);
        TextView tv = (TextView) v.findViewById(R.id.item_list_TextView);
        tv.setText("인원 불러오기");
        if (data == null) {
            adapter.add("등록된 인원이 없습니다.");
            //이 위는 데이터 세팅
            lv.setOnItemClickListener(null);
        } else {
            int count = data.length;
            for (int i = 0; i < count; i++) {
                adapter.add("" + data[i][1]);
            }
            //이 위는 데이터 셋팅
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dbhelper.insertRelationGU(Integer.parseInt(grpValues[0]), Integer.parseInt(data[position][0]));
                    drawList();
                    dialog.dismiss();
                }
            });
        }
        //공통 셋팅
        lv.setAdapter(adapter);
        View vv = inflater.inflate(R.layout.activity_grpheader, null);
        btn = (Button) vv.findViewById(R.id.grpaddbutton);
        btn.setText("인원 생성 하기");
        lv.addFooterView(vv);
        dialog.setContentView(v);
        dialog.show();

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
        View v = inflater.inflate(R.layout.dialog_groupadd, null);
        builder.setTitle("인원 생성");
        final EditText editText = (EditText) v.findViewById(R.id.ET_grpName);
        editText.setHint("인원 이름 (2글자 이상)");

        builder.setView(v);
        builder.setPositiveButton("생성", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String grpName = editText.getText().toString();
                if (grpName.length() < 2) {
                    Toast.makeText(getApplicationContext(), "인원의 이름을 두글자 이상으로 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    dbhelper.insertUSER(grpName);
                    Toast.makeText(getApplicationContext(), "인원이 추가되었습니다. \n 다시 눌러 모임에 추가하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("취소", null);
        builder.show();

    }

    public void drawList() {
        TextView grpCash = (TextView) findViewById(R.id.textview_cash_activity_grp_list);
        grpCash.setText(dbhelper.getGRPCash(Integer.parseInt(grpValues[0])) + " 원");
        ArrayList<UserData> Groups = setGroups(Integer.parseInt(grpValues[0]));
        TextView nullTV = (TextView) findViewById(R.id.nullTV);
        if (Groups == null) {
            nullTV.setVisibility(View.VISIBLE);
            exList.setVisibility(View.INVISIBLE);
        } else {
            nullTV.setVisibility(View.INVISIBLE);
            exList.setVisibility(View.VISIBLE);
            final UserDataAdapter adapter = new UserDataAdapter(this, Groups, this);
            exList.setAdapter(adapter);
            exList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    if (adapter.mUserData.get(groupPosition).cashListData == null) {

                    } else {
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
        Log.v("setGroupData", "시작");
        String temp[] = dbhelper.getUserData(uid);
        Log.v("누구냐", temp[0] + "  " + temp[1] + "   " + uid + "   " + gid);
        int cash = dbhelper.getUserCash(uid, gid);
        Log.v("getUserData", "완료");
        Log.v("temp", temp + " ");
        ArrayList<UserCashListData> ChildsTemp = setChildsData(uid, gid);
        UserData Group = new UserData(uid, temp[1], cash, ChildsTemp);
        return Group;
    }

    private ArrayList<UserData> setGroups(int gid) {
        int[] temp = dbhelper.getRelationGU(gid);
        if (temp == null) {
            return null;
        } else {
            ArrayList<UserData> grpsTemp = new ArrayList<>();
            int count = temp.length;
            for (int i = 0; i < count; i++) {
                grpsTemp.add(setGroupData(temp[i], gid));
            }
            return grpsTemp;
        }
    }

    private void init() {
        Intent intentFromMainActivity = new Intent(getIntent());
        dbhelper = new DBHelper(this);
        dbhelper.getGrpMemberFromGrpID(intentFromMainActivity.getIntExtra("GrpID", 0), grpValues);
        setTitle(grpValues[1]);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView grpCash = (TextView) findViewById(R.id.textview_cash_activity_grp_list);
        grpCash.setText(dbhelper.getGRPCash(Integer.parseInt(grpValues[0])) + " 원");
        exList = (ExpandableListView) findViewById(R.id.exlist_userlist_activity_grp_list);
        Log.v("init", "완료");
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
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

}
