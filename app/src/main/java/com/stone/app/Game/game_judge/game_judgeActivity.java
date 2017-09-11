package com.stone.app.Game.game_judge;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.stone.app.Util.ToastUtil;
import com.stone.app.Util.getDataUtil;
import com.stone.app.dataBase.DataBaseError;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.MemberData;
import com.stone.app.dataBase.PictureData;
import com.stone.app.dataBase.RealmDB;
import com.stone.app.library.CardAdapter;
import com.stone.app.library.CardSlidePanel;
import com.stone.app.photoBroswer.CardDataItem;
import com.stone.app.style_young.mainpageYoung;

import java.util.ArrayList;
import java.util.List;

import static com.stone.app.Util.staticConstUtil.DEFUATNUMBER;
import static com.stone.app.dataBase.DataBaseError.ErrorType.RequiredResultsReturnNULL;
import static com.stone.app.library.CardSlidePanel.VANISH_TYPE_LEFT;
import static com.stone.app.library.CardSlidePanel.VANISH_TYPE_RIGHT;


public class game_judgeActivity extends FragmentActivity implements View.OnClickListener {

    private CardSlidePanel.CardSwitchListener cardSwitchListener;
    //      private String imagePaths[]={};
    //    private String imagePaths[] = {"file:///android_asset/wall01.jpg",
    //            "file:///android_asset/wall02.jpg", "file:///android_asset/wall03.jpg",
    //            "file:///android_asset/wall04.jpg", "file:///android_asset/wall05.jpg",
    //            "file:///android_asset/wall06.jpg", "file:///android_asset/wall07.jpg",
    //            "file:///android_asset/wall08.jpg", "file:///android_asset/wall09.jpg",
    //            "file:///android_asset/wall10.jpg", "file:///android_asset/wall11.jpg",
    //            "file:///android_asset/wall12.jpg"}; // 12个图片资源
    private List<String> names = new ArrayList<>(10);
    private List<String> imageplaces = new ArrayList<>(10);
    ;
    private List<String> imagetimes = new ArrayList<>(10);
    ;
    private List<String> imagePaths = new ArrayList<>(10);
    ;

