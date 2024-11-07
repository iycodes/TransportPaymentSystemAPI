package com.mkyong.repository;

import com.mkyong.model.UserEntity;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findByName(String title);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(String id);

    Optional<UserEntity> deleteById(String id);

    @Query("SELECT name from UserEntity where id = ?1 ")
    Optional<String> getNameById(String id);
    // PaymentSuccessDto payDriver(PayDriverDto data);

    // Custom query
    // @Query("SELECT b FROM UserEntity b WHERE b.createdAt > :date")
    // List<UserEntity> findByCreatedAfterDate(@Param("date") LocalDate date);

    // @Query("SELECT")

    // PaymentSuccessDto payDriver(PaymentSuccessDto data);

}

// interface CustomizedUserRepository {
// Object payDriver(PayDriverDto data);
// }

// class CustomizedUserRepositoryImpl implements CustomizedUserRepository {

// public Object payDriver(PayDriverDto data) {
// return "";
// }
// }