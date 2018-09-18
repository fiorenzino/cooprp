package it.coopservice.cooprp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.coopservice.cooprp.model.enums.OperationStatus;
import it.coopservice.cooprp.model.enums.OperationType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

import static it.coopservice.cooprp.model.Operation.TABLE_NAME;

@Table(name = TABLE_NAME)
@Entity
public class Operation implements Serializable
{

   public static final String TABLE_NAME = "crp_operations";

   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   @Column(name = "uuid", unique = true)
   public String uuid;

   @Enumerated(EnumType.STRING)
   public OperationStatus operationStatus;

   @Enumerated(EnumType.STRING)
   public OperationType operationType;

   @JsonIgnore
   @ManyToOne
   @JoinColumn(name = "location_uuid", insertable = false, updatable = false)
   public Location location;
   public String location_uuid;

   @JsonIgnore
   @ManyToOne
   @JoinColumn(name = "companyConfiguration_uuid", insertable = false, updatable = false)
   public CompanyConfiguration companyConfiguration;
   public String companyConfiguration_uuid;

   public String dataOra;

   @Temporal(TemporalType.TIMESTAMP)
   public Date realDate;

   public String codiceFiscale;
   public String latitudine;
   public String longitudine;

   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Rome")
   public Date dataRicezione;

   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Rome")
   public Date dataNotifica;

   public String timezone;
   public String societaId;

   @Transient
   public String token;
   public Operation()
   {

   }
}
