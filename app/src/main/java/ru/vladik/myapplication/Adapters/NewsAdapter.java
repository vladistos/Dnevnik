package ru.vladik.myapplication.Adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import ru.vladik.myapplication.DiaryAPI.DataClasses.FeedPost.FeedPost;
import ru.vladik.myapplication.DiaryAPI.DataClasses.FeedPost.File;
import ru.vladik.myapplication.DiaryAPI.DataClasses.FeedPost.Reaction;
import ru.vladik.myapplication.DiaryAPI.DataClasses.FeedPost.Reactions;
import ru.vladik.myapplication.DiaryAPI.DiaryAPI;
import ru.vladik.myapplication.R;
import ru.vladik.myapplication.Utils.AsyncUtil;
import ru.vladik.myapplication.Utils.DateHelper;
import ru.vladik.myapplication.Utils.DiarySingleton;
import ru.vladik.myapplication.Utils.DrawableHelper;
import ru.vladik.myapplication.Utils.LayoutHelper;
import ru.vladik.myapplication.Views.SelectableGridView;

public class NewsAdapter extends ArrayAdapter<FeedPost> {

    private final int resourceLayout;
    private final DiaryAPI diaryAPI;

    public NewsAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        diaryAPI = DiarySingleton.getInstance().getDiaryAPI();
        resourceLayout = resource;
    }

    public void refreshList(List<FeedPost> feedPostList) {
        clear();
        addAll(feedPostList);
        AsyncUtil.startAsyncTask(() -> {
            for (FeedPost feedPost : feedPostList) {
                if (feedPost.getLogoDrawable() == null) {
                    try {
                        if (!feedPost.getAuthor().getAvatarUrl().isEmpty()) {
                            feedPost.setLogoDrawable(DrawableHelper.drawableFromUrl(feedPost.getAuthor().getAvatarUrl()));
                        } else {
                            feedPost.setLogoDrawable(DrawableHelper.drawableFromUrl(feedPost.getTopicLogoUrl()));
                        }
                        AsyncUtil.executeInMain(this::notifyDataSetChanged);
                    } catch (IOException e) {
                        TypedValue typedValue = new TypedValue();
                        getContext().getTheme().resolveAttribute(R.attr.colorOnPrimary, typedValue, false);
                        feedPost.setLogoDrawable(new ColorDrawable(typedValue.data));
                    }

                }
            }
            AsyncUtil.executeInMain(this::notifyDataSetChanged);
        });
        notifyDataSetChanged();
    }

    private String removeDuplicatesOf(String text, String regex) {
        Scanner scanner = new Scanner(text);
        scanner.useDelimiter(regex);
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNext()) {
            String next = scanner.next();
            stringBuilder.append(next);
            if (!next.isEmpty()) {
                stringBuilder.append(regex);
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(resourceLayout, null);
        }

        FeedPost feedPost = getItem(position);

        TextView authorTextView = view.findViewById(R.id.author_in_wall_post_item);
        TextView contentTextView = view.findViewById(R.id.content_in_wall_post_item);
        TextView viewsTextView = view.findViewById(R.id.views_in_wall_post_item);
        TextView commentsTextView = view.findViewById(R.id.comments_text_view_in_wall_post_item);
        TextView dateTextView = view.findViewById(R.id.date_in_wall_post_item);
        ImageView avatarImageView = view.findViewById(R.id.avatar_in_wall_post_item);
        SelectableGridView reactionsGrid = view.findViewById(R.id.emoji_count_layout_in_wall_post_item);

        List<Reaction> reactions = feedPost.getLikes().getVotes().getReactionsListWithoutEmpty();
        reactionsGrid.setAdapter(
                new ReactionsAdapter(
                        getContext(),
                        R.layout.reaction_item,
                        reactions,
                        reactionsGrid.getSelectedItemBackground()
                )
        );

        int userVotePos = -1;
        for (int i = 0; i < reactions.size(); i++) {
            if (feedPost.getLikes().getUserVoteId() == Reactions.getEmotionId(reactions.get(i).getEmoji().getName())) {
                userVotePos = i;
                break;
            }
        }

        reactionsGrid.setEnabled(false);

        ((ReactionsAdapter) reactionsGrid.getAdapter()).selectPosition(userVotePos);
        int finalUserVotePos = userVotePos;

        ((ReactionsAdapter) reactionsGrid.getAdapter()).setOnItemClickListener(new LayoutHelper.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int position) {
                if (feedPost.getThread() != null) {
                    String eventKey = feedPost.getThread().getEventKey();
                    if (finalUserVotePos == position) {
                        AsyncUtil.startAsyncTask(() -> {
                            feedPost.setLikes(diaryAPI.setReactionOnPost(
                                    eventKey,
                                    Reactions.getEmotion(Reactions.NOT_SET).getName()
                            ));
                            AsyncUtil.executeInMain(() -> notifyDataSetChanged());
                        });
                    } else if (position != reactionsGrid.getAdapter().getCount() - 1){
                        AsyncUtil.startAsyncTask(() -> {
                            feedPost.setLikes(diaryAPI.setReactionOnPost(
                                    eventKey,
                                    reactions.get(position).getEmoji().getName()
                            ));
                            AsyncUtil.executeInMain(() -> notifyDataSetChanged());
                        });
                    } else if (position == reactionsGrid.getAdapter().getCount() - 1) {
                        RecyclerView reactionsRecyclerView = (RecyclerView) LayoutInflater.from(getContext())
                                .inflate(R.layout.reactions_popup, (ViewGroup) view, false);

                        PopupWindow popupWindow = new PopupWindow(reactionsRecyclerView,
                                RecyclerView.LayoutParams.WRAP_CONTENT,
                                RecyclerView.LayoutParams.WRAP_CONTENT);

                        popupWindow.setBackgroundDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.solid_rounded));
                        popupWindow.setOutsideTouchable(true);

                        AllReactionsAdapter adapter = new AllReactionsAdapter();

                        reactionsRecyclerView.setAdapter(adapter);
                        reactionsRecyclerView.setLayoutManager(
                                new LinearLayoutManager(
                                        getContext(), LinearLayoutManager.HORIZONTAL,
                                        false
                                )
                        );

                        adapter.setOnItemClickListener(new LayoutHelper.OnItemClickListener() {
                            @Override
                            public void onItemClick(ViewGroup parent, View view, int position) {
                                AsyncUtil.startAsyncTask(() -> {
                                    feedPost.setLikes(diaryAPI.setReactionOnPost(
                                            eventKey,
                                            Reactions.getEmotion(position).getName()
                                    ));
                                    AsyncUtil.executeInMain(() -> {
                                        notifyDataSetChanged();
                                        popupWindow.dismiss();
                                    });
                                });
                            }
                        });

                        popupWindow.setElevation(10);
                        popupWindow.showAsDropDown(view);
                    }
                }
            }
        });

        StringBuilder contentTextBuilder = new StringBuilder(removeDuplicatesOf(
                HtmlCompat.fromHtml(feedPost.getText(), HtmlCompat.FROM_HTML_MODE_COMPACT)
                        .toString(),
                "\n"
        ));

        if (feedPost.getAttachmentsContainer() != null && !feedPost.getAttachmentsContainer().isEmpty()) {
            contentTextBuilder.append("\n");
            for (File file : feedPost.getAttachmentsContainer().getFiles()) {
                contentTextBuilder.append("Прикреплен файл ").append(file.getName()).append(".\n");
                contentTextBuilder.append("Скачать по ссылке: ");
                contentTextBuilder.append(file.getUrl()).append("\n");
            }
        }

        String contentText = contentTextBuilder.toString();

        while (contentText.endsWith("\n")) {
            contentText = contentText.substring(0, contentText.length()-2);
        }


        avatarImageView.setImageDrawable(feedPost.getLogoDrawable());

        try {
            dateTextView.setVisibility(View.VISIBLE);
            dateTextView.setText(DateHelper.getStringDateWithTime(new Date(Long.parseLong(feedPost.getCreatedDateUtc())*1000)));
        } catch (Exception e) {
            dateTextView.setVisibility(View.INVISIBLE);
        }
        authorTextView.setText(feedPost.getAuthor().getName());
        contentTextView.setText(contentText);
        viewsTextView.setText(feedPost.getViewsCount());
        commentsTextView.setText(feedPost.getCommentsCount());

        return view;
    }
}
