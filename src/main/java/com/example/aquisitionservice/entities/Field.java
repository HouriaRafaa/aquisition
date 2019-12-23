package com.example.aquisitionservice.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import sun.reflect.CallerSensitive;


import java.util.ArrayList;
import java.util.Collection;

@Document
@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class Field {


    @Id
    private String id;

    private int fieldId;

    private String nom;

    private String description;
    private  Long appUser;

    private String cleLecture;
    private String cleEcriture;

    @Override
    public String toString() {
        return "Field{" +
                "id='" + id + '\'' +
                ", fieldId=" + fieldId +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", appUser=" + appUser +
                ", cleLecture='" + cleLecture + '\'' +
                ", cleEcriture='" + cleEcriture + '\'' +
                '}';
    }

    @JsonManagedReference
    @DBRef
    private Collection<Valeur> valeur = new ArrayList<>();


    public Field(int fieldId, String nom, Collection<Valeur> valeur) {
        this.fieldId=fieldId;
        this.nom = nom;
        this.valeur = valeur;
    }


    public Field(String nom, Collection<Valeur> valeur) {

        this.nom = nom;
        this.valeur = valeur;
    }
    public Field(int id,String nom){
        this.fieldId=id;
        this.nom=nom;
    }


}
