package com.udemy.drawing_app

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

// (canvas) view 로 사용될 클래스
class DrawingView(context: Context, attrs:AttributeSet) : View(context, attrs){
    // 그릴때 필요한 변수
    private var mDrawPath: CustomPath? = null
    // bitmap
    private var mCanvasBitmap: Bitmap? = null
    // geometry, text 등 그릴 때 색상, 스타일 정보 변수
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null // view의 인스턴스가 될 변수
    //그림붓 두께 변수
    private var mBrushSize: Float = 0.toFloat()
    //색상변수
    private var color = Color.BLACK
    // 캔버스
    private var canvas: Canvas? = null

    //그린거 저장하는 변수
    private val mPaths = ArrayList<CustomPath>()

    init{
        setUpDrawing()
    }

    // 화면 크기가 바뀔 때마다 불림 (캔버스 초기화)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 비트맵 초기화
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        // 캔버스 초기화
        canvas = Canvas(mCanvasBitmap!!)

    }

    // 오류가 나면 Canvas를 널러블로 다시
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 캔버스에 비트맵그리기 왼쪽위 0 부터
         canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)

        //path를 전부 돌면서 그린거 유지하기
        for(path in mPaths){
            mDrawPaint!!.strokeWidth = path!!.brushThickness // 붓두께
            mDrawPaint!!.color = path.color
            canvas.drawPath(path, mDrawPaint!!)
        }

        // Paint 그리기(설정)
        if(!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness // 붓두께
            mDrawPaint!!.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    // 화면 터치했을때
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x //터치 x좌표
        val touchY = event?.y //터치 y좌표

        // 이벤트 실행시 행동
        when(event?.action){
            // action done(끝), action move(움직임), action up(손가락 듦)
            MotionEvent.ACTION_DOWN -> {
                // path 설정
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrushSize

                mDrawPath!!.reset()
                if (touchX != null) {
                    if (touchY != null) {
                        mDrawPath!!.moveTo(touchX, touchY)
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (touchX != null) {
                    if (touchY != null) {
                        mDrawPath!!.lineTo(touchX, touchY)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                mPaths.add(mDrawPath!!)
                mDrawPath = CustomPath(color, mBrushSize)
            }
            else -> return false
        }
        // 뷰가 화면에 보일 때 전체뷰를 무효화한다.
        invalidate()
        return true
    }


    private fun setUpDrawing() {
        // 변수 초기화
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color,mBrushSize)
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE // 선 그리기
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND //stroke의 끝 모양 결정
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND // 선 끝의 위치를 정함
        // 인스턴스 설정
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
        mBrushSize = 20.toFloat()
    }

    // 이 클래스 안에서 쓰일 클래스 - Path android library
    // --> 자르기 그리기등을 지원하는 라이브러리 (2d,3d 가능)
    internal inner class CustomPath(var color: Int,
                                    var brushThickness: Float): Path() {
        
    }
}