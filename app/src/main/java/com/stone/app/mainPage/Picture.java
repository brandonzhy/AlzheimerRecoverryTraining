package com.stone.app.mainPage;//package com.example.application.mainPage;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.example.application.R;
//
//import java.util.ArrayList;
//
//public class Picture extends Activity {
//    private ViewPager viewPager;
//    private TextView tv_intro;
//    private LinearLayout dot_layout;
//    private ArrayList<Ad> list = new ArrayList<Ad>();
//    /**
//     * 用于设置自动轮播
//     */
//    private Handler handler = new Handler(){
//        public void handleMessage(android.os.Message msg){
//            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
//            handler.sendEmptyMessageDelayed(0, 4000);
//        }
//    };
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initView();
//        initData();
//        initListener();
//        Button button24 = (Button) findViewById(R.id.button_24);
//        button24.setOnClickListener(new View.OnClickListener() {
//            @Override
//
//            public void onClick(View v) {
//
//                finish();
//            }
//        });
//
//    }
//    //初始化视图
//    private void initView() {
//        setContentView(R.layout.activity_picture);
//        viewPager = (ViewPager)findViewById(R.id.viewpage);
//        tv_intro = (TextView) findViewById(R.id.tv_intro);
//        dot_layout = (LinearLayout)findViewById(R.id.dot_layout);
//    }
//    //初始化数据
//    private void initData(){
//        list.add(new Ad(R.drawable.shamo,"黄色福利"));
//        list.add(new Ad(R.drawable.kola, "考拉"));
//        list.add(new Ad(R.drawable.qier, "腾讯"));
//        list.add(new Ad(R.drawable.yujinxiang, "明日黄花"));
//        list.add(new Ad(R.drawable.dengta, "房子"));
//        list.add(new Ad(R.drawable.juhua, "花"));
//        list.add(new Ad(R.drawable.baxian, "八仙"));
//        initDots();
//        viewPager.setAdapter(new MyPagerAdapter());
//        int centerValue = Integer.MAX_VALUE/2;
//        int value = centerValue % list.size();
//        //设置viewPager的第一页为最大整数的中间数，实现伪无限循环
//        viewPager.setCurrentItem(centerValue-value);
//        updateIntroAndDot();
//        handler.sendEmptyMessageDelayed(0,4000);
//    }
//    //初始化文字下方的圆点
//    private void initDots() {
//        for (int i=0; i< list.size(); i++)
//        {
//            View view = new View(this);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8,8);
//            if(i!=0) {
//                params.leftMargin = 5;
//            }
//            view.setLayoutParams(params);
//            view.setBackgroundResource(R.drawable.selector_dot);
//            dot_layout.addView(view);
//        }
//    }
//    //初始化监听器，当页面改变时，更新其相应数据
//    private void initListener() {
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                updateIntroAndDot();
//            }
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
//    }
//    //更新数据与圆点
//    private void updateIntroAndDot(){
//        int currentPage = viewPager.getCurrentItem() % list.size();
//        tv_intro.setText(list.get(currentPage).getIntro());
//        for (int i = 0; i < dot_layout.getChildCount(); i++)
//            dot_layout.getChildAt(i).setEnabled(i==currentPage);
//    }
//    //ViewPager的主体部分
//    class MyPagerAdapter extends PagerAdapter{
//        /**
//         * 返回多少page
//         */
//        public int getCount() {
//            return Integer.MAX_VALUE;
//        }
//        /**view滑动到一半时是否创建新的view
//         * true:表示不去创建，使用缓存；false:去重新创建
//         */
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view==object;
//        }
//        /**
//         * 类似于BaseAdapter的getView方法
//         */
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            View view = View.inflate(Picture.this, R.layout.adapter_ad, null);
//            ImageView imageView = (ImageView) view.findViewById(R.id.image);
//            Ad ad = list.get(position%list.size());
//            imageView.setImageResource(ad.getIconResId());
//            container.addView(view);
//            return view;
//        }
//        /**
//         * @param position:当前需要销毁第几个page
//         * @param object:当前需要销毁的page
//         */
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            //super.destroyItem(container, position, object);
//            container.removeView((View) object);
//        }
//    }
//}