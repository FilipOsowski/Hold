package com.example.rc211.prototype2;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class CircleView extends View {
    private float scale = 1;
    private int height;
    private int width;
    private TextView liveScoreTextView;
    private CircleRunnable circleRunnable;
    private Thread circleThread;
    private Activity mainActivity;
    private MainActivity mA;
    private CompositeForce compositeForce = new CompositeForce();
    private float[] velocity = new float[]{0, 0};
    private Random random = new Random();
    private Boolean gameOver = false;

    public CircleView(Context context, View view, MainActivity mainActivity) {
        super(context);

        this.mainActivity = (Activity) context;
        this.mA = mainActivity;

        liveScoreTextView = view.findViewById(R.id.testTextView);
        liveScoreTextView.setTextSize(50);
        liveScoreTextView.setAlpha(0);

        setTranslationY(getHeight() / 2);
        setTranslationX(getWidth() / 2);
        this.setScale(0.85f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        height = this.getHeight();
        width = this.getWidth();

        Paint paint = new Paint();
        paint.setARGB(255, 190,237,170);
        canvas.drawCircle(width / 2, height / 2, this.getWidth() / 2, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!gameOver) {
            if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                circleRunnable = new CircleRunnable(this, new Handler());
                circleThread = new Thread(circleRunnable);
                circleThread.start();
                fade(liveScoreTextView, 1f, 1000);
                fade(mA.finalScoreTextView, 0f, 500);
                fade(mA.holdTextView, 0f, 500);

            }
            else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                setGameOver();
            }

            if (!(inCircle(event))) {
                setGameOver();
            }
        }
        return true;
    }

    public void fade(Object target, Float value, long duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target, "alpha", value);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    public void setGameOver() {
        circleRunnable.shutdown();
        gameOver = true;

        try {
            mA.showScore((String) liveScoreTextView.getText());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ObjectAnimator circleAnimator = ObjectAnimator.ofFloat(this, "scale", 3);
        circleAnimator.setDuration(1500);

        fade(this, 0f, 1500);
        fade(liveScoreTextView, 0f, 500);

        circleAnimator.start();
    }

    public void setScale(float scale) {
        this.scale = scale;
        this.setScaleX(scale);
        this.setScaleY(scale);
    }

    public float getScale() {
        return scale;
    }

    public void setScore(String score) {
        final String s = score;

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                liveScoreTextView.setText(s);
            }
        });
    }

    public void updatePosition() {
        System.out.println(scale);
        updateVelocity();
        float[] nextPosition = new float[]{(getX() + velocity[0]), (getY() + velocity[1])};

        nextPosition = normalizePosition(nextPosition);

        setX(nextPosition[0]);
        setY(nextPosition[1]);
    }

    public float[] normalizePosition(float[] position) {
        float realRadius = getRealWidth() / 2;
        float[] max = new float[]{position[0] + realRadius, position[1] + realRadius};
        float[] min = new float[]{position[0] - realRadius, position[1] - realRadius};

        float[] normalizedPosition = new float[]{position[0], position[1]};

        for (float[] point : new float[][]{max, min}) {
            if ((-width/2 >= point[0]) || (width/2 <= point[0])) {
               normalizedPosition[0] = (width/2 - realRadius) * position[0] / Math.abs(position[0]);
               velocity[0] *= -1;
            }
            if ((-height/2 >= point[1]) || (height/2 <= point[1])) {
                normalizedPosition[1] = (height/2 - realRadius) * position[1] / Math.abs(position[1]);
                velocity[1] *= -1;
            }
        }

        return normalizedPosition;
    }

    public float getRealWidth() {
        return scale * getWidth();
    }

    public void updateVelocity() {
        compositeForce.setCurrentTime(compositeForce.getCurrentTime() + 10);
        float aX = compositeForce.getX() / scale;
        float aY = compositeForce.getY() / scale;

        velocity[0] += aX;
        velocity[1] += aY;
    }

    public int getRandomSign() {
        double x = Math.random();

        if (x > 0.5) {
            return 1;
        }
        else {
            return -1;
        }
    }

    public void addNewForce() {
        float xForce = (float) ((random.nextInt(9) + 1) * getRandomSign() * 0.005);
        float yForce = (float) ((random.nextInt(9) + 1) * getRandomSign() * 0.005);
        int expirationTime = random.nextInt(2000) + 1000 + compositeForce.getCurrentTime();
        Force force = new Force(xForce, yForce, expirationTime);
        compositeForce.addForce(force);
    }

    private boolean inCircle(MotionEvent event) {
        Double x = (Math.pow((event.getX() - width / 2), 2) + Math.pow((event.getY() - height / 2), 2));
        return (x <= 518400);
    }
}