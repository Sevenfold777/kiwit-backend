package com.kiwit.backend.repository;

import com.kiwit.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select U " +
            "from User U " +
            "join fetch U.userInfo " +
            "where U.id = :userId")
    Optional<User> findUserWithInfo(@Param("userId") Long userId);


    @Query("select U " +
            "from User U " +
            "join fetch U.userInfo " +
            "where U.email = :email " +
            "and U.status != DELETED " +
            "and U.status != BANNED")
    Optional<User> findUserWithInfoByEmail(@Param("email") String email);

}
