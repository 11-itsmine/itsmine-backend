//package com.sparta.itsmine.domain.user.repository;
//
//
//import com.sparta.itsmine.domain.user.entity.User;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class UserAdapter {
//
//    private final UserRepository userRepository;
//
//    public User findUserByUsername(String username) {
//        return userRepository.findUserByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException(
//                        String.format("This \"%s\" does not exist.", username)));
//    }
//
//
//}
