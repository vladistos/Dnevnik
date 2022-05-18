package ru.vladik.dnevnik.DiaryAPI.DataClasses.v2;

import lombok.Data;

public @Data
class School {
    private String fullName, avatarSmall, city, municipality, markType, name, educationType,
            tsoRegionTreePath;
    private Long regionId, timeZone, id, tsoCityId;
    private Boolean uses_avg, uses_weighted_avg;

    public School(String fullName, String avatarSmall, String city, String municipality,
                  String markType, String name, String educationType, String tsoRegionTreePath,
                  Long regionId, Long timeZone, Long id, Long tsoCityId, Boolean uses_avg,
                  Boolean uses_weighted_avg) {
        this.fullName = fullName;
        this.avatarSmall = avatarSmall;
        this.city = city;
        this.municipality = municipality;
        this.markType = markType;
        this.name = name;
        this.educationType = educationType;
        this.tsoRegionTreePath = tsoRegionTreePath;
        this.regionId = regionId;
        this.timeZone = timeZone;
        this.id = id;
        this.tsoCityId = tsoCityId;
        this.uses_avg = uses_avg;
        this.uses_weighted_avg = uses_weighted_avg;
    }

    public School() {
        this("", "", "", "", "", "",
                "", "", -1L, -1L, -1L, -1L,
                false, false);
    }
}
