package com.demo.dao;

import com.demo.models.MyUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {
    private Integer id;
    private String email;
    private String password;
    private String role;
    private String name;
    public void setRole(String role) {
        this.role = role.toUpperCase();
    }

    private String token;
//    private String refreshToken;
    private String expirationTime;
    private int statusCode;
    private String error;
    private String message;

//    user Details;
    private MyUser user;
    private List<MyUser> allUsers;
}
