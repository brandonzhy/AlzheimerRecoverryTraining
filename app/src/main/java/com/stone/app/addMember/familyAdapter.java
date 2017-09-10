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
import com.stone.app.R;

import java.util.List;

/**
 * Created by Brandon Zhang on 2017/9/10.
 */

public class familyAdapter extends ArrayAdapter<familyItem> {
    private int resID;
    private Context mycontext;

    public familyAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<familyItem> objects) {
        super(context, resource, objects);
        this.resID = resource;
        mycontext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        familyItem familyItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resID, parent,false);
//        View view = LayoutInflater.from(getContext()).inflate(resID, null);
        ImageView imageView = view.findViewById(R.id.img_family);
        TextView tv_familyName = view.findViewById(R.id.tv_familyName);
        TextView tv_familyID = view.findViewById(R.id.tv_familyID);
        TextView tv_createrName = view.findViewById(R.id.tv_createrName);
        TextView tv_createrID = view.findViewById(R.id.tv_createrID);
        if(familyItem.getFamilyName()!=null){
            tv_familyName.setText(familyItem.getFamilyName());

        }else {
            tv_familyName.setText("");
        }
        if(familyItem.getFamilyID()!=null){
            tv_familyID.setText(familyItem.getFamilyID());

        }else {
            tv_familyID.setText("");
        }
        if(familyItem.getFamilyCreaterName()!=null){
            tv_createrName.setText(familyItem.getFamilyCreaterName());

        }else {
            tv_createrName.setText("");
        }
        if(familyItem.getFamilyCreaterID()!=null){
            tv_createrID.setText(familyItem.getFamilyCreaterID());

        }else {
            tv_createrID.setText("");
        }



        if (familyItem.getImagePath().equals("")) {

            imageView.setImageResource(R.mipmap.smiley);
        } else {
              Glide.with(mycontext).load(familyItem.getImagePath()).into(imageView);

        }
        return view;

    }
}