    //    private String names[] = {"郭富城", "刘德华", "张学友", "李连杰", "成龙", "谢霆锋", "李易峰",
    //            "霍建华", "胡歌", "曾志伟", "吴彦祖", "梁朝伟"}; // 12个人名
    private DataBaseManager dataBaseManager;
    //    private String imageplaces[] = {"上海", "南京", "北京", "杭州", "温州", "哈尔滨", "广州", "武汉", "云南", "香港", "四川", "新疆"};
    //    private String imagetimes[]={};
    private int circulatetimes;
    private ImageView img_back;
    private int questionLocation;
    private RadioGroup radiogroup;
    private TextView tv_question;
    private List<CardDataItem> dataList = new ArrayList<>();
    private int correctnum;
    private int lenth = 0;
    private boolean qflag;
    String memberID;
    String memberName;
    private List<PictureData> pictlist = null;
    private String familyID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示图片的时候不要标题栏
        Intent intent = getIntent();
        memberID = intent.getStringExtra("memberID");
        memberName = intent.getStringExtra("memberName");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choice_game);
        initMainView();
        initdData();

        //        initView();
    }

    //    数据库初始化
    private void initdData() {
        //            Intent intent = getIntent();
        //        dataBaseManager = new DataBaseManager();
        dataBaseManager = RealmDB.getDataBaseManager();
        memberID = getDataUtil.getmemberID(game_judgeActivity.this);
        Log.i("TAG", "photoBrowser获得的memberID：" + memberID);
        try {
            List<MemberData> memberList = dataBaseManager.getMemberList(memberID, "", "", "");
            if (memberList.size() > 0) {
                familyID = memberList.get(0).getFamilyID();
                if (familyID.equals("")) {
                    Log.i("TAG", "你现在还没有家庭呢，赶紧创建一个或加入一个家庭吧");
                    ToastUtil.showToast(game_judgeActivity.this, "你现在还没有家庭呢，赶紧创建一个或加入一个家庭吧");
                    finish();
                }
                memberName = memberList.get(0).getName();
                Log.i("TAG", "game_judgeActivity获得的familyID为：" + familyID);
                Log.i("TAG", "game_judgeActivity获得的name为：" + memberName);
            } else {
                Log.i("TAG", "memberIDb不存在");
                ToastUtil.showToast(game_judgeActivity.this, "memberIDb不存在");
                finish();
            }
        } catch (DataBaseError dataBaseError) {
            Log.i("TAG", "dataBaseError 的类型为： " + dataBaseError);
            ToastUtil.showToast(game_judgeActivity.this, " judgegame的dataBaseError 的类型为： " + dataBaseError);
            dataBaseError.printStackTrace();
        }
        try {
            //            pictlist = dataBaseManager.getRandomPicturesFromMember(memberID, memberName, "", 0, 0, DEFUATNUMBER);
            pictlist = dataBaseManager.getRandomPicturesFromFamily(familyID, memberName, "", 0, 0, DEFUATNUMBER);
            if (pictlist != null  ) {
                lenth = pictlist.size();
                Log.i("TAG", "lenth在第127行获得");
                listToArray();
                qflag = getQuestion(questionLocation, tv_question);
                initView();
            } else {
                Log.i("TAG", "获取列表不存在");
                finish();
            }
        } catch (DataBaseError dataBaseError) {
            if (dataBaseError.getErrorType() == DataBaseError.ErrorType.RequiredImageNotEnough) {
                try {
                    if (!familyID.equals("")) {
                        pictlist = dataBaseManager.getRandomPicturesFromMember(familyID, memberName, "", 0, 0, -1);
//                        if (pictlist != null || pictlist.size() != 0) {
//                        if (pictlist != null  ) {
//                            lenth = pictlist.size();
//                            Log.i("TAG", "lenth在第142行获得");
//                            listToArray();
//                            qflag = getQuestion(questionLocation, tv_question);
//                            initView();
//                        } else {
//                            Log.i("TAG", "获取列表不存在,下面要finish啦");
//                            finish();
//                            onDestroy();
//                        }
                    } else {
                        Log.i("TAG", "你现在还没有家庭呢，赶紧创建一个或加入一个家庭吧");
                        ToastUtil.showToast(game_judgeActivity.this, "你现在还没有家庭呢，赶紧创建一个或加入一个家庭吧");
                        finish();
                    }

                    //                    if (pictlist != null) {
                    //                        lenth = pictlist.size();
                    //
                    //                    } else {
                    //                        Log.i("TAG", "获取列表不存在");
                    //                        finish();
                    //                    }

                } catch (DataBaseError dataBaseError1) {
                    if (dataBaseError1.getErrorType() == RequiredResultsReturnNULL) {
                        Log.i("TAG", "获取全部图片的错误类型为" + dataBaseError1.getErrorType());
                        finish();
                    }
                    Log.i("TAG", "dataBaseError 的类型为： " + dataBaseError);
                    dataBaseError1.printStackTrace();
                }
                if ((pictlist != null) ) {
                    lenth = pictlist.size();
                    Log.i("TAG", "lenth在第174行获得");
                    listToArray();
                    qflag = getQuestion(questionLocation, tv_question);
                    initView();
                } else {
                    Log.i("TAG", "获取全部列表不存在");
                    //                    ToastUtil.showToast(game_judgeActivity.this,"家庭里还没有人传你的图片呢");
                    AlertDialog.Builder builder = new AlertDialog.Builder(game_judgeActivity.this);
                    builder.setTitle("家庭信息");
                    builder.setCancelable(false);
                    builder.setMessage("啊偶，家庭里还没有人传你的图片呢");
                    builder.setPositiveButton("好的，知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(game_judgeActivity.this, mainpageYoung.class));
                        }
                    });
                    builder.show();
                    //                    finish();
                }
            }
        }

        //        if (pictlist != null) {
        //            //            int i = 0;
        //            for (int i = 0; i < pictlist.size(); i++) {
        //                imagePaths.add(pictlist.get(i).getImagePath());
        //                names.add(pictlist.get(i).getName());
        //                imageplaces.add(pictlist.get(i).getLocation());
        //                imagetimes.add(String.valueOf(pictlist.get(i).getDate()));
        //            }
        //        }
        //            //            for (PictureData data : pictlist) {
        //            //
        //            //                i++;
        //            //            }
        //        }

    }

    private void initMainView() {
        img_back = findViewById(R.id.img_gamejudgeback);
        tv_question = findViewById(R.id.tv_question);
        questionLocation = 0;
        correctnum = 0;
        img_back.setOnClickListener(game_judgeActivity.this);

    }

    private void initView() {
        final CardSlidePanel slidePanel = (CardSlidePanel) findViewById(R.id.image_slide_panel);

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
                    }
                        // else {
