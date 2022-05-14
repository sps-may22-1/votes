package com.example.votes.app.repository;

import com.example.votes.app.domain.Vote;
import com.example.votes.app.domain.VoteStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, UUID> {

    boolean existsByUserId(UUID userId);

    @Query(nativeQuery = true, value = "select" +
            " count(*) filter(where value = 'Y') as totalY," +
            " count(*) filter(where value = 'N') as totalN" +
            " from votes")
    VoteStats getStats();
}
