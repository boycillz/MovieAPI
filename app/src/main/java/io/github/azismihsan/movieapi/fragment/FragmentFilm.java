rocessor(mRow,
                com.android.internal.R.id.icon,
                sIconExtractor,
                sIconVisibilityComparator,
                sVisibilityApplicator));
        // To grey them out the icons and expand button when the icons are not the same
        mComparators.add(new HeaderProcessor(mRow,
                com.android.internal.R.id.notification_header,
                sIconExtractor,
                sGreyComparator,
                mGreyApplicator));
        mComparators.add(new HeaderProcessor(mRow,
                com.android.internal.R.id.profile_badge,
                null /* Extractor */,
                new ViewComparator() {
                    @Override
                    public boolean compare(View parent, View child, Object parentData,
                            Object childData) {
                        return parent.getVisibility() != View.GONE;
                    }

                    @Override
                    public boolean isEmpty(View view) {
                        if (view instanceof ImageView) {
                            return ((ImageView) view).getDrawable() == null;
                        }
                        return false;
                    }
                },
                sVisibilityApplicator));
        mComparators.add(HeaderProcessor.forTextView(mRow,
                com.android.internal.R.id.app_name_text));
        mComparators.add(HeaderProcessor.forTextView(mRow,
                com.android.internal.R.id.header_text));
        mDividers.add(com.android.internal.R.id.header_text_divider);
        mDividers.add(com.android.internal.R.id.time_divider);
    }

    public void updateChildrenHeaderAppearance() {
        List<ExpandableNotificationRow> notificationChildren = mRow.getNotificationChildren();
        if (notificationChildren == null) {
            return;
        }
        // Initialize the comparators
        for (int compI = 0; compI < mComparators.size(); compI++) {
            mComparators.get(compI).init();
        }

        // Compare all notification headers
        for (int i = 0; i < notificationChildren.size(); i++) {
            ExpandableNotificationRow row = notificationChildren.get(i);
            for (int compI = 0; compI < mComparators.size(); compI++) {
                mComparators.get(compI).compareToHeader(row);
            }
        }

        // Apply the comparison to the row
        for (int i = 0; i < notificationChildren.size(); i++) {
            ExpandableNotificationRow row = notificationChildren.get(i);
            for (int compI = 0; compI < mComparators.size(); compI++) {
                mComparators.get(compI).apply(row);
