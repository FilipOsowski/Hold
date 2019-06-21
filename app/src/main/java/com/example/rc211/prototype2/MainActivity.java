package com.example.rc211.prototype2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout layout;
    CircleView circleView;
    TextView finalScoreTextView;
    TextView holdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.myLayout);

        finalScoreTextView = new TextView(this);
        finalScoreTextView.setAlpha(0);
        finalScoreTextView.setText("Score:\n" + "100");
        finalScoreTextView.setTextSize(64);
        finalScoreTextView.setTypeface(null, Typeface.BOLD_ITALIC);
        finalScoreTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        holdTextView = new TextView(this);
        holdTextView.setAlpha(1);
        holdTextView.setText("Hold!");
        holdTextView.setTextSize(64);
        holdTextView.setTypeface(null, Typeface.BOLD_ITALIC);
        holdTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        circleView = new CircleView(this, layout, this);
        circleView.getViewTreeObserver().addOnGlobalLayoutListener((new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                System.out.println("WIDTH- " + finalScoreTextView.getWidth());
                finalScoreTextView.setX(circleView.getX() + circleView.getWidth() / 2 - finalScoreTextView.getWidth() / 2);
                finalScoreTextView.setY(0.15f * (circleView.getY() + circleView.getHeight() / 2 - finalScoreTextView.getHeight() / 2));
                holdTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                holdTextView.setX(circleView.getX() + circleView.getWidth() / 2 - holdTextView.getWidth() / 2);
                holdTextView.setY(circleView.getY() + circleView.getHeight() / 2 - holdTextView.getHeight() / 2);
            }
        }));

        layout.addView(circleView);
        layout.addView(finalScoreTextView);
        layout.addView(holdTextView);
    }

    public void showScore(String score) throws InterruptedException {
        if (score.equals("")) {
            score = "0";
        }
        finalScoreTextView.setText("Score:\n" + score);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(finalScoreTextView, "alpha", 1);
        objectAnimator.setDuration(2000);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                reset();
                super.onAnimationEnd(animation);
            }
        });
        objectAnimator.start();
    }

    public void reset() {
        layout.removeView(circleView);
        circleView = new CircleView(this, layout, this);
        circleView.setAlpha(0);
        circleView.setY(250);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(circleView, "alpha", 1);
        objectAnimator.setDuration(1000);
        objectAnimator.start();

        layout.addView(circleView);
    }
}
