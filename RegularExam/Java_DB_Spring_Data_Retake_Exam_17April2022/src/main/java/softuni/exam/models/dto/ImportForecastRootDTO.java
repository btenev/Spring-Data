package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "forecasts")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportForecastRootDTO {

    @XmlElement(name = "forecast")
    private List<SummaryForecastDTO> forecasts;

    public ImportForecastRootDTO() {
    }

    public List<SummaryForecastDTO> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<SummaryForecastDTO> forecasts) {
        this.forecasts = forecasts;
    }

}
