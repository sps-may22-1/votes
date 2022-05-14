package com.example.votes.web;

import com.example.votes.app.domain.Vote;
import com.example.votes.app.domain.VoteStats;
import com.example.votes.app.domain.VoteValue;
import com.example.votes.app.service.VoteService;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class VoteApi {

    private final VoteService service;

    @PostMapping(path = "/votes")
    public SaveVoteResponse save(@RequestBody SaveVoteRequest request) {
        Vote vote = new Vote();
        vote.setUserId(request.getUserId());
        vote.setValue(request.getVoteValue());

        boolean isSaved = service.save(vote);

        SaveVoteResponse response = new SaveVoteResponse();
        response.setSaved(isSaved);

        return response;
    }

    @GetMapping(path = "/votes/stats")
    public GetVoteStatsResponse getStats() {
        VoteStats voteStats = service.getStats();

        return GetVoteStatsResponse.builder()
                .totalY(voteStats.getTotalY())
                .totalN(voteStats.getTotalN())
                .build();
    }
}

@Getter
@Setter
class SaveVoteRequest {

    private UUID userId;

    private VoteValue voteValue;
}

@Getter
@Setter
class SaveVoteResponse {
    private boolean isSaved;
}

@Getter
@Builder
class GetVoteStatsResponse {

    private final long totalY;

    private final long totalN;
}