package com.example.aquisitionservice.service;

import com.example.aquisitionservice.dao.FieldRepository;
import com.example.aquisitionservice.entities.Field;
import com.example.aquisitionservice.models.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.UUID;


@Service
public class FieldServiceImpl implements FieldService {
    @Autowired
    FieldRepository fieldRepository;

    @Autowired
    NextSequenceService nextSequenceService;


    @Autowired
    NewEmailSenderService newEmailSenderService;


    @Override
    public Field saveCanal(String nom, String description, Long appUser) {

        Field field = new Field();


        field.setNom(nom);
        field.setFieldId(nextSequenceService.getNextSequence("customSequences"));

        field.setDescription(description);
        field.setAppUser(appUser);


        return field;
    }

    @Override
    public void EnvoyerEmail(String email,String message) {

        try {
            newEmailSenderService.sendEmail(email,"","ESI Agriculture IOT",message);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
