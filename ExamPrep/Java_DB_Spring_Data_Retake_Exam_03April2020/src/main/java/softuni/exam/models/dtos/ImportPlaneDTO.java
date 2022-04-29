package softuni.exam.models.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "planes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportPlaneDTO {

    @XmlElement(name = "plane")
    private List<PlaneSummaryDTO> planes;

    public ImportPlaneDTO() {
    }

    public List<PlaneSummaryDTO> getPlanes() {
        return planes;
    }

    public void setPlanes(List<PlaneSummaryDTO> planes) {
        this.planes = planes;
    }

}
