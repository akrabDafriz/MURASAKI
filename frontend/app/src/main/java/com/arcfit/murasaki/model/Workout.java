package com.arcfit.murasaki.model;

import java.util.List;

public class Workout {
    private String name;
    private List<String> attributes;

    public Workout(String name, List<String> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public List<String> getAttributes() {
        return attributes;
    }
}
