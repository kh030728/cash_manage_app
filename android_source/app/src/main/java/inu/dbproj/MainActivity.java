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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private GrpListViewAdapter grpadapter;
    private ListView grpListView;
    DBHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(getClass().getName(), "onCreate 시작");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 타이틀 변경
        setTitle("모임");
        //그룹 리스트뷰와 어뎁터 생성.
        grpadapter = new GrpListViewAdapter();
        grpListView = (ListView) findViewById(R.id.listvw);
        //리스트 footer 생성 및 버튼 처리
        View footer = getLayoutInflater().inflate(R.layout.activity_grpheader,null,false);
        grpListView.addFooterView(footer);
        Button grpaddbuton = (Button)footer.findViewById(R.id.grpaddbutton);
        grpaddbuton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        //데이터 베이스 생성
        dbHelper = new DBHelper(getApplicationContext());

        //리스트 연동
        drawListView();
        Log.v(getClass().getName(), "onCreate 종료");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        drawListView();
    }

    private void setData(int resId, String grpName) {
        GrpListData dto = new GrpListData();
        dto.setResId(resId);
        dto.setGrpName(grpName);
        dto.setGrpCash(dbHelper.getGRPCash(resId));
        grpadapter.addItem(dto);
    }

    private void drawListView() {
        Log.v(getClass().getName(), "drawListView 시작");
        String Result[][];
        Result = dbHelper.getGRPAllMember();
        if (Result == null) {
            String empty[] = {"그룹이 없습니다. 그룹을 추가하세요"};
            ArrayAdapter emptyAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, empty);
            grpListView.setAdapter(emptyAdapter);
            grpListView.setOnItemClickListener(null);
        } else {
            grpadapter = null;
            grpadapter = new GrpListViewAdapter();
            Log.v("생성", "grpadapter 재생성");
            for (int i = 0; i < Result.length; i++) {
                setData(Integer.parseInt(Result[i][0]), Result[i][1]);
            }
            grpListView.setAdapter(grpadapter);
            grpListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    GrpListData values = (GrpListData)grpadapter.getItem(position);
                    Log.v("리스트 클릭확인",values.getResId()+" "+values.getGrpName());
                    Intent intentMain2GrpList = new Intent(MainActivity.this,GrpListActivity.class);
                    intentMain2GrpList.putExtra("GrpID",values.getResId());
                    startActivity(intentMain2GrpList);
                }
            });
        }
        Log.v(getClass().getName(), "drawListView 종료");
    }
    void showDialog() {
        LayoutInflater inflaterDialogGrp = getLayoutInflater();
        final View viewDialogGrp = inflaterDialogGrp.inflate(R.layout.dialog_groupadd, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("모임 추가");
        //builder.setIcon(android.R.drawable.sym_def_app_icon);
        builder.setView(viewDialogGrp);
        builder.setPositiveButton("생성", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText ETtemp = (EditText) viewDialogGrp.findViewById(R.id.ET_grpName);
                String grpName = ETtemp.getText().toString();
                if(grpName.length() < 2) {
                    Toast.makeText(getApplicationContext(),"모임 이름을 두글자 이상으로 입력해 주세요",Toast.LENGTH_SHORT).show();
                }else {
                    dbHelper.insertGrp(grpName);
                    drawListView();
                }
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

}
