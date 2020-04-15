package com.example.lab3;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GroupsCache {
    private static GroupsCache instance;

    public static GroupsCache getInstance() {
        if (instance == null) {
            synchronized (GroupsCache.class) {
                if (instance == null) {
                    instance = new GroupsCache();
                }
            }
        }
        return instance;
    }

    private Set<Group> groups = new LinkedHashSet<>();

    private GroupsCache() {
    }

    @NonNull
    public List<Group> getGroups() {
        return new ArrayList<>(groups);
    }

    public void addGroup(@NonNull Group group) {
        groups.add(group);
    }

    public boolean contains(@NonNull Group group) {
        return groups.contains(group);
    }
}
