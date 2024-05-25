package br.com.ibm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "TAB_PIX_KEY")
public class PixKey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="pix_key_id")
    private UUID id;
    private String type;
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private User user;
    private String keyValue;

}
