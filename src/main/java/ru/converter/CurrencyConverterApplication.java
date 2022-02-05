package ru.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.converter.service.SaveXmlParseService;

@SpringBootApplication
public class CurrencyConverterApplication implements ApplicationRunner {

    @Autowired
    private SaveXmlParseService saveXmlParse;


    public static void main(String[] args) {
        SpringApplication.run(CurrencyConverterApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        saveXmlParse.saveXMLParseData();
        saveXmlParse.addRu();
    }
}
