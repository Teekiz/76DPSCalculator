package Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.mods.receiver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor
public enum Ammo
{
	//P/Point, UC/Ultracite, R/Round
	P308(".308 round", "Ultracite .308 round"),
	P38(".38 round", "Ultracite .38 round"),
	P44(".44 round", "Ultracite .44 round"),
	P45(".45 round", "Ultracite .45 round"),
	P50BALL(".50 caliber ball", null),
	P50ROUND(".50 round", "Ultracite .50 round"),
	R10MM("10mm round", "Ultracite 10mm round"),
	R556("556 round", "Ultracite 556 round"),
	R5MM("5mm round", "Ultracite 5mm round"),
	ARROW("Arrow", "Ultracite arrow"),
	CANNONBALL("Cannonball", null),
	CROSSBOWBOLT("Crossbow bolt", "Ultracite crossbow bolt"),
	FLARE("Flare", null),
	HARPOON("Harpoon", null),
	PADDLEBALL("Paddle ball string", null),
	RAILWAYSPIKE("Railway spike", "Ultracite railway spike"),
	SHOTGUNSHELL("Shotgun shell", "Ultracite shotgun shell"),
	SYRINGER("Syringer ammo", null),
	R2MMEC("2mm electromagnetic cartridge", "Ultracite 2mm electromagnetic cartridge"),
	ALIENBLASTERROUND("Alien blaster round", null),
	CRYOCELL("Cryo cell", null),
	FUEL("Fuel", null),
	FUSIONCELL("Fusion cell", "Ultracite fusion cell"),
	FUSIONCORE("Fusion core", "Ultracite fusion core"),
	GAMMAROUND("Gamma round", null),
	PLASMACARTRIDGE("Plasma cartridge", "Ultracite plasma cartridge"),
	PLASMACORE("Plasma core", "Ultracite plasma core"),
	R40MMGRENADE("40mm grenade round", null),
	MINENUKE("Mine nuke", null),
	MISSLE("Missle", null);

	private final String ammoName;
	private final String ammoUltraciteName;
}
