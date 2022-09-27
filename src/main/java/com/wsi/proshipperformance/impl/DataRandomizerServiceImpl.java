package com.wsi.proshipperformance.impl;

import com.wsi.proshipperformance.api.DataRandomizerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

@Component
@Slf4j
public class DataRandomizerServiceImpl implements DataRandomizerService {

    public DataRandomizerServiceImpl(){}

    @Override
    public String randomizeElementData(String originalXML, List<String> tagsToRandomize)
            throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new InputSource(new StringReader(originalXML)));
        Element root = document.getDocumentElement();
        for (String tagName: tagsToRandomize)
        {
            root.getElementsByTagName(tagName).item(0).setTextContent(getRandomNumber(tagName));
        }

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
        return output;
    }

    private String getRandomNumber(String tagName)
    {
        long range = 0;
        long min = 0;
        switch(tagName)
        {
            case "CCN_CARTON_NUMBER":
            case "CCN_PICKTKT_CTRL":
                range = 99999999L - 10000000L + 1;
                min = 10000000L;
                break;
            case "CCN_ORDER_NUMBER":
                range = 99999999999999L - 10000000000000L + 1;
                min = 10000000000000L;
                break;
        }
        long rand = (long)(Math.random() * range) + min;
        return String.valueOf(rand);
    }
}
