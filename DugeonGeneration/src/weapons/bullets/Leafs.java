package weapons.bullets;

import weapons.EntityType;
import weapons.abstracts.Bullet;

public class Leafs extends Bullet{

	public Leafs(EntityType typeToIgnore) {
		super(typeToIgnore);
		damage=17;
		lifetimeInMs=3500;
		bulletName="leafs";
		setSpeed(500);
	}

}
