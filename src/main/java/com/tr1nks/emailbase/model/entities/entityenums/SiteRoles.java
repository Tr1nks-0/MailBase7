package com.tr1nks.emailbase.model.entities.entityenums;

/**
 * роли сайта
 */
public enum SiteRoles {
    /**
     * администратор
     */
    ADMIN("ADMIN"),
    /**
     * пользователь
     */
    USER("USER");

    private final String role;

    SiteRoles(String role) {
        this.role = role;
    }

    /**
     * получить строку роли
     *
     * @return строка роли
     */
    public String getRoleWithPrefix() {
        return "ROLE_" + this.role;
    }

    /**
     * получить строку роли без приписки "ROLE_"
     *
     * @return строка роли
     */
    public String getRole() {
        return this.role;
    }

//    /**
//     * получить строку роли в виде "hasRole('ИМЯ_РОЛИ')
//     * @param role роль
//     * @return строка роли
//     */
//    public static String hasRole(SiteRoles role) {
//        return "hasRole('" + role.role + "')";
//    }
}
