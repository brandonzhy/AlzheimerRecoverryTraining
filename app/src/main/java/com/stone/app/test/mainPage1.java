package com.stone.app.test;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.photoUpload.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class mainPage1 extends Activity {
    private int Mode = PictureMimeType.ofAll();
    private String memberID;
    private final static String TAG = mainPage1.class.getSimpleName();
    private List<LocalMedia> selectList = new ArrayList<>();
    private RecyclerView modifyportrait_recycler;
    private myGridImageAdapter adapter;
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
    ImageView iv_test;

    private myGridImageAdapter.onAddPicClickListener onAddPicClickListener = new myGridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            Log.i("TAG", "加号按钮被点击");
            PictureSelector.create(mainPage1.this)
                    .openGallery(Mode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
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
                    //                        //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
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

    //    String imagepath=
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page1);
        
       
        modifyportrait_recycler = findViewById(R.id.modifyportrait_recycler);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(mainPage1.this, 4, GridLayoutManager.VERTICAL, false);
        modifyportrait_recycler.setLayoutManager(manager);
        adapter = new myGridImageAdapter(mainPage1.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        modifyportrait_recycler.setAdapter(adapter);
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(mainPage1.this);
                } else {
                    Toast.makeText(mainPage1.this,
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

        //        Intent intent=getIntent();
        //        memberID=intent.getStringExtra("memberID");
        //        Log.i("TAG","mainPage 通过intent获得得到ID为： " +memberID );
    }
    //   public boolean onCreateOptionsMenu(Menu menu){
    //       getMenuInflater().inflate(R.menu.toolbar_main,menu);
    //       return true;
    //
    //   }
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
                    DebugUtil.i(TAG, "onActivityResult:" + selectList.size());
                    Log.i("TAG", "图片选择完成");
                    break;
            }
        }
    }
    private long mypoe(int number, int i1) {
        for (int i = 0; i < i1; i++) {
            number = number * number;
        }
        return number;
    }
}
   