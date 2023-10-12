package com.example.oa.Filter;

import com.example.oa.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return buildUserDetails(user);
    }

    private UserDetails buildUserDetails(User user) {
        List<GrantedAuthority> authorities = getUserAuthorities(user.getRole());
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    private List<GrantedAuthority> getUserAuthorities(String role) {
        if (role.equals("2")) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (role.equals("1")) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_TEACHER"));
        } else if (role.equals("0")) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return Collections.emptyList();
    }
}
