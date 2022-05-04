package io.github.azismihsan.movieapi.listener;

import android.view.View;

public class CustomOnItemListener implements View.OnClickListener {

    private int position;
    private OnItemClickCallBack onItemClickCallBack;

    public CustomOnItemListener(int position, OnItemClickCallBack onItemClickCallBack){
        this.position = position;
        this.onItemClickCallBack = onItemClickCallBack;
    }

    @Override
    public void onClick(View view) {
        onItemClickCallBack.onItemClicked(view, position);
    }

    public interface OnItemClickCallBack{
        void onItemClicked(View view, int positon);
    }
}
