package com.wsi.proshipperformance.api;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public interface ProshipTestService {

    String shipRequest() throws IOException, ParserConfigurationException, SAXException, TransformerException;

}
