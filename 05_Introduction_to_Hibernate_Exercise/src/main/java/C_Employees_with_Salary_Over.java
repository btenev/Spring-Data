import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;


public class C_Employees_with_Salary_Over {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("soft_uni");
        EntityManager em = emf.createEntityManager();

        List<String> listOfNames = em
                .createQuery("SELECT e.firstName FROM Employee e WHERE e.salary > 50000", String.class)
                .getResultList();

        listOfNames.forEach(System.out::println);
    }
}
