import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class K_Employees_Maximum_Salaries {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("soft_uni");
        EntityManager em  = emf.createEntityManager();

        em.getTransaction().begin();

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = em.
                createNativeQuery("SELECT d.name,max(salary) as max_salary  FROM employees as e"
                + " JOIN departments d on e.department_id = d.department_id"
                + " GROUP BY d.name"
                + " HAVING max_salary not between 30000 AND 70000").getResultList();

        resultList.forEach(d -> System.out.println(d[0].toString() + " " + d[1]));
    }
}
