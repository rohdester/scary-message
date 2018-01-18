package dk.bearsoft.scaries.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ScaryMessage {

    @Id
    @GeneratedValue
    private long id;
    private String message;

    public ScaryMessage(long id, String message) {
        this.id = id;
        this.message = message;
    }

    public ScaryMessage(String message) {
        this.message = message;
    }

    ScaryMessage() {
    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
