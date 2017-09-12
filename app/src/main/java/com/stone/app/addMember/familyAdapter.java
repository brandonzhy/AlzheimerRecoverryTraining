package com.stone.app.addMember;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stone.app.R;

import java.util.List;

import static com.stone.app.R.id.tv_createrName;
import static com.stone.app.R.id.tv_familyID;
import static com.stone.app.R.id.tv_familyName;

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
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resID, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView=view.findViewById(R.id.img_family);
            viewHolder.tv_familyName=view.findViewById(tv_familyName);
            viewHolder.tv_familyID=view.findViewById(tv_familyID);
            viewHolder.tv_createrName=view.findViewById(tv_createrName);
            viewHolder.tv_createrID=view.findViewById(R.id.tv_createrID);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        //        View view = LayoutInflater.from(getContext()).inflate(resID, null);
//        ImageView imageView = view.findViewById(R.id.img_family);
//        TextView tv_familyName = view.findViewById(R.id.tv_familyName);
//        TextView tv_familyID = view.findViewById(R.id.tv_familyID);
//        TextView tv_createrName = view.findViewById(R.id.tv_createrName);
//        TextView tv_createrID = view.findViewById(R.id.tv_createrID);

        if (familyItem.getFamilyName() != null) {
            viewHolder.tv_familyName.setText(familyItem.getFamilyName());

        } else {
            viewHolder.tv_familyName.setText("");
        }
        if (familyItem.getFamilyID() != null) {
            viewHolder.tv_familyID.setText(familyItem.getFamilyID());

        } else {
            viewHolder.tv_familyID.setText("");
        }
        if (familyItem.getFamilyCreaterName() != null) {
            viewHolder.tv_createrName.setText(familyItem.getFamilyCreaterName());

        } else {
            viewHolder.tv_createrName.setText("");
        }
        if (familyItem.getFamilyCreaterID() != null) {
            viewHolder.tv_createrID.setText(familyItem.getFamilyCreaterID());

        } else {
            viewHolder.tv_createrID.setText("");
        }


        if ((familyItem.getImagePath()==null)||(familyItem.getImagePath().equals(""))) {

//            Glide.with(mycontext).load("file:///android_asset/person1.jpg").into(viewHolder.imageView);
            viewHolder.imageView.setImageResource(R.mipmap.wall01);
        } else {
            Log.i("TAG","getImagePath()=!null,  value=" +familyItem.getImagePath() );
            Glide.with(mycontext).load(familyItem.getImagePath()).into(viewHolder.imageView);
//            BitmapFactory.Options options = new BitmapFactory.Options();
//
//            options.inSampleSize = 2;
//
//            Bitmap img = BitmapFactory.decodeFile(familyItem.getImagePath(), options);
        }
        return view;

    }

    class ViewHolder {
        ImageView imageView;
        TextView tv_familyName;
        TextView tv_familyID;
        TextView tv_createrName;
        TextView tv_createrID;
    }

}
