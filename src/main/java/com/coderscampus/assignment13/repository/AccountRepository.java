package com.coderscampus.assignment13.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coderscampus.assignment13.domain.Account;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
    @Query("SELECT a FROM User u JOIN u.accounts a WHERE u.userId = :userId")
    List<Account> findAccountsByUserId(@Param("userId") Long userId);


}


