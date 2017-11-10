package inu.dbproj;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    private GrpListViewAdapter grpadapter;
    private ListView grpListView;
    String grpName;
    int grpBaseCash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 액션바 설정
        grpName =null;
        grpBaseCash = 0;
        getSupportActionBar().setTitle("모임");
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));
        //그룹 추가 팝업 레이아웃 연결


        grpadapter = new GrpListViewAdapter();
        grpListView = (ListView) findViewById(R.id.listvw);

        drawListView();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_grpadd:
                LayoutInflater inflaterDialogGrp = getLayoutInflater();
                final View viewDialogGrp = inflaterDialogGrp.inflate(R.layout.dialog_groupadd,null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("모임 추가");
                //builder.setIcon(android.R.drawable.sym_def_app_icon);
                builder.setView(viewDialogGrp);
                builder.setPositiveButton("생성", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText ETtemp = (EditText)viewDialogGrp.findViewById(R.id.ET_grpName);
                        grpName = ETtemp.getText().toString();
                        ETtemp = (EditText)viewDialogGrp.findViewById(R.id.ET_grpBaseCash);
                        grpBaseCash = Integer.parseInt(ETtemp.getText().toString());
                        setData(1,grpName,grpBaseCash);
                        drawListView();
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                break;
            default:
                break;
        }
        return true;
    }

    private void setData(int resId, String grpName, int cash) {
        GrpListData dto = new GrpListData();
        dto.setResId(resId);
        dto.setGrpName(grpName);
        dto.setGrpCash(cash);
        grpadapter.addItem(dto);
    }

    private void drawListView() {
        if (grpadapter.getCount() == 0) {
            String empty[] = {"그룹이 없습니다. 그룹을 추가하세요"};
            ArrayAdapter emptyAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, empty);
            grpListView.setAdapter(emptyAdapter);
        } else grpListView.setAdapter(grpadapter);
    }


}
