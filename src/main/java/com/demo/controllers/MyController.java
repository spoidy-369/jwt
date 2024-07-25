package com.demo.controllers;

import com.demo.dao.ReqRes;
import com.demo.services.MyUserService;
import com.demo.services.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @Autowired
    UserManagementService service;

    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes reqRes) {
        System.out.println(reqRes);
        ReqRes res = service.login(reqRes);
        return new ResponseEntity<>(
                res, HttpStatusCode.valueOf(res.getStatusCode())
        );
    }

    @PostMapping("/auth/register")
    public ResponseEntity<ReqRes> register(@RequestBody ReqRes reqRes) {
        ReqRes res = service.register(reqRes);
        return new ResponseEntity<>(
                res, HttpStatusCode.valueOf(res.getStatusCode())
        );
    }

    @GetMapping("/test/get")
    public ResponseEntity<String> getAdmin() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
                //.getPrincipal().toString();
        return new ResponseEntity<>("Welcome " + username,HttpStatusCode.valueOf(200));
    }
}
