package org.example.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface StudentRepository extends MongoRepository<Student, String>{

    List<Student> findAllByNameAndAge(String name, int age);
    List<Student> findAllByName(String name);
    void deleteByName(String name);
}
