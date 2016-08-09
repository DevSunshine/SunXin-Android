package com.sunshine.sunxin.plugin;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.sunshine.sunxin.BuildConfig;
import com.sunshine.sunxin.otto.BusProvider;
import com.sunshine.sunxin.plugin.model.PluginInfo;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by gyzhong on 16/8/6.
 */
public class PluginApk {

    public static boolean installed ;
    public static void install(final Context context){

        //异步加载
        new Thread(new Runnable() {
            @Override
            public void run() {
                installed = false ;
                InputStream inputStream = null;
                NodeList nodeList;
                boolean isDebug = false ;
                int position = 0;
                if (BuildConfig.DEBUG){
                    String pluginSrcDir = Environment.getExternalStorageDirectory().getPath() + "/plugins";
                    File file = new File(pluginSrcDir+File.separator+"plugins.xml") ;
                    try {
                        inputStream = new FileInputStream(file) ;
                        isDebug = true ;
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }else {
                    try {
                        inputStream = context.getAssets().open("config/plugins.xml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (inputStream == null){
                    return;
                }
                try {
                    nodeList = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream).
                            getDocumentElement().getElementsByTagName("plugin");
                    while (position < nodeList.getLength()) {
                        PluginInfo pluginInfo = new PluginInfo();
                        Element element = (Element) nodeList.item(position);
                        pluginInfo.id = element.getAttribute("id");
                        pluginInfo.path = element.getAttribute("path");
                        pluginInfo.version = element.getAttribute("version");
                        pluginInfo.rootFragment = element.getAttribute("rootFragment");
                        pluginInfo.crc = Long.parseLong(element.getAttribute("crc"));
                        Log.v("plugin","=============pluginInfo.crc======="+pluginInfo.crc) ;
                        pluginInfo.debug = isDebug ;
                        PluginApkExtractor.loadPlugin(context,pluginInfo,false);
                        position ++;
                    }
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                installed = true ;
                BusProvider.provide().post(new PluginInstalledEvent());
            }
        }).start();
    }

    public static void checkInstall(final Context context, final String pluginId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = null;
                    NodeList nodeList;
                    boolean isSDCard = false ;
                    int position = 0;
                    String pluginSrcDir = Environment.getExternalStorageDirectory().getPath() + "/plugins";
                    File file = new File(pluginSrcDir+File.separator+"plugins.xml") ;
                    try {
                        inputStream = new FileInputStream(file) ;
                        isSDCard = true ;
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    nodeList = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream).
                            getDocumentElement().getElementsByTagName("plugin");
                    while (position < nodeList.getLength()) {
                        PluginInfo pluginInfo = new PluginInfo();
                        Element element = (Element) nodeList.item(position);
                        pluginInfo.id = element.getAttribute("id");
                        pluginInfo.path = element.getAttribute("path");
                        pluginInfo.version = element.getAttribute("version");
                        pluginInfo.rootFragment = element.getAttribute("rootFragment");
                        pluginInfo.crc = Long.parseLong(element.getAttribute("crc"));
                        Log.v("plugin","=============pluginInfo.crc======="+pluginInfo.crc) ;
                        pluginInfo.debug = isSDCard ;
                        if ( pluginInfo.id.equals(pluginId)){
                            PluginApkExtractor.loadPlugin(context.getApplicationContext(),pluginInfo,false);
                            break;
                        }

                        position ++;
                    }
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
