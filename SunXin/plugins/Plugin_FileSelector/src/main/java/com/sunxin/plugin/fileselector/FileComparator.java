package com.sunxin.plugin.fileselector;

import java.util.Comparator;

public class FileComparator implements Comparator<FileInfo> {

    public int compare(FileInfo file1, FileInfo file2) {

        if (file1.isDir && !file2.isDir) {
            return -1000;
        } else if (!file1.isDir && file2.isDir) {
            return 1000;
        }

        return file1.name.toUpperCase().compareTo(file2.name.toUpperCase());
    }
}