package com.stone.app.Game.game_judge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;

import org.xclcharts.chart.DialChart;
import org.xclcharts.common.MathHelper;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotAttrInfo;
import org.xclcharts.view.GraphicalView;

import java.util.ArrayList;
import java.util.List;

public class DiaCharView extends GraphicalView {



//    private String TAG = "DialChart06View";

    private DialChart chart = new DialChart();
    private float mPercentage = 0f;

    public DiaCharView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initView();
    }

    public DiaCharView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DiaCharView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }


    private void initView() {
        chartRender();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        chart.setChartRange(w, h);
    }

    public void chartRender() {
        try {

            //设置标题背景
            chart.setApplyBackgroundColor(true);
            chart.setBackgroundColor(Color.rgb(28, 129, 243));
            //绘制边框
            chart.showRoundBorder();

            chart.setTotalAngle(360f);

            //设置当前百分比
            chart.getPointer().setPercentage(mPercentage);

            chart.getPointer().setLength(0.6f, 0.2f);

            //增加轴
            addAxis();
            /////////////////////////////////////////////////////////////
            //增加指针
            addPointer();
            //设置附加信息
            addAttrInfo();
            /////////////////////////////////////////////////////////////

        } catch (Exception e) {
            // TODO Auto-generated catch block
//            Log.e(TAG, e.toString());
        }

    }

    public void addAxis() {
        List<Float> ringPercentage = new ArrayList<Float>();
        float rper = MathHelper.getInstance().div(1, 4); //相当于40%	//270, 4
        ringPercentage.add(rper);
        ringPercentage.add(rper);
        ringPercentage.add(rper);
        ringPercentage.add(rper);

        List<Integer> rcolor = new ArrayList<Integer>();
        rcolor.add(Color.rgb(242, 110, 131));
        rcolor.add(Color.rgb(238, 204, 71));
        rcolor.add(Color.rgb(42, 231, 250));
        rcolor.add(Color.rgb(140, 196, 27));
        chart.addStrokeRingAxis(0.85f, 0.7f, ringPercentage, rcolor);

        chart.addCircleAxis(0.6f, Color.WHITE);
        chart.getPlotAxis().get(1).getAxisPaint().setStyle(Style.STROKE);
        chart.getPlotAxis().get(0).getFillAxisPaint().setColor(Color.rgb(28, 129, 243));


        chart.getPointer().setPointerStyle(XEnum.PointerStyle.TRIANGLE);
        chart.getPointer().getPointerPaint().setStrokeWidth(4);
        chart.getPointer().getPointerPaint().setStyle(Style.FILL);

        if (Float.compare(mPercentage, 0.25f) == -1) {
            chart.getPointer().getPointerPaint().setColor(Color.rgb(242, 110, 131));
        } else if (Float.compare(mPercentage, 0.5f) == -1
                || Float.compare(mPercentage, 0.5f) == 0) {
            chart.getPointer().getPointerPaint().setColor(Color.rgb(238, 204, 71));
        } else if (Float.compare(mPercentage, 0.75f) == -1
                || Float.compare(mPercentage, 0.75f) == 0) {
            chart.getPointer().getPointerPaint().setColor(Color.rgb(42, 231, 250));
        } else if (Float.compare(mPercentage, 1f) == -1
                || Float.compare(mPercentage, 1f) == 0) {
            chart.getPointer().getPointerPaint().setColor(Color.rgb(140, 196, 27));
        } else {
            chart.getPointer().getPointerPaint().setColor(Color.YELLOW);
        }

        chart.getPointer().getBaseCirclePaint().setColor(Color.GREEN);
        chart.getPointer().setBaseRadius(10f);

    }

    //增加指针
    public void addPointer() {

    }


    private void addAttrInfo() {
        /////////////////////////////////////////////////////////////
        PlotAttrInfo plotAttrInfo = chart.getPlotAttrInfo();
        //设置附加信息
        Paint paintTB = new Paint();
        paintTB.setColor(Color.WHITE);
        paintTB.setTextAlign(Align.CENTER);
        paintTB.setTextSize(75);
        paintTB.setAntiAlias(true);
        plotAttrInfo.addAttributeInfo(XEnum.Location.TOP, "正确率"+ Float.toString(MathHelper.getInstance().round(mPercentage , 2))+"%", 0.3f, paintTB);
        Paint paintDesc = new Paint();
        paintDesc.setColor(Color.WHITE);
        paintDesc.setTextAlign(Align.CENTER);
        paintDesc.setTextSize(75);
        paintDesc.setFakeBoldText(true);
        paintDesc.setAntiAlias(true);
        plotAttrInfo.addAttributeInfo(XEnum.Location.LEFT, "普通", 0.9f, paintDesc);
        plotAttrInfo.addAttributeInfo(XEnum.Location.TOP, "一般", 0.9f, paintDesc);
        plotAttrInfo.addAttributeInfo(XEnum.Location.RIGHT, "较好", 0.9f, paintDesc);
        plotAttrInfo.addAttributeInfo(XEnum.Location.BOTTOM, "优秀", 0.9f, paintDesc);

    }

    public void setCurrentStatus(float percentage) {
        //			setPercentage(percentage);
        mPercentage = percentage;
        //清理
        chart.clearAll();
        //设置当前百分比
        chart.getPointer().setPercentage(mPercentage/100);
        addAxis();
        addPointer();
        addAttrInfo();
    }
    @Override
    public void render(Canvas canvas) {
        // TODO Auto-generated method stub
        try {
            chart.render(canvas);
        } catch (Exception e) {
//            Log.e(TAG, e.toString());
    }
    }

}
