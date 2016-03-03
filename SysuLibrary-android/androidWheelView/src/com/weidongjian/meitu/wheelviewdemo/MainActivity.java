package com.weidongjian.meitu.wheelviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import java.util.ArrayList;

import com.weidongjian.meitu.wheelviewdemo.view.LoopView;
import com.weidongjian.meitu.wheelviewdemo.view.OnItemSelectedListener;

public class MainActivity extends Activity {
    private RelativeLayout rootview;
    private RelativeLayout.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        rootview = (RelativeLayout) findViewById(R.id.rootview);

        LoopView loopView = new LoopView(this);
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 15; i++) {
            list.add("item " + i);
        }
        //�����Ƿ�ѭ������
        //loopView.setNotLoop();
        //��������
        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Log.d("debug", "Item " + index);
            }
        });
        //����ԭʼ����
        loopView.setItems(list);
        //���ó�ʼλ��
        loopView.setInitPosition(5);
        //���������С
        loopView.setTextSize(30);
        rootview.addView(loopView, layoutParams);

    }

}
