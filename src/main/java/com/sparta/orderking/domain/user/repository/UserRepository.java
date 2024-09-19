package com.sparta.orderking.domain.user.repository;

import com.sparta.orderking.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
