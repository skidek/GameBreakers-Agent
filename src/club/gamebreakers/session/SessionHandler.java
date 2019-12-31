package club.gamebreakers.session;

import club.gamebreakers.GBI;
import club.gamebreakers.session.impl.VanityHandler;
import club.gamebreakers.utils.Tuple;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public abstract class SessionHandler {
    public static Map<Class<? extends SessionHandler>, SessionHandler> HANDLERS = new HashMap<>();

    public static void load(GBI instance) {
        HANDLERS.put(VanityHandler.class, new VanityHandler(instance));
    }

    protected final GBI instance;

    public Random getRandom() {
        return ThreadLocalRandom.current();
    }

    public abstract void updateJoinURL();
    public abstract Tuple<String, String> authenticate(String username, String password);
}