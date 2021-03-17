package br.com.publicenter.backendtestapi.repository.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Data
@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roleName;
    private String description;

    @Override
    public String getAuthority() {
        return this.roleName;
    }

    public static List<Role> of(String roleName) {
        return List.of(RoleEnum.of(roleName));
    }
}
