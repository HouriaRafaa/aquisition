package com.example.aquisitionservice.web;

import com.example.aquisitionservice.dao.FieldRepository;
import com.example.aquisitionservice.dao.ValeurRepository;
import com.example.aquisitionservice.entities.Field;
import com.example.aquisitionservice.entities.Valeur;
import com.example.aquisitionservice.models.AppUser;
import com.example.aquisitionservice.service.FieldService;
import com.pusher.rest.Pusher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class FieldController {

    @Autowired
    ValeurRepository valeurRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    FieldService fieldService;

    double maxSeuil =20;

    @Autowired
    FieldRepository fieldRepository;
    @RequestMapping(value = "/add-field", method = RequestMethod.POST)
    public void create(@RequestBody Map<String, Object> payload, HttpServletRequest request) throws Exception {

        Field field;
//        System.out.println("http://localhost:8091/appUsers/"+String.valueOf(payload.get("userId")));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", request.getHeader("Authorization"));
        headers.add("content-type", "application/json");

        ResponseEntity<AppUser> responseEntity = restTemplate.exchange("http://authentification-service/appUsers/" + String.valueOf(payload.get("userId")),
                HttpMethod.GET,
                new HttpEntity<>("parameters", headers),
                AppUser.class
        );


        System.out.println("UserId" + responseEntity.getBody().getId());
        Long userId = responseEntity.getBody().getId();


        if (userId != null) {
            field = fieldService.saveCanal(
                    payload.get("nom").toString(),
                    payload.get("description").toString(),
                    userId

            );

            field.setCleEcriture(UUID.randomUUID().toString().replaceAll("-","").toUpperCase());
            field.setCleLecture(UUID.randomUUID().toString().replaceAll("-","").toUpperCase());

            fieldRepository.save(field);


        }
    }


    @GetMapping("/record")
    @ResponseBody
    public String updateCanal(@RequestParam Map<String,String> allParams, HttpServletRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", request.getHeader("Authorization"));
        headers.add("content-type","application/json");
        ArrayList<Valeur> myvalues= new ArrayList<>();


        Field field=fieldRepository.findFieldByCleEcriture(allParams.get("key"));

        Pusher pusher = new Pusher("762880", "84bee67aad46ed497369", "5017a5ee0387085255ae");
        pusher.setCluster("eu");
        pusher.setEncrypted(true);


            for (Map.Entry<String, String> entry : allParams.entrySet()) {
                if (field.getNom().equalsIgnoreCase(entry.getKey())) {
                    try {
                        Double data = Double.parseDouble(entry.getValue());
                        System.out.println("aaaaaaaaaaaaaaaaaaaaaaa  " + field.getNom());
                        Valeur valeur = new Valeur(data, field, new Date(), new Date());
                        valeurRepository.save(valeur);
                        field.getValeur().add(valeur);
                        fieldRepository.save(field);

                        ResponseEntity<AppUser> responseEntity = restTemplate.exchange("http://authentification-service/appUsers/" + field.getAppUser(),
                                HttpMethod.GET,
                                new HttpEntity<>("parameters", headers),
                                AppUser.class
                        );
                        String email = responseEntity.getBody().getEmail();
                        if(data> maxSeuil && field.getNom().equals("Temperature")){
                             fieldService.EnvoyerEmail(email , "<b> Warning </b> , Votre Tempearture dépasse "+data);
                        }



                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        return "bad entries";
                    }
                    pusher.trigger("my-channel", "my-event", Collections.singletonMap("data", allParams.get("key")));
                }
            }

        return " " + allParams.entrySet();

    }



    @RequestMapping(value = "/read", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Valeur2> readData(@RequestParam Map<String,String> allParams ) {

        List<Valeur> list=null;
        List<Valeur2> valeur = new ArrayList<Valeur2>();

         Field field = fieldRepository.findFieldByCleLecture(allParams.get("key"));

        if ( field!=null) {

                if (field.getNom().equalsIgnoreCase(allParams.get("field"))){
                    list = new ArrayList(field.getValeur());

                    for(Valeur v:list){
                        Valeur2 valeur2 = new Valeur2(v.getId(),v.getValeur(),v.getDate());
                        valeur.add(valeur2);
                    }
                    return valeur;
                }
        }
        return valeur;
    }

    @GetMapping("/getField/{idUser}")
    public List<Field> getMyFields(@PathVariable Long idUser){

        return fieldRepository.findFieldByAppUser(idUser);

    }

    @GetMapping("/getOneField/{idField}")
    public Field getOneField(@PathVariable int idField){
        return fieldRepository.findFieldByFieldId(idField);
    }
}


@Data
@AllArgsConstructor
@NoArgsConstructor
class Valeur2{
    String id;
    Double valeur;
    Date date;

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class CanalModel {

    private Long id;

    private String nom;
    private String description;
    private Date dateCreation;

    private String cleLecture;
    private String cleEcriture;

    private Long appUser;}
