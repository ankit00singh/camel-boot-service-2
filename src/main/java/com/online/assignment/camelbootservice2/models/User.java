package com.online.assignment.camelbootservice2.models;

import lombok.*;
import java.io.*;

@Getter @Setter @NoArgsConstructor
@ToString @AllArgsConstructor
public class User implements Serializable {

    private int userId;
    private String name;
    private Long age;
    private String dob;
    private Double salary;

}
