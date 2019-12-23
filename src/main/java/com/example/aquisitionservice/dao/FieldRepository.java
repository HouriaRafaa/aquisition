package com.example.aquisitionservice.dao;



import com.example.aquisitionservice.entities.Field;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface FieldRepository extends MongoRepository<Field,String> {

    public Field findFieldById(String id);
       Field findFieldByFieldId(int id);

       List<Field> findFieldByAppUser(Long d);
       void deleteFieldByFieldId(int id);

       Field findFieldByCleEcriture(String cle);

       Field findFieldByCleLecture(String cle);
}
