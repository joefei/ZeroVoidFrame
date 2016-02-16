package com.zerovoid.lib.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

import com.zerovoid.lib.view.yfRecyclerView.YfRecyclerViewAdapter;
import com.zerovoid.lib.view.yfRecyclerView.YfListInterface;


/**
 * YfRecyclerView（某下拉刷新组件的基类）
 * <p/>
 * Created by YangWei on 2015/12/22.
 *
 * @author zv
 */
public abstract class BaseYfRecyclerViewActivity extends TitleBarActivity implements YfListInterface.YfLoadMoreListener {
    protected boolean mLoadingLock = false;
    protected int pageSize = 1;
    protected int pageNo = 8;
    protected boolean isLoadMore = false;
    /** 滑动-加载更多模式的请求 */
    protected final int REQ_MODE_LOAD_MORE = 0;
    /** 滑动-下拉刷新模式的请求 */
    protected final int REQ_MODE_REFRESH = 1;
    /** 初始化的请求 */
    protected final int REQ_MODE_INIT = 2;
    /** 当前的请求模式 */
    protected int currentReqMode = REQ_MODE_INIT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSwipeLayout(getSwipeRefreshLayout());
    }

    /** 获得子类中的mSwipeRefreshLayout的实例，父类中需要用到此实例。如果分类写mSwipeRefreshLayout，子类实例化XXX，可读性不高 */
    protected abstract SwipeRefreshLayout getSwipeRefreshLayout();

    /**
     * 初始化
     *
     * @param swipeRefreshLayout
     */
    protected void initSwipeLayout(final SwipeRefreshLayout swipeRefreshLayout) {
        if (swipeRefreshLayout == null) {
            throw new NullPointerException("SwipeRefreshLayout为空，请检查，1、如果重写了onCreate(Bundle)，则必须调用super.onCreate(Bundle)方法，2、或者调用initSwipeLayout()");
        }
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                refresh();
            }
        });
    }

    protected void showRefreshing() {
        if (getSwipeRefreshLayout() != null) {
            if (getSwipeRefreshLayout().isRefreshing()) {
                getSwipeRefreshLayout().setRefreshing(false);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSwipeRefreshLayout().setRefreshing(true);
                }
            }, 3000);
        }
    }

    protected void hideRefreshing() {
        if (getSwipeRefreshLayout() != null) {
            if (getSwipeRefreshLayout().isRefreshing()) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSwipeRefreshLayout().setRefreshing(false);
                    }
                }, 3000);
            }
        }
    }

    protected abstract void refresh();

    protected abstract void loadMoreData();

    //onLoadMore();

    /** 获取RecyclerViewAdapter */
    protected abstract YfRecyclerViewAdapter getAdapter();

    final String strLoading = "正在拼命加载中...";
    private boolean isFirstEnableAutoLoadMore = true;

    @Override
    public void loadMore() {
        YfRecyclerViewAdapter adapter = getAdapter();
        if (adapter == null) {
            throw new NullPointerException("getAdapter()返回的Adapter不能为空...");
        }
        if (mLoadingLock) {
            return;
        }
        if (adapter.getData().size() < adapter.getTotalNum() && adapter.getData().size() > 0) {
            // has more
            if (!adapter.getFooters().contains(strLoading)) {
                adapter.addFooter(strLoading);
            }
            if (!isFirstEnableAutoLoadMore) {
                mLoadingLock = true;
                pageNo++;
                isLoadMore = true;
                loadMoreData();
            } else {
                isFirstEnableAutoLoadMore = false;
            }
        } else {
            // no more
            if (adapter.getFooters().contains(strLoading)) {
                adapter.removeFooter(strLoading);
            }
        }
    }
}
