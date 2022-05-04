/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.policy;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.vr.IVrManager;
import android.service.vr.IVrStateCallbacks;
import android.util.DisplayMetrics;
import android.util.Slog;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;

import com.android.internal.R;
import com.android.server.vr.VrManagerService;

/**
 *  Helper to manage showing/hiding a confirmation prompt when the navigation bar is hidden
 *  entering immersive mode.
 */
public class ImmersiveModeConfirmation {
    private static final String TAG = "ImmersiveModeConfirmation";
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_SHOW_EVERY_TIME = false; // super annoying, use with caution
    private static final String CONFIRMED = "confirmed";

    private final Context mContext;
    private final H mHandler;
    private final long mShowDelayMs;
    private final long mPanicThresholdMs;

    private boolean mConfirmed;
    private ClingWindowView mClingWindow;
    private long mPanicTime;
    private WindowManager mWindowManager;
    private int mCurrentUserId;
    // Local copy of vr mode enabled state, to avoid calling into VrManager with
    // the lock held.
    boolean mVrModeEnabled = false;

    public ImmersiveModeConfirmation(Context context) {
        mContext = context;
        mHandler = new H();
        mShowDelayMs = getNavBarExitDuration() * 3;
        mPanicThresholdMs = context.getResources()
                .getInteger(R.integer.config_immersive_mode_confirmation_panic);
        mWindowManager = (WindowManager)
                mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    private long getNavBarExitDuration() {
        Animation exit = AnimationUtils.loadAnimation(mContext, R.anim.dock_bottom_exit);
        return exit != null ? exit.getDuration() : 0;
    }

    public void loadSetting(int currentUserId) {
        mConfirmed = false;
        mCurrentUserId = currentUserId;
        if (DEBUG) Slog.d(TAG, String.format("loadSetting() mCurrentUserId=%d", mCurrentUserId));
        String value = null;
        try {
            value = Settings.Secure.getStringForUser(mContext.getContentResolver(),
                    Settings.Secure.IMMERSIVE_MODE_CONFIRMATIONS,
                    UserHandle.USER_CURRENT);
            mConfirmed = CONFIRMED.equals(value);
            if (DEBUG) Slog.d(TAG, "Loaded mConfirmed=" + mConfirmed);
        } catch (Throwable t) {
            Slog.w(TAG, "Error loading confirmations, value=" + value, t);
        }
    }

    private void saveSetting() {
        if (DEBUG) Slog.d(TAG, "saveSetting()");
        try {
            final String value = mConfirmed ? CONFIRMED : null;
            Settings.Secure.putStringForUser(mContext.getContentResolver(),
                    Settings.Secure.IMMERSIVE_MODE_CONFIRMATIONS,
                    value,
                    UserHandle.USER_CURRENT);
            if (DEBUG) Slog.d(TAG, "Saved value=" + value);
        } catch (Throwable t) {
            Slog.w(TAG, "Error saving confirmations, mConfirmed=" + mConfirmed, t);
        }
    }

    void systemReady() {
        IVr