import entities.Employee;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class J_Find_Employees_by_First_Name {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("soft_uni");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        em.createQuery("SELECT e FROM Employee e WHERE e.firstName LIKE :pattern", Employee.class)
                .setParameter("pattern",input + "%" )
                .getResultStream()
                .forEach(e->
                        System.out.printf("%s %s %s - ($%.2f)%n",e.getFirstName(), e.getLastName(),
                                e.getJobTitle(), e.getSalary() ));


    }
}
