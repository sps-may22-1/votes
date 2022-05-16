package com.example.votes.app.repository;

import com.example.votes.app.domain.Vote;
import com.example.votes.app.domain.VoteStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, UUID> {

    boolean existsByUserId(UUID userId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into votes (user_id, value)" +
            " select" +
            "   user_id, value" +
            " from" +
            "   (select" +
            "       cast(:#{#vote.userId} as varchar) as user_id," +
            "       cast(:#{#vote.value.name} as varchar) as value)" +
            " where" +
            "   not exists (select value from votes where user_id = cast(:#{#vote.userId} as varchar))")
    int saveOnce(Vote vote);

    @Query(nativeQuery = true, value = "select" +
            " count(*) filter(where value = 'Y') as totalY," +
            " count(*) filter(where value = 'N') as totalN" +
            " from votes")
    VoteStats getStats();
}
