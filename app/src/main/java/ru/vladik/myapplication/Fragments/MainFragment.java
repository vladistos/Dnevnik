package ru.vladik.myapplication.Fragments;

import static ru.vladik.myapplication.Utils.StaticRecourses.UserContext;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Collections;
import java.util.List;

import ru.vladik.myapplication.Adapters.MarksAdapter;
import ru.vladik.myapplication.Adapters.NewsAdapter;
import ru.vladik.myapplication.DiaryAPI.DataClasses.FeedPost.FeedPost;
import ru.vladik.myapplication.DiaryAPI.DataClasses.FeedPost.FeedPostList;
import ru.vladik.myapplication.DiaryAPI.DataClasses.FullMark;
import ru.vladik.myapplication.DiaryAPI.DiaryAPI;
import ru.vladik.myapplication.R;
import ru.vladik.myapplication.Utils.AsyncUtil;
import ru.vladik.myapplication.Utils.DiarySingleton;
import ru.vladik.myapplication.Utils.LayoutHelper;
import ru.vladik.myapplication.Utils.StaticRecourses;

import ru.vladik.myapplication.DiaryAPI.DataClasses.FeedPost.Reactions;

public class MainFragment extends Fragment {

    private final DiaryAPI diaryAPI;
    private RecyclerView marksRecyclerView;
    private ListView newsListView;
    private FeedPostList feedPostList;
    private List<FullMark> fullMarkList;
    private SwipeRefreshLayout refreshLayout;
    private ViewGroup parent;

    public MainFragment() {
        diaryAPI = DiarySingleton.getInstance().getDiaryAPI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        newsListView = view.findViewById(R.id.news_feed_list);
        refreshLayout = view.findViewById(R.id.swipe_refresh_layout_in_main_fragment);
        parent = (ViewGroup) view;
        if (getContext() != null && getActivity() != null) {
            newsListView.setAdapter(new NewsAdapter(getContext(), R.layout.wall_feed_element));
            View headerView = getActivity().getLayoutInflater().inflate(R.layout.header_to_news, null);
            newsListView.addHeaderView(headerView);
            newsListView.setDivider(null);
            marksRecyclerView = headerView.findViewById(R.id.marks_main_recycler);
            marksRecyclerView.setLayoutManager(new LinearLayoutManager(
                    getContext(), RecyclerView.HORIZONTAL, false
            ));
            marksRecyclerView.setAdapter(new MarksAdapter(getContext()));
            LayoutHelper.setLoading(parent, true, null);
        }
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(true);
            }
        });
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        loadData(false);
    }

    private void loadData(boolean refresh) {
        AsyncUtil.startAsyncTask(() -> {
            MarksAdapter marksAdapter = (MarksAdapter) marksRecyclerView.getAdapter();
            NewsAdapter newsAdapter = (NewsAdapter) ((HeaderViewListAdapter)newsListView.getAdapter()).getWrappedAdapter();
            if (marksAdapter != null && (refresh || marksAdapter.getItemCount() == 0)) {
                if (fullMarkList == null || refresh) {
                    fullMarkList = diaryAPI.getRecentPersonMarks(
                            UserContext.getPersonId(),
                            UserContext.getGroupIds().get(0)
                    );
                }
                if (feedPostList == null || refresh) {
                    feedPostList = diaryAPI.getEduGroupPosts(
                            UserContext.getSchoolIds().get(0),
                            UserContext.getGroupIds().get(0),
                            null,
                            null
                    );
                }

                AsyncUtil.executeInMain(() -> {
                    marksAdapter.refreshList(fullMarkList);
                    newsAdapter.refreshList(feedPostList.getPosts());
                    refreshLayout.setRefreshing(false);
                    LayoutHelper.setLoading(parent, false, null);
                });

            }
        });
    }

}