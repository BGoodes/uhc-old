package fr.aiidor.dwuhc;

public class Hermit extends DWRole {
	
	public Hermit(DWplayer player, DWCamp camp) {
		super(player, camp);
	}

	@Override
	public final DWRoleType getRoleType() {
		return DWRoleType.HERMIT;
	}
}
