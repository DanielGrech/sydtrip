package com.dgsd.android.sydtrip.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dgsd.android.sydtrip.R;
import com.dgsd.sydtrip.model.Stop;
import com.dgsd.sydtrip.routing.model.RoutingItinery;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RoutingResultListItemView extends LinearLayout {

    private static final int HOURS_CUTOFF = 120; // mins
    private static final int DAYS_CUTOFF = (int) TimeUnit.DAYS.toMinutes(1); // mins

    @InjectView(R.id.time_until)
    TextView timeUntil;

    @InjectView(R.id.depart_time)
    TextView departureTime;

    @InjectView(R.id.depart_platform)
    TextView departurePlatform;

    @InjectView(R.id.arrival_time)
    TextView arrivalTime;

    @InjectView(R.id.arrival_platform)
    TextView arrivalPlatform;

    private final Paint paint;

    private int tripColor;

    private int colorWidth;

    private final Calendar calendar;

    public RoutingResultListItemView(Context context) {
        this(context, null);
    }

    public RoutingResultListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoutingResultListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        calendar = Calendar.getInstance();
        colorWidth = getResources().getDimensionPixelSize(
                R.dimen.routing_result_list_item_countdown_width);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        paint.setColor(tripColor);
        canvas.drawRect(0, 0, colorWidth, canvas.getHeight(), paint);

        super.dispatchDraw(canvas);
    }

    public void populate(RoutingItinery itinery) {
        tripColor = itinery.getBestDisplayColor();

        final int start = itinery.getTimeAtOrigin();
        final int end = itinery.getTimeAtDestination();

        final Stop origin = itinery.getOriginStation();
        final Stop dest = itinery.getDestinationStation();

        departurePlatform.setText(origin == null ? null : origin.getName());
        arrivalPlatform.setText(dest == null ? null : dest.getName());

        departureTime.setText(convertToTimestamp(start));
        arrivalTime.setText(convertToTimestamp(end));
        timeUntil.setText(Html.fromHtml(convertDurationUntilTimeUntil(getSecondsUntil(start))));

        invalidate();
    }

    private String convertToTimestamp(int secondsSinceMidnight) {
        resetCalendar();
        calendar.add(Calendar.SECOND, secondsSinceMidnight);
        return DateUtils.formatDateTime(getContext(), calendar.getTimeInMillis(),
                DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_SHOW_TIME);
    }

    private String convertDurationUntilTimeUntil(int durationInSeconds) {
        final long mins = TimeUnit.SECONDS.toMinutes(Math.abs(durationInSeconds));
        final String minsVal;
        if (mins <= HOURS_CUTOFF) {
            minsVal = getResources().getString(R.string.x_mins, mins);
        } else if (mins <= DAYS_CUTOFF) {
            minsVal = getResources().getString(R.string.x_hours, TimeUnit.MINUTES.toHours(mins));
        } else {
            minsVal = getResources().getString(R.string.x_days, TimeUnit.MINUTES.toDays(mins));
        }

        return durationInSeconds < 0 ? getResources().getString(R.string.x_ago, minsVal) : minsVal;
    }

    private int getSecondsUntil(int seconds) {
        resetCalendar();
        calendar.add(Calendar.SECOND, seconds);

        final int millisUntil = (int) (calendar.getTimeInMillis() - System.currentTimeMillis());

        return (int) TimeUnit.MILLISECONDS.toSeconds(millisUntil);
    }

    private void resetCalendar() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
