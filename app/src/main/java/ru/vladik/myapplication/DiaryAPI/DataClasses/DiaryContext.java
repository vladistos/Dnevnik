package ru.vladik.myapplication.DiaryAPI.DataClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

public @Data
class DiaryContext {
    private String shortName;
    private Long userId, personId;
    private Object splitId;
    private List<Long> schoolIds, groupIds;
    private List<String> roles, children;
    private List<School> schools;
    private List<EduGroup> eduGroups;

    public DiaryContext(String shortName, Long userId, Long personId, Object splitId,
                        List<Long> schoolIds, List<Long> groupIds, List<String> roles,
                        List<String> children, List<School> schools, List<EduGroup> eduGroups) {
        this.shortName = shortName;
        this.userId = userId;
        this.personId = personId;
        this.splitId = splitId;
        this.schoolIds = schoolIds;
        this.groupIds = groupIds;
        this.roles = roles;
        this.children = children;
        this.schools = schools;
        this.eduGroups = eduGroups;
    }

    public DiaryContext() {
        this("", -1L, -1L, "", new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }
}
