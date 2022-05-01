package exam.model.dto.shop;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "shops")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShopRootDTO {

    @XmlElement(name = "shop")
    private List<ShopSummaryDTO> shops;

    public ShopRootDTO() {
    }

    public List<ShopSummaryDTO> getShops() {
        return shops;
    }
}
