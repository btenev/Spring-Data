import entities.Project;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class H_Find_Latest_10_Projects {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("soft_uni");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        List<Project> projectList = em.createQuery("SELECT p FROM Project p order by p.startDate DESC", Project.class)
                .setMaxResults(10)
                .getResultList();

        projectList
                .stream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(p -> {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                        System.out.printf("Project name: %s%n" +
                                "        Project Description: %s%n" +
                                "        Project Start Date:%s%n" +
                                "        Project End Date: %s%n", p.getName(), p.getDescription(), dtf.format(p.getStartDate().minusHours(3)) ,p.getEndDate());
                        ;
                });

        em.getTransaction().commit();
    }
}
