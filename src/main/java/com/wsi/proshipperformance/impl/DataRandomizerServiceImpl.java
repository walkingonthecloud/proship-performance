package com.wsi.proshipperformance.impl;

import com.wsi.proshipperformance.api.DataRandomizerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Random;

@Component
@Slf4j
public class DataRandomizerServiceImpl implements DataRandomizerService {

    @Value("#{'${proship.services}'.split(',')}")
    private List<String> serviceLevels;

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
        return writer.getBuffer().toString().replaceAll("\n|\r", "");
    }

    private String getRandomNumber(String tagName)
    {
        long range = 0;
        float lengthRange, widthRange, heightRange, length, width, height = 0;
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
            case "WEIGHT":
                range = 100L - 10L + 1;
                min = 10L;
                break;
            case "SERVICE":
                Random random = new Random();
                int index = random.nextInt(serviceLevels.size());
                return serviceLevels.get(index);
            case "DIMENSION":
                lengthRange = 10.50f - 5.00f + 1f;
                widthRange = 10.50f - 5.00f + 1f;
                heightRange = 12.00f - 5.00f + 1f;
                length = (float) Math.random() * lengthRange + 5.00f;
                width = (float) Math.random() * widthRange + 5.00f;
                height = (float) Math.random() * heightRange + 5.00f;

                length = Math.round(length * 100) / 100.0f;
                width = Math.round(width * 100) / 100.0f;
                height = Math.round(height * 100) / 100.0f;

                return String.valueOf(length)
                        .concat("X")
                        .concat(String.valueOf(width)
                                .concat("X")
                                .concat(String.valueOf(height)));
        }
        long rand = (long)(Math.random() * range) + min;
        return String.valueOf(rand);
    }
}
