package br.com.publicenter.backendtestapi.resource.dto.response;

import br.com.publicenter.backendtestapi.repository.model.UserCustomer;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String roleDescription;

    public static UserResponse of(UserCustomer userCustomer) {
        var response = new UserResponse();
        response.setId(userCustomer.getId());
        response.setUsername(userCustomer.getUsername());
        response.setRoleDescription(userCustomer.getRoles().get(0).getDescription());
        return response;
    }
}
