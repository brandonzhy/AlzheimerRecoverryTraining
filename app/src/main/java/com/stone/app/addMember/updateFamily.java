package com.stone.app.addMember;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.stone.app.R;
import com.stone.app.Util.DateUtil;
import com.stone.app.Util.ToastUtil;
import com.stone.app.dataBase.DataBaseError;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.DataBaseSignal;
import com.stone.app.dataBase.FamilyData;
import com.stone.app.dataBase.MemberData;
import com.stone.app.dataBase.PictureData;
import com.stone.app.dataBase.RealmDB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.stone.app.dataBase.DataBaseSignal.SignalType.FamilyUpdated;
import static com.stone.app.dataBase.DataBaseSignal.SignalType.ImageAddedAlready;
import static com.stone.app.dataBase.DataBaseSignal.SignalType.MemberUpdated;


public class updateFamily extends Activity {
    private DataBaseManager dataBaseManager;
    private String memberID = "";
    private String familyID = "";
    private ListView lv_update_info;
    private List<FamilyData> familyDataList = null;
    private List<MemberData> memberDataList = null;
    private int modifyType = 10;
    private int type;
    private String pic_neme = String.valueOf(DateUtil.getTime());
    private List<updateFamilyItem> updateFamilyItemList = new ArrayList<updateFamilyItem>();
    //    private List<updateFamilyItem> updatememberItemList = new ArrayList<updateFamilyItem>();
    private ImageView ivHead;//头像显示
    private Bitmap head;// 头像Bitmap
    private static String path = "/sdcard/myHead/";// sd路径
    private String imgmagrPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_family);
        Log.i("TAG", "updateFamily onCreate  ");
        dataBaseManager = RealmDB.getDataBaseManager();
        lv_update_info = findViewById(R.id.lv_updata_info);
        //        ivHead=findViewById(R.id.iv_portrait);
        //        ivHead.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //                findView();
        //            }
        //        });
        final ImageView imageView = findViewById(R.id.iv_update_leftback);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAG", "返回按钮被点击");
                finish();
            }
        });
        Intent intent = getIntent();

        modifyType = intent.getIntExtra("modifyType", 10);
        //        修改家庭信息
        if (modifyType == 0) {
            String imagepath = "";
            familyID = intent.getStringExtra("familyID");
            Log.i("TAG", "获得的familyID 为： " + familyID);
            try {
                familyDataList = dataBaseManager.getFamilyList(familyID, "", "");
                if (familyDataList != null) {
                    FamilyData familyData = familyDataList.get(0);
                    updateFamilyItem updateFamilyItem = new updateFamilyItem();
                    updateFamilyItem.setLeftText("头像");
                    imagepath=dataBaseManager.getFamilyPortraitPath(familyID);
//                    List<PictureData> pictureDataList = dataBaseManager.getPictureList(familyData.getPortraitID(), "", memberID, "", "", 0, 0);
//
//                    if (pictureDataList != null) {
//                        imagepath = pictureDataList.get(0).getImagePath();
//                    }
                    updateFamilyItem.setRightImagepath(imagepath);
                    Log.i("TAG","imagepath = " +imagepath );
                    updateFamilyItemList.add(updateFamilyItem);


                    updateFamilyItem updateFamilyItem1 = new updateFamilyItem();
                    updateFamilyItem1.setLeftText("家庭名");
                    updateFamilyItem1.setRightText(familyData.getName());
                    updateFamilyItemList.add(updateFamilyItem1);

                    updateFamilyItem updateFamilyItem2 = new updateFamilyItem();
                    updateFamilyItem2.setLeftText("家庭ID");
                    updateFamilyItem2.setRightText(familyData.getID());
                    updateFamilyItemList.add(updateFamilyItem2);

                    updateFamilyItem updateFamilyItem3 = new updateFamilyItem();
                    updateFamilyItem3.setLeftText("家庭人数");
                    updateFamilyItem3.setRightText(String.valueOf(dataBaseManager.getMemberList("", familyID, "", "").size()));
                    updateFamilyItemList.add(updateFamilyItem3);

                    updateAdapter Adapter = new updateAdapter(updateFamily.this, R.layout.updatefamily_item, updateFamilyItemList);
                    lv_update_info.setAdapter(Adapter);
                    lv_update_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            switch (i) {
                                case 0:
                                    Log.i("TAG", "修改家庭图像按钮被点击了");
                                    //                                    ivHead = (ImageView) view.getTag(R.id.tag_first);
                                    ivHead = (ImageView) view.getTag(R.id.tag_first);
                                    if (ivHead != null) {
                                        Log.i("TAG", "ivHead的信息" + ivHead.toString());
                                        setType(0);
                                        findView(0);

                                    }
                                    break;
                                case 1:
                                    Log.i("TAG", "修改家庭名字按钮被点击了");
                                    Intent intentmodify_name = new Intent(updateFamily.this, modifyName.class);
                                    intentmodify_name.putExtra("familyID", familyID);
                                    intentmodify_name.putExtra("modifyType", 0);
                                    startActivity(intentmodify_name);
                                    finish();
                                    //                                    finish();
                                    break;
                            }
                        }
                    });
                }
            } catch (DataBaseError dataBaseError) {
                dataBaseError.printStackTrace();
            }

        } else if (modifyType == 1) {
            String memberImagePath="";
            //修改个人信息
            memberID = intent.getStringExtra("memberID");
            Log.i("TAG", "update的memberID：" + memberID);

            try {
                memberDataList = dataBaseManager.getMemberList(memberID, "", "", "");
            } catch (DataBaseError dataBaseError) {
                dataBaseError.printStackTrace();
            }
            if (memberDataList != null && memberDataList.size() > 0) {
                MemberData memberData = memberDataList.get(0);
                updateFamilyItem updateFamilyItem = new updateFamilyItem();
                updateFamilyItem.setLeftText("头像");
                try {
                    memberImagePath=dataBaseManager.getMemberPortraitPath(memberID);
                } catch (DataBaseError dataBaseError) {
                    dataBaseError.printStackTrace();
                }
//                List<PictureData> pictureDataList = null;
//                try {
//                    pictureDataList = dataBaseManager.getPictureList(memberData.getPortraitID(), "", "", "", "", 0, 0);
//                } catch (DataBaseError dataBaseError) {
//                    Log.i("TAG", "  updateamily 的dataBaseError type" +dataBaseError.getErrorType()+dataBaseError.getMessage());
//                    dataBaseError.printStackTrace();
//                }

//                if (pictureDataList != null && pictureDataList.size() > 0) {
//                    imgmagrPath = pictureDataList.get(0).getImagePath();
//                }
                updateFamilyItem.setRightImagepath(memberImagePath);
                Log.i("TAG","imgmagrPath= " +memberImagePath );
                updateFamilyItemList.add(updateFamilyItem);

                updateFamilyItem updateFamilyItem1 = new updateFamilyItem();
                updateFamilyItem1.setLeftText("姓名");
                if (TextUtils.isEmpty(memberData.getName())) {
                    updateFamilyItem1.setRightText("无");
                } else {

                    updateFamilyItem1.setRightText(memberData.getName());
                }
                updateFamilyItemList.add(updateFamilyItem1);

                updateFamilyItem updateFamilyItem2 = new updateFamilyItem();
                updateFamilyItem2.setLeftText("ID");
                if (TextUtils.isEmpty(memberData.getID())) {
                    updateFamilyItem2.setRightText("无");
                } else {

                    updateFamilyItem2.setRightText(memberData.getID());
                }
                updateFamilyItemList.add(updateFamilyItem2);

                updateFamilyItem updateFamilyItem3 = new updateFamilyItem();
                updateFamilyItem3.setLeftText("昵称");
                if (TextUtils.isEmpty(memberData.getNickName())) {
                    updateFamilyItem3.setRightText("无");
                } else {
                    updateFamilyItem3.setRightText(memberData.getNickName());
                }

                updateFamilyItemList.add(updateFamilyItem3);
            }else {
                ToastUtil.showToast(updateFamily.this,"用户不存在");
                finish();
            }
            updateAdapter Adapter = new updateAdapter(updateFamily.this, R.layout.updatefamily_item, updateFamilyItemList);
            lv_update_info.setAdapter(Adapter);
            lv_update_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i) {
                        case 0:
                            ivHead = (ImageView) view.getTag(R.id.tag_first);
                            if (ivHead != null) {
                                Log.i("TAG", "ivHead的信息" + ivHead.toString());
                                setType(1);

                                findView(1);
                            } else {
                                finish();
                            }

                            Log.i("TAG", "修改个人图像按钮被点击了");


                            break;
                        case 1:
                            Log.i("TAG", "修改个人名字按钮被点击了");
                            Intent intentmodify_membername = new Intent(updateFamily.this, modifyName.class);
                            intentmodify_membername.putExtra("modifyType", 1);
                            intentmodify_membername.putExtra("memberID", memberID);
                            startActivity(intentmodify_membername);
                            //                                    Log.i("TAG"," onDestroy is called" );
                            //                                    onDestroy();
                            finish();
                            break;
                        case 3:
                            Log.i("TAG", "修改nickname按钮被点击了");
                            Intent intentmodify_nickname = new Intent(updateFamily.this, modifyName.class);
                            intentmodify_nickname.putExtra("modifyType", 2);
                            intentmodify_nickname.putExtra("memberID", memberID);
                            startActivity(intentmodify_nickname);
                            finish();
                            break;
                    }
                }
            });
        }


    }


    @Override
    protected void onResume() {


        super.onResume();

    }

    @Override
    protected void onStart() {

        super.onStart();

    }

    @Override
    protected void onRestart() {

        super.onRestart();


    }

    private void findView(int type) {
        //  Bitmap bt = BitmapFactory.decodeFile(path +family+".png");// 从SD卡中找头像，转换成Bitmap
        //              if(bt!=null){
        //        @SuppressWarnings("deprecation")
        //        Drawable drawable = new BitmapDrawable(bt);//转换成drawable
        //ivHead.setImageDrawable(drawable);

        Intent intent1 = new Intent(Intent.ACTION_PICK, null);
        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent1, 1);
        //        }else{
        //
        //            /**
        //             *  如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
        //             *
        //             */
        //        }
    }

    //创文件夹
    public void setPicToView(Bitmap mBitmap, String family, int type) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + family + ".png";//图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //
        try {
            dataBaseManager.AddImage(memberID, pic_neme, "", fileName, 0, "", "");
            Log.i("TAG", "fileName" + fileName);

        } catch (DataBaseSignal dataBaseSignal) {
            if (dataBaseSignal.getSignalType() == ImageAddedAlready) {
                Log.i("TAG", "头像上传成功");

            }
            dataBaseSignal.printStackTrace();
        } catch (DataBaseError dataBaseError) {
            dataBaseError.printStackTrace();
            Log.i("TAG", "dataBaseError " + dataBaseError.getErrorType() + dataBaseError.getMessage());
        }

        List<PictureData> pictures = null;
        try {

            pictures = dataBaseManager.getPictureList("", pic_neme, memberID, "", "", 0, 0);
        } catch (DataBaseError dataBaseError) {
            dataBaseError.printStackTrace();
        }
        try {
            if (type == 1) {
                Log.i("TAG", "更新个人头像");
                dataBaseManager.UpdateMember(memberID, "", 0, "", "", pictures.get(0).getID());
            }
        } catch (DataBaseError dataBaseError) {
            dataBaseError.printStackTrace();
        } catch (DataBaseSignal dataBaseSignal) {
            if (dataBaseSignal.getSignalType() == MemberUpdated) {
                Log.i("TAG", "头像更新成功");
                ToastUtil.showToast(updateFamily.this, "更新成功");
                finish();
            }
            dataBaseSignal.printStackTrace();
        }
        if (type == 0) {
            Log.i("TAG", "更新家庭头像");
            try {
                dataBaseManager.UpdateFamily(familyID, "", "", pictures.get(0).getID());
            } catch (DataBaseError dataBaseError) {
                dataBaseError.printStackTrace();
            } catch (DataBaseSignal dataBaseSignal) {
                if (FamilyUpdated == dataBaseSignal.getSignalType()) {
                    Log.i("TAG", "更新成功");
                    ToastUtil.showToast(updateFamily.this, "更新成功");

                } else {
                    finish();
                }
                dataBaseSignal.printStackTrace();
            }
        }
        //        }

    }

    @NonNull
    private String setPicName() {
        return String.valueOf(DateUtil.getTime()) + "pic_portrait";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());//裁剪图片
                    Log.i("TAG", "裁剪图片");
                }

                break;

            case 0:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");

                    if (head != null) {
                        Log.i("TAG", "updatafamilyNo.341  head不为空");
                        /**
                         * 上传服务器代码
                         */

                        //head= toRoundBitmap(head); //变圆


                        setPicToView(head, String.valueOf(DateUtil.getTime()), getType());//保存在SD卡中

                        ivHead.setImageBitmap(head);//用ImageView显示出来
                    }
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private int getType() {
        return type;
    }

    private void setType(int Type) {
        type = Type;
    }

    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 0);
    }

    //裁剪成圆
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        if (bitmap == null) {

        } else {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float roundPx;
            float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
            if (width <= height) {
                roundPx = width / 2;
                top = 0;
                bottom = width;
                left = 0;
                right = width;
                height = width;
                dst_left = 0;
                dst_top = 0;
                dst_right = width;
                dst_bottom = width;
            } else {
                roundPx = height / 2;
                float clip = (width - height) / 2;
                left = clip;
                right = width - clip;
                top = 0;
                bottom = height;
                width = height;
                dst_left = 0;
                dst_top = 0;
                dst_right = height;
                dst_bottom = height;
            }

            Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect src = new Rect((int) left, (int) top, (int) right,
                    (int) bottom);
            final Rect dst = new Rect((int) dst_left, (int) dst_top,
                    (int) dst_right, (int) dst_bottom);
            final RectF rectF = new RectF(dst);

            paint.setAntiAlias(true);

            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, src, dst, paint);
            return output;
        }
        return null;

    }

}
