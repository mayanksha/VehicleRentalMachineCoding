package com.navi.model;

import lombok.Data;

@Data
public class Branch {

    /**
     * Unique identifier for a branch. Currently, we'll set the name as this value
     */
    private String ID;

    /**
     * Name of the branch
     */
    private String name;

    public Branch(String ID, String name) {
        this.ID = ID;
        this.name = name;
    }
}
