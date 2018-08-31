package it.coopservice.cooprp.model;

import it.coopservice.cooprp.model.enums.OperationStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

import static it.coopservice.cooprp.model.Operation.TABLE_NAME;

@Table(name = TABLE_NAME)
@Entity
public class Notification implements Serializable {


    public static final String TABLE_NAME = "crp_notifications";


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "uuid", unique = true)
    public String uuid;

    public OperationStatus operationStatus;


    public Notification() {

    }

}
