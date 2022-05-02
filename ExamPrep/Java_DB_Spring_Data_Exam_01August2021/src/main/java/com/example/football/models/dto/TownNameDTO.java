package com.example.football.models.dto;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "town")
@XmlAccessorType(XmlAccessType.FIELD)
public class TownNameDTO {

    @XmlElement(name = "name")
    @Size(min = 2)
    private String name;

    public TownNameDTO() {
    }

    public String getName() {
        return name;
    }
}
