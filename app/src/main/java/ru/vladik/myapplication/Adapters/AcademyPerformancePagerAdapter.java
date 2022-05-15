package ru.vladik.myapplication.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.ListFragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ru.vladik.myapplication.Fragments.FinalMarksFragment;

public class AcademyPerformancePagerAdapter extends FragmentStateAdapter {

    public AcademyPerformancePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new FinalMarksFragment();
        } else return new Fragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
