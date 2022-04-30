package softuni.exam.instagraphlite.models.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "posts")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportRootPostDTO {

    @XmlElement(name = "post")
    private List<SummaryPostDTO> posts;

    public ImportRootPostDTO(){
    }

    public List<SummaryPostDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<SummaryPostDTO> posts) {
        this.posts = posts;
    }
}
