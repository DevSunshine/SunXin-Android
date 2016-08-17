package com.sunxin.plugin.fileselector;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.text.TextUtils;

import com.sunshine.sunxin.util.AndroidDevices;
import com.sunshine.sunxin.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by 钟光燕 on 2016/8/16.
 * e-mail guangyanzhong@163.com
 */
public class FileUtil {

    public static ArrayList<String> getStorageDirectories() {
        BufferedReader bufReader = null;
        ArrayList<String> list = new ArrayList<String>();
        list.add(AndroidDevices.EXTERNAL_PUBLIC_DIRECTORY);

        List<String> typeWL = Arrays.asList("vfat", "exfat", "sdcardfs", "fuse", "ntfs", "fat32", "ext3", "ext4", "esdfs");
        List<String> typeBL = Arrays.asList("tmpfs");
        String[] mountWL = {"/mnt", "/Removable", "/storage"};
        String[] mountBL = {
                "/mnt/secure",
                "/mnt/shell",
                "/mnt/asec",
                "/mnt/obb",
                "/mnt/media_rw/extSdCard",
                "/mnt/media_rw/sdcard",
                "/storage/emulated"};
        String[] deviceWL = {
                "/dev/block/vold",
                "/dev/fuse",
                "/mnt/media_rw"};

        try {
            bufReader = new BufferedReader(new FileReader("/proc/mounts"));
            String line;
            while ((line = bufReader.readLine()) != null) {

                StringTokenizer tokens = new StringTokenizer(line, " ");
                String device = tokens.nextToken();
                String mountpoint = tokens.nextToken();
                String type = tokens.nextToken();

                // skip if already in list or if type/mountpoint is blacklisted
                if (list.contains(mountpoint) || typeBL.contains(type) || startsWith(mountBL, mountpoint))
                    continue;

                // check that device is in whitelist, and either type or mountpoint is in a whitelist
                if (startsWith(deviceWL, device) && (typeWL.contains(type) || startsWith(mountWL, mountpoint))) {
                    int position = containsName(list, getFileNameFromPath(mountpoint));
                    if (position > -1)
                        list.remove(position);
                    list.add(mountpoint);
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            Util.close(bufReader);
        }
        return list;
    }

    static boolean startsWith(String[] array, String text) {
        for (String item : array)
            if (text.startsWith(item))
                return true;
        return false;
    }

    static int containsName(List<String> array, String text) {
        for (int i = array.size() - 1; i >= 0; --i)
            if (array.get(i).endsWith(text))
                return i;
        return -1;
    }

    public static String getFileNameFromPath(String path) {
        if (path == null)
            return "";
        int index = path.lastIndexOf('/');
        if (index > -1)
            return path.substring(index + 1);
        else
            return path;
    }

    public static String getParent(String path) {
        if (TextUtils.equals("/", path))
            return path;
        String parentPath = path;
        if (parentPath.endsWith("/"))
            parentPath = parentPath.substring(0, parentPath.length() - 1);
        int index = parentPath.lastIndexOf('/');
        if (index > 0) {
            parentPath = parentPath.substring(0, index);
        } else if (index == 0)
            parentPath = "/";
        return parentPath;
    }

    public static void getFiles(List<FileInfo> fileList, String path) {
        File f = new File(path);
        File[] files = f.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                FileInfo fileInfo = new FileInfo();
                String localName = file.getName();
                fileInfo.name = localName;
                fileInfo.isDir = file.isDirectory();
                fileInfo.path = file.getAbsolutePath();
                fileInfo.fileSize = file.length();
                fileInfo.lastModified = file.lastModified();
                fileInfo.isHide = file.isHidden();
                fileInfo.mParentPath = path;
                if (fileInfo.isDir) {
                    File[] subFiles = file.listFiles();
                    if (subFiles != null){
                        fileInfo.fileCount = subFiles.length ;
                    }
                }
                fileList.add(fileInfo);

            }
        }


        Collections.sort(fileList, new FileComparator());
    }
}
