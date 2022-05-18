package ru.vladik.dnevnik.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import ru.vladik.dnevnik.Adapters.RatingAdapter;
import ru.vladik.dnevnik.DataClasses.RatingData;
import ru.vladik.dnevnik.R;
import ru.vladik.dnevnik.Utils.StaticRecourses;
import ru.vladik.dnevnik.databinding.ActivityRaitingBinding;

public class RatingActivity extends AppCompatActivity {

    private ActivityRaitingBinding binding;
    private RecyclerView ratingRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRaitingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ratingRecyclerView = binding.ratingRecycler;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Рейтинг в классе");

        }
        Intent intent = getIntent();
        if (intent != null) {
            RatingData ratingData = (RatingData) intent.getSerializableExtra(StaticRecourses.INTENT_WITH_RATING_NAME);
            RatingAdapter adapter = new RatingAdapter(ratingData);
            ratingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            ratingRecyclerView.setAdapter(adapter);
        }


    }

}
