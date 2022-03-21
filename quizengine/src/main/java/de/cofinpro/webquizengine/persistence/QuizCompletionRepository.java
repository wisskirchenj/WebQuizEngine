package de.cofinpro.webquizengine.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizCompletionRepository extends PagingAndSortingRepository<QuizCompletion, Long> {

    @Query(value = "SELECT qc FROM QuizCompletion qc WHERE qc.completedByUsername = :name")
    Page<QuizCompletion> findAllUserCompletions(Pageable pageable, @Param("name") String username);
}
