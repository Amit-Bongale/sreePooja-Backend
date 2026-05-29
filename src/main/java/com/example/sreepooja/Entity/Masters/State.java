package com.example.sreepooja.Entity.Masters;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "states")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String stateName;

    @Builder.Default
    private Boolean active = true;

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL)
    @Builder.Default
    private List<City> cities = new ArrayList<>();
}