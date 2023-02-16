package johncervantes.springproject.dao;

import johncervantes.springproject.entity.Role;

public interface RoleDao {
	public Role findRoleByName(String theRoleName);
}
