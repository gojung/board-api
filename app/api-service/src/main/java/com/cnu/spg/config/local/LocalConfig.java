package com.cnu.spg.config.local;

import com.cnu.spg.board.domain.Board;
import com.cnu.spg.board.domain.project.ProjectBoard;
import com.cnu.spg.board.domain.project.ProjectCategory;
import com.cnu.spg.board.repository.BoardRepository;
import com.cnu.spg.board.repository.project.ProjectCategoryRepository;
import com.cnu.spg.user.domain.Role;
import com.cnu.spg.user.domain.RoleName;
import com.cnu.spg.user.domain.User;
import com.cnu.spg.user.repository.RoleRepository;
import com.cnu.spg.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
@Profile("local")
@RequiredArgsConstructor
public class LocalConfig {

    private final UserLocalInitService userLocalInitService;
    private final BoardLocalInitSerivce boardLocalSerivce;

    @PostConstruct
    void postConstructor() {
        userLocalInitService.init();
        boardLocalSerivce.init();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    private static class BoardLocalInitSerivce {
        private final UserRepository userRepository;
        private final BoardRepository boardRepository;
        private final ProjectCategoryRepository categoryRepository;

        public void init() {
            String content = "content";
            String title = "title";

            User john = userRepository.findByUsername("john@gmail.com")
                    .orElseThrow(() -> new UsernameNotFoundException("not found"));
            ProjectCategory category = categoryRepository.save(ProjectCategory.builder()
                    .categoryName("category")
                    .user(john)
                    .build());


            for (int index = 0; index < 30; index++) {
                String titleWithNum = title + "_" + index;
                String contentWithNum = content + "_" + index;

                Board board = ProjectBoard.builder()
                        .user(john)
                        .title(titleWithNum)
                        .content(contentWithNum)
                        .projectCategory(category)
                        .build();

                boardRepository.save(board);
            }
        }
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    private static class UserLocalInitService {
        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final PasswordEncoder passwordEncoder;

        public void init() {
            Role admin = new Role(RoleName.ROLE_ADMIN);
            Role student = new Role(RoleName.ROLE_STUDENT);
            Role unAuth = new Role(RoleName.ROLE_UNAUTH);

            roleRepository.save(admin);
            roleRepository.save(student);
            roleRepository.save(unAuth);
            String password = "Abcd123!";

            User john = User.createUser("john", "john@gmail.com", passwordEncoder.encode(password), admin);
            User susan = User.createUser("susan", "susan@gmail.com", passwordEncoder.encode(password), unAuth);
            User amanda = User.createUser("amanda", "amanda@gmail.com", passwordEncoder.encode(password), admin, student);

            userRepository.save(john);
            userRepository.save(susan);
            userRepository.save(amanda);
        }
    }
}
