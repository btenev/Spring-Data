package C_University_System;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main_3 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CodeFirstEx");
        EntityManager em  = emf.createEntityManager();

        em.getTransaction().begin();

        em.getTransaction().commit();

    }
}
//The relationships between the tables are as follows:
//•	Each student can be enrolled in many courses and in each course many students can be enrolled
//•	A teacher can teach in many courses but one course can be taught only by one teacher