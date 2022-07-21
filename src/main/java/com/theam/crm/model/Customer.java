package com.theam.crm.model;

import com.theam.crm.model.audit.AuditMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Customer extends AuditMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 30)
    private String firstName;

    @NotBlank
    @Size(max = 30)
    private String lastName;

    @Email
    private String email;

    private String photoUrl;

    public Customer(@NotBlank @Size(max = 30) String firstName, @NotBlank @Size(max = 30) String lastName, @Email String email, String photoUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public Customer(@NotBlank @Size(max = 30) String firstName, @NotBlank @Size(max = 30) String lastName, @Email String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }


}
