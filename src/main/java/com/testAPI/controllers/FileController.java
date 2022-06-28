package com.testAPI.controllers;

import com.webdav.model.*;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@RestController
@RequestMapping("/api/resources")
public class FileController {
    private final static String WEBDAV_FS_PATH = ""; // TODO: Add the path of the folder where you want to mount your local filesystem for testing

    @GetMapping(path = "/getFSRootPath")
    public String getFSRootPath() {
        return WEBDAV_FS_PATH;
    }

    @GetMapping(path = "/allDirs")
    public FolderItem[] getRootDirs() {
        return getDirsOfParent(new File(WEBDAV_FS_PATH));
    }

    @GetMapping(path = "/allFiles")
    public FileItem[] getRootFiles() {
        return getFilesOfParent(new File(WEBDAV_FS_PATH));
    }

    @PostMapping(path = "/getSubdirs")
    public FolderItem[] getSubDirs(@RequestBody String path) {
        return getDirsOfParent(new File(path));
    }

    @PostMapping(path = "/getSubfiles")
    public FileItem[] getSubFiles(@RequestBody String path) {
        return getFilesOfParent(new File(path));
    }

    @PostMapping(path = "/readFile")
    public byte[] readFileContent(@RequestBody String path) throws IOException {
        return Files.readAllBytes(Path.of(path));
    }

    @PostMapping(path = "/addNewFile/")
    public FileItem addNewFile(@RequestBody FileItem file) throws IOException {
        Files.write(Path.of(file.getPath()), file.getBytes());

        FileItem createdFile = new FileItem(file.getPath());
        createdFile.setName(new File(file.getPath()).getName());

        return createdFile;
    }

    @PostMapping(path = "/addNewFolder")
    public FolderItem addNewFolder(@RequestBody FolderItem newFolder) throws IOException {
        Path pathOfDir = Files.createDirectory(Path.of(newFolder.getPath()));

        return new FolderItem(pathOfDir.toString());
    }

    @PostMapping(path = "/deleteResource")
    public void deleteResource(@RequestBody String filePath) throws IOException {
        FileUtils.delete(new File(filePath));
    }

    private static FileItem[] getFilesOfParent(File parentFile) {
        List<FileItem> subFiles = new ArrayList<>();

        for (File file : Objects.requireNonNull(parentFile.listFiles())) {
            if (!file.isDirectory()) {
                FileItem dir = new FileItem(file.getAbsolutePath());
                dir.setName(file.getName());

                subFiles.add(dir);
            }
        }

        return subFiles.toArray(FileItem[]::new);
    }

    private static FolderItem[] getDirsOfParent(File parentFile) {
        List<FolderItem> subDirs = new ArrayList<>();

        for (File file : Objects.requireNonNull(parentFile.listFiles())) {
            if (file.isDirectory()) {
                FolderItem dir = new FolderItem(file.getAbsolutePath());
                dir.setName(file.getName());

                subDirs.add(dir);
            }
        }

        return subDirs.toArray(FolderItem[]::new);
    }
}
