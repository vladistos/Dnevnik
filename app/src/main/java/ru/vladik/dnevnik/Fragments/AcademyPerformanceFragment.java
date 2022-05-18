package ru.vladik.dnevnik.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import ru.vladik.dnevnik.Adapters.AcademyPerformancePagerAdapter;
import ru.vladik.dnevnik.databinding.FragmentAcademyPerformanceBinding;


public class AcademyPerformanceFragment extends Fragment {

    private FragmentAcademyPerformanceBinding binding;

    public static final String[] TAB_NAMES = new String[] {"четверти", "итог"};

    public AcademyPerformanceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAcademyPerformanceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager2 viewPager = binding.viewPagerAcademyPerformance;
        TabLayout tabs = binding.navigationTabsAcademyPerformance;
        if (getActivity() != null) {
            viewPager.setAdapter(new AcademyPerformancePagerAdapter(getActivity()));
            new TabLayoutMediator(tabs, viewPager, (tab, pos) -> tab.setText(TAB_NAMES[pos])).attach();
        }
    }
}