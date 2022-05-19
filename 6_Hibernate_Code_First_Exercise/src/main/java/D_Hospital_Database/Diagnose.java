package D_Hospital_Database;

import javax.persistence.*;

@Entity
@Table(name = "diagnoses")
public class Diagnose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50)
    private String name;

    private String comment;

    @ManyToOne
    private Patient patient;

    public Diagnose() {}

    public Diagnose(String name, String comment, Patient patient) {
        this.name = name;
        this.comment = comment;
        this.patient = patient;
    }
}
