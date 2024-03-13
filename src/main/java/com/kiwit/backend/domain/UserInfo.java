package com.kiwit.backend.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_info")
@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class UserInfo {
    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;

    private String jwtRefreshToken;

    private String fcmToken;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime latestVisit;

    public void setJwtRefreshToken(String jwtRefreshToken) {
        this.jwtRefreshToken = jwtRefreshToken;
    }
}
