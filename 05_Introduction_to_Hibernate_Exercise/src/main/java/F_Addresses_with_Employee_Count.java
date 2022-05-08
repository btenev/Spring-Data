import entities.Address;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class F_Addresses_with_Employee_Count {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("soft_uni");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.createQuery("SELECT a FROM Address a ORDER BY a.employees.size DESC", Address.class)
                .setMaxResults(10)
                .getResultStream()
                .forEach(address -> System.out.printf("%s, %s - %d employees%n",
                        address.getText(),address.getTown() == null ?"" : address.getTown().getName() , address.getEmployees().size()));
        em.getTransaction().commit();
    }
}
