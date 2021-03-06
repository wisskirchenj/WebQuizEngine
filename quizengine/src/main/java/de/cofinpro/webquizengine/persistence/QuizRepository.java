package de.cofinpro.webquizengine.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends PagingAndSortingRepository<Quiz, Long> {

    Optional<Quiz> findByTitle(String title);
}
