package com.projet.musichall.group.calendar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projet.musichall.R;

public class GroupCalendarFragment extends Fragment {
    private String currentgroupId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.group_calendar, container, false);
    }

    public void setCurrentgroupId(String currentgroupId) {
        this.currentgroupId = currentgroupId;
    }
}
