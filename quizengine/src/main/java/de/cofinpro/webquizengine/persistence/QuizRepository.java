package de.cofinpro.webquizengine.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends CrudRepository<Quiz, Long> {

    Optional<Quiz> findByTitle(String title);
}
