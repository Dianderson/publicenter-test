package br.com.publicenter.backendtestapi.resource.dto.response;

import br.com.publicenter.backendtestapi.repository.model.UserCustomer;
import lombok.Data;

@Data
public class LoginResponse {
    private String username;
    private String roleName;
    private String token;
    private String type;

    public static LoginResponse of(UserCustomer userCustomer, String token, String tokenType) {
        var response = new LoginResponse();
        response.setUsername(userCustomer.getUsername());
        response.setRoleName(userCustomer.getRoles().get(0).getRoleName());
        response.setToken(token);
        response.setType(tokenType);
        return response;
    }
}
