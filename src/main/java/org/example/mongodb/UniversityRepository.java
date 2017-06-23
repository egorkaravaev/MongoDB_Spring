package org.example.mongodb;


import org.springframework.data.mongodb.repository.MongoRepository;

public interface UniversityRepository extends MongoRepository<University, String>{
    University findByDepartment(String department);
}
