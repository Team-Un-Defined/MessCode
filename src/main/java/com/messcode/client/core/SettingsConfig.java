package com.messcode.client.core;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.messcode.client.Start;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class is used to retrieve and save the system settings config of the application in the XML file format.
 */
public class SettingsConfig {
    private static File configFile;
    private static Document doc;

    /**
     * This method is used to initialize the reading and editing of the XML file with config.
     * Loads config as Document class from XML file
     */
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

    /**
     * This method finds and returns the requested setting in config
     * @param parameter name of the requested setting
     * @return returns string with actual value of the requested setting
     */
    public static String getConfigOf(String parameter) {
        String result = null;
        NodeList nodes = doc.getElementsByTagName("config");
        Node mynode = nodes.item(0);

        if (mynode.getNodeType() == Node.ELEMENT_NODE) {
            Element myelement = (Element) mynode;

            result = myelement.getElementsByTagName(parameter).item(0).getTextContent();
           
        }
        return result;
    }

    /**
     * This method sets the desired setting in the config and calls function to save it
     * @param stuff name of the setting
     * @param value new value of the setting
     */
    public static void setConfigOf(String stuff, String value) {
        NodeList nodes = doc.getElementsByTagName("config");
        Node mynode = nodes.item(0);
        if (mynode.getNodeType() == Node.ELEMENT_NODE) {
            Element myelement = (Element) mynode;

          

            myelement.getElementsByTagName(stuff).item(0).setTextContent(value);

           
        }
        saveConfig();
    }

    /**
     *  Method saves current loaded config to XML file
     */
    public static void saveConfig() {
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
