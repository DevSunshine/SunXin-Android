// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by zhonggy on 2016/8/8.
 * e-mail guangyanzhong@163.com
 */
public class XMLOperate {

    public static void updateCrcXml(String xmlPath, String pluginId, String version,String fragment,String projectName, long crc) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            Document document = db.parse(xmlPath);
            NodeList nodeList = document.getElementsByTagName("plugin");
            boolean hasInner = false;
            for (int i = 0 ; i < nodeList.getLength(); i ++){
                Element element = (Element) nodeList.item(i);
                if (projectName.equals(element.getAttribute("project"))){
                    element.setAttribute("crc",crc+"");
                    element.setAttribute("path","plugins/"+projectName+".apk");
                    element.setAttribute("id",pluginId);
                    element.setAttribute("version",version);
                    element.setAttribute("rootFragment",fragment);
                    hasInner = true ;
                    break;
                }
            }
            if (!hasInner){
                Element root = document.getDocumentElement() ;
                Element plugin = document.createElement("plugin") ;
                plugin.setAttribute("crc",crc+"");
                plugin.setAttribute("path","plugins/"+projectName+".apk");
                plugin.setAttribute("id",pluginId);
                plugin.setAttribute("project",projectName);
                plugin.setAttribute("version",version);
                plugin.setAttribute("rootFragment",fragment);
                root.appendChild(plugin) ;
            }

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(xmlPath));
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
