package de.cofinpro.webquizengine.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizCompletionRepository extends PagingAndSortingRepository<QuizCompletion, Long> {
}
