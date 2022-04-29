package softuni.exam.models.dtos;

import softuni.exam.models.entities.Ticket;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "tickets")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportTicketRootDTO {

    @XmlElement(name = "ticket")
    private List<TicketSummaryDTO> tickets;

    private ImportTicketRootDTO(){
    }

    public List<TicketSummaryDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketSummaryDTO> tickets) {
        this.tickets = tickets;
    }

}
