package com.demo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(includeFieldNames = true)
@SuppressWarnings("serial")
@Entity
public class User implements Serializable {

    @Id
    @Column(columnDefinition = "BIGINT(20)")
    private Long id;

    @NotEmpty
    @Column(length = 80)
    private String name;

    @NotEmpty
    @Column(length = 100, unique = true)
    private String email;

    @NotEmpty
    @Column(length = 250)
    private String address1;

    @Column(length = 250)
    private String address2;

    @Column(length = 150)
    private String townCity;

    private Integer postCode;

    @Column(length = 150)
    private String country;

    @ElementCollection
    @CollectionTable(name = "telephone", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "phoneno")
    private List<String> telephoneList = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "retrieval_timestamp", unique = true, nullable = true)
    private Date retrievalTimeStamp;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}