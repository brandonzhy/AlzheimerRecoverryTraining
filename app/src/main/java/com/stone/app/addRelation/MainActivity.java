package com.stone.app.addRelation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.stone.app.R;
import com.stone.app.addRelation.view.ArcMenu;
import com.stone.app.addRelation.view.RoundBitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;









public class MainActivity extends Activity {

    private ImageView ivHead;//头像显示
    private Bitmap head;// 头像Bitmap
    private static String path = "/sdcard/myHead/";// sd路径
    private String myfamily;
//    Bitmap picture0;
//    Bitmap picture2;
//    Bitmap picture3;
//    Bitmap picture4;
//    Bitmap picture5;
//    Bitmap picture6;
//    Bitmap picture7;
//private     ImageView look1 = (ImageView) findViewById(R.id.id_button);
//private     ImageView look2 = (ImageView) findViewById(R.id.id_Father);
//private     ImageView look3 = (ImageView) findViewById(R.id.id_Mother);
//private     ImageView look4 = (ImageView) findViewById(R.id.id_Brother);
//private     ImageView look5 = (ImageView) findViewById(R.id.id_Sister);
//private      ImageView look6 = (ImageView) findViewById(R.id.id_Son);
//private     ImageView look7 = (ImageView) findViewById(R.id.id_Daughter);
//private     ImageView look0 = (ImageView) findViewById(R.id.id_white);


    private void findView(ImageView I) {

          ivHead = I;
//        Bitmap bt = BitmapFactory.decodeFile(path +family+".png");// 从SD卡中找头像，转换成Bitmap
//        if(bt!=null){
            //@SuppressWarnings("deprecation")
//            Drawable drawable = new BitmapDrawable(bt);//转换成drawable
//            ivHead.setImageDrawable(drawable);
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
    private void setPicToView(Bitmap mBitmap,String family) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName =path +family+".png";//图片名字
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

                       head= RoundBitmap.toRoundBitmap(head);
                        setPicToView(head,myfamily);//保存在SD卡中
                        ivHead.setImageBitmap(head);//用ImageView显示出来
                    }
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    };


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
    public boolean isEquals(Bitmap b1,Bitmap b2){
        //先判断宽高是否一致，不一致直接返回false
        if(b1.getWidth()==b2.getWidth()
                &&b1.getHeight()==b2.getHeight()){
            int xCount = b1.getWidth();
            int yCount = b1.getHeight();
            for(int x=0; x<xCount; x++){
                for(int y=0; y<yCount; y++){
                    //比较每个像素点颜色
                    if(b1.getPixel(x, y)!=b2.getPixel(x, y)){
                        return false;
                    }
                }
            }
            return true;
        }else{
            return false;
        }
    }


