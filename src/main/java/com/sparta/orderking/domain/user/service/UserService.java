package com.sparta.orderking.domain.user.service;

import com.sparta.orderking.config.PasswordEncoder;
import com.sparta.orderking.domain.auth.dto.AuthUser;
import com.sparta.orderking.domain.user.dto.request.DeleteUserRequestDto;
import com.sparta.orderking.domain.user.dto.request.UpdateProfileRequestDto;
import com.sparta.orderking.domain.user.dto.response.UserResponseDto;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.repository.UserRepository;
import com.sparta.orderking.exception.EntityAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 없습니다."));
    }

    public UserResponseDto getUser(AuthUser authUser) {
        return new UserResponseDto(findUserById(authUser.getUserId()));
    }

    @Transactional
    public void updateUserProfile(AuthUser authUser, UpdateProfileRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();
        String newPassword = requestDto.getNewPassword();
        String username = requestDto.getUsername();
        String location = requestDto.getLocation();

        // 기존 유저 엔티티 가져오기
        User findUser = findUserById(authUser.getUserId());

        // 현재 비밀번호가 일치하지 않는 경우
        if(!passwordEncoder.matches(password, findUser.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 틀립니다.");
        }

        // 현재 비밀번호와 바꿀 비밀번호가 일치하는 경우
        if(passwordEncoder.matches(newPassword, findUser.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호와 변경할 비밀번호가 같지 않아야 합니다.");
        }

        // 닉네임 중복 체크
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if(!Objects.equals(findUser.getUsername(), username) && checkUsername.isPresent()) {
            throw new EntityAlreadyExistsException("이미 존재하는 닉네임입니다.");
        }

        // 이메일 중복 체크
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if(!Objects.equals(findUser.getEmail(), email) && checkEmail.isPresent()) {
            throw new EntityAlreadyExistsException("이미 가입된 이메일입니다.");
        }

        // 새로운 정보로 유저 생성
        User user = new User(username, email, passwordEncoder.encode(newPassword),location,authUser.getUserEnum());

        // 업데이트
        findUser.update(user);
    }

    public void deleteUser(AuthUser authUser, DeleteUserRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();
        User user = findUserById(authUser.getUserId());

        // 유저가 이미 탈퇴했는지 확인
        if(user.isDeleted()) {
            throw new IllegalArgumentException("이미 탈퇴한 유저입니다.");
        }

        // 이메일 일치 확인
        if(!user.getEmail().equals(email)) {
            throw new IllegalArgumentException("이메일이 일치하지 않습니다.");
        }

        // 비밀번호 일치 확인
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        user.setDeleted(true);

        userRepository.save(user);
    }

}
