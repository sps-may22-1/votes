package com.example.votes.web;

import com.example.votes.app.domain.Vote;
import com.example.votes.app.domain.VoteStats;
import com.example.votes.app.domain.VoteValue;
import com.example.votes.app.service.VoteService;
import com.example.votes.app.service.VoteStatsChangedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/votes")
public class VoteApi {

    private static final List<SseEmitter> voteStatsStreams = new CopyOnWriteArrayList<>();

    private final VoteService service;

    private final ObjectMapper objectMapper;

    @PostMapping
    public SaveVoteResponse save(@RequestBody SaveVoteRequest request) {
        Vote vote = new Vote();
        vote.setUserId(request.getUserId());
        vote.setValue(request.getVoteValue());

        boolean isSaved = service.save(vote);

        SaveVoteResponse response = new SaveVoteResponse();
        response.setSaved(isSaved);

        return response;
    }

    @GetMapping(path = "/stats")
    public GetVoteStatsResponse getStats() {
        VoteStats voteStats = service.getStats();

        return GetVoteStatsResponse.builder()
                .totalY(voteStats.getTotalY())
                .totalN(voteStats.getTotalN())
                .build();
    }

    @GetMapping(path = "/stream")
    public SseEmitter getStatsStream() throws JsonProcessingException {
        SseEmitter voteStatsStream = new SseEmitter();

        notifyVoteStatsStreams(List.of(voteStatsStream));

        voteStatsStreams.add(voteStatsStream);

        voteStatsStream.onError(e -> {
            voteStatsStreams.remove(voteStatsStream);
        });

        voteStatsStream.onTimeout(() -> {
            voteStatsStreams.remove(voteStatsStream);
        });

        voteStatsStream.onCompletion(() -> {
            voteStatsStreams.remove(voteStatsStream);
        });

        return voteStatsStream;
    }

    @Async
    @EventListener
    public void on(VoteStatsChangedEvent event) throws Exception {
        notifyVoteStatsStreams(voteStatsStreams);
    }

    private void notifyVoteStatsStreams(List<SseEmitter> voteStatsStreams) throws JsonProcessingException {
        VoteStats stats = service.getStats();

        String statsJson = objectMapper.writeValueAsString(stats);

        for (SseEmitter voteStatsStream : voteStatsStreams) {
            try {
                voteStatsStream.send(statsJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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