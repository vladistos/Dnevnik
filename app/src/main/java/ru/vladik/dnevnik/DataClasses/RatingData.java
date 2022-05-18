package ru.vladik.dnevnik.DataClasses;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RatingData implements Serializable {
    private Double maxVal;
    private List<RatingEntry> entries;

    public RatingData() {
        maxVal = 0D;
        entries = new ArrayList<>();
    }

    public RatingEntry get(int i) {
        return entries.get(i);
    }

    public int getSize() {
        return entries.size();
    }

    public Double getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(Double maxVal) {
        this.maxVal = maxVal;
    }

    public void init() {
        entries.sort((v1, v2) -> Double.compare(v2.value, v1.value));
        for (int i = 0; i < entries.size(); i++) {
            RatingEntry entry = entries.get(i);
            if (i == 0) {
                entry.place = 1;
            } else if (entry.value.equals(entries.get(i - 1).value)){
                entry.place = entries.get(i-1).place;
            } else {
                entry.place = entries.get(i-1).place + 1;
            }
        }
    }

    public void add(RatingEntry entry) {
        entries.add(entry);
    }

    public static class RatingEntry implements Serializable {
        public Double value;
        public int place;
        public String name;

        public RatingEntry() {

        }

        public RatingEntry(String name, Double value) {
            this.value = value;
            this.name = name;
        }
    }
}
