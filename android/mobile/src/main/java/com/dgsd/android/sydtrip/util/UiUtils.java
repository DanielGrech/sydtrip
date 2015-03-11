package com.dgsd.android.sydtrip.util;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Property;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

public class UiUtils {

    private UiUtils() {
        // No instances...
    }

    public static void startActivity(Activity activity, MenuItem item, Intent intent) {
        final View view = activity.findViewById(item.getItemId());
        if (view == null) {
            activity.startActivity(intent);
        } else {
            startActivity(view, intent);
        }
    }

    @SuppressWarnings("InlinedApi")
    public static void startActivity(View view, Intent intent) {
        if (Api.isMin(Api.JELLYBEAN)) {
            final int w = view.getWidth();
            final int h = view.getHeight();
            final ActivityOptions opts
                    = ActivityOptions.makeScaleUpAnimation(view, w / 2, h / 2, w, h);
            view.getContext().startActivity(intent, opts.toBundle());
        } else {
            view.getContext().startActivity(intent);
        }
    }

    public static void fadeTextColor(TextView tv, int color) {
        final ObjectAnimator anim = ObjectAnimator.ofInt(tv, TEXT_VIEW_COLOR_PROPERTY, color);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setInterpolator(new DecelerateInterpolator(1.5f));
        anim.setDuration(800);
        anim.start();
    }

    private static final Property<TextView, Integer> TEXT_VIEW_COLOR_PROPERTY
            = new Property<TextView, Integer>(int.class, "textColor") {
        @Override
        public Integer get(TextView object) {
            return object.getCurrentTextColor();
        }

        @Override
        public void set(TextView object, Integer value) {
            object.setTextColor(value);
        }
    };
}
