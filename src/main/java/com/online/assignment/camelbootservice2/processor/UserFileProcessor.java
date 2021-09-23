package com.online.assignment.camelbootservice2.processor;


import java.io.*;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.online.assignment.camelbootservice2.models.User;

@Component("userFileProcessor")
public class UserFileProcessor {

    public User readUserFile() throws IOException {

        User user = new User();

        ObjectMapper mapper = new XmlMapper();
        InputStream inputStream = new FileInputStream(new File("files/xml/userdata.xml"));
        TypeReference<User> typeReference = new TypeReference<User>() {
        };
        user = mapper.readValue(inputStream, typeReference);
        inputStream.close();

        return user;
    }
}
