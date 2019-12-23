package com.example.aquisitionservice.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customSequences2") @Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomSequences2 {

    @Id
    private String id;
    private int seq;
}
