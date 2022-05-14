package com.example.votes.app.service;

import com.example.votes.app.domain.Vote;
import com.example.votes.app.domain.VoteStats;
import com.example.votes.app.domain.VoteValue;
import com.example.votes.app.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

    @Autowired
    private VoteRepository repository;

    public boolean save(Vote vote) {
        if (repository.existsByUserId(vote.getUserId())) {
            return false;
        }

        repository.save(vote);

        return true;
    }

    public VoteStats getStats() {
        long totalY = repository.count(Example.of(Vote.builder()
                .value(VoteValue.Y)
                .build()));

        long totalN = repository.count(Example.of(Vote.builder()
                .value(VoteValue.N)
                .build()));

        return VoteStats.builder()
                .totalY(totalY)
                .totalN(totalN)
                .build();
    }
}
