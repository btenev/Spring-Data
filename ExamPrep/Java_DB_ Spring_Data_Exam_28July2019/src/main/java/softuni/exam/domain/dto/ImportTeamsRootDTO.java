package softuni.exam.domain.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "teams")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportTeamsRootDTO {

    @XmlElement(name = "team")
    private List<TeamSummaryDTO> teams;

    public ImportTeamsRootDTO() {
    }

    public List<TeamSummaryDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamSummaryDTO> teams) {
        this.teams = teams;
    }
}
