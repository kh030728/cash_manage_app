package inu.dbproj;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 김광현 on 2017-11-12.
 */

public class UserDataAdapter extends BaseExpandableListAdapter {
    Context mContext;
    ArrayList<UserData> mUserData;

    public UserDataAdapter(Context context, ArrayList<UserData> userData) {
        mContext = context;
        mUserData = userData;
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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = getChildGenericView();
        } else view = convertView;

        TextView text = (TextView) view.findViewById(android.R.id.text1);
        //여기서는 갚아야 할것만 보도록 하자.
        text.setText(mUserData.get(groupPosition).getuName() + "(이)가 " + mUserData.get(groupPosition).cashListData.get(childPosition).getPrice() + "원 빌림");

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mUserData.get(groupPosition).cashListData.size();
    }

    public View getChildGenericView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(android.R.layout.simple_list_item_1, null);
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
        Log.v("그룹뷰 진입","정말 진업");
        View view;
        if(convertView == null) {
            view = getParentGenericView();
        } else {
            view = convertView;
        }
        //TextView name = (TextView)view.findViewById(android.R.id.text1);
        TextView name = (TextView)view.findViewById(R.id.Tv_name_userparentitem);
        TextView cash = (TextView)view.findViewById(R.id.Tv_cash_userparentitem);
        ImageButton imgview = (ImageButton) view.findViewById(R.id.downbtn);
        name.setText(mUserData.get(groupPosition).getuName());
        cash.setText("101010원");
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
