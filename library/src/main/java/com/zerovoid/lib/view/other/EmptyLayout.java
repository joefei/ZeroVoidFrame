/*
 * Copyright (c) 2015, 张涛.
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
package com.zerovoid.lib.view.other;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zerovoid.lib.util.StringUtil;
import com.zerovoid.lib.util.WindowHelper;
import com.zerovoid.library.R;

/**
 * KJFrame中抽取出来的
 * @author ZhangTao
 */
public class EmptyLayout extends LinearLayout implements
        View.OnClickListener {

    public static final int NETWORK_ERROR = 1; // 网络错误
    public static final int NETWORK_LOADING = 2; // 加载中
    public static final int NODATA = 3; // 没有数据
    public static final int HIDE_LAYOUT = 4; // 隐藏
    private int mErrorState = NETWORK_LOADING;
    private String strNoDataTip = "糟糕，取不到数据勒，点击重试下吧~";
    private String strNoNetTip = "没有网络啊~";
    private String strLoadingTip = "加载中…";
    private OnClickListener listener;
    private boolean clickEnable = true;
    private String strNoDataContent = "";

    private TextView tv;
    public ImageView img;
    //    private ProgressBar animProgress;
    private ProgressWheel progressWheel;
    private Context mContext;

    public EmptyLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        View view = View
                .inflate(getContext(), R.layout.view_error_layout, null);
        img = (ImageView) view.findViewById(R.id.img_error_layout);
        tv = (TextView) view.findViewById(R.id.tv_error_layout);
        progressWheel = (ProgressWheel) view.findViewById(R.id.progress_wheel);

        setBackgroundColor(-1);
        setOnClickListener(this);
        setErrorType(NETWORK_LOADING);

        img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickEnable) {
                    if (listener != null)
                        listener.onClick(v);
                }
            }
        });
        this.addView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowHelper.getScreenHeight(mContext)));
//        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.getScreenHeight(mContext)));
    }

    @Override
    public void onClick(View v) {
        if (clickEnable && listener != null) {
            listener.onClick(v);
        }
    }

    public void dismiss() {
        mErrorState = HIDE_LAYOUT;
        setVisibility(View.GONE);
    }

    public int getErrorState() {
        return mErrorState;
    }

    public boolean isLoadError() {
        return mErrorState == NETWORK_ERROR;
    }

    public boolean isLoading() {
        return mErrorState == NETWORK_LOADING;
    }

    public void setErrorMessage(String msg) {
        tv.setText(msg);
    }

    public void setErrorImag(int imgResource) {
        try {
            img.setImageResource(imgResource);
        } catch (Exception e) {
        }
    }

    public void setNoDataContent(String noDataContent) {
        strNoDataContent = noDataContent;
    }

    public void setOnLayoutClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setTvNoDataContent() {
        if (StringUtil.isBlank(strNoDataContent)) {
            tv.setText(strNoDataTip);
        } else {
            tv.setText(strNoDataContent);
        }
    }

    public void setErrorType(int i) {
        setVisibility(View.VISIBLE);
        switch (i) {
            case NETWORK_ERROR:
                mErrorState = NETWORK_ERROR;
                tv.setText(strNoNetTip);
//            if (SystemTool.isWiFi(getContext())) {
//                img.setBackgroundResource(R.drawable.page_icon_network);
//            } else {
//                img.setBackgroundResource(R.drawable.pagefailed_bg);
//            }
                img.setVisibility(View.VISIBLE);
                progressWheel.setVisibility(View.GONE);
                clickEnable = true;
                break;
            case NETWORK_LOADING:
                mErrorState = NETWORK_LOADING;
                progressWheel.setVisibility(View.VISIBLE);
                img.setVisibility(View.GONE);
//                tv.setText(strLoadingTip);
                clickEnable = false;
                break;
            case NODATA:
                mErrorState = NODATA;
                img.setBackgroundResource(R.drawable.page_icon_empty);
                img.setVisibility(View.VISIBLE);
                progressWheel.setVisibility(View.GONE);
                setTvNoDataContent();
                clickEnable = true;
                break;
            case HIDE_LAYOUT:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE) {
            mErrorState = HIDE_LAYOUT;
        }
        super.setVisibility(visibility);
    }
}
