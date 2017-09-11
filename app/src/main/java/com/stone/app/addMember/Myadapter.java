//package com.stone.app.addMember;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import com.stone.app.R;
//
//import java.util.List;
//
///**
// * Created by Brandon Zhang on 2017/9/8.
// */
//
//public class Myadapter extends BaseAdapter {
//    List<familyMember>list;
//    private Context context;
//
//    public Myadapter(Context context,List<familyMember> list ) {
//        this.list = list;
//        this.context = context;
//    }
//
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        view= LayoutInflater.from(context).inflate(R.layout.item_person,null);
//        TextView tv_addmember_name=view.findViewById(R.id.tv_addmember_name);
//        TextView tv_addmember_id=view.findViewById(R.id.tv_addmember_id);
//        tv_addmember_name.setText(list.get(i).getName());
//        tv_addmember_id.setText("ID: "+list.get(i).getID());
//
//        return view;
//
//
//    }
//}
