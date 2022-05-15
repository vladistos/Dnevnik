package ru.vladik.myapplication.Fragments;

import static ru.vladik.myapplication.Utils.StaticRecourses.UserContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import ru.vladik.myapplication.Adapters.MarksAdapter;
import ru.vladik.myapplication.Adapters.NewsAdapter;
import ru.vladik.myapplication.DiaryAPI.DataClasses.v6.ImportantRecent;
import ru.vladik.myapplication.DiaryAPI.DataClasses.webApi.FeedPostList;
import ru.vladik.myapplication.DiaryAPI.DataClasses.v2.FullMark;
import ru.vladik.myapplication.DiaryAPI.DiaryAPI;
import ru.vladik.myapplication.R;
import ru.vladik.myapplication.Utils.AsyncUtil;
import ru.vladik.myapplication.Utils.DiarySingleton;
import ru.vladik.myapplication.Utils.LayoutHelper;
import ru.vladik.myapplication.databinding.FragmentMainBinding;
import ru.vladik.myapplication.databinding.HeaderToNewsBinding;

public class MainFragment extends Fragment {

    private DiaryAPI diaryAPI;
    private FragmentMainBinding bindingMain;
    private ListView newsListView;
    private RecyclerView marksRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HeaderToNewsBinding bindingHeader;

    private List<FullMark> fullMarkList;
    private FeedPostList feedPostList;

    private ImportantRecent news;

    public MainFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bindingMain = FragmentMainBinding.inflate(inflater, container, false);
        if (getContext() != null && getActivity() != null) {
            diaryAPI = DiarySingleton.getInstance(getContext()).getDiaryAPI();

            bindingHeader = HeaderToNewsBinding.inflate(getActivity().getLayoutInflater());

            marksRecyclerView = bindingHeader.marksMainRecycler;
            newsListView = bindingMain.newsFeedList;
            swipeRefreshLayout = bindingMain.swipeRefreshLayoutInMainFragment;

            marksRecyclerView.setLayoutManager(new LinearLayoutManager(
                    getContext(), RecyclerView.HORIZONTAL, false
            ));
            marksRecyclerView.setAdapter(new MarksAdapter(getContext()));

            newsListView.setAdapter(new NewsAdapter(getContext(), R.layout.wall_feed_element));
            newsListView.setDivider(null);
            newsListView.addHeaderView(bindingHeader.getRoot());

            LayoutHelper.setLoading(bindingMain.getRoot(), true, null);
        }
        swipeRefreshLayout.setOnRefreshListener(() -> loadData(true));
        return bindingMain.getRoot();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        loadData(false);
    }

    private void loadData(boolean refresh) {
        AsyncUtil.startAsyncTask(() -> {
            MarksAdapter marksAdapter = (MarksAdapter) marksRecyclerView.getAdapter();
            NewsAdapter newsAdapter = (NewsAdapter) ((HeaderViewListAdapter) newsListView.getAdapter()).getWrappedAdapter();
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

                if (news == null || refresh) {
                    news = diaryAPI.getRecentNewsWithSubjectNames(UserContext.getPersonId(), UserContext.getGroupIds().get(0));
                }

                AsyncUtil.executeInMain(() -> {
                    marksAdapter.refreshList(news.getRecentMarks());
                    newsAdapter.refreshList(news.getFeed());
                    swipeRefreshLayout.setRefreshing(false);
                    LayoutHelper.setLoading(bindingMain.getRoot(), false, null);
                });

            }
        });
    }

}