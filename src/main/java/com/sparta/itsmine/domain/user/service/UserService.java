package com.sparta.itsmine.domain.user.service;


import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.INVALID_PASSWORD;
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
import com.sparta.itsmine.domain.user.utils.UserRole;
import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;
import com.sparta.itsmine.global.exception.DataDuplicatedException;
import com.sparta.itsmine.global.exception.DataNotFoundException;
import com.sparta.itsmine.global.exception.user.UserAlreadyExistsException;
import com.sparta.itsmine.global.exception.user.UserDeletedException;
import com.sparta.itsmine.global.exception.user.UserNotDeletedException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
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

    // @Value("${admin.token}")
    // private String adminToken;
    @Transactional
    public String signup(SignupRequestDto requestDto) {
        if (adapter.existsByUsername(requestDto.getUsername())) {
            throw new UserAlreadyExistsException(ResponseExceptionEnum.USER_ALREADY_EXIST);
        }

        UserRole role = UserRole.USER;
        // 필요시 어드민 토큰 사용
        // if (requestDto.isAdmin()) {
        //     if (!requestDto.getAdminToken().equals(adminToken)) {
        //         throw new AdminTokenNotValidException(ResponseExceptionEnum.)
        //     }
        // }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(requestDto.getUsername(), encodedPassword, requestDto.getName(),
                requestDto.getNickname(), requestDto.getEmail(), role, requestDto.getAddress());
        userRepository.save(user);
        return requestDto.getName();
    }

    public void logout(String username) {
        redisTemplate.delete(username);
    }

    public UserResponseDto getUser(Long userId) {
        User user = userAdapter.findById(userId);
        if (user.getDeletedAt() != null) {
            throw new UserDeletedException(ResponseExceptionEnum.USER_DELETED);
        }
        return new UserResponseDto(user);
    }

    @Transactional
    public void withdraw(User user) {

        if (user.getDeletedAt() != null) {
            throw new UserDeletedException(ResponseExceptionEnum.USER_DELETED);
        }

        user.updateDeletedAt(LocalDateTime.now());
        userAdapter.save(user);
    }

    @Transactional
    public void resign(Long userId) {

        User user = userAdapter.findById(userId);
        if (user.getDeletedAt() == null) {
            throw new UserNotDeletedException(ResponseExceptionEnum.USER_NOT_DELETED);
        }

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

    public void checkUserRole(User user) {
        if (!user.getUserRole().equals(UserRole.MANAGER)) {
            throw new DataDuplicatedException(ResponseExceptionEnum.REPORT_MANAGER_STATUS);
        }
    }

    public String findPassword(PasswordFindRequest passwordFindRequest) {
        Optional<User> optionalUser = userRepository.findUserByUsernameAndNameAndEmail(
                passwordFindRequest.getUsername(), passwordFindRequest.getName(), passwordFindRequest.getEmail());

        if (!optionalUser.isPresent()) {
            return null;
        }

        User user = optionalUser.get();
        String temporaryPassword = generateTemporaryPassword();
        user.setPassword(passwordEncoder.encode(temporaryPassword));
        userRepository.save(user);

        // 인증 객체 갱신
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), temporaryPassword)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return temporaryPassword;
    }

    public void changePassword(PasswordChangeRequest passwordChangeRequest) {
        Optional<User> optionalUser = userRepository.findUserByUsernameAndNameAndEmail(
                passwordChangeRequest.getUsername(), passwordChangeRequest.getName(), passwordChangeRequest.getEmail());

        if(!passwordEncoder.matches(passwordChangeRequest.getCurrentPassword(), optionalUser.get().getPassword())){
            throw new DataNotFoundException(USER_NOT_FOUND);
        }

        User user = optionalUser.get();

        if(!Objects.equals(passwordChangeRequest.getNewPassword(), passwordChangeRequest.getConfirmPassword())){
            throw new DataNotFoundException(INVALID_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
        userRepository.save(user);

        // 인증 객체 갱신
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), passwordChangeRequest.getNewPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    // 10자리 랜덤한 패스워드를 생성하는 메서드
    public String generateTemporaryPassword() {
        int length = 10;
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialCharacters = "!@#$%^&*()";
        String allCharacters = upperCaseLetters + lowerCaseLetters + digits + specialCharacters;

        Random random = new Random();
        StringBuilder password = new StringBuilder();

        // 각 종류에서 최소 하나의 문자 추가
        password.append(upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length())));
        password.append(lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));

        // 나머지 자리수를 랜덤하게 채움
        for (int i = 4; i < length; i++) {
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        // 패스워드를 섞어서 반환
        return shuffleString(password.toString());
    }

    // 문자열을 랜덤하게 섞는 메서드
    private String shuffleString(String input) {
        List<Character> characters = input.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(characters);
        StringBuilder shuffledString = new StringBuilder();
        for (char c : characters) {
            shuffledString.append(c);
        }
        return shuffledString.toString();
    }
}
