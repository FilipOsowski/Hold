package com.example.rc211.prototype2;

import android.os.Handler;

public class CircleRunnable implements Runnable {
    private boolean stop = false;
    private CircleView circleView;
    private Handler handler;
    private int timeElapsed = 0;
    private int timeSinceLastForce = 500;

    public CircleRunnable(CircleView circleView, Handler handler) {
        this.circleView = circleView;
        this.handler = handler;
    }

    @Override
    public void run() {
        if (!stop) {
            if (timeSinceLastForce == 500) {
                circleView.addNewForce();
                timeSinceLastForce = 0;
            }
            else {
                timeSinceLastForce += 5;
            }

            updateScore();
            if (circleView.getScale() > 0.3) {
                circleView.setScale((float) (circleView.getScale() * 0.999));
            }
            circleView.updatePosition();
            handler.postDelayed(this, 5);
        }
    }

    private void updateScore() {
        timeElapsed += 5;
        System.out.println("UPDATED SCORE");
        circleView.setScore(Integer.toString(timeElapsed/10));
    }

    public Boolean stopped() {
        return stop;
    }

    public void shutdown() {
        stop = true;
    }
}
