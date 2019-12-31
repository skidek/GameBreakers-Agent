package club.gamebreakers.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthType {

    MOJANG("mojang"),
    VANITY("vanity");

    private String identifier;

    public static AuthType getType(String name) {
        for (AuthType value : values()) {
            if (value.getIdentifier().equalsIgnoreCase(name)) return value;
        }
        return null;
    }

}
