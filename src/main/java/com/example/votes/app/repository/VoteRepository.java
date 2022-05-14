package com.example.votes.app.repository;

import com.example.votes.app.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;
import java.util.List;
import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, UUID> {

    boolean existsByUserId(UUID userId);

    @Query("select value as voteValue, count(*) as voteTotal from Vote group by value")
    List<Tuple> getStats();
}
