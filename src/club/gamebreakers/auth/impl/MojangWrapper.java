package club.gamebreakers.auth.impl;

import club.gamebreakers.auth.AuthType;
import club.gamebreakers.auth.AuthWrapper;
import club.gamebreakers.exception.TransformException;
import club.gamebreakers.utils.AuthUtil;

public class MojangWrapper extends AuthWrapper {

    public MojangWrapper() {
        super(AuthType.MOJANG);
    }

    @Override
    public Object transform(String username, String password) throws TransformException {
        return null;
    }

    @Override
    public Object revert(String username, String password) {
        return null;
    }

    @Override
    public String[] getURLs() {
        return new String[] {
                "https://authserver.mojang.com/authenticate",
                "https://authserver.mojang.com/refresh",
                "https://authserver.mojang.com/validate",
                "https://authserver.mojang.com/invalidate",
                "https://authserver.mojang.com/signout"
        };
    }

    @Override
    public String[] getAllowedDomains() {
        return new String[0];
    }
}
