package inu.dbproj;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 김광현 on 2017-11-08.
 * 사용자 정의한 리스트에서 사용할 데이터들을 담은 클래스 ( GrpListData.java)와 ArrayList를 연결하는
 * 어댑터이다.
 */

public class GrpListViewAdapter extends BaseAdapter {
    private ArrayList<GrpListData> CustListData = new ArrayList<>();

    @Override
    public int getCount() {
        return CustListData.size();
    }

    @Override
    public Object getItem(int position) {
        return CustListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     *
     * getView 메소드는 데이터를 리스트 항목에 매칭하는 작업이다.
     * CustomViewHolder는 GrpListData 클래스의 데이터를 레이아웃에 있는 뷰로 매칭하기 위해서
     * 뷰에대한 아이디를 저장해두는 클래스이다.
     * convertView가 를 지정하는 부분이 사용할 커스텀 레이아웃을 연결해주는 부분이다.
     *
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CustomViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_list_item, null, false);
        /*CustomViewHolder에 커스텀 리스트 아이템의 뷰 id를 매칭*/
            holder = new CustomViewHolder();
            holder.TVgrpName = (TextView)convertView.findViewById(R.id.group_list_item_name_tv);
            holder.TVgrpCash = (TextView)convertView.findViewById(R.id.group_list_item_cash_tv);
            convertView.setTag(holder);
        } else {
            holder = (CustomViewHolder) convertView.getTag();

        }
        GrpListData DTO = CustListData.get(position);
        holder.TVgrpName.setText(DTO.getGrpName());
        if(DTO.getGrpCash() < 0/*즉 음수라면*/) {
            holder.TVgrpCash.setText("-"+DTO.getGrpCash() + "원");
        } else {
            holder.TVgrpCash.setText(DTO.getGrpCash() + "원");
        }
        /*
         *만약 리스트 항목에서 터치 이벤트를 필요로 한다면 아래와 같이한다.
         *
        Button btn = (Button)convertView.findViewById(R.id.moretv);
        btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                holder.moretv.setVisibility(View.VISIBLE);
            }
        });
        */

        return convertView;
    }

    class CustomViewHolder {
        TextView TVgrpName;
        TextView TVgrpCash;
    }
    public void addItem(GrpListData dto) {
        CustListData.add(dto);
    }


}


