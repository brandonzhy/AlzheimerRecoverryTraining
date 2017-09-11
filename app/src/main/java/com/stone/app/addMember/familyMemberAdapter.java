package com.stone.app.addMember;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import static com.stone.app.R.id.img_family_member;
import static com.stone.app.R.id.tv_family_memberID;
import static com.stone.app.R.id.tv_family_memberName;

/**
 * Created by Brandon Zhang on 2017/9/10.
 */

public class familyMemberAdapter extends ArrayAdapter<familyMemberItem> {
    private int resID;
    private Context mycontext;

    public familyMemberAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<familyMemberItem> objects) {
        super(context, resource, objects);
        resID = resource;
        mycontext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        familyMemberItem familyMemberItem = getItem(position);
        View view;
        ViewHolder viewHolder;
         view = LayoutInflater.from(getContext()).inflate(resID, parent, false);

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resID, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.img_family_member = view.findViewById(img_family_member);
            viewHolder.tv_family_memberName = view.findViewById(tv_family_memberName);
            viewHolder.tv_family_memberID = view.findViewById(tv_family_memberID);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        if (familyMemberItem.getMemberName() != null) {

            viewHolder.tv_family_memberName.setText(familyMemberItem.getMemberName());
        } else {
            viewHolder.tv_family_memberName.setText("");
        }
        if (familyMemberItem.getMemberID() != null) {

            viewHolder.tv_family_memberID.setText(familyMemberItem.getMemberID());
        } else {
            viewHolder.tv_family_memberID.setText("");
        }
        //  Glide.with(mycontext).load(familyMemberItem.getImagePath()).into(img_family_member);
        if (familyMemberItem.getImagePath().equals("")) {
            Glide.with(mycontext).load("file:///android_asset/person1.jpg").into(viewHolder.img_family_member);
            //            img_family_member.setImageResource(R.mipmap.smiley);
        } else {
            Glide.with(mycontext).load(familyMemberItem.getImagePath()).into(viewHolder.img_family_member);

        }
        return view;

    }

    class ViewHolder {
        ImageView img_family_member;
        TextView tv_family_memberName;
        TextView tv_family_memberID;
    }
}
