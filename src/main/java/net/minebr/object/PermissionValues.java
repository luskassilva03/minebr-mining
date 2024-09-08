package net.minebr.object;

import lombok.Getter;

@Getter
public class PermissionValues {

    private final String group;
    private final String display;
    private final String permission;
    private final double bonus;

    public PermissionValues(String group, String display, String permission, double bonus) {
        this.group = group;
        this.display = display;
        this.permission = permission;
        this.bonus = bonus;
    }
}
