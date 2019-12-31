package club.gamebreakers;

import java.lang.instrument.Instrumentation;

public class PreMain {

    public static void premain(String arg, Instrumentation inst) {
        String[] args = arg.split(" ");

        if (args.length != 3) System.exit(133742069);

        String type = args[0], username = args[1], password = args[2];

        new GBI(type, username, password, inst);
    }

    public static void agentmain(String arg, Instrumentation inst) {
        premain(arg, inst);
    }

}