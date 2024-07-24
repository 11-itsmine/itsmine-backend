package com.sparta.itsmine.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.domain.user.repository.UserAdapter;
import com.sparta.itsmine.domain.user.repository.UserRepository;
import com.sparta.itsmine.domain.user.service.UserService;
import com.sparta.itsmine.domain.user.utils.UserRole;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

	User user;

	@Mock
	UserAdapter adapter;

	@InjectMocks
	UserService userService;

	@BeforeEach
	void init() {
		user = User.builder()
			.username("seongchankim123")
			.encodedPassword("123asdQWE!@#")
			.name("김성찬")
			.email("myemail@mail.com")
			.nickname("nick")
			.role(UserRole.USER)
			.address("서울")
			.build();
		adapter.save(user);
	}

	@AfterEach
	void remove() {
		User user1 = adapter.findById(user.getId());
		adapter.delete(user1);
	}

	@Test
	@DisplayName("회원 조회 테스트")
	void test1() {
		// given
		given(adapter.findByUsername("seongchankim123")).willReturn(user);

		// when
		User user1 = adapter.findByUsername("seongchankim123");

		// then
		assertThat(user1.getName()).isEqualTo(user.getName());
		assertThat(user1.getUsername()).isEqualTo(user.getUsername());
		assertThat(user1.getEmail()).isEqualTo(user.getEmail());
		assertThat(user1.getNickname()).isEqualTo(user.getNickname());
		assertThat(user1.getUserRole()).isEqualTo(user.getUserRole());
		assertThat(user1.getAddress()).isEqualTo(user.getAddress());
	}

	@Test
	@DisplayName("회원 탈퇴 테스트")
	void test2() {
		// given
		given(adapter.findById(user.getId())).willReturn(user);
		// when
		userService.withdraw(user);

		// then
		assertNotNull(user.getDeletedAt());
	}

	@Test
	@DisplayName("회원 복구 테스트")
	void test3() {
		// given
		given(adapter.findById(user.getId())).willReturn(user);
		// when
		userService.withdraw(user);
		userService.resign(user.getId());
		// then
		assertNull(user.getDeletedAt());
		assertThat(userService.getUser(user.getId()).getName()).isEqualTo(user.getName());
	}
}
