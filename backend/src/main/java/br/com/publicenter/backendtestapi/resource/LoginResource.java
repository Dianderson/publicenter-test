package br.com.publicenter.backendtestapi.resource;

import br.com.publicenter.backendtestapi.resource.dto.request.LoginRequest;
import br.com.publicenter.backendtestapi.resource.dto.response.LoginResponse;
import br.com.publicenter.backendtestapi.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginResource {

    private final LoginService loginService;

    @PostMapping
    public LoginResponse authentication(@RequestBody @Valid LoginRequest request) {
        return loginService.authentication(request.of());
    }
}
