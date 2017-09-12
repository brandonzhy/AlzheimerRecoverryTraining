package com.stone.app.photoUpload;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.DebugUtil;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.stone.app.R;
import com.stone.app.Util.DateUtil;
import com.stone.app.Util.getDataUtil;
import com.stone.app.dataBase.DataBaseError;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.DataBaseSignal;
import com.stone.app.dataBase.RealmDB;
import com.stone.app.photoUpload.adapter.GridImageAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.stone.app.dataBase.DataBaseError.ErrorType.MemberNotExist;
import static com.stone.app.dataBase.DataBaseError.ErrorType.UnknownError_AddImage;


public class photoUploadActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = photoUploadActivity.class.getSimpleName();
    private List<LocalMedia> selectList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private static int maxSelectNum = 1;
    private EditText et_photoinfo_name, et_photoinfo_date_year, et_photoinfo_date_month, et_photoinfo_date_day, et_photoinfo_place;
    private int x = 0, y = 0;
    private int aspect_ratio_x, aspect_ratio_y;
    private int compressMode = PictureConfig.SYSTEM_COMPRESS_MODE;
    private int themeId;
    private int chooseMode = PictureMimeType.ofAll();
    private Button btn_right_upload;
    private ImageView left_back;
    private DataBaseManager dataBaseManager;
    private LinearLayout ly_date;
    private String mypicname=getDataUtil.setPicName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoupload);
        init();
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(photoUploadActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(photoUploadActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(photoUploadActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(photoUploadActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });

        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(photoUploadActivity.this);
                } else {
                    Toast.makeText(photoUploadActivity.this,
                            getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });

    }

    private void init() {
        themeId = R.style.picture_default_style;
        left_back = (ImageView) findViewById(R.id.left_back);
        et_photoinfo_name = (EditText) findViewById(R.id.et_photoinfo_name);
        et_photoinfo_place = (EditText) findViewById(R.id.et_photoinfo_place);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        et_photoinfo_date_year = (EditText) findViewById(R.id.et_photoinfo_data_year);
        et_photoinfo_date_month = (EditText) findViewById(R.id.et_photoinfo_data_month);
        et_photoinfo_date_day = (EditText) findViewById(R.id.et_photoinfo_data_day);
        ly_date = (LinearLayout) findViewById(R.id.ly_data);
        left_back.setOnClickListener(photoUploadActivity.this);
//        dataBaseManager = new DataBaseManager();
                dataBaseManager= RealmDB.getDataBaseManager();
        FullyGridLayoutManager manager = new FullyGridLayoutManager(photoUploadActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(photoUploadActivity.this, onAddPicClickListener);
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            Log.i("TAG", "加号按钮被点击");
            PictureSelector.create(photoUploadActivity.this)
                    .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(R.style.picture_green_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    //                        .selectionMode(cb_choose_mode.isChecked() ?
                    //                                PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选
                    //                        .previewImage()// 是否可预览图片  true/false
                    //                        .previewVideo()// 是否可预览视频
                    //                        .enablePreviewAudio() // 是否可播放音频
                    //                        .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                    //                        .isCamera()// 是否显示拍照按钮  true/false  默认为true
                    //                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .setOutputCameraPath("/storage/youAreMyAlways/mypicname")// 自定义拍照保存路径
                    .enableCrop(true)// 是否裁剪
                    //                        .compress()// 是否压缩
                    .compressMode(compressMode)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .glideOverride(150, 150)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    //                        .hideBottomControls()// 是否显示uCrop工具栏，默认不显示
                    //                        .isGif()// 是否显示gif图片
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
//                                          .circleDimmedLayer(true)// 是否圆形裁剪
                    .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                                   .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    //                        .openClickSound()// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
                    //                        .videoMaxSecond(15)
                    //                        .videoMinSecond(10)
                    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                    //.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                    //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled() // 裁剪是否可旋转图片
                    //.scaleEnabled()// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()//显示多少秒以内的视频or音频也可适用
                    //.recordVideoSecond()//录制视频秒数 默认60s
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);

                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    btn_right_upload = (Button) findViewById(R.id.btn_right_upload);
                    btn_right_upload.setOnClickListener(photoUploadActivity.this);
                    btn_right_upload.setVisibility(View.VISIBLE);
                    et_photoinfo_place.setVisibility(View.VISIBLE);
                    et_photoinfo_name.setVisibility(View.VISIBLE);
                    ly_date.setVisibility(View.VISIBLE);
                    DebugUtil.i(TAG, "onActivityResult:" + selectList.size());
                    Log.i("TAG", "图片选择完成");
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_back:
                Log.i("TAG", "back 图标被选中");
                finish();
                break;
            case R.id.btn_right_upload:
                //上传按钮点击后触发事件
                Log.i("TAG", "上传 图标被选中");
                Intent intent = getIntent();
                //                String memberID = intent.getStringExtra("memberID");
                String memberID = getDataUtil.getmemberID(photoUploadActivity.this);
                Log.i("TAG", "photoupload的memberID" + memberID);
                try {
                    //                    dataBaseManager.AddImage("memberID", et_photoinfo_name.getText().toString()
                    //                            , adapter.getImagePath(), date, "", "");
                    long mydata = 0;
                    String name = "";
                    String plece = "";
                    String  month="";
                    String day="";
                    String year="";
                    if ((!TextUtils.isEmpty(et_photoinfo_name.getText().toString()))) {
                        name = et_photoinfo_name.getText().toString();

                    }
                    if ((!TextUtils.isEmpty(et_photoinfo_place.getText().toString()))) {

                        plece = et_photoinfo_place.getText().toString();

                    }
                    if ((!TextUtils.isEmpty(et_photoinfo_date_year.getText().toString()))) {

                        year = et_photoinfo_date_year.getText().toString();
                        //小于4位默认是今年
                        if(year.length()<4){
                            year=String.valueOf(DateUtil.getyear());
                        }

                    }
                    if(!TextUtils.isEmpty(et_photoinfo_date_month.getText().toString().trim())){
                        if(Long.parseLong(et_photoinfo_date_month.getText().toString().trim())<10){
                            month="0"+et_photoinfo_date_month.getText().toString().trim();
                        }
                    }else {
                        month="00";
                    }
                    Log.i("TAG","month= " +month );
                    if((!TextUtils.isEmpty(et_photoinfo_date_day.getText().toString().trim()))){
                        if(Long.parseLong(et_photoinfo_date_day.getText().toString().trim())<10){
                            day="0"+et_photoinfo_date_day.getText().toString().trim();
                        }
                    }else {
                        day = "00";
                    }

                    String date = year + month + day;
                    if ((!TextUtils.isEmpty(date))) {
//                        mydata = Long.parseLong(date) * mypoe(10, 2);
                        mydata = Long.parseLong(date) ;
                        Log.i("TAG","现在的时间为" + DateUtil.getTime());
                        Log.i("TAG","上传的时间为" +mydata );
//                        ToastUtil.showToast(photoUploadActivity.this, adapter.getImagePath());

                    }
//                    dataBaseManager.AddImage(memberID, name,plece , adapter.getImagePath(), mydata, "", "");
                    if(selectList.get(0).isCut()){
                        dataBaseManager.AddImage(memberID, name,plece , selectList.get(0).getCutPath(), mydata, "", "");
                        Log.i("TAG","图片的cutpath为" + selectList.get(0).getPath());
                    }else {
                        Log.i("TAG","图片没有的path为" + selectList.get(0).getPath());

                        dataBaseManager.AddImage(memberID, name,plece , selectList.get(0).getPath(), mydata, "", "");
                    }
                    //                    if(!TextUtils.isEmpty(et_photoinfo_name.getText().toString())){
                    //                      if(!TextUtils.isEmpty(et_photoinfo_place.getText().toString())){
                    //                          if((!TextUtils.isEmpty(date))){
                    //                              dataBaseManager.AddImage(memberID, et_photoinfo_name.getText().toString()
                    //                                      , et_photoinfo_place.getText().toString(), adapter.getImagePath(),
                    //                                      //                             DateUtil.getDate(), "", "");
                    //                                      mydata   , "", "");
                    //
                    //                          }else {
                    //                              ToastUtil.showToast(photoUploadActivity.this,"请输入完整日期");
                    //
                    //                          }
                    //                      }
                    //                    }
                    //                    dataBaseManager.AddImage("", et_photoinfo_name.getText().toString()

                } catch (DataBaseSignal dataBaseSignal) {
                    dataBaseSignal.printStackTrace();
                    if (dataBaseSignal.getSignalType() == DataBaseSignal.SignalType.ImageAddedAlready) {
                        //                        Toast.makeText(photoUploadActivity.this, "照片已存在", Toast.LENGTH_SHORT).show();
                        Toast.makeText(photoUploadActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (DataBaseError dataBaseError) {
                    dataBaseError.printStackTrace();
                    if (dataBaseError.getErrorType() == UnknownError_AddImage) {

                        Toast.makeText(photoUploadActivity.this, "上传失败，重新上传", Toast.LENGTH_SHORT).show();
                    } else if (dataBaseError.getErrorType() == MemberNotExist) {
                        Toast.makeText(photoUploadActivity.this, "成员不存在啊", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
        }
    }

    private long mypoe(int number, int i1) {
        for (int i = 0; i < i1; i++) {
            number = number * number;
        }
        return number;
    }


}
