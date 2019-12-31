package club.gamebreakers.auth;

import club.gamebreakers.auth.impl.MojangWrapper;
import club.gamebreakers.auth.impl.VanityWrapper;
import club.gamebreakers.exception.TransformException;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class AuthWrapper {
    public static Map<AuthType, AuthWrapper> WRAPPERS = new HashMap<>();

    public static void load() {
        WRAPPERS.put(AuthType.MOJANG, new MojangWrapper());
        WRAPPERS.put(AuthType.VANITY, new VanityWrapper());
    }

    private AuthType type;

    public AuthWrapper(AuthType type) {
        this.type = type;
    }

    public abstract Object transform(String username, String password) throws TransformException;
    public abstract Object revert(String username, String password);

    public abstract String[] getURLs();
    public abstract String[] getAllowedDomains();
}