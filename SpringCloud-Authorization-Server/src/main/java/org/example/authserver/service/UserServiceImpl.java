package org.example.authserver.service;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService {

    private static List<SecurityUser> mockUsers;

    static {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 这里密码必须加密
        String pwd = passwordEncoder.encode("yanch");
        mockUsers = new ArrayList<>();
        SecurityUser user = new SecurityUser();
        user.setId("001");
        user.setUserName("yanch");
        user.setPassword(pwd);
        user.setAuthorities(Arrays.asList(new SimpleGrantedAuthority("ADMIN")));
        user.setEnabled(true);
        mockUsers.add(user);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<SecurityUser> user = mockUsers.stream().filter(u -> u.getUsername().equals(userName)).findFirst();
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("MessageConstant.USERNAME_PASSWORD_ERROR");
        }

        SecurityUser securityUser = user.get();
        // 下面抛出的异常 Spring Security 会自动捕获并进行返回
        if (!securityUser.isEnabled()) {
            throw new DisabledException("MessageConstant.ACCOUNT_DISABLED");
        } else if (!securityUser.isAccountNonLocked()) {
            throw new LockedException("MessageConstant.ACCOUNT_LOCKED");
        } else if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException("MessageConstant.ACCOUNT_EXPIRED");
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("MessageConstant.CREDENTIALS_EXPIRED");
        }
        return securityUser;
    }
}
