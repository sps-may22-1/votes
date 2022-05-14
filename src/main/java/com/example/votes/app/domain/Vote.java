package com.example.votes.app.domain;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Результат голосования.
 */
@Entity
@Table(name = "votes")
@Check(constraints = "value in ('Y', 'N')")
public class Vote {

    @Id
    @Type(type = "uuid-char")
    @Column(length = 36)
    private UUID userId;

    @Column(length = 1, nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private VoteValue value;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public VoteValue getValue() {
        return value;
    }

    public void setValue(VoteValue value) {
        this.value = value;
    }
}
