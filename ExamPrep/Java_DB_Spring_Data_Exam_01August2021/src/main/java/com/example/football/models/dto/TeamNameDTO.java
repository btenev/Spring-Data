package com.example.football.models.dto;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "town")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamNameDTO {

    @XmlElement(name = "name")
    @Size(min = 3)
    private String name;

    public TeamNameDTO() {}

    public String getName() {
        return name;
    }
}
