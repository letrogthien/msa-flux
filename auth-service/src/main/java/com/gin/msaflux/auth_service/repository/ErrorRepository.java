package com.gin.msaflux.auth_service.repository;


import com.gin.msaflux.auth_service.models.Error;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorRepository extends ReactiveMongoRepository<Error, String> {

}
