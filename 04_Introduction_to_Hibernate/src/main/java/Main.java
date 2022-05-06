import entities.Student;

import javax.persistence.*;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("school");

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Student student = new Student("Teo", 25);
        em.persist(student);

        em.getTransaction().commit();
        em.close();
    }
}
