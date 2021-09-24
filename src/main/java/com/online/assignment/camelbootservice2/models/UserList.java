package com.online.assignment.camelbootservice2.models;

import java.io.Serializable;
import java.util.List;

import lombok.*;

@Getter @Setter @NoArgsConstructor
@ToString @EqualsAndHashCode
public class UserList implements Serializable {

    private List<User> userData;
}
