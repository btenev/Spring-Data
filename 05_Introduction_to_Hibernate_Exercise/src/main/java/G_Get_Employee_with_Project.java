import entities.Employee;
import entities.Project;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Comparator;
import java.util.Scanner;

public class G_Get_Employee_with_Project {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("soft_uni");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Scanner scanner = new Scanner(System.in);
        int idEmployee = Integer.parseInt(scanner.nextLine());

        Employee employee = em.createQuery("SELECT e FROM Employee e WHERE e.id = : employee_id", Employee.class)
                .setParameter("employee_id",idEmployee).getSingleResult();

        System.out.printf("%s %s - %s%n", employee.getFirstName(), employee.getLastName(), employee.getJobTitle());
        employee
                .getProjects()
                .stream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(p -> System.out.println("      " + p.getName()));
        em.getTransaction().commit();

    }
}
