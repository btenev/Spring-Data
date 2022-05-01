package exam.model.dto.town;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "towns")
@XmlAccessorType(XmlAccessType.FIELD)
public class TownRootDTO {

    @XmlElement(name = "town")
    List<TownSummaryDTO> towns;

    public TownRootDTO() {}

    public List<TownSummaryDTO> getTowns() {
        return towns;
    }
}
