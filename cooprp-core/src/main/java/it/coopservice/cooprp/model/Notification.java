package it.coopservice.cooprp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.coopservice.cooprp.model.enums.OperationStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static it.coopservice.cooprp.model.Notification.TABLE_NAME;

@Table(name = TABLE_NAME)
@Entity
public class Notification implements Serializable {


    public static final String TABLE_NAME = "crp_notifications";


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "uuid", unique = true)
    public String uuid;

    @Enumerated(EnumType.STRING)
    public OperationStatus operationStatus;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "operation_uuid", insertable = false, updatable = false)
    public Operation operation;
    public String operation_uuid;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Rome")
    public Date dataOra;
    public String codiceFiscale;
    public String uuid_esterno;

    public Notification() {

    }

}
