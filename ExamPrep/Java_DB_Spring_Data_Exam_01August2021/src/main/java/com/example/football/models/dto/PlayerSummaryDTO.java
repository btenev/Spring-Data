package com.example.football.models.dto;

import com.example.football.models.entity.Position;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "player")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerSummaryDTO {

    @XmlElement(name = "first-name")
    @Size(min = 3)
    private String firstName;

    @XmlElement(name = "last-name")
    @Size(min = 3)
    private String lastName;

    @XmlElement
    @Email
    private String email;

    @XmlElement(name = "birth-date")
    private String birthDate;

    @XmlElement
    private Position position;

    @XmlElement(name = "town")
    private TownNameDTO town;

    @XmlElement(name = "team")
    private TeamNameDTO team;

    @XmlElement(name = "stat")
    private StatIdDTO stat;

    public PlayerSummaryDTO() {}

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public Position getPosition() {
        return position;
    }

    public TownNameDTO getTown() {
        return town;
    }

    public TeamNameDTO getTeam() {
        return team;
    }

    public StatIdDTO getStat() {
        return stat;
    }
}
