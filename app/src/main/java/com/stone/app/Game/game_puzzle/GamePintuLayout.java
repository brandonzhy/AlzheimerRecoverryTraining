package com.stone.app.Game.game_puzzle;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.stone.app.dataBase.DataBaseError;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.PictureData;
import com.stone.app.dataBase.RealmDB;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by Administrator on 2017/8/25 0025.
 */

public class GamePintuLayout extends RelativeLayout implements View.OnClickListener {
    private int totaltime;
    private int mColumn = 2;//拼图行数
    private int mPadding;//与窗口边沿的间距
    private int mMargin = 3;//图片
    private ImageView[] mGamePintuItems;//存储图片的宽高
    private int mItemWidth;//实际拼图宽度
    private Bitmap mBitmap;//图片
    private List<image> mItemBitmaps;//图片切割后的存放
    private boolean once = false;//标记
    private int mWidth;//屏幕宽度
    private ImageView mFirst;//交互图片1
    private ImageView mSecond;//交互图片2


    private boolean isAniming;//动画的条件限制
    private boolean isTimeEnabled = true;
    private boolean isGameSuccess = false;
    private boolean isGameOver = false;
    private RelativeLayout mAnimLayout;//拼图界面
    private static final int TIME_CHANGED = 2;
    private static final int NEXT_LEVEL = 4;
    private int level = 0;


    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    private String memberID;

    private Context mycontext;
    private String imagepath;

    public Context getMycontext() {
        return mycontext;
    }

    public GamePintuLayout(Context context) {
        this(context, null);
        mycontext = context;
    }

    public GamePintuLayout(Context context, AttributeSet attrs, int defStyleAttr, String memberID) {
        super(context, attrs, defStyleAttr);
        this.memberID = memberID;
    }

