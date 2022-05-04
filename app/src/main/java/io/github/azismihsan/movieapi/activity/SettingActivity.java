.isDynamic()) continue;

            if (removeList == null) {
                removeList = new ArrayList<>();
            }
            removeList.add(si.getId());
        }
        if (removeList != null) {
            for (int i = removeList.size() - 1; i >= 0; i--) {
                deleteShortcut(s, removeList.get(i));
            }
        }
    }

    /**
     * Remove all dynamic shortcuts.
     */
    public void deleteAllDynamicShortcuts(@NonNull ShortcutService s) {
        for (int i = mShortcuts.size() - 1; i >= 0; i--) {
            mShortcuts.valueAt(i).clearFlags(ShortcutInfo.FLAG_DYNAMIC);
        }
        removeOrphans(s);
        mDynamicShortcutCount = 0;
    }

    /**
     * Remove a dynamic shortcut by ID.
     */
    public void deleteDynamicWithId(@NonNull ShortcutService s, @NonNull String shortcutId) {
        final ShortcutInfo oldShortcut = mShortcuts.get(shortcutId);

        if (oldShortcut == null) {
            return;
        }
        if (oldShortcut.isDynamic()) {
            mDynamicShortcutCount--;
        }
        if (oldShortcut.isPinned()) {
            oldShortcut.clearFlags(ShortcutInfo.FLAG_DYNAMIC);
        } else {
            deleteShortcut(s, shortcutId);
        }
    }

    /**
     * Called after a launcher updates the pinned set.  For each shortcut in this package,
     * set FLAG_PINNED if any launcher has pinned it.  Otherwise, clear it.
     *
     * <p>Then remove all shortcuts that are not dynamic and no longer pinned either.
     */
    public void refreshPinnedFlags(@NonNull ShortcutService s) {
        // First, un-pin all shortcuts
        for (int i = mShortcuts.size() - 1; i >= 0; i--) {
            mShortcuts.valueAt(i).clearFlags(ShortcutInfo.FLAG_PINNED);
        }

        // Then, for the pinned set for each launcher, set the pin flag one by one.
        s.getUserShortcutsLocked(getPackageUserId()).forAllLaunchers(launcherShortcuts -> {
            final ArraySet<String> pinned = launcherShortcuts.getPinnedShortcutIds(
                    getPackageName(), getPackageUserId());

            if (pinned == null || pinned.size() == 0) {
                return;
            }
            for (int i = pinned.size() - 1; i >= 0; i--) {
                final String id = pinned.valueAt(i);
                final ShortcutInfo si = mShortcuts.get(id);
                if (si == null) {
                    // This happens if a launcher pinned shortcuts from this package, then backup&
                    // restored, but this package doesn't allow backing up.
                    // In that case the launcher ends up having a dangling pinned shortcuts.
                    // That's fine, when the launcher is restored, we'll fix it.
                    continue;
                }
                si.addFlags(ShortcutInfo.FLAG_PINNED);
            }
        });

        // Lastly, remove the ones that are no longer pinned nor dynamic.
        removeOrphans(s);
    }

    /**
     * Number of calls that the caller has made, since the last reset.
     *
     * <p>This takes care of the resetting the counter for foreground apps as well as after
     * locale changes.
     */
    public int getApiCallCount(@NonNull ShortcutService s) {
        mShortcutUser.resetThrottlingIfNeeded(s);

        // Reset the counter if:
        // - the package is in foreground now.
        // - the package is *not* in foreground now, but was in foreground at some point
        // since the previous time it had been.
        if (s.isUidForegroundLocked(mPackageUid)
                || mLastKnownForegroundElapsedTime
                    < s.getUidLastForegroundElapsedTimeLocked(mPackageUid)) {
            mLastKnownForegroundElapsedTime = s.injectElapsedRealtime();
            resetRateLimiting(s);
        }

        // Note resetThrottlingIfNeeded() and resetRateLimiting() will set 0 to mApiCallCount,
        // but we just can't return 0 at this point, because we may have to update
        // mLastResetTime.

        final long last = s.getLastResetTimeLocked();

        final long now = s.injectCurrentTimeMillis();
        if (ShortcutService.isClockValid(now) && mLastResetTime > now) {
            Slog.w(TAG, "Clock rewound");
            // Cloc