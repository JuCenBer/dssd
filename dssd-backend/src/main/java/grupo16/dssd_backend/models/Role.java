package grupo16.dssd_backend.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    ONG_SOL("ong_sol"),
    ONG_COL("ong_col"),
    DIRECTIVO("directivo");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static Role fromValue(String name) {
        for (Role contact : values()) {
            String currentContact = contact.getName();
            if (currentContact.equals(name)) {
                return contact;
            }
        }
        // Return a response entity with a 400 Bad Request status
        throw new IllegalArgumentException("Invalid value for Role Enum: " + name);
    }
}
