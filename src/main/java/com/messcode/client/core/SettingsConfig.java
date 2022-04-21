package com.messcode.client.core;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SettingsConfig {
    private static File configFile;
    private static Document doc;

    public static void readConfig() {
        ClassLoader classLoader = SettingsConfig.class.getClassLoader();
        configFile = new File(classLoader.getResource("config.xml").getFile());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        doc = null;

        try {
            doc = builder.parse(configFile);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        doc.getDocumentElement().normalize();
    }

    public static String getConfigOf(String stuff){
        String result = null;
        NodeList nodes = doc.getElementsByTagName("config");
        Node mynode = nodes.item(0);

        if (mynode.getNodeType() == Node.ELEMENT_NODE) {
            Element myelement = (Element) mynode;

            result = myelement.getElementsByTagName(stuff).item(0).getTextContent();
            System.out.println("Get config of "+ stuff + ": " + result);
        }
        return result;
    }


    public static void setConfigOf(String stuff, String value){
        NodeList nodes = doc.getElementsByTagName("config");
        Node mynode = nodes.item(0);
        if (mynode.getNodeType() == Node.ELEMENT_NODE) {
            Element myelement = (Element) mynode;
            System.out.println("Set config of "+ stuff + ": " + myelement.getElementsByTagName(stuff).item(0).getTextContent());
            myelement.getElementsByTagName(stuff).item(0).setTextContent(value);
            System.out.println("Updated " + stuff + " to " + myelement.getElementsByTagName(stuff).item(0).getTextContent());
        }
        SaveConfig();
    }


    public static void SaveConfig(){
        // write DOM back to the file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer xtransform;
        DOMSource mydom = new DOMSource(doc);
        StreamResult streamResult = new StreamResult(configFile);

        try {
            xtransform = transformerFactory.newTransformer();
            xtransform.transform(mydom, streamResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}