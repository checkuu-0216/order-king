package com.sparta.orderking.domain.auth.service;

import com.sparta.orderking.config.JwtUtil;
import com.sparta.orderking.config.PasswordEncoder;
import com.sparta.orderking.domain.user.dto.request.LoginRequestDto;
import com.sparta.orderking.domain.user.dto.request.SignupRequestDto;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public String signup(SignupRequestDto requestDto) {
        // 닉네임 중복 체크
        String username = requestDto.getUsername();
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent() && !checkUsername.get().isDeleted()) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        // 이메일 중복 체크
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent() && !checkEmail.get().isDeleted()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        requestDto.encodedPassword(passwordEncoder.encode(requestDto.getPassword()));
        User user = new User(requestDto);
        userRepository.save(user);
        Long userId = user.getId();

        return jwtUtil.createToken(userId, requestDto.getUserEnum());
    }

    @Transactional(readOnly = true)
    public String login(LoginRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NullPointerException("등록된 사용자가 없습니다.")
        );

        if (user.isDeleted()) {
            throw new IllegalArgumentException("탈퇴한 유저 입니다.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.createToken(user.getId(), user.getUserEnum());
    }

}
