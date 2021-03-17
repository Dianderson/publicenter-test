package br.com.publicenter.backendtestapi.config;

import br.com.publicenter.backendtestapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) {
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", userName)));
    }
}
