package com.example.aquisitionservice.dao;




import com.example.aquisitionservice.entities.Field;
import com.example.aquisitionservice.entities.Valeur;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ValeurRepository extends MongoRepository<Valeur,String> {


    List<Valeur> findValeurByField(Field field);
}
