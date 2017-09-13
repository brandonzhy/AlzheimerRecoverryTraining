package com.stone.app.addRelation;

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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stone.app.R;
import com.stone.app.dataBase.DataBaseError;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.DataBaseSignal;
import com.stone.app.dataBase.MemberData;
import com.stone.app.dataBase.MemberRelationData;
import com.stone.app.dataBase.RealmDB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;



public class wife extends AppCompatActivity {
    private String myname;
    private String membername="";
    private String  member_ID="";
    private String my_ID="";
    private ImageView ivHead;//头像显示
    private Bitmap head;// 头像Bitmap
    private static String path = "/sdcard/myHead/";// sd路径
    private String myfamily;


    private void findView() {
     Intent intent1 = new Intent(Intent.ACTION_PICK, null);
            intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent1, 1);
            }

             private void setPicToView(Bitmap mBitmap,String family) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName =path +"wife.png";//图片名字
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
    }
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());//裁剪图片
                }

                break;

            case 0:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if(head!=null){
                        /**
                         * 上传服务器代码
                         */

                       head= toRoundBitmap(head);
                               setPicToView(head,myfamily);//保存在SD卡中
                               ivHead.setImageBitmap(head);//用ImageView显示出来
                               }
                               }
                               break;
default:
        break;

        }
        super.onActivityResult(requestCode, resultCode, data);
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wife);
        final DataBaseManager dbm = RealmDB.getDataBaseManager();
        //dbm.AddMemberRelation();
        final TextView me=(TextView)findViewById(R.id.me);
        myname=me.getText().toString();
        final EditText member=(EditText)findViewById(R.id.wifeName);
        membername=member.getText().toString();
         final TextView memberID=(TextView) findViewById(R.id.FamilyMemberID);

        //member_ID=memberID.getText().toString();
        final TextView myID=(TextView) findViewById(R.id.MyId);

        //my_ID=myID.getText().toString();
        Button confirm=(Button) findViewById(R.id.confirm);
        Button back=(Button) findViewById(R.id.back);
        Button search=(Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // member_ID=member.getText().toString();
//                myname=me.getText().toString();

                try{
                    List<MemberData> mdl1 = dbm.getMemberList("","","",membername);
                    List<MemberData> mdl2 = dbm.getMemberList("","","",myname);
                    String memberID1="";
                    String myID1="";
                    if(mdl1.size()>0)
                    {
                        memberID1 = mdl1.get(0).getID();
                        member_ID=memberID1;
                    }
                    else throw new Exception("No Match");
                    if(mdl2.size()>0)
                    {
                        myID1 = mdl2.get(0).getID();
                        my_ID=myID1;
                    }
                    else throw new Exception("No Match");
                    memberID.setText(memberID1);
                    myID.setText(myID1);


                } catch (Exception e){

                    Toast.makeText(wife.this,"此用户不存在",Toast.LENGTH_SHORT).show();
                }
            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(wife.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });




        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 member_ID=memberID.getText().toString();
                membername=myID.getText().toString();




                if(member_ID==""){
                    return;
                }

                try {
                    dbm.AddMemberRelation(member_ID,my_ID, MemberRelationData.DB_RELATION_PARENT);
                } catch (DataBaseSignal s) {
                    if(DataBaseSignal.SignalType.MemberRelationAddedAlready == s.getSignalType()){
                        Toast.makeText(wife.this, "提交成功", Toast.LENGTH_SHORT).show();
                        findView();
                    }
                    else{
                        s.printStackTrace();
                        Toast.makeText(wife.this, "提交失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (DataBaseError dataBaseError) {
                    dataBaseError.printStackTrace();
                    Toast.makeText(wife.this, "提交失败", Toast.LENGTH_SHORT).show();
                    finish();
                } catch(Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(wife.this, "提交失败", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });





    }
}
