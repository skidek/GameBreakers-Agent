/*
 * Copyright (c)
 *     2017 - 2019; vaperion (Tamás Szabó)
 *     All Rights Reserved.
 *
 * NOTICE:
 *     The intellectual and technical concepts contained herein
 *     are, and remains the property of the author, and are protected
 *     by trade secret or copyright law. Dissemination of this information
 *     or reproduction of this material is strictly forbidden. Violators will
 *     be prosecuted to the fullest extent permitted by applicable law.
 */

package club.gamebreakers.utils.reflection;

public interface MethodInvoker {
    /**
     * Invoke a method on a specific target object.
     *
     * @param target    - the target object, or NULL for a static method.
     * @param arguments - the arguments to pass to the method.
     * @return The return value, or NULL if is void.
     */
    public Object invoke(Object target, Object... arguments);
    public Class<?>[] getParameterTypes();
}