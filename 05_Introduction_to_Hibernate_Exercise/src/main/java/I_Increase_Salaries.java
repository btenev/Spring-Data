import entities.Employee;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.List;

public class I_Increase_Salaries {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("soft_uni");
        EntityManager em  = emf.createEntityManager();

        em.getTransaction().begin();

        List<Employee> employeeList = em.createQuery("SELECT e FROM Employee e" +
                    " WHERE e.department.name IN ('Engineering', 'Tool Design' , 'Marketing', 'Information Services')",
                    Employee.class)
                    .getResultList();

        employeeList.forEach(e-> e.setSalary(e.getSalary().multiply(BigDecimal.valueOf(1.12))));

        employeeList.forEach(e -> System.out.printf("%s %s ($%.2f)%n", e.getFirstName(), e.getLastName(),e.getSalary()));
        em.getTransaction().commit();


    }
}
