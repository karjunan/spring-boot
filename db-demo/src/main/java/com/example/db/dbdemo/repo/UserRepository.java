package com.example.db.dbdemo.repo;


import com.example.db.dbdemo.dao.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByLastName(String lastName);

    User findById(long id);
}