package inu.dbproj;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by 김광현 on 2017-11-12.
 */

public class UserDataAdapter extends BaseExpandableListAdapter {
    Context mContext;
    ArrayList<UserData> mUserData;
    GrpListActivity aaaa;

    public UserDataAdapter(Context context, ArrayList<UserData> userData, GrpListActivity aaaa) {
        mContext = context;
        mUserData = userData;
        this.aaaa = aaaa;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mUserData.get(groupPosition).cashListData.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, final View convertView, final ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = getChildGenericView();
        } else view = convertView;

        TextView text = (TextView) view.findViewById(R.id.textChild);
        ImageButton imgbtncheck = (ImageButton)view.findViewById(R.id.imgbtnChild);

        imgbtncheck.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dial = new AlertDialog.Builder(parent.getContext());
                dial.setTitle("상환 처리");
                dial.setMessage(" 상환을 하면 내역에 나타지나 않게 됩니다.\n 정말로 상환 하실건가요?");
                dial.setNegativeButton("취소",null);
                dial.setPositiveButton("상환", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper dbHelper = new DBHelper(parent.getContext());
                        //Log.v("확인요", ""+mUserData.get(groupPosition).cashListData.get(childPosition).listCheck);
                        dbHelper.updateCheckList(mUserData.get(groupPosition).cashListData.get(childPosition).listId,1);
                        mUserData.get(groupPosition).cashListData.get(childPosition).setListCheck(true);
                        aaaa.drawList();

                    }
                });
                dial.show();


            }
        });
        //여기서는 갚아야 할것만 보도록 하자.
        text.setText(mUserData.get(groupPosition).getuName() + "(이)가 " +mUserData.get(groupPosition).cashListData.get(childPosition).getListContext()+"한 이유로 "+ mUserData.get(groupPosition).cashListData.get(childPosition).getPrice() + "원 빌렸습니다.");

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mUserData.get(groupPosition).cashListData.size();
    }

    public View getChildGenericView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.childitem, null);
        return view;
    }


    @Override
    public Object getGroup(int groupPosition) {
        return mUserData.get(groupPosition);
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null) {
            view = getParentGenericView();
        } else {
            view = convertView;
        }
        TextView name = (TextView)view.findViewById(R.id.Tv_name_userparentitem);
        TextView cash = (TextView)view.findViewById(R.id.Tv_cash_userparentitem);
        ImageButton imgview = (ImageButton) view.findViewById(R.id.downbtn);
        name.setText(mUserData.get(groupPosition).getuName());
        cash.setText(mUserData.get(groupPosition).getTotalcash()+"원");
        imgview.setFocusable(false);

        return view;
    }

    @Override
    public int getGroupCount() {
        return mUserData.size();
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }

    public View getParentGenericView() {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.userparentitem,null);
        return view;
    }
}
