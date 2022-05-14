package com.example.votes.app.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "votes")
@Check(constraints = "value in ('Y', 'N')")
public class Vote {

    @EqualsAndHashCode.Include
    @Id
    @Type(type = "uuid-char")
    @Column(length = 36)
    private UUID userId;

    @Column(length = 1, nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private VoteValue value;
}
