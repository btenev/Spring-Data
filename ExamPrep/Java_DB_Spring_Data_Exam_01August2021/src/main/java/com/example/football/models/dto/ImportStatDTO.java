package com.example.football.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "stats")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportStatDTO {

    @XmlElement(name = "stat")
    List<StatValues> stats;

    public ImportStatDTO() {}

    public List<StatValues> getStats() {
        return stats;
    }
}
