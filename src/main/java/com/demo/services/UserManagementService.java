package com.demo.services;

import com.demo.dao.ReqRes;
import com.demo.models.MyUser;
import com.demo.repos.MyUserRepo;
import com.demo.services.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserManagementService {

    private final MyUserRepo userRepo;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UserManagementService(MyUserRepo userRepo, JwtUtils jwtUtils, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public ReqRes login(ReqRes reqRes) {
        ReqRes res = new ReqRes();
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            reqRes.getEmail(),reqRes.getPassword()
                    )
            );
            var user = userRepo.findByEmail(reqRes.getEmail()).orElseThrow();
            String token = jwtUtils.generateToken(user);
            res.setStatusCode(200);
            res.setToken(token);
            res.setRole(user.getRole());
            res.setExpirationTime("24Hrs");
            res.setMessage("Successfully Logged in");
        }catch (Exception e) {
            res.setStatusCode(500);
            res.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return res;
    }

    public ReqRes register(ReqRes reqRes) {
        ReqRes res = new ReqRes();
        try{
            MyUser newUser = new MyUser();
            newUser.setEmail(reqRes.getEmail());
            newUser.setPassword(passwordEncoder.encode(reqRes.getPassword()));
            newUser.setRole(reqRes.getRole());
            newUser.setName(reqRes.getName());
            var savedUser = userRepo.save(newUser);
            if(savedUser.getId() > 0) {
                res.setStatusCode(200);
                res.setUser(savedUser);
                res.setRole(reqRes.getRole());
                res.setMessage("User Registered Successfully");
            }
        }catch (Exception e) {
            res.setStatusCode(500);
            res.setMessage(e.getMessage());
            e.printStackTrace();
        }

        return res;
    }
}
