package com.webdav.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webdav.model.*;
import io.milton.annotations.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ResourceController
public class FSController {
    private final static String fileBaseURI = "http://localhost:8081/api/resources/";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String WEBDAV_FS_PATH;

    static {
        // Make a request to get the path of the root directory in the file system
        // in order to provide the full path of files and directories to be added
        // in the root (because these depend on where the file system is mounted).
        try {
            URI uri = new URI(fileBaseURI + "getFSRootPath");
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

            WEBDAV_FS_PATH = response.getBody();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Root
    public FSController getRoot() {
        return this;
    }

    @ChildrenOf
    public List<FolderItem> getRootDirectories(FSController root) throws URISyntaxException, JsonProcessingException {
        URI uri = new URI(fileBaseURI + "allDirs");
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        FolderItem[] rootDirs = objectMapper.readValue(response.getBody(), FolderItem[].class);

        return Arrays.asList(rootDirs);
    }

    @ChildrenOf
    public List<FileItem> getRootFiles(FSController root) throws URISyntaxException, JsonProcessingException {
        URI uri = new URI(fileBaseURI + "allFiles");
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        FileItem[] rootFiles = objectMapper.readValue(response.getBody(), FileItem[].class);

        return Arrays.asList(rootFiles);
    }

    @ChildrenOf
    public Collection<FolderItem> getSubDirs(FolderItem folder) throws URISyntaxException, JsonProcessingException {
        URI uri = new URI(fileBaseURI + "getSubdirs");
        ResponseEntity<String> response = restTemplate.postForEntity(uri, folder.getPath(), String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        FolderItem[] rootFiles = objectMapper.readValue(response.getBody(), FolderItem[].class);

        return Arrays.asList(rootFiles);
    }

    @ChildrenOf
    public Collection<FileItem> getSubFiles(FolderItem folder) throws URISyntaxException, JsonProcessingException {
        URI uri = new URI(fileBaseURI + "getSubfiles");
        ResponseEntity<String> response = restTemplate.postForEntity(uri, folder.getPath(), String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        FileItem[] rootFiles = objectMapper.readValue(response.getBody(), FileItem[].class);

        return Arrays.asList(rootFiles);
    }

    @Get
    public byte[] getFileContent(FileItem file) throws URISyntaxException, IOException {
        URI uri = new URI(fileBaseURI + "readFile/");
        ResponseEntity<byte[]> response = restTemplate.postForEntity(uri, file.getPath(), byte[].class);

        return response.getBody();
    }

    @PutChild
    public FileItem putFile(FolderItem parent, String name, byte[] bytes) throws URISyntaxException {
        String newFilePath = String.valueOf(Paths.get(parent.getPath(), name));
        FileItem newFile = new FileItem(newFilePath);
        newFile.setBytes(bytes);

        URI uri = new URI(fileBaseURI + "addNewFile/");
        ResponseEntity<FileItem> response = restTemplate.postForEntity(uri, newFile, FileItem.class);

        return response.getBody();
    }

    @PutChild
    public FileItem putFile(FSController root, String name, byte[] bytes) throws URISyntaxException {
        // Special implementation required for adding files to the root of the filesystem.
        // This is required because the request for adding a file at the root of the file system will not match
        // the (FolderItem, String, byte[]) signature.
        String newFilePath = String.valueOf(Paths.get(WEBDAV_FS_PATH, name));
        FileItem newFile = new FileItem(newFilePath);
        newFile.setBytes(bytes);

        URI uri = new URI(fileBaseURI + "addNewFile/");
        ResponseEntity<FileItem> response = restTemplate.postForEntity(uri, newFile, FileItem.class);

        return response.getBody();
    }

    @MakeCollection
    public FolderItem putFolder(FolderItem parent, String name) throws URISyntaxException {
        String newFolderPath = String.valueOf(Paths.get(parent.getPath(), name));
        FolderItem newFolder = new FolderItem(newFolderPath);

        URI uri = new URI(fileBaseURI + "addNewFolder/");
        ResponseEntity<FolderItem> response = restTemplate.postForEntity(uri, newFolder, FolderItem.class);

        return response.getBody();
    }

    @MakeCollection
    public FolderItem putFolder(FSController root, String name) throws URISyntaxException {
        String newFolderPath = String.valueOf(Paths.get(WEBDAV_FS_PATH, name));
        FolderItem newFolder = new FolderItem(newFolderPath);

        URI uri = new URI(fileBaseURI + "addNewFolder/");
        ResponseEntity<FolderItem> response = restTemplate.postForEntity(uri, newFolder, FolderItem.class);

        return response.getBody();
    }

    @Delete
    public void deleteFile(FileItem file) throws URISyntaxException {
        URI uri = new URI(fileBaseURI + "deleteResource/");
        restTemplate.postForEntity(uri, file.getPath(), Void.class);
    }

    @Delete
    public void deleteFolder(FolderItem folder) throws URISyntaxException {
        URI uri = new URI(fileBaseURI + "deleteResource/");
        restTemplate.postForEntity(uri, folder.getPath(), Void.class);
    }
}
