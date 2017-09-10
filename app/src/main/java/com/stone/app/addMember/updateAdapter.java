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

public class updateAdapter extends ArrayAdapter<updateFamilyItem> {
    private int resID;
    private Context mycontext;

    public updateAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<updateFamilyItem> objects) {
        super(context, resource, objects);
        this.resID = resource;
        mycontext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        updateFamilyItem updateFamily = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resID, parent,false);
//        View view = LayoutInflater.from(getContext()).inflate(resID, null);
        ImageView imageView = view.findViewById(R.id.img_family);
        TextView tv_updte_right = view.findViewById(R.id.tv_updte_right);
        TextView tv_updte_left = view.findViewById(R.id.tv_updte_left);
        if(updateFamily.getLeftText()!=null){
            tv_updte_left.setText(updateFamily.getLeftText());

        }else {
            tv_updte_left.setText("");
        }


        if (position == 0) {
            imageView.setVisibility(View.VISIBLE);
            tv_updte_right.setVisibility(View.GONE);
            if(updateFamily.getRightImagepath().equals("")||updateFamily.getRightImagepath()==null){

                imageView.setImageResource(R.mipmap.smiley);
            }else {
                Glide.with(mycontext).load(updateFamily.getRightImagepath()).into(imageView);

            }
        } else {
            tv_updte_right.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            if(updateFamily.getRightText()!=null){
                tv_updte_right.setText(updateFamily.getRightText());

            }else {
                tv_updte_right.setText("");
            }

        }

        return view;
    }
}