////                        correctnum--;
//                    }
                } else if (type == VANISH_TYPE_RIGHT) {
                    if (qflag == false) {
                        correctnum++;
                    }
                        // else {
////                        correctnum--;
//                    }
                }
                Log.i("TAG", "correctnum= " + correctnum);
                questionLocation++;
                if (questionLocation == lenth) {
                    //最后一张图片了，就跳到结果页面
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
        Log.i("TAG", "typeNumber = " + typeNumber);
        Log.i("TAG", "questionLocation = " + questionLocation);
        Log.i("TAG", "lenth " + lenth);
        Log.i("TAG", "Location of photo= " + Location);
        boolean flag = false;
        //0代表名字，1代表地点,2代表时间
        if (typeNumber == 0) {
            if (!names.get(questionLocation).equals("")) {
                textView.setText("图片中的人的名字是叫" + names.get(questionLocation) + "吗？\n" + "左滑是右滑不是");
            } else {
                //                getQuestion(Location, textView);
                return false;
            }
        } else if (typeNumber == 1) {
            if (!imageplaces.get(questionLocation).equals("")) {
                textView.setText("图片拍摄的地点是在" + imageplaces.get(questionLocation) + "吗？\n" + "左滑是右滑不是");
            } else {
                getQuestion(Location, textView);
            }


        } else if (typeNumber == 2) {
            String photoDate = "";

            if (!imagetimes.get(questionLocation).equals("")) {
                photoDate = imagetimes.get(questionLocation);
            } else {
                Log.i("TAG", "imagetimes不存在");
                getQuestion(Location, textView);
            }

            //            if (photoDate.charAt(4) == '0') {
            //
            //                if (photoDate.charAt(6) == '0') {
            //                    textView.setText("图片拍摄的时间是在" + photoDate.substring(0, 4) + "年" + photoDate.substring(5, 6) + "月" + photoDate.substring(7) + "日吗？\n" + "左滑是右滑不是");
            //                } else {
            //                    textView.setText("图片拍摄的时间是在" + photoDate.substring(0, 4) + "年" + photoDate.substring(5, 6) + "月" + photoDate.substring(6) + "日吗？\n" + "左滑是右滑不是");
            //                }
            //            } else {
            //                if (photoDate.charAt(6) == '0') {
            //                    textView.setText("图片拍摄的时间是在" + photoDate.substring(0, 4) + "年" + photoDate.substring(4, 6) + "月" + photoDate.substring(7) + "日吗？\n" + "左滑是右滑不是");
            //                }
            //                textView.setText("图片拍摄的时间是在" + photoDate.substring(0, 4) + "年" + photoDate.substring(4, 6) + "月" + photoDate.substring(6) + "日吗？\n" + "左滑是右滑不是");
            //            }
            //            //            textView.setText("图片拍摄的时间是在" + imagetimes[questionLocation] + "吗？\n" + "左滑是右滑不是");
            if (photoDate.length() == 8) {
                if (photoDate.charAt(4) == '0') {

                    if (photoDate.charAt(6) == '0') {
                        textView.setText("图片拍摄的时间是在"+photoDate.substring(0, 4) + "年" + photoDate.substring(5, 6) + "月" + photoDate.substring(7, 8) + "日"+"吗？\n" + "左滑是右滑不是");
                    } else {
                        textView.setText("图片拍摄的时间是在"+photoDate.substring(0, 4) + "年" + photoDate.substring(5, 6) + "月" + photoDate.substring(6, 8) + "日"+"吗？\n" + "左滑是右滑不是");
                    }
                } else {
                    if (photoDate.charAt(6) == '0') {

                        textView.setText("图片拍摄的时间是在"+photoDate.substring(0, 4) + "年" + photoDate.substring(4, 6) + "月" + photoDate.substring(7, 8) + "日"+"吗？\n" + "左滑是右滑不是");
                    }

                    textView.setText("图片拍摄的时间是在"+photoDate.substring(0, 4) + "年" + photoDate.substring(4, 6) + "月" + photoDate.substring(6, 8) + "日"+"吗？\n" + "左滑是右滑不是");
                }
            } else if (photoDate.length() == 6) {
                if (photoDate.charAt(4) == '0') {
                    textView.setText("图片拍摄的时间是在"+photoDate.substring(0, 4) + "年" + photoDate.substring(5, 6) + "月"+"吗？\n" + "左滑是右滑不是");
                } else {
                    textView.setText("图片拍摄的时间是在"+photoDate.substring(0, 4) + "年" + photoDate.substring(4, 6) + "月"+"吗？\n" + "左滑是右滑不是");

                }
            } else if (photoDate.length() == 4) {
                textView.setText("图片拍摄的时间是在"+photoDate.substring(0, 4) + "年"+"吗？\n" + "左滑是右滑不是");
            } else if (photoDate.length() < 4) {
                getQuestion(Location, textView);
            }
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
            dataItem.userName = names.get(i);
            dataItem.imagePath = imagePaths.get(i);
            dataItem.imagePlace = imageplaces.get(i);
            //                    dataItem.likeNum = (int) (Math.random() * 10);
            //                    dataItem.imageNum = (int) (Math.random() * 6);
            dataList.add(dataItem);
        }
    }

    private void listToArray() {
        for (int i = 0; i < pictlist.size(); i++) {
            imagePaths.add(pictlist.get(i).getImagePath());
            names.add(pictlist.get(i).getName());
            imageplaces.add(pictlist.get(i).getLocation());
            imagetimes.add(String.valueOf(pictlist.get(i).getDate()));
            Log.i("TAG", "imagePaths " + imagePaths.get(i) + " names " + names.get(i) + " imageplaces " + imageplaces.get(i) + " imagetimes " + imagetimes.get(i));
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
