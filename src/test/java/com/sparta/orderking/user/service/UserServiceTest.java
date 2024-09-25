package com.sparta.orderking.user.service;

import com.sparta.orderking.config.PasswordEncoder;
import com.sparta.orderking.domain.auth.dto.AuthUser;
import com.sparta.orderking.domain.user.dto.request.DeleteUserRequestDto;
import com.sparta.orderking.domain.user.dto.request.UpdateProfileRequestDto;
import com.sparta.orderking.domain.user.dto.response.UserResponseDto;
import com.sparta.orderking.domain.user.entity.User;
import com.sparta.orderking.domain.user.entity.UserEnum;
import com.sparta.orderking.domain.user.repository.UserRepository;
import com.sparta.orderking.domain.user.service.UserService;
import com.sparta.orderking.exception.EntityNotFoundException;
import com.sparta.orderking.exception.UnauthorizedAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.sparta.orderking.store.CommonValue.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void CheckAdmin() {
        User user = TEST_USER2;
        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class, () -> {
            userService.checkAdmin(user);
        });

        assertEquals("owner only", exception.getMessage());
    }

    @Test
    void findUser() {
        Long userId = 1L;
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.findUser(userId);
        });

        assertEquals("no such user", exception.getMessage());
    }

    @Test
    void getUser() {
        AuthUser authUser = TEST_AUTHUSER;
        given(userRepository.findById(anyLong())).willReturn(Optional.of(TEST_USER));
        UserResponseDto dto = userService.getUser(authUser);

        assertNotNull(dto);
    }

    @Test
    public void testUpdateUserProfile_Success() {
        UpdateProfileRequestDto requestDto = new UpdateProfileRequestDto("newEmail@example.com", "oldPassword", "newPassword", "newUsername", "newLocation");
        AuthUser authUser = new AuthUser(1L, UserEnum.USER); // 적절한 값으로 설정
        User existingUser = new User("oldUsername", "oldEmail@example.com", "encodedOldPassword", "oldLocation", authUser.getUserEnum());
        when(userRepository.findById(authUser.getUserId())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("oldPassword", existingUser.getPassword())).thenReturn(true);
        when(passwordEncoder.matches("newPassword", existingUser.getPassword())).thenReturn(false);
        when(userRepository.findByUsername("newUsername")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("newEmail@example.com")).thenReturn(Optional.empty());

        userService.updateUserProfile(authUser, requestDto);

        assertEquals("newUsername", existingUser.getUsername());
        assertEquals("newEmail@example.com", existingUser.getEmail());
        assertEquals("newLocation", existingUser.getLocation());
    }

    @Test
    public void testUpdateUserProfile_InvalidCurrentPassword() {
        UpdateProfileRequestDto requestDto = new UpdateProfileRequestDto("newEmail@example.com", "wrongPassword", "newPassword", "newUsername", "newLocation");
        AuthUser authUser = new AuthUser(1L, UserEnum.USER); // 적절한 값으로 설정
        User existingUser = new User("oldUsername", "oldEmail@example.com", "encodedOldPassword", "oldLocation", authUser.getUserEnum());
        when(userRepository.findById(authUser.getUserId())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("wrongPassword", existingUser.getPassword())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUserProfile(authUser, requestDto);
        });

        assertEquals("현재 비밀번호가 틀립니다.", exception.getMessage());
    }

    @Test
    public void testDeleteUser_Success() {
        DeleteUserRequestDto requestDto = new DeleteUserRequestDto("email@example.com", "password");
        AuthUser authUser = new AuthUser(1L, UserEnum.USER); // 적절한 값으로 설정
        User existingUser = new User("username", "email@example.com", "encodedPassword", "location", authUser.getUserEnum());

        when(userRepository.findById(authUser.getUserId())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("password", existingUser.getPassword())).thenReturn(true);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.deleteUser(authUser, requestDto);

        assertTrue(existingUser.isDeleted());
        verify(userRepository).save(existingUser);
    }

    @Test
    public void testDeleteUser_UserAlreadyDeleted() {
        AuthUser authUser = new AuthUser(1L, UserEnum.USER); // 적절한 값으로 설정
        User existingUser = new User("username", "email@example.com", "encodedPassword", "location", authUser.getUserEnum());
        existingUser.setDeleted(true);
        DeleteUserRequestDto requestDto = new DeleteUserRequestDto("email@example.com", "password");

        when(userRepository.findById(authUser.getUserId())).thenReturn(Optional.of(existingUser));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(authUser, requestDto);
        });

        assertEquals("이미 탈퇴한 유저입니다.", exception.getMessage());
    }

    @Test
    public void testDeleteUser_EmailMismatch() {
        DeleteUserRequestDto requestDto = new DeleteUserRequestDto("wrongEmail@example.com", "password");
        AuthUser authUser = new AuthUser(1L, UserEnum.USER); // 적절한 값으로 설정
        User existingUser = new User("username", "email@example.com", "encodedPassword", "location", authUser.getUserEnum());
        when(userRepository.findById(authUser.getUserId())).thenReturn(Optional.of(existingUser));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(authUser, requestDto);
        });

        assertEquals("이메일이 일치하지 않습니다.", exception.getMessage());
    }

    @Test
    public void testDeleteUser_PasswordMismatch() {
        DeleteUserRequestDto requestDto = new DeleteUserRequestDto("email@example.com", "wrongPassword");
        AuthUser authUser = new AuthUser(1L, UserEnum.USER); // 적절한 값으로 설정
        User existingUser = new User("username", "email@example.com", "encodedPassword", "location", authUser.getUserEnum());
        when(userRepository.findById(authUser.getUserId())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("wrongPassword", existingUser.getPassword())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(authUser, requestDto);
        });

        assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
    }
}