    public GamePintuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public GamePintuLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mMargin, getResources().getDisplayMetrics());

        mPadding = min(getPaddingLeft(), getPaddingTop(), getPaddingRight(),
                getPaddingBottom());
    }

    //碎片随机排列,调用drawable中的 image2
    private void initBitmap(int i) {
        //        if (mBitmap != null) {
        //            mBitmap = BitmapFactory.decodeResource(getResources(),
        //                    R.drawable.image);
        //获取图片
        DataBaseManager dataBaseManager = RealmDB.getDataBaseManager();
        List<PictureData> listdata = null;
        try {
            Log.i("TAG", " gamepuzzle 的 memberID= " + memberID);
            listdata = dataBaseManager.getRandomPicturesFromMember(memberID, "", "", 0, 0, 0);
            if (listdata.size() > 0 && i < listdata.size()) {
                imagepath = listdata.get(i).getImagePath().trim();
                Log.i("TAG", "gamepuzzle 的 imagepath = " + imagepath);
                mBitmap = BitmapFactory.decodeFile(imagepath);
                Log.i("TAG", "gamepuzzle 的 mBitmap = " + mBitmap);
                if(mBitmap==null){
                    Log.i("TAG","mBitmap==null " );
                    return;
                }
            }
        } catch (DataBaseError dataBaseError) {
            Log.i("TAG", "错误类型为" + dataBaseError.getMessage());
            dataBaseError.printStackTrace();

        }
        mItemBitmaps = ImageSplitterUtil.split(mBitmap, mColumn);
        Collections.sort(mItemBitmaps, new Comparator<image>() {
            @Override
            public int compare(image a, image b) {
                return Math.random() > 0.5 ? 1 : -1;
            }
        });

    }

    //拼图页面的高宽设置
    private void initItem() {
        mItemWidth = (mWidth - mPadding * 2 - mMargin * (mColumn - 1)) / mColumn;
        mGamePintuItems = new ImageView[mColumn * mColumn];
        for (int i = 0; i < mGamePintuItems.length; i++) {
            ImageView item = new ImageView(getContext());
            item.setOnClickListener(this);
            item.setImageBitmap(mItemBitmaps.get(i).bitmap);
            mGamePintuItems[i] = item;
            item.setId(i + 1);
            item.setTag(i + "_" + mItemBitmaps.get(i).index);
            LayoutParams lP = new LayoutParams(mItemWidth, mItemWidth);


            if ((i + 1) % mColumn != 0) {
                lP.rightMargin = mMargin;

            }
            if (i % mColumn != 0) {
                lP.addRule(RelativeLayout.RIGHT_OF, mGamePintuItems[i - 1].getId());
            }
            if ((i + 1) > mColumn) {
                lP.topMargin = mMargin;
                lP.addRule(RelativeLayout.BELOW, mGamePintuItems[i - mColumn].getId());
            }
            addView(item, lP);

        }
    }

    //出始界面，拼图的排列，计时的开启
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = Math.min(getMeasuredHeight(), getMeasuredWidth());
        if (!once) {
            initBitmap(level);
            initItem();
            checkTimeEnable();
            once = true;

        }

        setMeasuredDimension(mWidth, mWidth);

    }

    private int min(int... params) {
        int min = params[0];
        for (int param : params) {
            if (param < min) {
                min = param;
            }
        }
        return min;
    }

    @Override
    public void onClick(final View v) {

        if (isAniming)
            return;
        if (mFirst == v) {
            mFirst.setColorFilter(null);
            mFirst = null;
            return;
        }
        if (mFirst == null) {
            mFirst = (ImageView) v;
            mFirst.setColorFilter(Color.parseColor("#55FF0000"));


        } else {
            mSecond = (ImageView) v;

            new Thread(new Runnable() {
                @Override
                public void run() {

                    v.post(new Runnable() {
                        @Override
                        public void run() {
                            exchangeView();


                        }
                    });

                }
            }).start();


            //mSecond.setColorFilter(Color.YELLOW);
        }


    }

    private void AnimLayout() {
        if (mAnimLayout == null) {
            mAnimLayout = new RelativeLayout(getContext());
            addView(mAnimLayout);
        }

    }

    private int getImageIndexByTag(String tag) {
        String[] split = tag.split("_");
        return Integer.parseInt(split[0]);

    }

    //交互图片动画
    private void exchangeView() {
        mFirst.setColorFilter(null);
        AnimLayout();

        ImageView first = new ImageView(getContext());
        first.setImageBitmap(mItemBitmaps.get(getImageIndexByTag((String) mFirst.
                getTag())).bitmap);
        LayoutParams lp = new LayoutParams(mItemWidth, mItemWidth);
        lp.leftMargin = mFirst.getLeft() - mPadding;
        lp.topMargin = mSecond.getTop() - mPadding;
        first.setLayoutParams(lp);
        mAnimLayout.addView(first);

        ImageView second = new ImageView(getContext());
        second.setImageBitmap(
                mItemBitmaps.get(getImageIndexByTag((String) mSecond.
                        getTag())).bitmap);
        LayoutParams lp2 = new LayoutParams(mItemWidth, mItemWidth);
        lp2.leftMargin = mSecond.getLeft() - mPadding;
        lp2.topMargin = mSecond.getTop() - mPadding;
        second.setLayoutParams(lp2);
        mAnimLayout.addView(second);

        TranslateAnimation anim = new TranslateAnimation(0, mSecond.getLeft()
                - mFirst.getLeft(), 0, mSecond.getTop() - mFirst.getTop());
        anim.setDuration(200);
        anim.setFillAfter(true);
        first.startAnimation(anim);

        TranslateAnimation animSecond = new TranslateAnimation(0,
                mFirst.getLeft() - mSecond.getLeft(), 0, mFirst.getTop()
                - mSecond.getTop());
        animSecond.setDuration(300);//动画延迟300ms
        animSecond.setFillAfter(true);
        second.startAnimation(animSecond);

        animSecond.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAniming = true;
                mFirst.setVisibility(INVISIBLE);
                mSecond.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mFirst.setColorFilter(null);
                AnimLayout();
                String firstTag = (String) mFirst.getTag();
                String secondTag = (String) mSecond.getTag();
                String[] firstParams = firstTag.split("_");
                String[] secondParams = secondTag.split("_");
                mFirst.setImageBitmap(
                        mItemBitmaps.get(Integer.parseInt(secondParams[0])).bitmap
                );
                mSecond.setImageBitmap(
                        mItemBitmaps.get(Integer.parseInt(firstParams[0])).bitmap
                );
                mFirst.setTag(secondTag);
                mSecond.setTag(firstTag);
                mFirst.setVisibility(VISIBLE);
                mSecond.setVisibility(VISIBLE);
                mFirst = mSecond = null;
                mAnimLayout.removeAllViews();
                checkSuccess();
                isAniming = false;

            }


            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //拼图成功判断
    private void checkSuccess() {
        boolean isSuccess = true;
        for (int i = 0; i < mGamePintuItems.length; i++) {
            ImageView first = mGamePintuItems[i];

            if (getIndexByTag((String) first.getTag()) != i) {
                isSuccess = false;
            }
        }

        if (isSuccess) {
            isGameSuccess = true;
            if (mColumn < 4) {
                Toast.makeText(getContext(), "您已经成功进入下一难度",
                        Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(getContext(), "恭喜你，今天的任务已经完成！！！",
                        Toast.LENGTH_LONG).show();
                mListener.gamefinish();


            }
            mHandler.sendEmptyMessage(NEXT_LEVEL);
        }

    }


    private int getIndexByTag(String tag) {
        String[] split = tag.split("_");
        return Integer.parseInt(split[1]);
    }

    //下一关卡
    public void nextLevel() {
        this.removeAllViews();
        mAnimLayout = null;
        //        mColumn++;
        isGameSuccess = false;
        checkTimeEnable();

        initBitmap(level);
        initItem();
        Log.i("tag", "next leval of layout is called");
    }

    //成功后对下一关的设置
    //    public int gamelevel() {
    //        this.removeAllViews();
    //        mAnimLayout = null;
    //        mColumn++;
    //        isGameSuccess = false;
    //        checkTimeEnable();
    //
    //        initBitmap();
    //        initItem();
    //
    //        Log.i("tag", "next leval of layout is called");
    //        return level;
    //    }

    private int mTime = 30;//初始时间=30+30，每一关加30

    public int gametimechange() {
        return mTime;
    }

    public GamePintuListener mListener;

    public void setOnGamemListener(GamePintuListener MListener) {


        mListener = MListener;
    }


    //开启时间记录
    public void backgame() {
        isGameOver = true;
    }

    public void setTimeEnabled() {
        isTimeEnabled = true;
    }

    public void checkTimeEnable() {
        if (isTimeEnabled) {
            counttimeLv();
            Message msg = new Message();
            msg.what = TIME_CHANGED;
            mHandler.sendMessage(msg);
            //            mHandler.sendEmptyMessage(TIME_CHANGED);
        }

    }

    public int getmTime() {
        return totaltime;
    }

    private void counttimeLv() {
        mTime = (int) Math.pow(2, 1) * 30;
        totaltime = mTime;
    }

    //接口的调用


    public interface GamePintuListener {
        //关卡
        void nextLevel();

        //时间
        void timechanged();

        void gameover();

        void gamefinish();

    }

    //子线程
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_CHANGED:
                    if (isGameOver || isGameSuccess)
                        return;
                    if (mListener != null)
                        //if(mTime%30==0)
                        mListener.timechanged();


                    if (mTime == 0) {
                        isGameOver = true;
                        mListener.gameover();
                        return;
                    }

                    Log.i("tag", "time--");
                    mTime--;

                    mHandler.sendEmptyMessageDelayed(TIME_CHANGED, 1000);

                    break;
                case NEXT_LEVEL:

                    level = level + 1;

                    if (mListener != null) {

                        // mListener.nextLevel();
                    } else {
                        nextLevel();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
}

