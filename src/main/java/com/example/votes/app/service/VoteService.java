package com.example.votes.app.service;

import com.example.votes.app.domain.Vote;
import com.example.votes.app.domain.VoteStats;
import com.example.votes.app.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

    private static final String VOTE_STATS_CACHE_NAME = "vote-stats";

    @Autowired
    private VoteRepository repository;

    @Autowired
    private CacheManager cacheManager;

    public boolean save(Vote vote) {
        if (repository.existsByUserId(vote.getUserId())) {
            return false;
        }

        repository.save(vote);

        cacheManager.getCache(VOTE_STATS_CACHE_NAME).clear();

        return true;
    }

    @Cacheable(VOTE_STATS_CACHE_NAME)
    public VoteStats getStats() {
        return repository.getStats();
    }
}
