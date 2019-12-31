package club.gamebreakers.obf.impl;

import club.gamebreakers.GBI;
import club.gamebreakers.obf.Obfuscation;
import club.gamebreakers.utils.Table;
import club.gamebreakers.utils.reflection.ReflectionUtils;

public class GENERAL_UNMAPPED extends Obfuscation {

    private Table<String, String, String>
            fieldTable = new Table<>(),
            methodTable = new Table<>(),
            classTable = new Table<>();

    public GENERAL_UNMAPPED(GBI instance) {
        super(instance);

        methodTable.put("getMinecraft", "getMinecraft", "Returns current minecraft instance");
        fieldTable.put("session", "session", "Session from Minecraft main class");

        fieldTable.put("playerID", "playerID", "UUID in session");
        fieldTable.put("token", "token", "Session Token in session");
        fieldTable.put("username", "username", "Username in session");

        Class<?> minecraftClass;
        Class<?> sessionClass;

        ReflectionUtils.certCheckDisarm(getClass().getClassLoader());
        try {
            minecraftClass = ReflectionUtils.getClass("net.minecraft.client.Minecraft");
            sessionClass = ReflectionUtils.getClass("net.minecraft.util.Session");
        } catch (Exception ex) {
            minecraftClass = null;
            sessionClass = null;
        }

        classTable.put((minecraftClass == null) ? null : "net.minecraft.client.Minecraft", "Minecraft", "Minecraft main class");
        classTable.put((sessionClass == null) ? null : "net.minecraft.util.Session", "Session", "Session class");
    }

    @Override
    public String getIdentifier() {
        return "general-unmapped";
    }

    @Override
    public String getDisplayName() {
        return "General (UNMAPPED)";
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