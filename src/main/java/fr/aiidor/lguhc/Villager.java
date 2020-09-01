package fr.aiidor.lguhc;

public class Villager extends LGRole {

	public Villager(LGPlayer player) {
		super(player);
	}

	@Override
	public LGRoleType getRoleType() {
		return LGRoleType.VILLAGER;
	}

}
