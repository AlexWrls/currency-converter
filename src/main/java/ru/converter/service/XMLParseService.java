package ru.converter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.converter.dto.XMLParseDto;
import ru.converter.entity.CurrencyItem;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class XMLParseService {
    private static final String URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final String NUM_CODE = "NumCode";
    private static final String NOMINAL = "Nominal";
    private static final String NAME = "Name";
    private static final String VALUE = "Value";
    private static final String ID = "ID";
    private static final String DATE = "Date";

    public XMLParseDto parseRecourse() {
        List<CurrencyItem> currencyItems = new ArrayList<>();
        XMLParseDto parseDto = new XMLParseDto();
        Node node = getDOM();

        String date = node.getAttributes().getNamedItem(DATE).getNodeValue();
        LocalDate localDate = LocalDate.parse(date, FORMATTER_DATE);

        final NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            final Node valCurse = nodes.item(i);
            if (valCurse.getNodeType() == Node.ELEMENT_NODE) {
                final String id = valCurse.getAttributes().getNamedItem(ID).getNodeValue();
                Element element = (Element) valCurse;
                final String value = element.getElementsByTagName(VALUE).item(0).getTextContent();
                final String name = element.getElementsByTagName(NAME).item(0).getTextContent();
                final String nominal = element.getElementsByTagName(NOMINAL).item(0).getTextContent();
                final String numCode = element.getElementsByTagName(NUM_CODE).item(0).getTextContent();
                CurrencyItem currencyItem = new CurrencyItem();
                currencyItem.setId(id);
                currencyItem.setNumCode(numCode);
                currencyItem.setName(name);
                currencyItem.setNominal(Integer.parseInt(nominal));
                currencyItem.setValue(Double.parseDouble(value.replaceAll(",", ".")));
                currencyItem.setCursDate(localDate);
                currencyItems.add(currencyItem);
            }
        }
        log.trace("Текущий курс валют: " + currencyItems);
        parseDto.setCurrencyItems(currencyItems);
        return parseDto;
    }

    public LocalDate getCursDate() {
        Node node = getDOM();
        String date = node.getAttributes().getNamedItem("Date").getNodeValue();
        log.trace("Текущая дата валюты: " + date);
        return LocalDate.parse(date, FORMATTER_DATE);
    }


    private Node getDOM() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(URL);
            doc.getDocumentElement().normalize();
            return doc.getFirstChild();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.error("Ошибка получения DOM дерева по адресу: " + URL);
            throw new RuntimeException(e.getMessage());
        }
    }


}
