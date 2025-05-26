package com.group_7.antismoking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    String id;
    String name;
    String email;
    boolean gender;
    String password;
    Date dob;
    Date created;
}
