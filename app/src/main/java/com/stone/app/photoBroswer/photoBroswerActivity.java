package com.stone.app.photoBroswer;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stone.app.R;
import com.stone.app.library.CardAdapter;
import com.stone.app.library.CardSlidePanel;
import com.stone.app.mainPage.MyInformation;

import java.util.ArrayList;
import java.util.List;


public class photoBroswerActivity extends FragmentActivity implements View.OnClickListener {

    private CardSlidePanel.CardSwitchListener cardSwitchListener;
    //    private String imagePaths[];
    private String imagePaths[] = {"file:///android_asset/wall01.jpg",
            "file:///android_asset/wall02.jpg", "file:///android_asset/wall03.jpg",
            "file:///android_asset/wall04.jpg", "file:///android_asset/wall05.jpg",
            "file:///android_asset/wall06.jpg", "file:///android_asset/wall07.jpg",
            "file:///android_asset/wall08.jpg", "file:///android_asset/wall09.jpg",
            "file:///android_asset/wall10.jpg", "file:///android_asset/wall11.jpg",
            "file:///android_asset/wall12.jpg"}; // 12个图片资源
    //    private String names[];
    //    private String imageplaces[];
    private String names[] = {"郭富城", "刘德华", "张学友", "李连杰", "成龙", "谢霆锋", "李易峰",
            "霍建华", "胡歌", "曾志伟", "吴彦祖", "梁朝伟"}; // 12个人名
    //    private DataBaseManager dataBaseManager;
    private String imageplaces[] = {"上海", "南京", "北京", "杭州", "温州", "哈尔滨", "广州", "武汉", "云南", "香港", "四川", "新疆"};
    private int circulatetimes;
    private ImageView img_back;
    private int motionType;
    private List<CardDataItem> dataList = new ArrayList<>();
    //    private List<PictureData> pictlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示图片的时候不要标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_photobrowser);
        //        initdData();
        initView();

    }
    //数据库初始化
    //    private void initdData() {
    //        Intent intent = getIntent();
    //        dataBaseManager = new DataBaseManager();
    //        pictlist = dataBaseManager.getPictureList("", "",intent.getStringExtra("memberID"), "", "");
    //        int i = 0;
    //        for (PictureData data : pictlist) {
    //
    //            imagePaths[i] = data.getImagePath();
    //            names[i] = data.getName();
    //            imageplaces[i] = data.getLocation();
    //            i++;
    //        }
    //    }

    private void initView() {
        final CardSlidePanel slidePanel = (CardSlidePanel) findViewById(R.id.image_slide_panel);
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(photoBroswerActivity.this);
        circulatetimes = 1;
        // 1. 左右滑动监听
        cardSwitchListener = new CardSlidePanel.CardSwitchListener() {

            @Override
            public void onShow(int index) {
                Log.d("Card", "正在显示-" + dataList.get(index).userName);
                if (index == (imagePaths.length - 1) * circulatetimes) {
                    appendDataList();
                    //4. 数据更新<
                    slidePanel.getAdapter().notifyDataSetChanged();
                    circulatetimes++;
                }
            }

            @Override
            public void onCardVanish(int index, int type) {
                Log.d("Card", "正在消失-" + dataList.get(index).userName + " 消失type=" + type);
                motionType = type;
            }
        };
        //        slidePanel.vanishOnBtnClick(VANISH_TYPE_LEFT);      按钮的监听
        slidePanel.setCardSwitchListener(cardSwitchListener);
        slidePanel.setLongClickable(true);
        //        slidePanel.setOnTouchListener(photoBroswerActivity.this);

        // 2. 绑定Adapter
        prepareDataList();

        slidePanel.setAdapter(new CardAdapter() {
            @Override
            public int getLayoutId() {
                return R.layout.card_item;
            }

            @Override
            public int getCount() {
                return dataList.size();
            }

            @Override
            public void bindView(View view, int index) {
                Object tag = view.getTag();
                final ViewHolder viewHolder;
                if (null != tag) {
                    viewHolder = (ViewHolder) tag;
                } else {
                    viewHolder = new ViewHolder(view);
                    view.setTag(viewHolder);
                }

                //                final int finalIndex = index;
                //                new Handler().postDelayed(new Runnable(){
                //                    public void run() {
                //                        //execute the task
                //
                //                    }
                //                }, 500);
                viewHolder.bindData(dataList.get(index));

            }

            @Override
            public Rect obtainDraggableArea(View view) {
                // 可滑动区域定制，该函数只会调用一次
                View contentView = view.findViewById(R.id.card_item_content);
                View topLayout = view.findViewById(R.id.card_top_layout);
                View bottomLayout = view.findViewById(R.id.card_bottom_layout);
                int left = view.getLeft() + contentView.getPaddingLeft() + topLayout.getPaddingLeft();
                int right = view.getRight() - contentView.getPaddingRight() - topLayout.getPaddingRight();
                int top = view.getTop() + contentView.getPaddingTop() + topLayout.getPaddingTop();
                int bottom = view.getBottom() - contentView.getPaddingBottom() - bottomLayout.getPaddingBottom();
                Log.i("TAG", "bottom = " + bottom + "view of bottom =" + view.getBottom());
                return new Rect(left, top, right, bottom);
            }
        });


        // 3. notifyDataSetChanged调用
        //        findViewById(R.id.notify_change).setOnClickListener(new View.OnClickListener() {
        //        notify_change.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                appendDataList();
        //                //4. 数据更新<
        //                slidePanel.getAdapter().notifyDataSetChanged();
        //            }
        //        });
    }

    private void prepareDataList() {
        //        int num = imagePaths.length;
        int num = imagePaths.length;

        for (int i = 0; i < num; i++) {
            CardDataItem dataItem = new CardDataItem();
            dataItem.userName = names[i];
            dataItem.imagePath = imagePaths[i];
            dataItem.imagePlace = imageplaces[i];
            dataItem.likeNum = (int) (Math.random() * 10);
            dataItem.imageNum = (int) (Math.random() * 6);
            dataList.add(dataItem);
        }
    }

    private void appendDataList() {
        //        for (int i = 0; i < 6; i++) {
        //            CardDataItem dataItem = new CardDataItem();
        //            dataItem.userName = "From Append";
        //            dataItem.imagePath = imagePaths[8];
        //            dataItem.likeNum = (int) (Math.random() * 10);
        //            dataItem.imageNum = (int) (Math.random() * 6);
        //            dataList.add(dataItem);
        //        }
        int num = imagePaths.length;
        for (int i = 0; i < num; i++) {
            CardDataItem dataItem = new CardDataItem();
            dataItem.userName = names[i];
            dataItem.imagePath = imagePaths[i];
            dataItem.imagePlace = imageplaces[i];
            dataItem.likeNum = (int) (Math.random() * 10);
            dataItem.imageNum = (int) (Math.random() * 6);
            dataList.add(dataItem);
        }
    }

    //    @Override
    //    public boolean onTouch(View view, MotionEvent motionEvent) {
    //        return false;
    //    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                //gotomypage()   返回我的界面
                startActivity(new Intent(photoBroswerActivity.this,MyInformation.class));
                finish();
                break;
        }
    }

    class ViewHolder {
        ImageView imageView;
        View maskView;
        TextView userNameTv;
        //        TextView imageNumTv;
        TextView likeNumTv;
        TextView tv_imageplace;

        public ViewHolder(View view) {
            maskView = view.findViewById(R.id.maskView);
            tv_imageplace = (TextView) view.findViewById(R.id.tv_imageplace);
            imageView = (ImageView) view.findViewById(R.id.card_image_view);
            userNameTv = (TextView) view.findViewById(R.id.card_user_name);
            //            imageNumTv = (TextView) view.findViewById(R.id.card_pic_num);
            //            likeNumTv = (TextView) view.findViewById(R.id.card_like);
        }

        public void bindData(CardDataItem itemData) {
            Glide.with(photoBroswerActivity.this).load(itemData.imagePath).into(imageView);
            userNameTv.setText("姓名：" + itemData.userName);
            //            imageNumTv.setText(itemData.imageNum + "");
            //            likeNumTv.setText(itemData.likeNum + "");
            tv_imageplace.setText("拍摄地：" + itemData.imagePlace + "");
        }
    }

}
