package de.cofinpro.webquizengine.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisteredUserRepository extends CrudRepository<RegisteredUser, Long> {

    Optional<RegisteredUser> findByUsername(String username);
}
