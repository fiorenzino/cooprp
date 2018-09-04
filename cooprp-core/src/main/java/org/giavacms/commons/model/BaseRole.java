package org.giavacms.commons.model;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class BaseRole implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String alias;
    private String group;
    private String description;
    private String type;

    public BaseRole() {
    }

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Transient
    public Object getCustomerId() {
        return null;
    }
}