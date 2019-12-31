package club.gamebreakers;

import club.gamebreakers.auth.AuthType;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.bytebuddy.agent.ByteBuddyAgent;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        OptionParser parser = new OptionParser();
        parser.accepts("pid").withRequiredArg().required().ofType(String.class);
        parser.accepts("type").withRequiredArg().required().ofType(String.class);
        parser.accepts("username").withRequiredArg().required().ofType(String.class);
        parser.accepts("password").withRequiredArg().required().ofType(String.class);

        OptionSet options;

        try {
            options = parser.parse(args);
        } catch (OptionException ex) {
            System.out.println("Usage: --pid <minecraft pid> --type <authentication type> --username <username> --password <password>");
            System.out.println("authentication types: " + Arrays.stream(AuthType.values()).map(AuthType::getIdentifier).collect(Collectors.joining("; ")));
            System.exit(1);
            return;
        }

        try {
            URI uri = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            File file = new File(uri);

            String newArgs = options.valueOf("type") + " " + options.valueOf("username") + " " + options.valueOf("password");

            System.out.println("Attaching....");
            ByteBuddyAgent
                    .attach(file, (String) options.valueOf("pid"), newArgs);
            System.out.println("Agent has completed.");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(420);
        }
    }

}
