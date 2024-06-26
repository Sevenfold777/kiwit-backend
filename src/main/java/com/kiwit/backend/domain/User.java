package com.kiwit.backend.domain;

import com.kiwit.backend.common.constant.Plan;
import com.kiwit.backend.common.constant.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@DynamicInsert
@DynamicUpdate
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer point = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'NORMAL'")
    private Plan plan = Plan.NORMAL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'ACTIVATED'")
    private Status status = Status.ACTIVATED;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, optional = false)
    private UserInfo userInfo;

    @OneToMany(mappedBy = "user")
    private List<QuizSolved> quizSolvedList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ContentStudied> contentStudiedList = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//    }

    @Override
    public String getUsername() {
        return this.nickname;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public User(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public User(Long id) {
        this.id = id;
    }
}
