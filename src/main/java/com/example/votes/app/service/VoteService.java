package com.example.votes.app.service;

import com.example.votes.app.domain.Vote;
import com.example.votes.app.domain.VoteStats;
import com.example.votes.app.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

    private static final String VOTE_STATS_CACHE_NAME = "vote-stats";

    @Autowired
    private VoteRepository repository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public boolean save(Vote vote) {
        if (repository.saveOnce(vote) == 1) {
            cacheManager.getCache(VOTE_STATS_CACHE_NAME).clear();

            applicationEventPublisher.publishEvent(new VoteStatsChangedEvent("vote stats changed"));

            return true;
        }

        return false;
    }

    @Cacheable(VOTE_STATS_CACHE_NAME)
    public VoteStats getStats() {
        return repository.getStats();
    }
}
