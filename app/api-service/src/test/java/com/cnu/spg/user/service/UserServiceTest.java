package com.cnu.spg.user.service;

import com.cnu.spg.user.domain.Role;
import com.cnu.spg.user.domain.RoleName;
import com.cnu.spg.user.domain.User;
import com.cnu.spg.user.dto.requset.UserPasswordChangingDto;
import com.cnu.spg.user.dto.requset.UserRegisterDto;
import com.cnu.spg.user.dto.response.UserInfoResponseDto;
import com.cnu.spg.user.exception.PasswordNotMatchException;
import com.cnu.spg.user.exception.UserNotFoundException;
import com.cnu.spg.user.exception.UsernameAlreadyExistException;
import com.cnu.spg.user.repository.RoleRepository;
import com.cnu.spg.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원 가입 성공")
    void regiesterUser() {
        // givne
        Role unauth = new Role(RoleName.ROLE_UNAUTH);
        roleRepository.save(unauth);
        UserRegisterDto userRegisterDto = UserRegisterDto.builder()
                .userName("newuser@gmail.com")
                .name("name")
                .password("Password123!")
                .matchingPassword("Password123!")
                .build();

        // when
        Long userId = userService.regiesterUser(userRegisterDto);
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // then
        assertEquals("newuser@gmail.com", user.getUsername());
        assertTrue(passwordEncoder.matches("Password123!", user.getPassword()));
        assertEquals("name", user.getName());
        assertEquals(1, user.getRoles().size());
        assertThat(user.getRoles()).extracting(Role::getName).containsExactly(RoleName.ROLE_UNAUTH);
    }

    @Test
    @DisplayName("이미 존재하는 username 입력시 에러 발생")
    void registerUserFail_For_ExistUsername() throws Exception {
        // given
        Role admin = new Role(RoleName.ROLE_ADMIN);
        roleRepository.save(admin);
        User john = User.createUser("name", "john@gmail.com", passwordEncoder.encode("Abc123!"), admin);
        userRepository.save(john);

        // when
        UserRegisterDto userRegisterDto = UserRegisterDto.builder()
                .userName("john@gmail.com")
                .name("name")
                .password("NewPassword!1")
                .matchingPassword("NewPassword!1")
                .build();

        // then
        Assertions.assertThrows(UsernameAlreadyExistException.class, () -> userService.regiesterUser(userRegisterDto));
    }

    @Test
    @DisplayName("아이디로 회원 조회")
    void findByUserName() {
        // given
        Role admin = new Role(RoleName.ROLE_ADMIN);
        roleRepository.save(admin);
        User john = User.createUser("john", "john@gmail.com", passwordEncoder.encode("Abc123!"), admin);
        userRepository.save(john);

        // when
        User findUser = userService.findByUserName("john@gmail.com");

        // then
        assertEquals("john@gmail.com", findUser.getUsername());
        assertTrue(passwordEncoder.matches("Abc123!", findUser.getPassword()));
    }

    @Test
    @DisplayName("잘못된 아이디로 회원 찾기 실패 테스트")
    void findFail_By_Username() throws Exception {
        // given
        String notExistUseranme = "notexist";

        // when

        // then
        assertThrows(UserNotFoundException.class, () -> userService.findByUserName(notExistUseranme));
    }

    @Test
    @DisplayName("user 이름으로 사용자 제거")
    void delete_Exist_Username_Test() {
        // given
        Role admin = new Role(RoleName.ROLE_ADMIN);
        roleRepository.save(admin);
        User john = User.createUser("john", "john@gmail.com", passwordEncoder.encode("Abc123!"), admin);
        userRepository.save(john);

        // when
        userService.withdrawMemberShip("john@gmail.com");

        // then
        assertThrows(UserNotFoundException.class,
                () -> userService.findByUserName("john@gmail.com"));
    }

    @Test
    @DisplayName("존재하지 않는 user 회원 탈퇴 요청")
    void delete_NotExist_User_Test() throws Exception {
        // given
        String notExistUsername = "notexist@gmail.com";

        // when

        // then
        assertThrows(UserNotFoundException.class,
                () -> userService.withdrawMemberShip(notExistUsername));
    }

    @Test
    @DisplayName("회원 변경 정상 작동")
    void changeUserPassword_Test() {
        // given
        Role admin = new Role(RoleName.ROLE_ADMIN);
        roleRepository.save(admin);
        User john = User.createUser("john", "john@gmail.com", passwordEncoder.encode("Abc123!"), admin);
        userRepository.save(john);

        // when
        UserPasswordChangingDto passwordChangingDto =
                new UserPasswordChangingDto("Abc123!", "Newpassword1!", "Newpassword1!");
        User user = userService.changeUserPassword("john@gmail.com", passwordChangingDto);

        // then
        assertTrue(passwordEncoder.matches("Newpassword1!", user.getPassword()));
        assertEquals("Newpassword1!", passwordChangingDto.getPassword());
        assertEquals("Newpassword1!", passwordChangingDto.getMatchingPassword());
        assertEquals("john@gmail.com", user.getUsername());
    }

    @Test
    @DisplayName("이전 password가 일치 하지 않을 경우 에러 발생")
    void failChangin_Cuz_Not_Matched_PrevisouPassword_Test() throws Exception {
        // given
        Role admin = new Role(RoleName.ROLE_ADMIN);
        roleRepository.save(admin);
        User john = User.createUser("john", "john@gmail.com", passwordEncoder.encode("Abc123!"), admin);
        userRepository.save(john);

        // when
        UserPasswordChangingDto passwordChangingDto = new UserPasswordChangingDto("NotMatchedPassword1!", "Newpassword1!", "Newpassword1!");

        // then
        assertThrows(PasswordNotMatchException.class, () -> userService.changeUserPassword("john@gmail.com", passwordChangingDto));
    }

    @Test
    void failChanging_User_IsNot_Exist_Test() throws Exception {
        // given

        // when
        UserPasswordChangingDto passwordChangingDto = new UserPasswordChangingDto("Abc123!", "Newpassword1!", "Newpassword1!");

        // then
        assertThrows(UserNotFoundException.class, () -> userService.changeUserPassword("notExistuseranme", passwordChangingDto));
    }

    @Test
    @DisplayName("비밀번호 변경 성공 테스트")
    void changeUserPasswordTest() {
        // given
        Role admin = new Role(RoleName.ROLE_ADMIN);
        roleRepository.save(admin);
        User john = User.createUser("john", "john@gmail.com", passwordEncoder.encode("Abc123!"), admin);
        User saveUser = userRepository.save(john);

        // when
        UserPasswordChangingDto passwordChangingDto = new UserPasswordChangingDto("Abc123!", "NewPassword12!@", "NewPassword12!@");
        userService.changeUserPassword(saveUser.getId(), passwordChangingDto);
        User changedUser = userService.findByUserName("john@gmail.com");

        // then
        assertTrue(passwordEncoder.matches("NewPassword12!@", changedUser.getPassword()));
    }

    @Test
    @DisplayName("not confirm error")
    void changeUserPasswordButExistPasswordConfirm() throws Exception {
        // given
        Role admin = new Role(RoleName.ROLE_ADMIN);
        roleRepository.save(admin);
        User john = User.createUser("john", "john@gmail.com", passwordEncoder.encode("Abc123!"), admin);
        User savedUser = userRepository.save(john);
        Long id = savedUser.getId();

        // when
        UserPasswordChangingDto passwordChangingDto = new UserPasswordChangingDto("notConfirmA!@1", "NewPassword12!@", "NewPassword12!@");

        // then
        assertThrows(PasswordNotMatchException.class, () -> userService.changeUserPassword(id, passwordChangingDto));
    }

    @Test
    void searchUserInfoTest() {
        // given
        Role admin = new Role(RoleName.ROLE_ADMIN);
        roleRepository.save(admin);
        User john = User.createUser("john", "john@gmail.com", passwordEncoder.encode("Abc123!"), admin);
        User savedUser = userRepository.save(john);

        // when
        UserInfoResponseDto userInfoResponseDto = userService.searchUserInfo(savedUser.getId());

        // then
        assertEquals("john", userInfoResponseDto.getName());
        assertEquals(1, userInfoResponseDto.getRoles().size());
    }

    @Test
    @DisplayName("userId에 매칭되는 user가 없을 경우")
    void searchNotExistUserExceptionTest() throws Exception {
        // given

        // when

        // then
        assertThrows(UserNotFoundException.class, () -> userService.searchUserInfo(1L));
    }
}