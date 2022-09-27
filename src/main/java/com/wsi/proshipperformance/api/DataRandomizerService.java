package com.wsi.proshipperformance.api;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

public interface DataRandomizerService {

    String randomizeElementData(String originalXML, List<String> tagsToRandomize) throws ParserConfigurationException, IOException, SAXException, TransformerException;

}
