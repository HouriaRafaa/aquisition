package com.example.aquisitionservice.service;

import com.example.aquisitionservice.entities.Field;
import com.example.aquisitionservice.models.AppUser;

import java.util.List;

public interface FieldService {

    Field saveCanal(String nom, String description, Long appUser);
    void EnvoyerEmail(String email,String message);
}
