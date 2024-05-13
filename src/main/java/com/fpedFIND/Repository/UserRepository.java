package com.fpedFIND.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpedFIND.Entity.User;

import jakarta.transaction.Transactional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	User findByUsername(String username);

    Optional<User> findById(Integer userId);
    Optional<User> findByFirstnameAndLastname(String firstname, String lastname);
    
    @Query("SELECT u FROM User u WHERE u.status = :status")
    List<User> findByStatus(@Param("status") String status);
    
    List<User> findByStatusAndLastActiveLessThan(String status, LocalDateTime lastActive);

    
    // Define a custom query to update the lastActive field based on the user_id
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastActive = :lastActive WHERE u.user_id = :userId")
    void updateLastActive(Integer userId, LocalDateTime lastActive);
    
    
    @Query("SELECT u FROM User u WHERE u.user_id = :userId")
    Optional<User> findByUserId(@Param("userId") String userId);
    

}
