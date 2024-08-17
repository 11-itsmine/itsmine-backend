package com.sparta.itsmine.domain.user.service;


import static com.sparta.itsmine.domain.user.utils.UserRole.MANAGER;
import static com.sparta.itsmine.domain.user.utils.UserRole.USER;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.REPORT_MANAGER_STATUS;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.USER_ALREADY_EXIST;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.USER_NOT_FOUND;

import com.sparta.itsmine.domain.refreshtoken.repository.RefreshTokenAdapter;
import com.sparta.itsmine.domain.user.dto.BlockResponseDto;
import com.sparta.itsmine.domain.user.dto.PasswordChangeRequest;
import com.sparta.itsmine.domain.user.dto.PasswordFindRequest;
import com.sparta.itsmine.domain.user.dto.ProfileUpdateRequestDto;
import com.sparta.itsmine.domain.user.dto.SignupRequestDto;
import com.sparta.itsmine.domain.user.dto.UserResponseDto;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.domain.user.repository.UserAdapter;
import com.sparta.itsmine.domain.user.repository.UserRepository;
import com.sparta.itsmine.global.exception.DataDuplicatedException;
import com.sparta.itsmine.global.exception.DataNotFoundException;
import com.sparta.itsmine.global.exception.user.UserAlreadyExistsException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserAdapter adapter;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenAdapter refreshTokenAdapter;
    private final UserAdapter userAdapter;
    private final RedisTemplate<String, String> redisTemplate;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public String signup(SignupRequestDto requestDto) {
        if (adapter.existsByUsername(requestDto.getUsername())) {
            throw new UserAlreadyExistsException(USER_ALREADY_EXIST);
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(requestDto.getUsername(), encodedPassword, requestDto.getName(),
                requestDto.getNickname(), requestDto.getEmail(), USER, requestDto.getAddress());
        userRepository.save(user);
        return user.getName();
    }

    @Transactional
    public void logout(String username) {
        redisTemplate.delete(username);
    }

    public UserResponseDto getUser(Long userId) {
        User user = userAdapter.findById(userId);
        userAdapter.isDeleted(user.getUsername());
        return new UserResponseDto(user);
    }

    @Transactional
    public void withdraw(User user) {
        userAdapter.isDeleted(user.getUsername());
        user.updateDeletedAt(LocalDateTime.now());
        userAdapter.save(user);
    }

    @Transactional
    public void resign(Long userId) {
        User user = userAdapter.findById(userId);
        userAdapter.isDeleted(user.getUsername());
        user.updateDeletedAt(null);
        userAdapter.save(user);
    }

    @Transactional
    public void update(User user, ProfileUpdateRequestDto requestDto) {
        user.updateProfile(requestDto);
        userAdapter.save(user);
    }

    public List<UserResponseDto> getUserAllList(User user) {
        checkUserRole(user);
        return userAdapter.findAll().stream()  // 모든 사용자 목록을 스트림으로 변환
                .map(UserResponseDto::new)  // 각 User 객체를 UserResponseDto로 변환
                .collect(Collectors.toList());  // 결과를 리스트로 수집
    }

    public List<BlockResponseDto> blockUserList(User user) {
        checkUserRole(user);
        return userAdapter.blockUserList().stream().map(BlockResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void unBlockUser(User user, Long userId) {
        checkUserRole(user);
        User unBlockUser = userAdapter.findById(userId);
        unBlockUser.unBlock();
    }

    public String findPassword(PasswordFindRequest passwordFindRequest) {
        User user = userAdapter.findUserByUsernameAndNameAndEmail(passwordFindRequest);
        String temporaryPassword = userAdapter.generateTemporaryPassword();
        user.setPassword(passwordEncoder.encode(temporaryPassword));
        userRepository.save(user);

        // 인증 객체 갱신
        renewAuthentication(user.getUsername(), temporaryPassword);
        return temporaryPassword;
    }

    public void changePassword(PasswordChangeRequest passwordChangeRequest) {
        User user = userAdapter.findUserByUsernameAndNameAndEmail(passwordChangeRequest);

        validatePassword(passwordChangeRequest.getNewPassword(), user.getPassword());

        user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
        userRepository.save(user);

        // 인증 객체 갱신
        renewAuthentication(user.getUsername(), user.getPassword());
    }

    public void checkUserRole(User user) {
        if (!user.getUserRole().equals(MANAGER)) {
            throw new DataDuplicatedException(REPORT_MANAGER_STATUS);
        }
    }

    private void renewAuthentication(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void validatePassword(String newPassword, String oldPassword) {
        if (!passwordEncoder.matches(newPassword, oldPassword)) {
            throw new DataNotFoundException(USER_NOT_FOUND);
        }
    }
}
