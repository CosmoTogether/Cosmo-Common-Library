package com.cosmotogether.libs.statusbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import io.senlab.cosmo.R;
import io.senlab.cosmo.Util;

public class CosmoTextClock extends androidx.appcompat.widget.AppCompatTextView {
    private int display_type;
    private final BroadcastReceiver mIntentReceiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    onTimeChanged();
                }
            };
    private boolean registered;

    public CosmoTextClock(Context context) {
        super(context);
    }

    public CosmoTextClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttribs(context, attrs);
    }

    public CosmoTextClock(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAttribs(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onTimeChanged();
        if (!registered) {
            registered = true;
            registerReceiver();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (registered) {
            unregisterReceiver();
            registered = false;
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) refresh();
    }

    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        if (screenState == SCREEN_STATE_ON) onTimeChanged();
    }

    private void registerReceiver() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        getContext().registerReceiver(mIntentReceiver, filter);
    }

    private void unregisterReceiver() {
        try {
            getContext().unregisterReceiver(mIntentReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onTimeChanged() {
        boolean is24 = android.text.format.DateFormat.is24HourFormat(getContext());
        String[] elements =
                Util.getFormattedTime(
                                getContext(),
                                System.currentTimeMillis(),
                                Util.TIME_FORMAT_HH_H_hh_h_mm_ss_SSS_a)
                        .split("_");
        String display_text;
        switch (display_type) {
            case 4: // date_medium_text
                display_text =
                        Util.getFormattedDate(
                                getContext(),
                                System.currentTimeMillis(),
                                Util.DATE_FORMAT_MEDIUMTEXT);
                break;
            case 3: // am_pm_only_12
                display_text = is24 ? "" : elements[7];
                break;
            case 2: // two_line_no_am_pm
                display_text =
                        is24 ? elements[0] + "\n" + elements[4] : elements[2] + "\n" + elements[4];
                break;
            case 1: // no_am_pm
                display_text =
                        is24 ? elements[0] + ":" + elements[4] : elements[3] + ":" + elements[4];
                break;
            case 0: // default
            default:
                display_text = Util.getFormattedTime(getContext(), System.currentTimeMillis());
        }
        setText(display_text);
    }

    public void refresh() {
        onTimeChanged();
    }

    private void setAttribs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CosmoTextClock);
        display_type = array.getInteger(R.styleable.CosmoTextClock_display_type, 0);
        array.recycle();
    }
}
