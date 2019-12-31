package club.gamebreakers.obf;

import club.gamebreakers.GBI;
import club.gamebreakers.obf.impl.GENERAL_UNMAPPED;
import club.gamebreakers.obf.impl.VANILLA_1_8_8;
import club.gamebreakers.utils.Table;
import club.gamebreakers.utils.reflection.FieldAccessor;
import club.gamebreakers.utils.reflection.MethodInvoker;
import club.gamebreakers.utils.reflection.ReflectionUtils;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public abstract class Obfuscation {
    public static Map<Class<? extends Obfuscation>, Obfuscation> OBFUSCATIONS = new HashMap<>();

    public static void load(GBI instance) {
        OBFUSCATIONS.put(GENERAL_UNMAPPED.class, new GENERAL_UNMAPPED(instance));
        OBFUSCATIONS.put(VANILLA_1_8_8.class, new VANILLA_1_8_8(instance));
    }

    public static Obfuscation getByName(String name) {
        return OBFUSCATIONS.values().stream()
                .filter(obf ->
                        obf.getDisplayName().equalsIgnoreCase(name)
                        || obf.getIdentifier().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    protected final GBI instance;

    public Random getRandom() {
        return ThreadLocalRandom.current();
    }

    public abstract String getIdentifier();
    public abstract String getDisplayName();
    public abstract String getFieldObfName(String realFieldName);
    public abstract String getMethodObfName(String realMethodName);
    public abstract String getClassObfName(String realClassName);
    public abstract Table<String, String, String> getClassTable();

    public Class<?> getClass(String realClassName) {
        return ReflectionUtils.getClass(getClassObfName(realClassName));
    }

    public MethodInvoker getMethod(Class<?> clazz, String realMethodName, Class<?>... params) {
        return ReflectionUtils.getMethod(clazz, getMethodObfName(realMethodName), params);
    }

    public <T> FieldAccessor<T> getField(Class<?> clazz, String realFieldName, Class<?> type) {
        return (FieldAccessor<T>) ReflectionUtils.getField(clazz, getFieldObfName(realFieldName), type);
    }
}