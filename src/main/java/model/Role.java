package model;

public class Role {
    private int roleId;
    private String roleName;

    public Role() {
    }

    public Role(int role_id, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRole_id(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "Role{" + "role_id=" + roleId + ", role_name=" + roleName + '}';
    }
}
