package club.gamebreakers.obf.impl;

import club.gamebreakers.GBI;
import club.gamebreakers.obf.Obfuscation;
import club.gamebreakers.utils.Table;
import club.gamebreakers.utils.reflection.ReflectionUtils;

public class VANILLA_1_8_8 extends Obfuscation {

    private Table<String, String, String>
            fieldTable = new Table<>(),
            methodTable = new Table<>(),
            classTable = new Table<>();

    public VANILLA_1_8_8(GBI instance) {
        super(instance);

        methodTable.put("A", "getMinecraft", "Returns current minecraft instance");
        fieldTable.put("ae", "session", "Session from Minecraft main class");

        fieldTable.put("b", "playerID", "UUID in session");
        fieldTable.put("c", "token", "Session Token in session");
        fieldTable.put("a", "username", "Username in session");

        Class<?> minecraftClass;
        Class<?> sessionClass;

        ReflectionUtils.certCheckDisarm(getClass().getClassLoader());
        try {
            minecraftClass = ReflectionUtils.getClass("ave");
            sessionClass = ReflectionUtils.getClass("avm");
        } catch (Exception ex) {
            minecraftClass = null;
            sessionClass = null;
        }

        classTable.put((minecraftClass == null) ? null : "ave", "Minecraft", "Minecraft main class");
        classTable.put((sessionClass == null) ? null : "avm", "Session", "Session class");
    }

    @Override
    public String getIdentifier() {
        return "vanilla188";
    }

    @Override
    public String getDisplayName() {
        return "Vanilla (1.8.8)";
    }

    @Override
    public String getFieldObfName(String realFieldName) {
        return (String) fieldTable.getByColumn(realFieldName).getRow();
    }

    @Override
    public String getMethodObfName(String realMethodName) {
        return (String) methodTable.getByColumn(realMethodName).getRow();
    }

    @Override
    public String getClassObfName(String realClassName) {
        return (String) classTable.getByColumn(realClassName).getRow();
    }

    @Override
    public Table<String, String, String> getClassTable() {
        return classTable;
    }
}