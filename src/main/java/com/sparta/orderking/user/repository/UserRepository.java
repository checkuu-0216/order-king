package com.sparta.orderking.user.repository;

import com.sparta.orderking.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
