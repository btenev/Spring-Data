import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class B_Contains_employee {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("soft_uni");
        EntityManager em = emf.createEntityManager();

        Scanner scanner = new Scanner(System.in);

        String[] nameTokens = scanner.nextLine().split(" ");
        String firstName = "";
        String middleName = "";
        String lastName = "";

        long result;

        if (nameTokens.length == 3) {
            firstName = nameTokens[0];
            middleName = nameTokens[1];
            lastName = nameTokens[2];
            result = em.createQuery("SELECT count(e) FROM Employee e" +
                            " WHERE e.firstName = : first_Name AND  e.middleName = : middle_Name " +
                            "AND e.lastName = : last_Name", Long.class)
            .setParameter("first_Name", firstName)
            .setParameter("middle_Name", middleName)
            .setParameter("last_Name", lastName)
            .getSingleResult();
        } else {
            firstName = nameTokens[0];
            lastName = nameTokens[1];
                result = em.createQuery("SELECT count(e) FROM Employee e" +
                                " WHERE e.firstName = : first_Name AND e.lastName = : last_Name", Long.class)
                        .setParameter ("first_Name", firstName)
                        .setParameter("last_Name", lastName)
                        .getSingleResult();
        }

        String output = result == 0
                        ? "No"
                        : "Yes";
        System.out.println(output);

    }
}
