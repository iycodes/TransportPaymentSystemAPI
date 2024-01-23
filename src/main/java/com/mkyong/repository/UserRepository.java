package com.mkyong.repository;


import com.mkyong.model.Userr;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
public interface UserRepository extends JpaRepository<Userr, Long> {

    List<Userr> findByName(String title);

    Optional<Userr> findByEmail(String email);
    /*
 Custom query
    @Query("SELECT b FROM Userr b WHERE b.createdAt > :date")
    List<Userr> findByCreatedAfterDate(@Param("date") LocalDate date);
*/

}
