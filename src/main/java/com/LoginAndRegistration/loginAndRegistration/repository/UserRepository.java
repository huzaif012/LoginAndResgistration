package com.LoginAndRegistration.loginAndRegistration.repository;

import com.LoginAndRegistration.loginAndRegistration.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserNameOrEmail(String userName, String email);

    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
}
