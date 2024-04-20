package com.kiwit.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "level")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "num")
public class Level {

    @Id
    private Long num;

    @Column
    private String title;
}
