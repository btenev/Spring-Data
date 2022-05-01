package exam.model.dto.town;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "town")
@XmlAccessorType(XmlAccessType.FIELD)
public class TownSummaryDTO {

    @XmlElement
    @Size(min = 2)
    private String name;

    @XmlElement
    @Positive
    private int population;

    @XmlElement(name = "travel-guide")
    @Size(min = 10)
    private String travelGuide;

    public TownSummaryDTO() {
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public String getTravelGuide() {
        return travelGuide;
    }
}
