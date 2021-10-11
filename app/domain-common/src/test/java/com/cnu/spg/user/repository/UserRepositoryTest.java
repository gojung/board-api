package com.cnu.spg.user.repository;

import com.cnu.spg.user.domain.User;
import com.cnu.spg.user.exception.UserParamterOmittedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("user db 처리 관련 테스트")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;

    @Test
    void findByCorrectUsernameTest() {
        // given
        User user = User.createUser("kkh", "kkh@gmail.com", "password");
        em.persist(user);
        em.flush();
        em.clear();

        final String findedUsername = "kkh@gmail.com";

        // when
        User findedUser = userRepository.findByUsername(findedUsername)
                .orElseThrow(() -> new RuntimeException("exception"));

        // then
        assertEquals(findedUsername, findedUser.getUsername());
        assertEquals("password", findedUser.getPassword());
        assertEquals("kkh", findedUser.getName());
    }

    @Test
    void findByNotExistUsername() throws Exception {
        // given
        final String findedUsername = "error";

        // when
        Optional<User> findUser = userRepository.findByUsername(findedUsername);

        // then
        assertThrows(RuntimeException.class, () -> findUser.orElseThrow(() -> new RuntimeException("exception")));
    }

    @Test
    void deleteByUsername() throws Exception {
        // given
        User user = User.createUser("kkh", "kkh@gmail.com", "password");
        em.persist(user);
        em.flush();
        em.clear();

        // when
        userRepository.deleteByUsername("kkh@gmail.com");
        Optional<User> findUser = userRepository.findByUsername("kkh@gmail.com");

        // then
        assertThrows(RuntimeException.class, () -> findUser.orElseThrow(() -> new RuntimeException("not found")));
    }

    @Test
    void isNotExistUsernameTest() throws Exception {
        // given
        final String notExistUsername = "notExist";

        // when
        boolean result = userRepository.existsByUsername(notExistUsername);

        // then
        assertFalse(result);
    }

    @Test
    void isExistUsernameTest() throws Exception {
        // given
        User user = User.createUser("kkh", "kkh@gmail.com", "password");
        em.persist(user);
        em.flush();
        em.clear();

        // when
        boolean result = userRepository.existsByUsername("kkh@gmail.com");

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("username이 null인 경우 예외 케이스")
    void usernameIsNullExistTest() throws Exception {
        // given

        // when

        // then
        assertThrows(UserParamterOmittedException.class, () -> userRepository.existsByUsername(null));
    }
}