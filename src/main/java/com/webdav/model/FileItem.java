package com.webdav.model;

import io.milton.annotations.Name;

import java.io.Serializable;

public class FileItem implements Serializable {
    /**
     * This class only stores the file name, its path and the bytes of the file. The actual reading of the file's
     * content and file management is implemented through API calls.
     * */
    private String name;
    private String path;
    private byte[] bytes;

    public FileItem() {}

    public FileItem(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    @Name
    public String getName() {
        return this.name;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setBytes(byte[] bytes) {this.bytes = bytes;}
}
