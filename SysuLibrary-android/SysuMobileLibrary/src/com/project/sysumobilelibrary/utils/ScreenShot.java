package com.project.sysumobilelibrary.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.project.sysumobilelibrary.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ListView;
import android.widget.ScrollView;

public class ScreenShot {
    // ��ȡָ��Activity�Ľ��������浽png�ļ�
    public static Bitmap takeScreenShot(Activity activity) {
        // View������Ҫ��ͼ��View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
 
        // ��ȡ״̬���߶�
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println(statusBarHeight);
 
        // ��ȡ��Ļ���͸�
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // ȥ��������
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        savePic(b, "/sdcard/screen_test.png");
        return b;
    }
 
    // ���浽sdcard
    public static void savePic(Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * ��View����ת����bitmap
     * */
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        if (bitmap != null) {
            System.out.println("�ⲻ��nullde1");
            Log.d("nullde1", "nullde1");
        } else {
            System.out.println("��nullnulllnulnlul");
        }
        return bitmap;
    }
 
    // �������1
    public static void shoot(Activity a) {
        ScreenShot.savePic(ScreenShot.takeScreenShot(a), "/sdcard/screen_test.png");
    }
 
    // �������2
    public static void shootView(View view) {
        ScreenShot.savePic(ScreenShot.convertViewToBitmap(view),
                "sdcard/xx.png");
    }
 
    public static Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);
 
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
 
        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
 
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            Log.e("TTTTTTTTActivity", "failed getViewBitmap(" + v + ")",
                    new RuntimeException());
            return null;
        }
 
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
 
        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
 
        return bitmap;
    }
 
    /**
     * ��ȡscrollview����Ļ
     * **/
    public static Bitmap getBitmapByView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        // ��ȡlistViewʵ�ʸ߶�
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundResource(R.drawable.gray_bg);
        }
        Log.d(TAG, "ʵ�ʸ߶�:" + h);
        Log.d(TAG, " �߶�:" + scrollView.getHeight());
        // ������Ӧ��С��bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        // �������
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("/sdcard/screen_test.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            // TODO: handle exception
        }
        return bitmap;
    }
 
    private static String TAG = "Listview and ScrollView item ��ͼ:";
 
    /**
     *  ��ͼlistview
     * **/
    public static Bitmap getbBitmap(ListView listView) {
        int h = 0;
        Bitmap bitmap = null;
        // ��ȡlistViewʵ�ʸ߶�
        for (int i = 0; i < listView.getChildCount(); i++) {
            h += listView.getChildAt(i).getHeight();
        }
        Log.d(TAG, "ʵ�ʸ߶�:" + h);
        Log.d(TAG, "list �߶�:" + listView.getHeight());
        // ������Ӧ��С��bitmap
        bitmap = Bitmap.createBitmap(listView.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        listView.draw(canvas);
        // �������
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("/sdcard/screen_test.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            // TODO: handle exception
        }
        return bitmap;
    }
 
}
