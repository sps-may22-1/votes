package com.example.votes.app.service;

import com.example.votes.app.domain.Vote;
import com.example.votes.app.domain.VoteStats;
import com.example.votes.app.domain.VoteValue;
import com.example.votes.app.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
        List<Tuple> rawStatTuples = repository.getStats();

        Map<VoteValue, Long> rawStats = new TreeMap<>();

        for (Tuple rawStatTuple : rawStatTuples) {
            VoteValue voteValue = (VoteValue) rawStatTuple.get("voteValue");

            long voteTotal = (Long) rawStatTuple.get("voteTotal");

            rawStats.put(voteValue, voteTotal);
        }

        return VoteStats.builder()
                .totalY(rawStats.get(VoteValue.Y))
                .totalN(rawStats.get(VoteValue.N))
                .build();
    }
}
