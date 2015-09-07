package com.arkcraft.mod.core.machine.gui.book;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.lib.LogHelper;

public class ManualReader
{
    public static Document readManual (String location)
    {
        try
        {
            LogHelper.info("Loading Manual XML from: " + location);
            InputStream stream = Main.class.getResourceAsStream(location);
            LogHelper.info(stream == null ? "ManualReader - the inputStream with the specified location is null." : "ManualReader - the inputStream is not null with the specified location.");
            return readManual(stream, location);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public static Document readManual (InputStream is, String filenameOrLocation)
    {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        try
        {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();
            LogHelper.info(doc == null ? "ManualReader.readManual(InputStream, String) is returning a null object." : "ManualReader.readManual(InputStream, String) is not returning a null object.");
            return doc;
        }
        catch (Exception e)
        {
            LogHelper.error("Failed to Load Manual XML from: " + filenameOrLocation);
            e.printStackTrace();
            return null;
        }
    }
    	
}