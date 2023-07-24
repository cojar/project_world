package com.example.world.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<SiteUser, Long> {

    Optional<SiteUser> findByNickname(String nickname);

    Optional<SiteUser> findByUsername(String username);

    @Modifying
    @Transactional
    @Query("UPDATE SiteUser u SET u.mailKey = :mailKey WHERE u.username = :username AND u.id = :id")
    int updateMailKey(@Param("mailKey") int mailKey, @Param("username") String username, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE SiteUser u SET u.mailAuth = true WHERE u.username = :username AND u.mailKey = :mailKey")
    int updateMailAuth(@Param("username") String username, @Param("mailKey") int mailKey);

    Optional<SiteUser> findSiteUserByUsername(String username);
}
