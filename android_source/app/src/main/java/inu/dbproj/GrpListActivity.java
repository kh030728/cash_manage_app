package inu.dbproj;

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
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GrpListActivity extends AppCompatActivity {
    String[] grpValues = new String[3];
    DBHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grp_list);
        init();
        ArrayList<UserCashListData> arraycashListData = new ArrayList<>();
        UserCashListData cashListData = new UserCashListData(1,1,1,"이유",1,false);
        arraycashListData.add(cashListData);
        arraycashListData.add(cashListData);
        arraycashListData.add(cashListData);
        arraycashListData.add(cashListData);
        UserData userData = new UserData(1,"이름",arraycashListData);
        ArrayList<UserData> asd = new ArrayList<>();
        asd.add(userData);
        asd.add(userData);
        asd.add(userData);
        asd.add(userData);
        final ExpandableListView exList = (ExpandableListView) findViewById(R.id.exlist_userlist_activity_grp_list);
        UserDataAdapter adapter = new UserDataAdapter(this,asd);
        exList.setAdapter(adapter);

        exList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                }else {
                    parent.expandGroup(groupPosition);
                }

                return true;
            }
        });
    }


    private void init() {
        Intent intentFromMainActivity = new Intent(getIntent());
        dbhelper = new DBHelper(this);
        dbhelper.getGrpMemberFromGrpID(intentFromMainActivity.getIntExtra("GrpID", 0), grpValues);
        Log.v("grpValues", "[0] = " + grpValues[0] + "[1] = " + grpValues[1] + "[2] = " + grpValues[2]);
        setTitle(grpValues[1]);
        TextView grpCash = (TextView) findViewById(R.id.textview_cash_activity_grp_list);
        grpCash.setText(dbhelper.getGRPCash(Integer.parseInt(grpValues[0])) + " 원");
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

                builderGrpNameEdit.setTitle("모임 이름 수정");
                builderGrpNameEdit.setView(grpNameEditView);
                builderGrpNameEdit.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText temp = (EditText) grpNameEditView.findViewById(R.id.ET_grpName);
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
