package br.com.publicenter.backendtestapi.repository;

import br.com.publicenter.backendtestapi.repository.model.UserCustomer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserCustomer, Long> {
    Page<UserCustomer> findAll(Pageable pageable);

    Optional<UserCustomer> findByUsername(String username);
}
