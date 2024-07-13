package com.banco_financiera.repositories;

import com.banco_financiera.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>{

}
