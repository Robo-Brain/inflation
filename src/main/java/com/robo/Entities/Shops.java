package com.robo.Entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shops {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    Integer id;

    @Column(name = "name", nullable = false)
    @Getter
    @Setter
    String name;

    public Shops(String name) {
        this.name = name;
    }
}
