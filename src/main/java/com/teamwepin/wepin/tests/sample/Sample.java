package com.teamwepin.wepin.tests.sample;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Sample {

    @Id
    @GeneratedValue
    @Column(name = "sample_id")
    private Long id;

    private String name;

}
