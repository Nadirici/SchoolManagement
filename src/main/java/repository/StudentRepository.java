package repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import models.User;


@Repository
public interface StudentRepository extends CrudRepository<User,String> {
}
