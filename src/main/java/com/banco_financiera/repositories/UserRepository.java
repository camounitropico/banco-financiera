package com.banco_financiera.repositories;

import com.banco_financiera.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long>{
    Optional<User> findByIdentificationNumber(Long identificationNumber);
}
