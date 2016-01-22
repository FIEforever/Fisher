package com.doyoteam.fisher.learn;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doyoteam.fisher.R;
import com.doyoteam.fisher.db.bean.Article;
import com.doyoteam.fisher.db.bean.Page;
import com.doyoteam.fisher.net.HttpClient;
import com.doyoteam.fisher.net.HttpIn;
import com.doyoteam.fisher.net.learn.LearnHttpIn;
import com.doyoteam.fisher.net.learn.LearnHttpOut;
import com.doyoteam.util.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 学堂子项
 */
public class LearnSubFragment extends Fragment {

    private RecyclerView recyclerView;  // 列表
    private LearnAdapter adapter;         // 适配器
    private int articleType;                // 类型
    private List<Article> articleList;      // 列表
    private Page pageInfo;

    private SwipeRefreshLayout swipeRefreshLayout;
    private View view_progress;
    private TextView view_empty;

    private int curPage = 0;
    private boolean continueLoad;   // 是否加载完

    public LearnSubFragment() {
    }

    public static LearnSubFragment newInstance(int type) {
        LearnSubFragment fragment = new LearnSubFragment();
        Bundle args = new Bundle();
        args.putInt("ARTICLE_TYPE", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            articleType = getArguments().getInt("ARTICLE_TYPE");
        }
        articleList = new ArrayList<>();
        adapter = new LearnAdapter(getActivity(), articleType, articleList);
        continueLoad = true;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_learn_sub, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.setBackgroundResource(R.color.background_light);

        view_progress = rootView.findViewById(R.id.progress_view);
        view_empty = (TextView) rootView.findViewById(R.id.empty_view);
        view_empty.setText("暂无数据");
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        // 设置卷内的颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.primary, R.color
                .primary_dark, R.color.primary_default);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage = 0;
                continueLoad = true;
                gainLearnData();
            }
        });

        if (articleList.size() == 0) initData();
        return rootView;
    }

    // 初始化数据
    private void initData() {
        recyclerView.setAdapter(adapter);
        gainLearnData();
    }

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        boolean isLastRow = false;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            //当滚到最后一行且停止滚动时，执行加载
            if (continueLoad && isLastRow && newState == RecyclerView.SCROLL_STATE_IDLE) {
                //加载元素
                gainLearnData();
                isLastRow = false;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            //判断是否滚到最后一行
            int mItemCount;
            int mLastCompletely;
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager mLayoutManager = (LinearLayoutManager) recyclerView
                        .getLayoutManager();

                mItemCount = mLayoutManager.getItemCount();
                mLastCompletely = mLayoutManager.findLastCompletelyVisibleItemPosition();
                isLastRow = mItemCount == mLastCompletely + 1;
            } else {
                isLastRow = false;
            }
        }
    };

    // 获取活动列表
    private void gainLearnData() {
        view_empty.setText("暂无内容");
        if (curPage == 0) {
            articleList.clear();
            adapter.notifyDataSetChanged();
        }
        curPage++;
        setEmptyView(view_progress);
        switch (articleType)
        {
            case Article.NEW:
                getLearnNet("0",curPage);
                break;
            case Article.ABC:
                getLearnNet("18",curPage);
                break;
            case Article.SKILL:
                getLearnNet("9",curPage);
                break;
            case Article.BAIT:
                getLearnNet("17",curPage);
                break;
            case Article.VIDEO:
                getLearnNet("1",curPage);
                break;
            default:break;
        }
    }
    private void getLearnNet(String typeid,int page)
    {
        LearnHttpIn request = new LearnHttpIn(typeid, page);
        request.setActionListener(new HttpIn.ActionListener<LearnHttpOut>() {
            @Override
            public void onSuccess(LearnHttpOut result) {
                String status = result.getStatus();
                if (status.equals("SUCCESS")) {
                    if(result.getPage()!=null)
                    {
                        pageInfo = result.getPage();
                        if(pageInfo.nextPage.equals("null"))
                        {
                            continueLoad = false;
                        }
                        else
                        if(Integer.parseInt(pageInfo.nextPage) > Integer.parseInt(pageInfo.pageCount))
                        {
                            continueLoad = false;
                        }
                        else
                        {
                            continueLoad = true;
                        }
                    }
                    setArticleData(result.getArticleArrayList());
                } else {
                    Tools.showToast(status);
                }
            }

            @Override
            public void onFailure(String errInfo) {
                Tools.showToast(errInfo);
            }
        });
        HttpClient.get(request);
    }
    private void setArticleData(ArrayList<Article> articleArrayList)
    {
        swipeRefreshLayout.setRefreshing(false);
        setEmptyView(null);
        swipeRefreshLayout.setRefreshing(false);
        articleList.addAll(articleArrayList);
//        if (articleArrayList.size() == articleList.size()) continueLoad = false;
        adapter.notifyDataSetChanged();
        if (articleList.size() == 0) {
            setEmptyView(view_empty);
        }
    }
    public void setEmptyView(View view) {
        if (view == view_progress) {
            view_empty.setVisibility(View.GONE);
            view_progress.setVisibility(View.VISIBLE);
        } else if (view == view_empty) {
            view_empty.setVisibility(View.VISIBLE);
            view_progress.setVisibility(View.GONE);
        } else {
            view_empty.setVisibility(View.GONE);
            view_progress.setVisibility(View.GONE);
        }
    }

}
