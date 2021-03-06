package com.online.assignment.camelbootservice2.processor;


import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.online.assignment.camelbootservice2.models.User;
import com.online.assignment.camelbootservice2.models.bindy.UserDataCsvRecords;

@Component("userFileProcessor")
public class UserFileProcessor {

    public User readUserFile() throws IOException {

        User user = new User();

        ObjectMapper mapper = new XmlMapper();
        InputStream inputStream = new FileInputStream(new File("files/input/xml/userdata.xml"));
        TypeReference<User> typeReference = new TypeReference<User>() {
        };
        user = mapper.readValue(inputStream, typeReference);
        inputStream.close();

        return user;
    }


    public UserDataCsvRecords convertResponseObjectToBindyFormat(Exchange exchange) {

        User user = exchange.getIn().getBody(User.class);

        UserDataCsvRecords userDataCsvRecords = new UserDataCsvRecords(user);
        return userDataCsvRecords;
    }
}
