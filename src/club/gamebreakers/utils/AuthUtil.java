package club.gamebreakers.utils;

import club.gamebreakers.GBI;
import club.gamebreakers.session.SessionHandler;
import club.gamebreakers.utils.reflection.ConstructorInvoker;
import club.gamebreakers.utils.reflection.FieldAccessor;
import club.gamebreakers.utils.reflection.MethodInvoker;
import club.gamebreakers.utils.reflection.ReflectionUtils;

import javax.swing.*;
import java.net.Proxy;
import java.net.URL;

public class AuthUtil {

    private static final Class<?> YMSS = ReflectionUtils.getClass("com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService");
    private static final Class<?> YUA = ReflectionUtils.getClass("com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication");
    private static final Class<?> YGPR = ReflectionUtils.getClass("com.mojang.authlib.yggdrasil.YggdrasilGameProfileRepository");

    public static void swapSession(String username, String password, Class<? extends SessionHandler> sessionHandlerClazz) {
        SessionHandler sessionHandler = SessionHandler.HANDLERS.get(sessionHandlerClazz);
        if (sessionHandler == null) {
            JOptionPane.showMessageDialog(null, "no session handler");
            return;
        }
        Tuple<String, String> result = sessionHandler.authenticate(username, password);
        if (result == null) {
            JOptionPane.showMessageDialog(null, "no auth");
            return;
        }

        String uuid = result.getKey();
        String token = result.getValue();

        Class<?> sessionClazz = GBI.getObfuscation().getClass("Session");
        Class<?> minecraftClass = GBI.getObfuscation().getClass("Minecraft");

        if (sessionClazz == null) {
            JOptionPane.showMessageDialog(null, "No Session class");
            return;
        }
        if (minecraftClass == null) {
            JOptionPane.showMessageDialog(null, "No Minecraft class");
            return;
        }

        FieldAccessor<?> sessionField = GBI.getObfuscation().getField(minecraftClass, "session", sessionClazz);
        MethodInvoker getMinecraft = GBI.getObfuscation().getMethod(minecraftClass, "getMinecraft");

        Object mcInstance = getMinecraft.invoke(null);
        Object session = sessionField.get(mcInstance);

        FieldAccessor<String> usernameField = GBI.getObfuscation().getField(sessionClazz, "username", String.class),
                uuidField = GBI.getObfuscation().getField(sessionClazz, "playerID", String.class),
                tokenField = GBI.getObfuscation().getField(sessionClazz, "token", String.class);

        usernameField.set(session, username);
        uuidField.set(session, uuid);
        tokenField.set(session, token);

        sessionHandler.updateJoinURL();
    }

    @Deprecated
    public static void login(String username, String password) {
        Class<?> yas = ReflectionUtils.getClass("com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService"),
                yua = ReflectionUtils.getClass("com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication"),
                agent = ReflectionUtils.getClass("com.mojang.authlib.Agent");

        FieldAccessor<?> minecraftAgent = ReflectionUtils.getField(agent, "MINECRAFT", agent);

        ConstructorInvoker serviceInvoker = ReflectionUtils.getConstructor(yas, Proxy.class, String.class),
                            uaInvoker = ReflectionUtils.getConstructor(yua, yas, agent);
        MethodInvoker
                userSetter = ReflectionUtils.getMethod(yua, "setUsername", String.class),
                passSetter = ReflectionUtils.getMethod(yua, "setPassword", String.class),
                logIn = ReflectionUtils.getMethod(yua, "logIn");

        Object service = serviceInvoker.invoke(Proxy.NO_PROXY, "");
        Object auth = uaInvoker.invoke(service, minecraftAgent.get(null));

        userSetter.invoke(auth, username);
        passSetter.invoke(auth, password);

        logIn.invoke(auth);
    }

    public static void whitelistDomain(String domain) {
        whitelistDomains(new String[] {domain});
    }

    public static void whitelistDomains(String[] domains) {
        if (domains.length == 0) return;

        Class<?> yms = ReflectionUtils.getClass("com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService");

        FieldAccessor<String[]> list = ReflectionUtils.getField(yms, "WHITELISTED_DOMAINS", String[].class);
        String[] whitelisted_domains = list.get(null);
        String[] newDomains = new String[whitelisted_domains.length + domains.length];

        for (int i = 0; i < whitelisted_domains.length; i++) {
            newDomains[i] = whitelisted_domains[i];
        }
        int last = newDomains.length - 1;
        for (String domain : domains) {
            newDomains[last] = domain;
            last--;
        }

        list.set(null, newDomains);
    }

    public static void setJoinURL(String url) {
        ReflectionUtils.getField(YMSS, "JOIN_URL", URL.class).set(null, createURL(url));
    }

    public static void setAuthRoute(String url) {
        ReflectionUtils.getField(YUA, "ROUTE_AUTHENTICATE", URL.class).set(null, createURL(url));
    }

    public static void setRefreshRoute(String url) {
        ReflectionUtils.getField(YUA, "ROUTE_REFRESH", URL.class).set(null, createURL(url));
    }

    public static void setValidateRoute(String url) {
        ReflectionUtils.getField(YUA, "ROUTE_VALIDATE", URL.class).set(null, createURL(url));
    }

    public static void setInvalidateRoute(String url) {
        ReflectionUtils.getField(YUA, "ROUTE_INVALIDATE", URL.class).set(null, createURL(url));
    }

    public static void setSignoutRoute(String url) {
        ReflectionUtils.getField(YUA, "ROUTE_SIGNOUT", URL.class).set(null, createURL(url));
    }

    public static void setBaseURL(String url) {
        ReflectionUtils.getField(YGPR, "BASE_URL", String.class).set(null, url);
    }

    public static void setSearchPageURL(String url) {
        ReflectionUtils.getField(YGPR, "SEARCH_PAGE_URL", String.class).set(null, url);
    }

    public static URL createURL(String uri) {
        try {
            return new URL(uri);
        } catch (Exception ex) {}
        return null;
    }

}
