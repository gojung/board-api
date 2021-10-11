package com.cnu.spg.user.service;

import com.cnu.spg.user.domain.Role;
import com.cnu.spg.user.domain.RoleName;
import com.cnu.spg.user.domain.User;
import com.cnu.spg.user.dto.requset.UserPasswordChangingDto;
import com.cnu.spg.user.dto.requset.UserRegisterDto;
import com.cnu.spg.user.dto.response.UserInfoResponseDto;
import com.cnu.spg.user.exception.PasswordNotMatchException;
import com.cnu.spg.user.exception.RoleNotFoundException;
import com.cnu.spg.user.exception.UserNotFoundException;
import com.cnu.spg.user.exception.UsernameAlreadyExistException;
import com.cnu.spg.user.repository.RoleRepository;
import com.cnu.spg.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long regiesterUser(UserRegisterDto userRegisterDto) {
        if (userRepository.existsByUsername(userRegisterDto.getUserName())) {
            throw new UsernameAlreadyExistException(userRegisterDto.getUserName());
        }

        Role unAuthUserRole = roleRepository.findByName(RoleName.ROLE_UNAUTH)
                .orElseThrow(() -> new RoleNotFoundException("회원 가입에 필요한 기본 권한 정보가 없습니다."));

        String encrytPassword = passwordEncoder.encode(userRegisterDto.getMatchingPassword());
        User user = User.createUser(userRegisterDto.getName(), userRegisterDto.getUserName(), encrytPassword, unAuthUserRole);

        return userRepository.save(user).getId();
    }

    public User findByUserName(String userName) {
        return this.userRepository.findByUsername(userName)
                .orElseThrow(UserNotFoundException::new);
    }

    public User findByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public void withdrawMemberShip(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new UserNotFoundException();
        }

        userRepository.deleteByUsername(username);
    }

    @Transactional
    public User changeUserPassword(String username, UserPasswordChangingDto userPasswordChangingDto) {
        User oridinaryUser = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(userPasswordChangingDto.getBeforePassword()
                , oridinaryUser.getPassword())) {
            throw new PasswordNotMatchException();
        }
        oridinaryUser.changePassword(passwordEncoder.encode(userPasswordChangingDto.getPassword()));

        return oridinaryUser;
    }

    @Transactional
    public void changeUserPassword(Long userId, UserPasswordChangingDto userPasswordChangingDto) {
        User oridinaryUser = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(userPasswordChangingDto.getBeforePassword()
                , oridinaryUser.getPassword())) {
            throw new PasswordNotMatchException();
        }

        oridinaryUser.changePassword(this.passwordEncoder.encode(userPasswordChangingDto.getPassword()));
    }

    public UserInfoResponseDto searchUserInfo(Long userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return new UserInfoResponseDto(findUser);
    }

    public boolean checkNowPassword(User user, String passowrd) {
        return this.passwordEncoder.matches(passowrd, user.getPassword());
    }
}
