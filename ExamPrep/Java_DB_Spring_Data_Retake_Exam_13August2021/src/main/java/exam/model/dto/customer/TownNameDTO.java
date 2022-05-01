package exam.model.dto.customer;

import javax.validation.constraints.Size;

public class TownNameDTO {

    @Size(min = 2)
    private String name;

    public TownNameDTO(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
