NTED) {
                pw.println("Permission Denial: can't dump MediaProjectionManager from from pid="
                        + Binder.getCallingPid() + ", uid=" + Binder.getCallingUid());
                return;
            }

            final long token = Binder.clearCallingIdentity();
            try {
                MediaProjectionManagerService.this.dump(pw);
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }


        private boolean checkPermission(String packageName, String permission) {
            return mContext.getPackageManager().checkPermission(permission, packageName)
                    == PackageManager.PERMISSION_GRANTED;
        }
    }

    private final class MediaProjection extends IMediaProjection.Stub {
        public final int uid;
        public final String packageName;
        public final UserHandle userHandle;

        private IMediaProjectionCallback mCallback;
        private IBinder mToken;
        private IBinder.DeathRecipient mDeathEater;
        private int mType;

        public MediaProjection(int type, int uid, String packageName) {
            mType = type;
            this.uid = uid;
            this.packageName = packageName;
            userHandle = new UserHandle(UserHandle.getUserId(uid));
        }

        @Override // Binder call
        public boolean canProjectVideo() {
            return mType == MediaProjectionManager.TYPE_MIRRORING ||
                    mType == MediaProjectionManager.TYPE_SCREEN_CAPTURE;
        }

        @Override // Binder call
        public boolean canProjectSecureVideo() {
            return false;
        }

        @Override // Binder call
        public boolean canProjectAudio() {
            return mType == MediaProjectionManager.TYPE_MIRRORING ||
                    mType == MediaProjectionManager.TYPE_PRESENTATION;
        }

        @Override // Binder call
        public int applyVirtualDisplayFlags(int flags) {
            if (mType == MediaProjectionManager.TYPE_SCREEN_CAPTURE) {
                flags &= ~DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY;
                flags |= DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR
                        | DisplayManager.VIRTUAL_DISPLAY_FLAG_PRESENTATION;
                return flags;
            } else if (mType == MediaProjectionManager.TYPE_MIRRORING) {
                flags &= ~(DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC |
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR);
                flags |= DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY |
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_PRESENTATION;
                return flags;
            } else if (mType == MediaProjectionManager.TYPE_PRESENTATION) {
                flags &= ~DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY;
                flags |= DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC |
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_PRESENTATION |
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR;
                return flags;
            } else  {
                throw new RuntimeException("Unknown MediaProjection type");
            }
        }

        @Override // Binder call
        public void start(final IMediaProjectionCallback callback) {
            if (callback == null) {
                throw new IllegalArgumentException("callback must not be null");
            }
            synchronized (mLock) {
                if (isValidMediaProjection(asBinder())) {
                    throw new IllegalStateException(
                            "Cannot start already started MediaProjection");
                }
                mCallback = callback;
                registerCallback(mCallback);
                try {
                    mToken = callback.asBinder();
                    mDeathEater = new IBinder.DeathRecipient() {
                        @Override
                        public void binderDied() {
                            mCallbackDelegate.remove(callback);
                            stop();
                        }
                    };
                    mToken.linkToDeath(mDeathEater, 0);
                } catch (RemoteException e) {
                    Slog.w(TAG,
                            "MediaProjectionCallbacks must be valid, aborting MediaProjection", e);
                    return;
                }
                startProjectionLocked(this);
            }
        }

        @Override // Binder call
        public void stop() {
            synchronized (mLock) {
                if (!isValidMediaProjection(asBinder())) {
                    Slog.w(TAG, "Attempted to stop inactive MediaProjection "
                            + "(uid=" + Binder.getCallingUid() + ", "
                            + "pid=" + Binder.getCallingPid() + ")");
                    return;
                }
                stopProjectionLocked(this);
                mToken.unlinkToDeath(mDeathEater, 0);
                mToken = null;
                unregisterCallback(mCallback);
                mCallback = null;
            }
        }

        @Override
        public void registerCallback(IMediaProjectionCallback callback) {
            if (callback == null) {
                throw new IllegalArgumentException("callback must not be null");
            }
            mCallbackDelegate.add(callback);
        }

        @Override
        public void unregisterCallback(IMediaProjectionCallback callback) {
            if (callback == null) {
                throw new IllegalArgumentException("callback must not be null");
            }
            mCallbackDelegate.remove(callback);
        }

        public MediaProjectionInfo getProjectionInfo() {
            return new MediaProjectionInfo(packageName, userHandle);
        }

        public void dump(PrintWriter pw) {
            pw.println("(" + packageName + ", uid=" + uid + "): " + typeToString(mType));
        }
    }

    private class MediaRouterCallback extends MediaRouter.SimpleCallback {
        @Override
        public void onRouteSelected(MediaRouter router, int type, MediaRouter.RouteInfo info) {
            synchronized (mLock) {
                if ((type & MediaRouter.ROUTE_TYPE_REMOTE_DISPLAY) != 0) {
                    mMediaRouteInfo = info;
    