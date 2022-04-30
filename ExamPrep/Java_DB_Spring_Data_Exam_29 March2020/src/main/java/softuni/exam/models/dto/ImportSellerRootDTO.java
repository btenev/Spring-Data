package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sellers")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportSellerRootDTO {

    @XmlElement(name = "seller")
    private List<SellerSummaryDTO> sellers;

    public ImportSellerRootDTO() {
    }

    public List<SellerSummaryDTO> getSellers() {
        return sellers;
    }

    public void setSellers(List<SellerSummaryDTO> sellers) {
        this.sellers = sellers;
    }
}
