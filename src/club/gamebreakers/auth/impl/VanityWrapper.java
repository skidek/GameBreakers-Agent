package club.gamebreakers.auth.impl;

import club.gamebreakers.auth.AuthType;
import club.gamebreakers.auth.AuthWrapper;
import club.gamebreakers.exception.TransformException;
import club.gamebreakers.session.impl.VanityHandler;
import club.gamebreakers.utils.AuthUtil;

public class VanityWrapper extends AuthWrapper {

    public VanityWrapper() {
        super(AuthType.VANITY);
    }

    @Override
    public Object transform(String username, String password) throws TransformException {
        AuthUtil.swapSession(username, password, VanityHandler.class);
        return null;
    }

    @Override
    public Object revert(String username, String password) {
        AuthUtil.login(username, password);
        return null;
    }

    @Override
    public String[] getURLs() {
        return new String[] {
                "https://kliens.vanityempire.hu/auth",
                "https://kliens.vanityempire.hu/refresh",
                "https://kliens.vanityempire.hu/validate",
                "https://kliens.vanityempire.hu/invalidate",
                "https://kliens.vanityempire.hu/signout"
        };
    }

    @Override
    public String[] getAllowedDomains() {
        return new String[] {".vanityempire.hu"};
    }
}