//    private void blank(){
//        picture2=findViewById(R.id.id_Father).getDrawingCache();
//        picture3=findViewById(R.id.id_Mother).getDrawingCache();
//        picture4=findViewById(R.id.id_Brother).getDrawingCache();
//        picture5=findViewById(R.id.id_Sister).getDrawingCache();
//        picture6=findViewById(R.id.id_Son).getDrawingCache();
//        picture7=findViewById(R.id.id_Daughter).getDrawingCache();
//
//
//    }


    private void initView() {
        ImageView look1 = (ImageView) findViewById(R.id.id_button);
        ImageView look2 = (ImageView) findViewById(R.id.id_Father);
        ImageView look3 = (ImageView) findViewById(R.id.id_Mother);
        ImageView look4 = (ImageView) findViewById(R.id.id_Brother);
        ImageView look5 = (ImageView) findViewById(R.id.id_Sister);
        ImageView look6 = (ImageView) findViewById(R.id.id_SonOrDaughter);
        ImageView look7 = (ImageView) findViewById(R.id.id_Wife);
        ImageView look0 = (ImageView) findViewById(R.id.id_white);

        Bitmap  picture0= ((BitmapDrawable) ((ImageView) look0).getDrawable()).getBitmap();
        Bitmap  picture2= ((BitmapDrawable) ((ImageView) look2).getDrawable()).getBitmap();
        Bitmap  picture3= ((BitmapDrawable) ((ImageView) look3).getDrawable()).getBitmap();
        Bitmap  picture4=((BitmapDrawable) ((ImageView) look4).getDrawable()).getBitmap();
        Bitmap  picture5= ((BitmapDrawable) ((ImageView) look5).getDrawable()).getBitmap();
        Bitmap  picture6= ((BitmapDrawable) ((ImageView) look6).getDrawable()).getBitmap();
        Bitmap  picture7= ((BitmapDrawable) ((ImageView) look7).getDrawable()).getBitmap();




        Bitmap picture1 = BitmapFactory.decodeResource(getResources(),
                R.drawable.myp);
        picture1= RoundBitmap.toRoundBitmap(picture1);
        look1.setImageBitmap(picture1);

        if (isEquals(picture2,picture0)==false)
        {look2.setVisibility(View.VISIBLE);}
        else
        {look2.setVisibility(View.GONE);}
        if (isEquals(picture3,picture0)==false)
        {look3.setVisibility(View.VISIBLE);}
        else
        {look3.setVisibility(View.GONE);}
        if (isEquals(picture4,picture0) ==false )
        {look4.setVisibility(View.VISIBLE);}
        else
        {look4.setVisibility(View.GONE);}
        if (isEquals(picture5,picture0)==false )
        {look5.setVisibility(View.VISIBLE);}
        else
        {look5.setVisibility(View.GONE);}
        if (isEquals(picture6,picture0)==false )
        {look6.setVisibility(View.VISIBLE);}
        else
        {look6.setVisibility(View.GONE);}
        if (isEquals(picture7,picture0)==false )
        {look7.setVisibility(View.VISIBLE);}
        else
        {look7.setVisibility(View.GONE);}
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArcMenu view = (ArcMenu) findViewById(R.id.Arc);
        Button father=findViewById(R.id.father);
        Button mother=findViewById(R.id.mother);
        Button brother=findViewById(R.id.brother);
        Button sister=findViewById(R.id.sister);
        Button wife=findViewById(R.id.wife);
        Button sonOrdaughter=findViewById(R.id.sonOrdaughter);


         // ImageView look1 = (ImageView) findViewById(R.id.id_button);
           ImageView look2 = (ImageView) findViewById(R.id.id_Father);
           ImageView look3 = (ImageView) findViewById(R.id.id_Mother);
            ImageView look4 = (ImageView) findViewById(R.id.id_Brother);
            ImageView look5 = (ImageView) findViewById(R.id.id_Sister);
           ImageView look6 = (ImageView) findViewById(R.id.id_SonOrDaughter);
           ImageView look7 = (ImageView) findViewById(R.id.id_Wife);
         //  ImageView look0 = (ImageView) findViewById(R.id.id_white);

        //blank();
        Bitmap bt1 = BitmapFactory.decodeFile(path +"father.png");// 从SD卡中找头像，转换成Bitmap
        Bitmap bt2 = BitmapFactory.decodeFile(path +"mother.png");
        Bitmap bt3 = BitmapFactory.decodeFile(path +"brother.png");
        Bitmap bt4 = BitmapFactory.decodeFile(path +"sister.png");
        Bitmap bt5 = BitmapFactory.decodeFile(path +"sonOrdaughter.png");
        Bitmap bt6 = BitmapFactory.decodeFile(path +"wife.png");

        if(bt1!=null){
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt1);//转换成drawable
            look2.setImageDrawable(drawable);
        }
        if(bt2!=null){
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt2);//转换成drawable
            look3.setImageDrawable(drawable);
        }
        if(bt3!=null){
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt3);//转换成drawable
            look4.setImageDrawable(drawable);
        }
        if(bt4!=null){
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt4);//转换成drawable
            look5.setImageDrawable(drawable);
        }
        if(bt5!=null){
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt5);//转换成drawable
            look6.setImageDrawable(drawable);
        }
        if(bt6!=null){
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt6);//转换成drawable
            look7.setImageDrawable(drawable);
        }
   initView();


        ImageView look0=(ImageView) findViewById(R.id.id_white);
        look0.setVisibility(View.GONE);

        final Button change=(Button) findViewById(R.id.design);
        change.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                    initView();


            }
        });

       father.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this, father.class);
               startActivity(intent);
           }
       });
        mother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, mother.class);
                startActivity(intent);
            }
        });
        brother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, brother.class);
                startActivity(intent);
            }
        });
        sister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, sister.class);
                startActivity(intent);
            }
        });
        sonOrdaughter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, sonOrdaughter.class);
                startActivity(intent);
            }
        });
        wife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, wife.class);
                startActivity(intent);
            }
        });





        view.setOnMenuItemClickListener(new ArcMenu.OnMenuItemClickListener() {

            @Override
            public void onClick(View view, int pos) {
                if(pos==2){
                    String tag = (String) view.getTag();
                    Toast.makeText(MainActivity.this,"这是你的"+tag, Toast.LENGTH_SHORT).show();

                }



                //String tag = (String) view.getTag();
                //Toast.makeText(MainActivity.this, tag, Toast.LENGTH_SHORT).show();
//                if(pos==1){
//                    Intent intent = new Intent(MainActivity.this, mother.class);
//                    startActivity(intent);
//
//                }
            }

            @Override
            public boolean OnLongClickListener(View view, int pos) {
//                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
//                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                startActivityForResult(intent1, 1);
                if(pos==1){
                    ImageView A=findViewById(R.id.id_Father);
                    myfamily="father";
                    findView(A);

                }
                if(pos==2){
                    ImageView A=findViewById(R.id.id_Mother);
                    myfamily="mother";
                    findView(A);

                }
                if(pos==3){
                    ImageView A=findViewById(R.id.id_Brother);
                    myfamily="brother";
                    findView(A);

                }
                if(pos==4){
                    ImageView A=findViewById(R.id.id_Sister);
                    myfamily="sister";
                    findView(A);

                }
                if(pos==5){
                    ImageView A=findViewById(R.id.id_SonOrDaughter);
                    myfamily="sonOrdaughter";
                    findView(A);

                }
                if(pos==6){
                    ImageView A=findViewById(R.id.id_Wife);
                    myfamily="wife";
                    findView(A);

                }

                return false;
            }
        });
    }


}