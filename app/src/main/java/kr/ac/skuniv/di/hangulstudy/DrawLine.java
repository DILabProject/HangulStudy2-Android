package kr.ac.skuniv.di.hangulstudy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.View;

import java.io.Serializable;

/**
 * Created by namgiwon on 2018. 1. 5..
 */

public class DrawLine extends View implements Serializable
{
    //현재 그리기 조건(색상, 굵기, 등등.)을 기억 하는 변수.
    public Paint paint = null;

    //그리기를 할 bitmap 객체. -- 도화지라고 생각하면됨.
    public Bitmap bitmap = null;

    //bitmap 객체의 canvas 객체. 실제로 그리기를 하기 위한 객체.. -- 붓이라고 생각하면됨.
    public Canvas canvas = null;

    //마우스 포인터(손가락)이 이동하는 경로 객체.
    public Path path;

    //마우스 포인터(손가락)이 가장 마지막에 위치한 x좌표값 기억용 변수.
    public float oldX;

    //마우스 포인터(손가락)이 가장 마지막에 위치한 y좌표값 기억용 변수.
    public float oldY;

    /**
     * 생성자.. new DrawLine(this, rect) 하면 여기가 호출됨.
     * @param context   Context객체
     * @param rect      그리기 범위 화면 사이즈
     */
    public DrawLine(Context context, Rect rect)
    {
        this(context);

        //그리기를 할 bitmap 객체 생성.
        bitmap = Bitmap.createBitmap(rect.width(), rect.height(),
                Bitmap.Config.ARGB_8888);
        //그리기 bitmap에서 canvas를 알아옴.
        canvas = new Canvas(bitmap);

        //경로 초기화.
        path = new Path();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        //앱 종료시 그리기 bitmap 초기화 시킴...
        if(bitmap!= null) bitmap.recycle();
        bitmap = null;

        super.onDetachedFromWindow();
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        //그리기 bitmap이 있으면 현재 화면에 bitmap을 그린다.
        //자바의 view는 onDraw할때 마다 화면을 싹 지우고 다시 그리게 됨.
        if(bitmap != null)
        {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

    /**
     * 펜 색상 세팅
     * @param color 색상
     */
    public void setLineColor(int color)
    {
        paint = new Paint();
        paint.setColor(color);
        paint.setAlpha(255);
        paint.setDither(true);
        paint.setStrokeWidth(50); // 선 두께 설정
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
    }


    public DrawLine(Context context)
    {
        super(context);
    }
}
