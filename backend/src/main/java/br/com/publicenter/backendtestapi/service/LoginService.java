package br.com.publicenter.backendtestapi.service;

import br.com.publicenter.backendtestapi.config.MyTokenService;
import br.com.publicenter.backendtestapi.repository.UserRepository;
import br.com.publicenter.backendtestapi.repository.model.UserCustomer;
import br.com.publicenter.backendtestapi.resource.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final MyTokenService myTokenService;

    public LoginResponse authentication(UsernamePasswordAuthenticationToken request) {
        Authentication authentication = authenticationManager.authenticate(request);
        String token = myTokenService.createToken(authentication);
        UserCustomer user = userRepository.findByUsername(request.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format("User %s not found", request.getName())));
        return LoginResponse.of(user, token, "Bearer");
    }
}
