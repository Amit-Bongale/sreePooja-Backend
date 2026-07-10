package com.example.sreepooja.Entity.Priests;

import com.example.sreepooja.Entity.Masters.Language;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "priest_languages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriestLanguageMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priest_id")
    private Priest priest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;
}
