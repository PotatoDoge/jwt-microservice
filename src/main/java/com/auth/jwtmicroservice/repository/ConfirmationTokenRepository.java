package com.auth.jwtmicroservice.repository;

import com.auth.jwtmicroservice.entity.ConfirmationToken;
import com.auth.jwtmicroservice.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    void updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);

    @Query("SELECT ct FROM ConfirmationToken ct WHERE ct.user = :user ORDER BY ct.createdAt DESC")
    Optional<ConfirmationToken> findLatestTokenByUser(@Param("user") User user);

}
