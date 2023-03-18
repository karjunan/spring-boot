package com.titaniam.db.dbserver.dao;

import com.titaniam.db.dbserver.dao.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

}
