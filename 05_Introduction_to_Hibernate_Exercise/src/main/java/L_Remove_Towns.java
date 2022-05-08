import entities.Address;
import entities.Town;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Scanner;

public class L_Remove_Towns {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("soft_uni");
        EntityManager em = emf.createEntityManager();

        Scanner scanner = new Scanner(System.in);
        String inputName = scanner.nextLine();

        Town town = em.createQuery("SELECT t FROM Town t WHERE t.name =: townName", Town.class)
                .setParameter("townName", inputName)
                .getSingleResult();

        int numberOfDeletedAddresses = returnCountOfRemovedAddressesSelectedByTown(em, town.getId());

        em.getTransaction().begin();
        em.remove(town);
        em.getTransaction().commit();

        System.out.printf("%d address in %s deleted", numberOfDeletedAddresses, town.getName());
    }

    private static int returnCountOfRemovedAddressesSelectedByTown(EntityManager em, Integer id) {
        List<Address> addresses = em
                .createQuery("SELECT a FROM  Address a WHERE a.town.id =: town_id", Address.class)
                .setParameter("town_id", id).getResultList();

        setAddressIdInEmployeesToZero(em ,addresses);

        em.getTransaction().begin();
        addresses.forEach(em::remove);
        em.getTransaction().commit();
        return addresses.size();
    }

    private static void setAddressIdInEmployeesToZero(EntityManager em, List <Address> address) {
        List<Integer> listOfIds = address.stream().map(Address::getId).toList();
        em.getTransaction().begin();
        em.createQuery("UPDATE Employee AS e SET e.address.id = null WHERE e.address.id  IN :addresses")
                .setParameter("addresses", listOfIds).executeUpdate();
        em.getTransaction().commit();
    }
}
