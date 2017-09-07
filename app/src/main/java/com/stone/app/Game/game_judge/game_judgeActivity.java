package com.stone.app.Game.game_judge;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stone.app.R;
import com.stone.app.dataBase.DataBaseError;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.PictureData;
import com.stone.app.library.CardAdapter;
import com.stone.app.library.CardSlidePanel;
import com.stone.app.photoBroswer.CardDataItem;

import java.util.ArrayList;
import java.util.List;

import static com.stone.app.Util.staticConstUtil.DEFUATNUMBER;
import static com.stone.app.library.CardSlidePanel.VANISH_TYPE_LEFT;
import static com.stone.app.library.CardSlidePanel.VANISH_TYPE_RIGHT;


public class game_judgeActivity extends FragmentActivity implements View.OnClickListener {

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
    private DataBaseManager dataBaseManager;
    private String imageplaces[] = {"上海", "南京", "北京", "杭州", "温州", "哈尔滨", "广州", "武汉", "云南", "香港", "四川", "新疆"};
    private String imagetimes[];
    private int circulatetimes;
    private ImageView img_back;
    private int questionLocation;
    private RadioGroup radiogroup;
    private TextView tv_question;
    private List<CardDataItem> dataList = new ArrayList<>();
    private int correctnum;
    private int lenth;
    private boolean qflag;
    String memberID;
    String memberName;
    private List<PictureData> pictlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示图片的时候不要标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choice_game);
        Intent intent = getIntent();
        memberID = intent.getStringExtra("memberID");
        memberName = intent.getStringExtra("memberName");
        //        initdData();
        initView();
        qflag = getQuestion(questionLocation, tv_question);
    }

    //    数据库初始化
    private void initdData() {
        //            Intent intent = getIntent();
        dataBaseManager = new DataBaseManager();
        try {
            pictlist = dataBaseManager.getRandomPicturesFromMember(memberID, memberName, "", 0, 0, DEFUATNUMBER);
        } catch (DataBaseError dataBaseError) {
            if (dataBaseError.getErrorType() == DataBaseError.ErrorType.RequiredImageNotEnough) {
                try {
                    pictlist = dataBaseManager.getRandomPicturesFromMember(memberID, memberName, "", 0, 0, -1);
                } catch (DataBaseError dataBaseError1) {
                    dataBaseError1.printStackTrace();
                }
            }
        }
        int i = 0;
        for (PictureData data : pictlist) {
            imagePaths[i] = data.getImagePath();
            names[i] = data.getName();
            imageplaces[i] = data.getLocation();
            imagetimes[i] = String.valueOf(data.getDate());
            i++;
        }
    }

    private void initMainView() {
        img_back = findViewById(R.id.img_back);
        tv_question = findViewById(R.id.tv_question);
        questionLocation = 0;
        correctnum = 0;
        lenth = imagePaths.length;

    }

    private void initView() {
        final CardSlidePanel slidePanel = (CardSlidePanel) findViewById(R.id.image_slide_panel);
        initMainView();
        circulatetimes = 1;
        // 1. 左右滑动监听


        cardSwitchListener = new CardSlidePanel.CardSwitchListener() {

            @Override
            public void onShow(int index) {
                Log.d("Card", "正在显示-" + dataList.get(index).userName);
            }

            @Override
            public void onCardVanish(int index, int type) {
                Log.d("Card", "正在消失-" + dataList.get(index).userName + " 消失type=" + type);
                if (type == VANISH_TYPE_LEFT) {
                    if (qflag == true) {
                        correctnum++;
                    } else {
                        correctnum--;
                    }
                } else if (type == VANISH_TYPE_RIGHT) {
                    if (qflag == false) {
                        correctnum++;
                    } else {
                        correctnum--;
                    }
                }
                Log.i("TAG", "correctnum= " + correctnum);
                questionLocation++;
                if (questionLocation == lenth) {
                    float result = ((float) correctnum / lenth) * 100;
                    Log.i("TAG", "result=" + result);
                    Intent intent_result = new Intent(game_judgeActivity.this, showaResultiwthDialog.class);
                    intent_result.putExtra("result", result);
                    intent_result.putExtra("memberID", memberID);
                    startActivity(intent_result);
                    finish();
                }

                qflag = getQuestion(questionLocation, tv_question);

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
    }

    private boolean getQuestion(int Location, TextView textView) {
        int typeNumber = (int) (Math.random() * 3);
        int questionLocation = (int) (Math.random() * lenth);
        boolean flag = false;
        //0代表名字，1代表地点
        if (typeNumber == 0) {
            textView.setText("图片中的人的名字是叫" + names[questionLocation] + "吗？\n" + "左滑是右滑不是");
        } else if (typeNumber == 1) {
            textView.setText("图片拍摄的地点是在" + imageplaces[questionLocation] + "吗？\n" + "左滑是右滑不是");

        } else if (typeNumber == 2) {
            String photoDate = imagetimes[questionLocation];

            if (photoDate.charAt(4) == '0') {

                if (photoDate.charAt(6) == '0') {
                    textView.setText("图片拍摄的时间是在" + photoDate.substring(0, 4) + "年" + photoDate.substring(5, 6) + "月" + photoDate.substring(7) + "日吗？\n" + "左滑是右滑不是");
                } else {
                    textView.setText("图片拍摄的时间是在" + photoDate.substring(0, 4) + "年" + photoDate.substring(5, 6) + "月" + photoDate.substring(6) + "日吗？\n" + "左滑是右滑不是");
                }
            } else {
                if (photoDate.charAt(6) == '0') {
                    textView.setText("图片拍摄的时间是在" + photoDate.substring(0, 4) + "年" + photoDate.substring(4, 6) + "月" + photoDate.substring(7) + "日吗？\n" + "左滑是右滑不是");
                }
                textView.setText("图片拍摄的时间是在" + photoDate.substring(0, 4) + "年" + photoDate.substring(4, 6) + "月" + photoDate.substring(6) + "日吗？\n" + "左滑是右滑不是");
            }
            //            textView.setText("图片拍摄的时间是在" + imagetimes[questionLocation] + "吗？\n" + "左滑是右滑不是");
        }
        if (questionLocation == Location) {
            flag = true;
        }
        return flag;
    }


    private void prepareDataList() {
        //        int num = lenth;
        int num = lenth;

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                //gotomypage()   返回我的界面
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
            userNameTv.setVisibility(View.GONE);
            tv_imageplace.setVisibility(View.GONE);
            //            imageNumTv = (TextView) view.findViewById(R.id.card_pic_num);
            //            likeNumTv = (TextView) view.findViewById(R.id.card_like);
        }

        public void bindData(CardDataItem itemData) {
            Glide.with(game_judgeActivity.this).load(itemData.imagePath).into(imageView);
            //            userNameTv.setText("姓名："+itemData.userName);
            //            imageNumTv.setText(itemData.imageNum + "");
            //            likeNumTv.setText(itemData.likeNum + "");
            //            tv_imageplace.setText("拍摄地："+itemData.imagePlace + "");
        }
    }

}
