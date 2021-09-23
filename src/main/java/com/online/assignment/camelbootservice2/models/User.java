package com.online.assignment.camelbootservice2.models;

import lombok.*;
import java.io.*;

@Getter @Setter @NoArgsConstructor
@ToString
public class User {

    private int userId;
    private String name;
    private Long age;
    private String dob;
    private Double salary;
}
