package it.coopservice.cooprp.model;

//tab_operatori

import javax.persistence.*;
import java.io.Serializable;

import static it.coopservice.cooprp.model.CompanyConfiguration.TABLE_NAME;

@Entity
@Table(name = TABLE_NAME)
public class CompanyConfiguration implements Serializable {

    public static final String TABLE_NAME = "crp_companyconfigurations";

    @Id
    public Long id;

    public CompanyConfiguration() {
    }

    public Long getId() {
        return id;
    }
}
