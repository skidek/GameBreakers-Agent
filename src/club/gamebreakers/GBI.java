package club.gamebreakers;

import club.gamebreakers.auth.AuthType;
import club.gamebreakers.auth.AuthWrapper;
import club.gamebreakers.obf.Obfuscation;
import club.gamebreakers.session.SessionHandler;
import club.gamebreakers.utils.reflection.ReflectionUtils;
import lombok.Getter;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.instrument.Instrumentation;

public class GBI {

    @Getter private static GBI instance;
    @Getter private static Instrumentation instrumentation;
    @Getter private static AuthWrapper wrapper;
    @Getter private static Obfuscation obfuscation;

    private static AuthType authType;
    private static String username, password;

    public GBI(String type, String user, String pass, Instrumentation inst) {
        instance = this;
        instrumentation = inst;

        ReflectionUtils.certCheckDisarm(getClass().getClassLoader());
        Obfuscation.load(instance);

        try {
            Thread.getAllStackTraces().keySet().stream().filter(t -> t.getName().equalsIgnoreCase("Client Thread")).findFirst().get();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Not a minecraft instance (Check 1)");
            return;
        }

        try {
            for (Obfuscation obf : Obfuscation.OBFUSCATIONS.values()) {
                if (obfuscation != null) break;

                String mcName = obf.getClassObfName("Minecraft"),
                       sessionName = obf.getClassObfName("Session");

                if (mcName == null || sessionName == null) continue;

                try {
                    ReflectionUtils.getClass(mcName);
                    ReflectionUtils.getClass(sessionName);
                } catch (Exception ex) {
                    showTrace(ex);
                    continue;
                }

                obfuscation = obf;
            }
            if (obfuscation == null) throw new NullPointerException();
        } catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Not a minecraft instance (Check 2)");
            return;
        }

        JOptionPane.showMessageDialog(null, "Detected MC ObfuscationTable: " + obfuscation.getDisplayName());

        AuthWrapper.load();
        SessionHandler.load(instance);

        authType = AuthType.getType(type);
        username = user;
        password = pass;

        if (authType == null) {
            System.out.println("Could not find authentication type.");
            return;
        }

        wrapper = AuthWrapper.WRAPPERS.get(authType);

        if (wrapper == null) {
            System.out.println("Could not find wrapper for authentication type.");
            return;
        }

        execute();
    }

    public static void showTrace(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String sStackTrace = sw.toString();
        JOptionPane.showMessageDialog(null, sStackTrace);
    }

    private boolean executed;

    public void execute() {
        if (executed) return;
        executed = true;
        Object result;
        try {
            result = wrapper.transform(username, password);
        } catch (Exception ex) {
            showTrace(ex);
            return;
        }

        if (result != null) {
            System.out.println("Authentication returned: \"" + result + "\"");
        }
        JOptionPane.showMessageDialog(null, "Auth injection complete.");
    }

}
