package com.arkcraft.mod.core.machine.gui.book;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.lib.LogHelper;

public class ManualReader
{
	@SuppressWarnings("unused")
    public static Document readManual (String location)
    {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        try
        {
            LogHelper.info("Loading Manual XML from: " + location);
            InputStream stream = Main.class.getResourceAsStream(location);
            System.out.println(Main.class.getResourceAsStream(location).toString());
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