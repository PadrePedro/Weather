package com.pedroid.weather.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ViewAnimator;

import com.pedroid.weather.R;

import java.util.zip.Inflater;

/**
 * Created by pedro on 5/25/15.
 */
public class CrossFadeImageView extends RelativeLayout {

    private ImageView image1View;
    private ImageView image2View;
    private boolean image1Showing = true;

    public CrossFadeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.widget_cross_fade, this, true);
        image1View = (ImageView)layout.findViewById(R.id.image1View);
        image2View = (ImageView)layout.findViewById(R.id.image2View);
        image2View.setAlpha(0);
    }

    public void setImage(Bitmap bitmap) {

        final ImageView fadeInImageView = image1Showing ? image2View : image1View;
        final ImageView fadeOutImageView = image1Showing ? image1View : image2View;

        image1Showing = !image1Showing;
        fadeInImageView.setImageBitmap(bitmap);

        ValueAnimator animator = ValueAnimator.ofFloat(0,1);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float alpha = (Float) animation.getAnimatedValue();
                fadeInImageView.setAlpha(alpha);
                fadeOutImageView.setAlpha(1 - alpha);
            }
        });
        animator.start();
    }
}
