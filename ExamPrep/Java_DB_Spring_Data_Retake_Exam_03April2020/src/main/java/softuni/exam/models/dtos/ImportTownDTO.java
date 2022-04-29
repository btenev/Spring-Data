package softuni.exam.models.dtos;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class ImportTownDTO {

    @Size(min = 2)
    private String name;

    @Positive
    private int population;

    private String guide;

    public ImportTownDTO(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }
}
