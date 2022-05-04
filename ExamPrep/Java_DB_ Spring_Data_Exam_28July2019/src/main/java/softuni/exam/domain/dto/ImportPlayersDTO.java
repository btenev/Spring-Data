package softuni.exam.domain.dto;

import softuni.exam.domain.entities.Position;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class ImportPlayersDTO {
    @NotBlank
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 15)
    private String lastName;

    @Min(1)
    @Max(99)
    private int number;

    @NotNull
    @Min(0)
    private BigDecimal salary;

    @NotNull
    private Position position;

    private PictureDTO picture;

    private TeamSummaryDTO team;

    public ImportPlayersDTO() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public PictureDTO getPicture() {
        return picture;
    }

    public void setPicture(PictureDTO picture) {
        this.picture = picture;
    }

    public TeamSummaryDTO getTeam() {
        return team;
    }

    public void setTeam(TeamSummaryDTO team) {
        this.team = team;
    }
}
