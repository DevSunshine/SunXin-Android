package com.sunshine.sunxin.plugin;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.sunshine.sunxin.plugin.model.PluginInfo;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by gyzhong on 15/11/22.
 */
public class PluginSyncUtil {

    private Context mContext;

    private static PluginSyncUtil instance;

    private PluginSyncUtil(Context context) {
        mContext = context;
    }

    public static PluginSyncUtil getInstance(Context context) {
        if (instance == null) {
            instance = new PluginSyncUtil(context);
        }

        return instance;
    }

    public void syncInit() {
        updateLocalPluginFirstTime();
    }

    public boolean updateLocalPluginFirstTime() {
        boolean update = false;
        if (mContext == null) {
            update = false;
            return update;
        }
        NodeList nodeList;
        int i = 0;
        try {
            InputStream inputStream = mContext.getAssets().open("config/plugins.xml");
            nodeList = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream).
                    getDocumentElement().getElementsByTagName("plugin");
            Log.v("zgy","===========nodeList====="+nodeList.getLength()) ;
            while (i < nodeList.getLength()) {
                PluginInfo pluginInfo = new PluginInfo();
                Element element = (Element) nodeList.item(i);
                pluginInfo.id = element.getAttribute("id");
                pluginInfo.path = element.getAttribute("path");
                pluginInfo.version = element.getAttribute("version");
                pluginInfo.rootFragment = element.getAttribute("rootFragment");
                syncPluginFirstTime(pluginInfo, true);
                i++;
                Log.v("zgy","===========nodeList====="+i) ;
            }
            update = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return update;
    }

    public void syncPluginFirstTime(PluginInfo pluginInfo,boolean refresh){
        if (mContext == null || pluginInfo == null || TextUtils.isEmpty(pluginInfo.id)){
            return;
        }
        PluginInfo cachePluginInfo = PluginCache.getInstance(mContext).getPluginInfo(pluginInfo.id) ;
        if (!refresh && cachePluginInfo != null && pluginInfo.checkVersion(cachePluginInfo)){
            return;
        }
        createPluginLocalPath(pluginInfo);
        if (copyPluginFromAsset(pluginInfo)){
            Log.v("zgy","===========copy=====") ;
            PluginCache.getInstance(mContext).updatePluginInfo(pluginInfo.id,pluginInfo);
        }
    }

    protected boolean copyPluginFromAsset(PluginInfo pluginInfo) {
        boolean copy = false;
        if ((mContext == null) || (pluginInfo == null)) {
            return false;
        }

        try {
            InputStream inputStream = mContext.getAssets().open(pluginInfo.path);
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

    public void createPluginLocalPath(PluginInfo pluginInfo) {
        File plugDir = mContext.getDir("plugin", Context.MODE_PRIVATE);
        plugDir.mkdir();
        if (TextUtils.isEmpty(pluginInfo.id))
            return;
        String localName = new StringBuilder(pluginInfo.id).append(pluginInfo.rootFragment).toString();
        pluginInfo.localPath = (plugDir.getAbsolutePath() + File.separator + localName + ".zip");
    }

}
