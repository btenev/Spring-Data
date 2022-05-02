package com.example.football.util.impl;

import com.example.football.util.XmlParser;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class XmlParserImpl implements XmlParser {

    @Override
    @SuppressWarnings("unchecked")
    public <E> E fromFile(Path filePath, Class<E> eClass) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(eClass);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        return (E) unmarshaller.unmarshal(new BufferedReader(Files.newBufferedReader(filePath)));
    }
}
