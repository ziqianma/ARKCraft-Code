package com.arkcraft.mod.core.machine.gui.book;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.w3c.dom.Document;

import com.arkcraft.mod.core.Main;


public class DClient extends DCommon {
	
	public static Minecraft mc;
	public static SmallFontRenderer smallFontRenderer;
    
	public static Document dossier;
    public static DossierInfo dossierInfo;
    
	public void init() {
		Minecraft mc = Minecraft.getMinecraft();
		smallFontRenderer = new SmallFontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);
		dossierInfo = new DossierInfo();
		readManuals();
	}
	
	public void readManuals() {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        String CurrentLanguage = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();

        Document dossier_cl = readManual("/assets/arkcraft/dossier/" + CurrentLanguage + "/dinodossier.xml", dbFactory);

        dossier = dossier_cl != null ? dossier_cl : readManual("/assets/arkcraft/dossier/en_US/dinodossier.xml", dbFactory);

        initManualPages();
    }
	
	Document readManual(String location, DocumentBuilderFactory dbFactory) {
        try {
            InputStream stream = Main.class.getResourceAsStream(location);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static Document getDossier() {
		return dossier;
	}
	
	void initManualPages() {}
}
