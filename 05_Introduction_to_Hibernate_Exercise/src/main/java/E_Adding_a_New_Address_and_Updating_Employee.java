import entities.Address;
import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class E_Adding_a_New_Address_and_Updating_Employee {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("soft_uni");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Scanner scanner = new Scanner(System.in);
        String lastName = scanner.nextLine();

        Employee selectedEmployees = em
                .createQuery("SELECT e FROM Employee e WHERE e.lastName = : last_name", Employee.class)
                .setParameter("last_name", lastName).getSingleResult();

        String addressToInsert = "Vitoshka 15";


        Address address = new Address();
        em.persist(address);
        address.setText(addressToInsert);

        selectedEmployees.setAddress(address);
        em.getTransaction().commit();
    }
}
