package ru.converter;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.converter.service.XMLParseService;

@SpringBootApplication
public class CurrencyConverterApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyConverterApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new XMLParseService().parseRecourse();
    }
}
