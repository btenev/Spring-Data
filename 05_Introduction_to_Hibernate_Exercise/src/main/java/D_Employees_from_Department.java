import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class D_Employees_from_Department {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("soft_uni");
        EntityManager em = emf.createEntityManager();

        List<Employee> employeeList = em
                .createQuery("SELECT e FROM Employee e WHERE e.department.name = 'Research And Development'"
                        + " ORDER BY e.salary ASC, e.id ASC", Employee.class)
                .getResultList();

        employeeList.forEach(emp ->
            System.out.printf("%s %s from %s - $%.2f%n",
                    emp.getFirstName(), emp.getLastName(), emp.getDepartment().getName(), emp.getSalary()));
    }
}
