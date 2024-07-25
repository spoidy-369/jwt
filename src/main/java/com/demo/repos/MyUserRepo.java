package com.demo.repos;

import com.demo.models.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
@Repository
public interface MyUserRepo extends JpaRepository<MyUser, Integer> {

    Optional<MyUser> findByEmail(String email);
}
