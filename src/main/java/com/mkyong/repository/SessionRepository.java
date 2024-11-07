package com.mkyong.repository;

import java.awt.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mkyong.model.SessionEntity;

@Primary
@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {

    Optional<SessionEntity> findByRefreshToken(String refreshToken);

    Optional<SessionEntity> findById(String id);

    ArrayList<SessionEntity> findByUserId(String userId);

    // SessionEntity creatSession()
}
