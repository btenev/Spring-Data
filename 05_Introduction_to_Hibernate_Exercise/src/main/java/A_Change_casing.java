import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class A_Change_casing {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("soft_uni");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        List<Town> getTownList = em.createQuery("SELECT t FROM Town t", Town.class).getResultList();

        for (Town town : getTownList) {
            if (town.getName().length() <= 5) {
                String upperCaseTown = town.getName().toUpperCase();
                town.setName(upperCaseTown);

            }
        }

        em.getTransaction().commit();
        em.close();
    }
}
