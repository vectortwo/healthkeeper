package com.vectortwo.healthkeeper.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import com.vectortwo.healthkeeper.R;

/**
 * Created by skaper on 21.04.17.
 */
public class CreateAnimation {
    private Context contextThis;
    private int descriptionViewFullHeight;
    public CreateAnimation(Context context){
        this.contextThis = context;
    }

    public void expand_Anim(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int widthSpec = View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(widthSpec, heightSpec);

        final int targetHeight  = v.getMeasuredHeight();
        final int mStartHeight = v.getHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                int newHeight = mStartHeight + (int) ((targetHeight-mStartHeight) * interpolatedTime);
                v.getLayoutParams().height = newHeight;
                v.requestLayout();

            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density)*2);
        v.startAnimation(a);
    }

    public void callapse_Anim(final View v, final int endSize) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int widthSpec = View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(widthSpec, heightSpec);

        final int targetHeight  = v.getMeasuredHeight();
        final int mStartHeight = v.getHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                int newHeight = targetHeight - (int) ((mStartHeight-dpToPx(endSize)) * interpolatedTime);
                v.getLayoutParams().height = newHeight;
                v.requestLayout();
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int)(mStartHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
    public void showAnimSmile(View view, final long time){
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.5f);
        view.animate()
                .alpha(1.0f)
                .setDuration(time);


    }

    public void hideAnimSmile(final View view, final long time){
        view.animate()
                .setDuration(time)
                .alpha(0.5f);

    }

    public void showAnimFast(final View view){
        view.setAlpha(0.0f);
        view.animate()
                .alpha(1.0f)
                .setDuration(300);
    }
    public void hideAnimFast(final View view){
        view.animate()
                .setDuration(300)
                .alpha(0.0f);
    }


    public void showAnim(View view, final long time){
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);
        view.animate()
                .alpha(1.0f)
                .setDuration(time);
    }

    public void hideAnim(final View view, final long time){
        final boolean[] animEnd = {false};
        view.animate()
                .setDuration(time)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        animEnd[0] = true;
                    }
                });

        if(animEnd[0]){
            view.setVisibility(View.GONE);
            animEnd[0] = false;
        }
    }

    public void hideShowCard(final CardView cardView, int IDMinHeight, long starDelay) {

        int descriptionViewMinHeight = (int) contextThis.getResources().getDimension(IDMinHeight);
        if (cardView.getHeight() == descriptionViewMinHeight) {
            // expand
            ValueAnimator anim = ValueAnimator.ofInt(cardView.getMeasuredHeightAndState(),
                    descriptionViewFullHeight);

            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
                    layoutParams.height = val;
                    cardView.setLayoutParams(layoutParams);
                }
            });
            anim.setStartDelay(starDelay);
            anim.start();
        } else {
            // collapse
            ValueAnimator anim = ValueAnimator.ofInt(cardView.getMeasuredHeightAndState(),
                    descriptionViewMinHeight);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
                    layoutParams.height = val;
                    cardView.setLayoutParams(layoutParams);
                }
            });
            anim.start();
        }
    }

    public int dpToPx(int value){
        return  (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value, contextThis.getResources().getDisplayMetrics());
    }

}
