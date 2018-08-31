package it.coopservice.cooprp.model;

import com.vividsolutions.jts.geom.Coordinate;
import org.hibernate.annotations.GenericGenerator;

import com.vividsolutions.jts.geom.Point;
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



    public Point location;

    public String nome;
    public String wbs;
    public String societa_uuid;
    public String mail;



    public Location() {
    }


}
