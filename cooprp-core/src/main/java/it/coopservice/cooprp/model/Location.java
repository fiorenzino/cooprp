package it.coopservice.cooprp.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

import static it.coopservice.cooprp.model.Location.TABLE_NAME;

@Table(name = TABLE_NAME)
@Entity
public class Location implements Serializable {

    public static final String TABLE_NAME = "crp_locations";


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "uuid", unique = true)
    public String uuid;


    public Location() {
    }


}
