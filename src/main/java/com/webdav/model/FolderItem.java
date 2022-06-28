package com.webdav.model;

import io.milton.annotations.Name;

import java.io.Serializable;

public class FolderItem implements Serializable {
    /**
     * The class only stores the name of the directory and its path because the actual retrieval of data
     * (e.g. getting the files and subdirectories) is implemented through API calls.
    **/
    private String name;
    private String path;

    public FolderItem() {}

    public FolderItem(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    @Name
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
