package br.com.publicenter.backendtestapi.repository.model;

import br.com.publicenter.backendtestapi.error.RoleNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum RoleEnum {

    ADMIN(1L, "ROLE_ADMIN", "Admin"),
    MASTER(2L, "ROLE_MASTER", "Master"),
    CUSTOMER(3L, "ROLE_CUSTOMER", "Customer");

    private final Long id;
    private final String role;
    private final String description;

    public static Role of(String roleName) {
        try {
            var roleEnum = RoleEnum.valueOf(roleName);
            var roleEntity = new Role();
            roleEntity.setDescription(roleEnum.getDescription());
            roleEntity.setRoleName(roleEnum.getRole());
            roleEntity.setId(roleEnum.getId());
            return roleEntity;
        } catch (IllegalArgumentException e) {
            var errorMsg = String.format("Role %s not found!", roleName);
            log.error(errorMsg, e);
            throw new RoleNotFoundException(errorMsg);
        }
    }
}
