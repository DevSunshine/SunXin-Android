package com.sunshine.sunxin.plugin;

import android.content.Context;
import android.util.Log;

import com.sunshine.sunxin.plugin.model.PluginInfo;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by gyzhong on 16/8/6.
 */
public class PluginApk {

    public static void install(final Context context){

        //异步加载
        new Thread(new Runnable() {
            @Override
            public void run() {

                NodeList nodeList;
                int position = 0;
                try {
                    InputStream inputStream = context.getAssets().open("config/plugins.xml");
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
                            pluginInfo.sdcard = false ;
                            PluginApkExtractor.loadPlugin(context,pluginInfo,false);
                            position ++;
                        }
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    //加载 sdcard 中的插件
                }

            }
        }).start();
    }
}
