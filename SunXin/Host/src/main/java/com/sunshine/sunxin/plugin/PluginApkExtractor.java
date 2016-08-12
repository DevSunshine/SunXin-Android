package com.sunshine.sunxin.plugin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.sunshine.sunxin.lib.dex.ZipUtil;
import com.sunshine.sunxin.plugin.model.PluginInfo;

import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Created by gyzhong on 16/8/6.
 */
public class PluginApkExtractor {
    private static Method sApplyMethod;
    private static final String PLUGIN_DIR = "plugin";
    private static Map<String,Integer> map = new HashMap<>() ;

    static void loadPlugin(Context context, PluginInfo pluginInfo, boolean forceReload) {
        File plugDir = context.getDir(PLUGIN_DIR, Context.MODE_PRIVATE);
        Log.v("plugin","=============id======="+pluginInfo.id) ;
        String localName ;
        if (pluginInfo.debug){
            if (!map.containsKey(pluginInfo.id)){
                map.put(pluginInfo.id,0) ;
            }
            int temp = map.get(pluginInfo.id) ;
            localName = new StringBuilder(pluginInfo.id).append(pluginInfo.rootFragment).append(temp).append(".zip").toString();
        }else {
            localName = new StringBuilder(pluginInfo.id).append(pluginInfo.rootFragment).append(".zip").toString();
        }
        pluginInfo.localPath = (plugDir.getAbsolutePath() + File.separator + localName);
        File plugin = new File(plugDir, localName);
        long crc = pluginInfo.crc;
        Log.v("plugin","=============crc======="+crc) ;
        if (!forceReload && !isModified( plugin, crc)) {
            Log.v("plugin","=============不需要再次拷贝=======") ;
            // TODO: 16/8/6  不需要拷贝操作
            if (!verifyZipFile(plugin)) {
                loadPlugin(context, pluginInfo, true);
            }
            PluginCache.getInstance().updatePluginInfo(pluginInfo.id, pluginInfo);
        } else {
            //do copy
            try {

                if (pluginInfo.debug){
                    plugin.delete() ;
                    int temp = map.remove(pluginInfo.id) ;
                    temp ++ ;
                    map.put(pluginInfo.id,temp) ;
                    localName = new StringBuilder(pluginInfo.id).append(pluginInfo.rootFragment).append(temp).append(".zip").toString();
                    pluginInfo.localPath = (plugDir.getAbsolutePath() + File.separator + localName);

                }
                File pluginNew = new File(plugDir, localName);

                prepareDexDir(plugDir, localName);
                preformPlugin(context,pluginNew,pluginInfo);
                //
                Log.v("plugin","=============success=======") ;
                PluginCache.getInstance().updatePluginInfo(pluginInfo.id, pluginInfo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void preformPlugin(Context context,File pluginFile,PluginInfo pluginInfo) throws IOException {
        int numAttempts = 0;
        boolean isExtractionSuccessful = false;

        while (numAttempts < 3 && !isExtractionSuccessful) {
            ++numAttempts;
            extract(context, pluginFile, pluginInfo);
            isExtractionSuccessful = verifyZipFile(pluginFile);
            Log.i("Plugin", "Extraction " + (isExtractionSuccessful ? "success" : "failed") + " - length " + pluginFile.getAbsolutePath() + ": " + pluginFile.length());
            if (!isExtractionSuccessful) {
                pluginFile.delete();
                if (pluginFile.exists()) {
                    Log.w("Plugin", "Failed to delete corrupted secondary dex \'" + pluginFile.getPath() + "\'");
                }
            }
        }

        if (!isExtractionSuccessful) {
            throw new IOException("Could not create zip file " + pluginFile.getAbsolutePath());
        }
    }

    private static void extract(Context context, File extractTo, PluginInfo info) throws IOException {
        InputStream inputStream ;
        OutputStream out ;
        File tmp = File.createTempFile(info.id + "temp", ".zip", extractTo.getParentFile());

        if (!info.debug) {
            inputStream = context.getAssets().open(info.path);

        } else {
            String pluginPath = Environment.getExternalStorageDirectory().getPath() +File.separator+info.path;
            inputStream = new FileInputStream(pluginPath) ;
        }
        try {
            out = new FileOutputStream(tmp);
            try {

                byte[] buffer = new byte[16384];
                int i;
                while ((i = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, i);
                }

            } finally {
                out.close();
            }

            Log.i("Plugin", "Renaming to " + extractTo.getPath());
            if(!tmp.renameTo(extractTo)) {
                throw new IOException("Failed to rename \"" + tmp.getAbsolutePath() + "\" to \"" + extractTo.getAbsolutePath() + "\"");
            }
        } finally {
            closeQuietly(inputStream);
            tmp.delete();
        }
    }
    private static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException var2) {
            Log.w("Plugin", "Failed to close resource", var2);
        }

    }
    protected static boolean copyPluginFromAsset(Context context, PluginInfo pluginInfo) {
        boolean copy = false;
        try {
            InputStream inputStream = null;
            if (pluginInfo.debug) {
                inputStream = context.getAssets().open(pluginInfo.path);
            }
            FileOutputStream outputStream = new FileOutputStream(new File(pluginInfo.localPath));
            byte[] arrayOfByte = new byte[4096];
            int i = 0;
            while ((i = inputStream.read(arrayOfByte)) != -1) {
                outputStream.write(arrayOfByte, 0, i);
            }
            copy = true;
            inputStream.close();
            outputStream.close();
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        return copy;

    }

    private static void prepareDexDir(File pluginDir, final String fileName) throws IOException {
        pluginDir.mkdirs();
        if (!pluginDir.isDirectory()) {
            throw new IOException("Failed to create dex directory " + pluginDir.getPath());
        } else {
            FileFilter filter = new FileFilter() {
                public boolean accept(File pathname) {
                    return pathname.getName().equals(fileName);
                }
            };
            File[] files = pluginDir.listFiles(filter);
            if (files != null) {
                File[] plugin = files;
                int len = files.length;

                for (int i = 0; i < len; ++i) {
                    File oldFile = plugin[i];
                    if (!oldFile.delete()) {
                        Log.w("Plugin", "Failed to delete old file " + oldFile.getPath());
                    } else {
                        Log.i("Plugin", "Deleted old file " + oldFile.getPath());
                    }
                }
            }
        }
    }

    private static boolean isModified(File archive, long currentCrc) {
        long fileCrc = -1l;
        try {
            long startTime = System.currentTimeMillis() ;
            fileCrc = ZipUtil.getZipCrc(archive);
            long endTime = System.currentTimeMillis() ;
            Log.v("Plugin","==========fileCrc========="+fileCrc+"=======time="+(endTime - startTime)+"===currentCrc=="+currentCrc) ;

            //4056390309
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileCrc != currentCrc;
    }

    private static long getTimeStamp(File archive) {
        long timeStamp = archive.lastModified();
        if (timeStamp == -1L) {
            --timeStamp;
        }

        return timeStamp;
    }

    private static long getPluginCrc(File archive) {
        long computedValue = 0;
        try {
            computedValue = ZipUtil.getZipCrc(archive);
            if (computedValue == -1L) {
                --computedValue;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return computedValue;
    }

    private static SharedPreferences getMultiDexPreferences(Context context) {
        return context.getSharedPreferences("plugins.version", Build.VERSION.SDK_INT < 11 ? 0 : 4);
    }

    private static void apply(SharedPreferences.Editor editor) {
        if (sApplyMethod != null) {
            try {
                sApplyMethod.invoke(editor, new Object[0]);
                return;
            } catch (InvocationTargetException var2) {
                ;
            } catch (IllegalAccessException var3) {
                ;
            }
        }

        editor.commit();
    }

    static {
        try {
            Class unused = SharedPreferences.Editor.class;
            sApplyMethod = unused.getMethod("apply", new Class[0]);
        } catch (NoSuchMethodException var1) {
            sApplyMethod = null;
        }

    }

    static boolean verifyZipFile(File file) {
        try {
            ZipFile ex = new ZipFile(file);

            try {
                ex.close();
                return true;
            } catch (IOException var3) {
                Log.w("MultiDex", "Failed to close zip file: " + file.getAbsolutePath());
            }
        } catch (ZipException var4) {
            Log.w("MultiDex", "File " + file.getAbsolutePath() + " is not a valid zip file.", var4);
        } catch (IOException var5) {
            Log.w("MultiDex", "Got an IOException trying to open zip file: " + file.getAbsolutePath(), var5);
        }

        return false;
    }
}
