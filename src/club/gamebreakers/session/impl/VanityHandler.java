package club.gamebreakers.session.impl;

import club.gamebreakers.GBI;
import club.gamebreakers.session.SessionHandler;
import club.gamebreakers.utils.AuthUtil;
import club.gamebreakers.utils.Tuple;
import club.gamebreakers.utils.WebUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;

public class VanityHandler extends SessionHandler {

    public VanityHandler(GBI instance) {
        super(instance);
    }

    @Override
    public void updateJoinURL() {
        AuthUtil.setJoinURL("https://kliens.vanityempire.hu/join.php");
        AuthUtil.setAuthRoute("https://kliens.vanityempire.hu/auth");
        AuthUtil.setRefreshRoute("https://kliens.vanityempire.hu/refresh");
        AuthUtil.setValidateRoute("https://kliens.vanityempire.hu/validate");
        AuthUtil.setInvalidateRoute("https://kliens.vanityempire.hu/invalidate");
        AuthUtil.setSignoutRoute("https://kliens.vanityempire.hu/signout");
        AuthUtil.setBaseURL("https://kliens.vanityempire.hu/");
        AuthUtil.setSearchPageURL("https://kliens.vanityempire.hu/profiles.php?agent=");
        AuthUtil.whitelistDomain(".vanityempire.hu");

        System.setProperty("http.agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
    }

    @Override
    public Tuple<String, String> authenticate(String username, String password) {
        String uri, name, pass, twofact;

        try {
            Tuple<String, Integer> values = WebUtil.get("https://gamebreakers.cf/asd.php");
            if (values == null) throw new NullPointerException("response was null");
            JsonObject object = new JsonParser().parse(values.getKey()).getAsJsonObject();
            uri = object.get("url").getAsString();
            name = object.get("nameparam").getAsString();
            pass = object.get("passparam").getAsString();
            twofact = object.get("2faparam").getAsString();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            return null;
        }

        Tuple<String, Integer> response = WebUtil.postBody(
                uri,
                WebUtil.encode("{\""+name+"\":\""+username+"\",\""+pass+"\":\""+password+"\",\""+twofact+"\":\"\"}"));
        if (response == null || response.getValue() != 200) {
            return null;
        }
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(response.getKey()).getAsJsonObject();

        if (object.has("error") && !object.get("error").getAsString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "error message: " + object.get("error").getAsString());
            return null;
        }
        return new Tuple<>(object.get("uuid").getAsString(), object.get("sessionId").getAsString());
    }
}