package com.auth.jwtmicroservice.repository;

import com.auth.jwtmicroservice.entity.ConfirmationToken;
import com.auth.jwtmicroservice.entity.ResetPasswordToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPasswordToken, Long> {

    Optional<ResetPasswordToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE ResetPasswordToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    void updateConfirmedAt(String token,
                           LocalDateTime confirmedAt);
}
