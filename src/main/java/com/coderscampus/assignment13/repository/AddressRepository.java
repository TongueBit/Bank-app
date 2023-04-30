package com.coderscampus.assignment13.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


import com.coderscampus.assignment13.domain.Address;

import javax.transaction.Transactional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{
    @Transactional
    @Modifying
    void deleteByUserId(Long userId);
}
