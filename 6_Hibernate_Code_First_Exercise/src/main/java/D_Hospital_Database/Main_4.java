package D_Hospital_Database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main_4 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CodeFirstEx");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();



        em.getTransaction().commit();
    }
}
