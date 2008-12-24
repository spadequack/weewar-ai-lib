package weewarai.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import weewarai.datastr.DefaultValueMap;

/**
 * This class contains the Weewar unit and terrain specifications for
 * calculating battle damage and movement. All information is stored in maps.
 * (Note that this may not have been the most elegant way to organize it,
 * however it does keep all of these numbers in one file.)
 * 
 * @author spadequack
 */
public class Specs {

	/** Build costs for each unit */
	public static Map<String, Integer> buildCost = new HashMap<String, Integer>();

	static {
		// basic units
		buildCost.put(Unit.Trooper, 75);
		buildCost.put(Unit.Heavy_Trooper, 150);
		buildCost.put(Unit.Raider, 200);
		buildCost.put(Unit.Tank, 300);
		buildCost.put(Unit.Heavy_Tank, 600);
		buildCost.put(Unit.Light_Artillery, 200);
		buildCost.put(Unit.Heavy_Artillery, 600);
		buildCost.put(Unit.Capturing, 0); // TODO: needed?

		// pro units
		buildCost.put(Unit.Assault_Artillery, 450);
		buildCost.put(Unit.Berserker, 900);
		buildCost.put(Unit.DFA, 1200);
		buildCost.put(Unit.Anti_Aircraft, 300);
		buildCost.put(Unit.Hovercraft, 300);

		buildCost.put(Unit.Speedboat, 200);
		buildCost.put(Unit.Destroyer, 900);
		buildCost.put(Unit.Battleship, 2000);
		buildCost.put(Unit.Submarine, 1000);

		buildCost.put(Unit.Helicopter, 600);
		buildCost.put(Unit.Jetfighter, 800);
		buildCost.put(Unit.Bomber, 900);
	}

	/** Terrain attack specs (Name: (Type: Terrain Effect on Attack)) */
	public static Map<String, Map<String, Integer>> terrainAttack = new HashMap<String, Map<String, Integer>>();

	static {
		Map<String, Integer> airfield = new HashMap<String, Integer>();
		airfield.put(Unit.Soft_Type, 2);
		airfield.put(Unit.Hard_Type, 0);
		airfield.put(Unit.Air_Type, 3);
		airfield.put(Unit.Speedboat_Type, 0);
		airfield.put(Unit.Amphibic_Type, 0);
		airfield.put(Unit.Sub_Type, -2);
		airfield.put(Unit.Boat_Type, 0);
		terrainAttack.put(Terrain.Airfield, airfield);
	}

	static {
		Map<String, Integer> base = new HashMap<String, Integer>();
		base.put(Unit.Soft_Type, 2);
		base.put(Unit.Hard_Type, 0);
		base.put(Unit.Air_Type, 0);
		base.put(Unit.Speedboat_Type, 0);
		base.put(Unit.Amphibic_Type, 0);
		base.put(Unit.Sub_Type, 0);
		base.put(Unit.Boat_Type, 0);
		terrainAttack.put(Terrain.Base, base);
	}

	static {
		Map<String, Integer> desert = new HashMap<String, Integer>();
		desert.put(Unit.Soft_Type, -1);
		desert.put(Unit.Hard_Type, 0);
		desert.put(Unit.Air_Type, 0);
		desert.put(Unit.Speedboat_Type, 0);
		desert.put(Unit.Amphibic_Type, 0);
		desert.put(Unit.Sub_Type, 0);
		desert.put(Unit.Boat_Type, 0);
		terrainAttack.put(Terrain.Desert, desert);
	}

	static {
		Map<String, Integer> harbor = new HashMap<String, Integer>();
		harbor.put(Unit.Soft_Type, 2);
		harbor.put(Unit.Hard_Type, 0);
		harbor.put(Unit.Air_Type, 0);
		harbor.put(Unit.Speedboat_Type, 0);
		harbor.put(Unit.Amphibic_Type, 0);
		harbor.put(Unit.Sub_Type, -2);
		harbor.put(Unit.Boat_Type, 0);
		terrainAttack.put(Terrain.Harbor, harbor);
	}

	static {
		Map<String, Integer> mountains = new HashMap<String, Integer>();
		mountains.put(Unit.Soft_Type, 2);
		mountains.put(Unit.Hard_Type, -10);
		mountains.put(Unit.Air_Type, 0);
		mountains.put(Unit.Speedboat_Type, 0);
		mountains.put(Unit.Amphibic_Type, 0);
		mountains.put(Unit.Sub_Type, 0);
		mountains.put(Unit.Boat_Type, 0);
		terrainAttack.put(Terrain.Mountains, mountains);
	}

	static {
		Map<String, Integer> plains = new HashMap<String, Integer>();
		plains.put(Unit.Soft_Type, 0);
		plains.put(Unit.Hard_Type, 0);
		plains.put(Unit.Air_Type, 0);
		plains.put(Unit.Speedboat_Type, 0);
		plains.put(Unit.Amphibic_Type, 0);
		plains.put(Unit.Sub_Type, 0);
		plains.put(Unit.Boat_Type, 0);
		terrainAttack.put(Terrain.Plains, plains);
	}

	static {
		Map<String, Integer> repairPatch = new HashMap<String, Integer>();
		repairPatch.put(Unit.Soft_Type, 0);
		repairPatch.put(Unit.Hard_Type, 0);
		repairPatch.put(Unit.Air_Type, 0);
		repairPatch.put(Unit.Speedboat_Type, 0);
		repairPatch.put(Unit.Amphibic_Type, 0);
		repairPatch.put(Unit.Sub_Type, 0);
		repairPatch.put(Unit.Boat_Type, 0);
		terrainAttack.put(Terrain.Repair_Patch, repairPatch);
	}

	static {
		Map<String, Integer> swamp = new HashMap<String, Integer>();
		swamp.put(Unit.Soft_Type, -1);
		swamp.put(Unit.Hard_Type, -1);
		swamp.put(Unit.Air_Type, 0);
		swamp.put(Unit.Speedboat_Type, 0);
		swamp.put(Unit.Amphibic_Type, 0);
		swamp.put(Unit.Sub_Type, 0);
		swamp.put(Unit.Boat_Type, 0);
		terrainAttack.put(Terrain.Swamp, swamp);
	}

	static {
		Map<String, Integer> water = new HashMap<String, Integer>();
		water.put(Unit.Soft_Type, -10);
		water.put(Unit.Hard_Type, -10);
		water.put(Unit.Air_Type, 0);
		water.put(Unit.Speedboat_Type, 0);
		water.put(Unit.Amphibic_Type, 0);
		water.put(Unit.Sub_Type, 0);
		water.put(Unit.Boat_Type, 0);
		terrainAttack.put(Terrain.Water, water);
	}

	static {
		Map<String, Integer> woods = new HashMap<String, Integer>();
		woods.put(Unit.Soft_Type, 2);
		woods.put(Unit.Hard_Type, 0);
		woods.put(Unit.Air_Type, 0);
		woods.put(Unit.Speedboat_Type, 0);
		woods.put(Unit.Amphibic_Type, 0);
		woods.put(Unit.Sub_Type, 0);
		woods.put(Unit.Boat_Type, 0);
		terrainAttack.put(Terrain.Woods, woods);
	}

	/** Terrain defense specs (Name: (Type: Terrain Effect on Defense)) */
	public static Map<String, Map<String, Integer>> terrainDefense = new HashMap<String, Map<String, Integer>>();

	static {
		Map<String, Integer> airfield = new HashMap<String, Integer>();
		airfield.put(Unit.Soft_Type, 2);
		airfield.put(Unit.Hard_Type, -1);
		airfield.put(Unit.Air_Type, 3);
		airfield.put(Unit.Speedboat_Type, 0);
		airfield.put(Unit.Amphibic_Type, -1);
		airfield.put(Unit.Sub_Type, -1);
		airfield.put(Unit.Boat_Type, -1);
		terrainDefense.put(Terrain.Airfield, airfield);
	}

	static {
		Map<String, Integer> base = new HashMap<String, Integer>();
		base.put(Unit.Soft_Type, 2);
		base.put(Unit.Hard_Type, -1);
		base.put(Unit.Air_Type, 0);
		base.put(Unit.Speedboat_Type, 0);
		base.put(Unit.Amphibic_Type, 0);
		base.put(Unit.Sub_Type, 0);
		base.put(Unit.Boat_Type, 0);
		terrainDefense.put(Terrain.Base, base);
	}

	static {
		Map<String, Integer> desert = new HashMap<String, Integer>();
		desert.put(Unit.Soft_Type, -1);
		desert.put(Unit.Hard_Type, 0);
		desert.put(Unit.Air_Type, 0);
		desert.put(Unit.Speedboat_Type, 0);
		desert.put(Unit.Amphibic_Type, 0);
		desert.put(Unit.Sub_Type, 0);
		desert.put(Unit.Boat_Type, 0);
		terrainDefense.put(Terrain.Desert, desert);
	}

	static {
		Map<String, Integer> harbor = new HashMap<String, Integer>();
		harbor.put(Unit.Soft_Type, 2);
		harbor.put(Unit.Hard_Type, -1);
		harbor.put(Unit.Air_Type, 0);
		harbor.put(Unit.Speedboat_Type, 0);
		harbor.put(Unit.Amphibic_Type, -1);
		harbor.put(Unit.Sub_Type, -1);
		harbor.put(Unit.Boat_Type, -1);
		terrainDefense.put(Terrain.Harbor, harbor);
	}

	static {
		Map<String, Integer> mountains = new HashMap<String, Integer>();
		mountains.put(Unit.Soft_Type, 4);
		mountains.put(Unit.Hard_Type, -10);
		mountains.put(Unit.Air_Type, 0);
		mountains.put(Unit.Speedboat_Type, 0);
		mountains.put(Unit.Amphibic_Type, 0);
		mountains.put(Unit.Sub_Type, 0);
		mountains.put(Unit.Boat_Type, 0);
		terrainDefense.put(Terrain.Mountains, mountains);
	}

	static {
		Map<String, Integer> plains = new HashMap<String, Integer>();
		plains.put(Unit.Soft_Type, 0);
		plains.put(Unit.Hard_Type, 0);
		plains.put(Unit.Air_Type, 0);
		plains.put(Unit.Speedboat_Type, 0);
		plains.put(Unit.Amphibic_Type, 0);
		plains.put(Unit.Sub_Type, 0);
		plains.put(Unit.Boat_Type, 0);
		terrainDefense.put(Terrain.Plains, plains);
	}

	static {
		Map<String, Integer> repairPatch = new HashMap<String, Integer>();
		repairPatch.put(Unit.Soft_Type, -6);
		repairPatch.put(Unit.Hard_Type, -6);
		repairPatch.put(Unit.Air_Type, 0);
		repairPatch.put(Unit.Speedboat_Type, 0);
		repairPatch.put(Unit.Amphibic_Type, 0);
		repairPatch.put(Unit.Sub_Type, 0);
		repairPatch.put(Unit.Boat_Type, 0);
		terrainDefense.put(Terrain.Repair_Patch, repairPatch);
	}

	static {
		Map<String, Integer> swamp = new HashMap<String, Integer>();
		swamp.put(Unit.Soft_Type, -2);
		swamp.put(Unit.Hard_Type, -2);
		swamp.put(Unit.Air_Type, 0);
		swamp.put(Unit.Speedboat_Type, 0);
		swamp.put(Unit.Amphibic_Type, 0);
		swamp.put(Unit.Sub_Type, 0);
		swamp.put(Unit.Boat_Type, 0);
		terrainDefense.put(Terrain.Swamp, swamp);
	}

	static {
		Map<String, Integer> water = new HashMap<String, Integer>();
		water.put(Unit.Soft_Type, -10);
		water.put(Unit.Hard_Type, -10);
		water.put(Unit.Air_Type, 0);
		water.put(Unit.Speedboat_Type, 0);
		water.put(Unit.Amphibic_Type, 0);
		water.put(Unit.Sub_Type, 0);
		water.put(Unit.Boat_Type, 0);
		terrainDefense.put(Terrain.Water, water);
	}

	static {
		Map<String, Integer> woods = new HashMap<String, Integer>();
		woods.put(Unit.Soft_Type, 3);
		woods.put(Unit.Hard_Type, -3);
		woods.put(Unit.Air_Type, 0);
		woods.put(Unit.Speedboat_Type, 0);
		woods.put(Unit.Amphibic_Type, 0);
		woods.put(Unit.Sub_Type, 0);
		woods.put(Unit.Boat_Type, 0);
		terrainDefense.put(Terrain.Woods, woods);
	}

	/** Movement cost that represents the unit cannot move there */
	public static final int UNPASSABLE = 999999;

	/** Max number of tiles that a unit can move in one move */
	public static final int MAX_MOVE_RANGE = 6;

	/** Terrain movement specs (Name: (Type: Movement Cost)) */
	public static Map<String, Map<String, Integer>> terrainMovement = new HashMap<String, Map<String, Integer>>();

	static {
		Map<String, Integer> airfield = new HashMap<String, Integer>();
		airfield.put(Unit.Soft_Type, 3);
		airfield.put(Unit.Hard_Type, 2);
		airfield.put(Unit.Air_Type, 3);
		airfield.put(Unit.Speedboat_Type, UNPASSABLE);
		airfield.put(Unit.Amphibic_Type, 3);
		airfield.put(Unit.Sub_Type, UNPASSABLE);
		airfield.put(Unit.Boat_Type, UNPASSABLE);
		terrainMovement.put(Terrain.Airfield, airfield);
	}

	static {
		Map<String, Integer> base = new HashMap<String, Integer>();
		base.put(Unit.Soft_Type, 3);
		base.put(Unit.Hard_Type, 2);
		base.put(Unit.Air_Type, 3);
		base.put(Unit.Speedboat_Type, UNPASSABLE);
		base.put(Unit.Amphibic_Type, 3);
		base.put(Unit.Sub_Type, UNPASSABLE);
		base.put(Unit.Boat_Type, UNPASSABLE);
		terrainMovement.put(Terrain.Base, base);
	}

	static {
		Map<String, Integer> desert = new HashMap<String, Integer>();
		desert.put(Unit.Soft_Type, 5);
		desert.put(Unit.Hard_Type, 4);
		desert.put(Unit.Air_Type, 3);
		desert.put(Unit.Speedboat_Type, UNPASSABLE);
		desert.put(Unit.Amphibic_Type, 3);
		desert.put(Unit.Sub_Type, UNPASSABLE);
		desert.put(Unit.Boat_Type, UNPASSABLE);
		terrainMovement.put(Terrain.Desert, desert);
	}

	static {
		Map<String, Integer> harbor = new HashMap<String, Integer>();
		harbor.put(Unit.Soft_Type, 3);
		harbor.put(Unit.Hard_Type, 2);
		harbor.put(Unit.Air_Type, 3);
		harbor.put(Unit.Speedboat_Type, 3);
		harbor.put(Unit.Amphibic_Type, 3);
		harbor.put(Unit.Sub_Type, 3);
		harbor.put(Unit.Boat_Type, 3);
		terrainMovement.put(Terrain.Harbor, harbor);
	}

	static {
		Map<String, Integer> mountains = new HashMap<String, Integer>();
		mountains.put(Unit.Soft_Type, 6);
		mountains.put(Unit.Hard_Type, UNPASSABLE);
		mountains.put(Unit.Air_Type, 3);
		mountains.put(Unit.Speedboat_Type, UNPASSABLE);
		mountains.put(Unit.Amphibic_Type, UNPASSABLE);
		mountains.put(Unit.Sub_Type, UNPASSABLE);
		mountains.put(Unit.Boat_Type, UNPASSABLE);
		terrainMovement.put(Terrain.Mountains, mountains);
	}

	static {
		Map<String, Integer> plains = new HashMap<String, Integer>();
		plains.put(Unit.Soft_Type, 3);
		plains.put(Unit.Hard_Type, 3);
		plains.put(Unit.Air_Type, 3);
		plains.put(Unit.Speedboat_Type, UNPASSABLE);
		plains.put(Unit.Amphibic_Type, 3);
		plains.put(Unit.Sub_Type, UNPASSABLE);
		plains.put(Unit.Boat_Type, UNPASSABLE);
		terrainMovement.put(Terrain.Plains, plains);
	}

	static {
		Map<String, Integer> repairPatch = new HashMap<String, Integer>();
		repairPatch.put(Unit.Soft_Type, 3);
		repairPatch.put(Unit.Hard_Type, 3);
		repairPatch.put(Unit.Air_Type, 3);
		repairPatch.put(Unit.Speedboat_Type, UNPASSABLE);
		repairPatch.put(Unit.Amphibic_Type, 3);
		repairPatch.put(Unit.Sub_Type, UNPASSABLE);
		repairPatch.put(Unit.Boat_Type, UNPASSABLE);
		terrainMovement.put(Terrain.Repair_Patch, repairPatch);
	}

	static {
		Map<String, Integer> swamp = new HashMap<String, Integer>();
		swamp.put(Unit.Soft_Type, 6);
		swamp.put(Unit.Hard_Type, 6);
		swamp.put(Unit.Air_Type, 3);
		swamp.put(Unit.Speedboat_Type, UNPASSABLE);
		swamp.put(Unit.Amphibic_Type, 3);
		swamp.put(Unit.Sub_Type, UNPASSABLE);
		swamp.put(Unit.Boat_Type, UNPASSABLE);
		terrainMovement.put(Terrain.Swamp, swamp);
	}

	static {
		Map<String, Integer> water = new HashMap<String, Integer>();
		water.put(Unit.Soft_Type, UNPASSABLE);
		water.put(Unit.Hard_Type, UNPASSABLE);
		water.put(Unit.Air_Type, 3);
		water.put(Unit.Speedboat_Type, 3);
		water.put(Unit.Amphibic_Type, 3);
		water.put(Unit.Sub_Type, 3);
		water.put(Unit.Boat_Type, 3);
		terrainMovement.put(Terrain.Water, water);
	}

	static {
		Map<String, Integer> woods = new HashMap<String, Integer>();
		woods.put(Unit.Soft_Type, 4);
		woods.put(Unit.Hard_Type, 6);
		woods.put(Unit.Air_Type, 3);
		woods.put(Unit.Speedboat_Type, UNPASSABLE);
		woods.put(Unit.Amphibic_Type, UNPASSABLE);
		woods.put(Unit.Sub_Type, UNPASSABLE);
		woods.put(Unit.Boat_Type, UNPASSABLE);
		terrainMovement.put(Terrain.Woods, woods);
	}

	/** Unit attack specs (Name: (Type: Attack Strength)) */
	public static Map<String, Map<String, Integer>> unitAttack = new HashMap<String, Map<String, Integer>>();

	static {
		Map<String, Integer> antiAircraft = new HashMap<String, Integer>();
		antiAircraft.put(Unit.Soft_Type, 8);
		antiAircraft.put(Unit.Hard_Type, 3);
		antiAircraft.put(Unit.Air_Type, 9);
		antiAircraft.put(Unit.Speedboat_Type, 3);
		antiAircraft.put(Unit.Amphibic_Type, 3);
		antiAircraft.put(Unit.Sub_Type, 0);
		antiAircraft.put(Unit.Boat_Type, 3);
		unitAttack.put(Unit.Anti_Aircraft, antiAircraft);
	}

	static {
		Map<String, Integer> assualtArtillery = new HashMap<String, Integer>();
		assualtArtillery.put(Unit.Soft_Type, 8);
		assualtArtillery.put(Unit.Hard_Type, 6);
		assualtArtillery.put(Unit.Air_Type, 6);
		assualtArtillery.put(Unit.Speedboat_Type, 6);
		assualtArtillery.put(Unit.Amphibic_Type, 6);
		assualtArtillery.put(Unit.Sub_Type, 0);
		assualtArtillery.put(Unit.Boat_Type, 6);
		unitAttack.put(Unit.Assault_Artillery, assualtArtillery);
	}

	static {
		Map<String, Integer> battleship = new HashMap<String, Integer>();
		battleship.put(Unit.Soft_Type, 10);
		battleship.put(Unit.Hard_Type, 14);
		battleship.put(Unit.Air_Type, 6);
		battleship.put(Unit.Speedboat_Type, 14);
		battleship.put(Unit.Amphibic_Type, 14);
		battleship.put(Unit.Sub_Type, 4);
		battleship.put(Unit.Boat_Type, 14);
		unitAttack.put(Unit.Battleship, battleship);
	}

	static {
		Map<String, Integer> berserker = new HashMap<String, Integer>();
		berserker.put(Unit.Soft_Type, 14);
		berserker.put(Unit.Hard_Type, 16);
		berserker.put(Unit.Air_Type, 0);
		berserker.put(Unit.Speedboat_Type, 14);
		berserker.put(Unit.Amphibic_Type, 14);
		berserker.put(Unit.Sub_Type, 0);
		berserker.put(Unit.Boat_Type, 14);
		unitAttack.put(Unit.Berserker, berserker);
	}

	static {
		Map<String, Integer> bomber = new HashMap<String, Integer>();
		bomber.put(Unit.Soft_Type, 14);
		bomber.put(Unit.Hard_Type, 14);
		bomber.put(Unit.Air_Type, 0);
		bomber.put(Unit.Speedboat_Type, 14);
		bomber.put(Unit.Amphibic_Type, 14);
		bomber.put(Unit.Sub_Type, 0);
		bomber.put(Unit.Boat_Type, 14);
		unitAttack.put(Unit.Bomber, bomber);
	}

	static {
		Map<String, Integer> dfa = new HashMap<String, Integer>();
		dfa.put(Unit.Soft_Type, 16);
		dfa.put(Unit.Hard_Type, 14);
		dfa.put(Unit.Air_Type, 0);
		dfa.put(Unit.Speedboat_Type, 14);
		dfa.put(Unit.Amphibic_Type, 14);
		dfa.put(Unit.Sub_Type, 0);
		dfa.put(Unit.Boat_Type, 14);
		unitAttack.put(Unit.DFA, dfa);
	}

	static {
		Map<String, Integer> destroyer = new HashMap<String, Integer>();
		destroyer.put(Unit.Soft_Type, 10);
		destroyer.put(Unit.Hard_Type, 10);
		destroyer.put(Unit.Air_Type, 12);
		destroyer.put(Unit.Speedboat_Type, 12);
		destroyer.put(Unit.Amphibic_Type, 12);
		destroyer.put(Unit.Sub_Type, 16);
		destroyer.put(Unit.Boat_Type, 10);
		unitAttack.put(Unit.Destroyer, destroyer);
	}

	static {
		Map<String, Integer> heavyArtillery = new HashMap<String, Integer>();
		heavyArtillery.put(Unit.Soft_Type, 12);
		heavyArtillery.put(Unit.Hard_Type, 10);
		heavyArtillery.put(Unit.Air_Type, 10);
		heavyArtillery.put(Unit.Speedboat_Type, 10);
		heavyArtillery.put(Unit.Amphibic_Type, 10);
		heavyArtillery.put(Unit.Sub_Type, 0);
		heavyArtillery.put(Unit.Boat_Type, 10);
		unitAttack.put(Unit.Heavy_Artillery, heavyArtillery);
	}

	static {
		Map<String, Integer> heavyTank = new HashMap<String, Integer>();
		heavyTank.put(Unit.Soft_Type, 10);
		heavyTank.put(Unit.Hard_Type, 12);
		heavyTank.put(Unit.Air_Type, 10);
		heavyTank.put(Unit.Speedboat_Type, 10);
		heavyTank.put(Unit.Amphibic_Type, 10);
		heavyTank.put(Unit.Sub_Type, 0);
		heavyTank.put(Unit.Boat_Type, 10);
		unitAttack.put(Unit.Heavy_Tank, heavyTank);
	}

	static {
		Map<String, Integer> heavyTrooper = new HashMap<String, Integer>();
		heavyTrooper.put(Unit.Soft_Type, 6);
		heavyTrooper.put(Unit.Hard_Type, 8);
		heavyTrooper.put(Unit.Air_Type, 6);
		heavyTrooper.put(Unit.Speedboat_Type, 8);
		heavyTrooper.put(Unit.Amphibic_Type, 8);
		heavyTrooper.put(Unit.Sub_Type, 0);
		heavyTrooper.put(Unit.Boat_Type, 8);
		unitAttack.put(Unit.Heavy_Trooper, heavyTrooper);
	}

	static {
		Map<String, Integer> helicopter = new HashMap<String, Integer>();
		helicopter.put(Unit.Soft_Type, 16);
		helicopter.put(Unit.Hard_Type, 10);
		helicopter.put(Unit.Air_Type, 6);
		helicopter.put(Unit.Speedboat_Type, 12);
		helicopter.put(Unit.Amphibic_Type, 12);
		helicopter.put(Unit.Sub_Type, 0);
		helicopter.put(Unit.Boat_Type, 8);
		unitAttack.put(Unit.Helicopter, helicopter);
	}

	static {
		Map<String, Integer> hovercraft = new HashMap<String, Integer>();
		hovercraft.put(Unit.Soft_Type, 10);
		hovercraft.put(Unit.Hard_Type, 6);
		hovercraft.put(Unit.Air_Type, 0);
		hovercraft.put(Unit.Speedboat_Type, 8);
		hovercraft.put(Unit.Amphibic_Type, 10);
		hovercraft.put(Unit.Sub_Type, 0);
		hovercraft.put(Unit.Boat_Type, 6);
		unitAttack.put(Unit.Hovercraft, hovercraft);
	}

	static {
		Map<String, Integer> jetfighter = new HashMap<String, Integer>();
		jetfighter.put(Unit.Soft_Type, 6);
		jetfighter.put(Unit.Hard_Type, 8);
		jetfighter.put(Unit.Air_Type, 16);
		jetfighter.put(Unit.Speedboat_Type, 6);
		jetfighter.put(Unit.Amphibic_Type, 6);
		jetfighter.put(Unit.Sub_Type, 0);
		jetfighter.put(Unit.Boat_Type, 6);
		unitAttack.put(Unit.Jetfighter, jetfighter);
	}

	static {
		Map<String, Integer> lightArtillery = new HashMap<String, Integer>();
		lightArtillery.put(Unit.Soft_Type, 10);
		lightArtillery.put(Unit.Hard_Type, 4);
		lightArtillery.put(Unit.Air_Type, 0);
		lightArtillery.put(Unit.Speedboat_Type, 4);
		lightArtillery.put(Unit.Amphibic_Type, 4);
		lightArtillery.put(Unit.Sub_Type, 0);
		lightArtillery.put(Unit.Boat_Type, 4);
		unitAttack.put(Unit.Light_Artillery, lightArtillery);
	}

	static {
		Map<String, Integer> raider = new HashMap<String, Integer>();
		raider.put(Unit.Soft_Type, 10);
		raider.put(Unit.Hard_Type, 4);
		raider.put(Unit.Air_Type, 4);
		raider.put(Unit.Speedboat_Type, 4);
		raider.put(Unit.Amphibic_Type, 8);
		raider.put(Unit.Sub_Type, 0);
		raider.put(Unit.Boat_Type, 4);
		unitAttack.put(Unit.Raider, raider);
	}

	static {
		Map<String, Integer> speedboat = new HashMap<String, Integer>();
		speedboat.put(Unit.Soft_Type, 8);
		speedboat.put(Unit.Hard_Type, 6);
		speedboat.put(Unit.Air_Type, 6);
		speedboat.put(Unit.Speedboat_Type, 10);
		speedboat.put(Unit.Amphibic_Type, 16);
		speedboat.put(Unit.Sub_Type, 0);
		speedboat.put(Unit.Boat_Type, 6);
		unitAttack.put(Unit.Speedboat, speedboat);
	}

	static {
		Map<String, Integer> submarine = new HashMap<String, Integer>();
		submarine.put(Unit.Soft_Type, 0);
		submarine.put(Unit.Hard_Type, 0);
		submarine.put(Unit.Air_Type, 0);
		submarine.put(Unit.Speedboat_Type, 0);
		submarine.put(Unit.Amphibic_Type, 0);
		submarine.put(Unit.Sub_Type, 10);
		submarine.put(Unit.Boat_Type, 16);
		unitAttack.put(Unit.Submarine, submarine);
	}

	static {
		Map<String, Integer> tank = new HashMap<String, Integer>();
		tank.put(Unit.Soft_Type, 10);
		tank.put(Unit.Hard_Type, 7);
		tank.put(Unit.Air_Type, 0);
		tank.put(Unit.Speedboat_Type, 7);
		tank.put(Unit.Amphibic_Type, 7);
		tank.put(Unit.Sub_Type, 0);
		tank.put(Unit.Boat_Type, 7);
		unitAttack.put(Unit.Tank, tank);
	}

	static {
		Map<String, Integer> trooper = new HashMap<String, Integer>();
		trooper.put(Unit.Soft_Type, 6);
		trooper.put(Unit.Hard_Type, 3);
		trooper.put(Unit.Air_Type, 0);
		trooper.put(Unit.Speedboat_Type, 3);
		trooper.put(Unit.Amphibic_Type, 3);
		trooper.put(Unit.Sub_Type, 0);
		trooper.put(Unit.Boat_Type, 3);
		unitAttack.put(Unit.Trooper, trooper);
	}

	/** Unit defense specs (Name: Defense Strength) */
	public static Map<String, Integer> unitDefense = new HashMap<String, Integer>();

	static {
		unitDefense.put(Unit.Anti_Aircraft, 4);
		unitDefense.put(Unit.Assault_Artillery, 6);
		unitDefense.put(Unit.Battleship, 14);
		unitDefense.put(Unit.Berserker, 14);
		unitDefense.put(Unit.Bomber, 10);
		unitDefense.put(Unit.DFA, 14);
		unitDefense.put(Unit.Destroyer, 12);
		unitDefense.put(Unit.Heavy_Artillery, 4);
		unitDefense.put(Unit.Heavy_Tank, 14);
		unitDefense.put(Unit.Heavy_Trooper, 6); // 2 when capturing
		unitDefense.put(Unit.Helicopter, 10);
		unitDefense.put(Unit.Hovercraft, 8); // ? when capturing
		unitDefense.put(Unit.Jetfighter, 12);
		unitDefense.put(Unit.Light_Artillery, 3);
		unitDefense.put(Unit.Raider, 8);
		unitDefense.put(Unit.Speedboat, 6);
		unitDefense.put(Unit.Submarine, 10);
		unitDefense.put(Unit.Tank, 10);
		unitDefense.put(Unit.Trooper, 6); // 2 when capturing
	}

	/** Unit mobility first move specs (Name: Movement Points) */
	public static Map<String, Integer> unitMobilityFirst = new HashMap<String, Integer>();

	static {
		unitMobilityFirst.put(Unit.Anti_Aircraft, 9);
		unitMobilityFirst.put(Unit.Assault_Artillery, 12);
		unitMobilityFirst.put(Unit.Battleship, 6);
		unitMobilityFirst.put(Unit.Berserker, 6);
		unitMobilityFirst.put(Unit.Bomber, 18);
		unitMobilityFirst.put(Unit.DFA, 6);
		unitMobilityFirst.put(Unit.Destroyer, 12);
		unitMobilityFirst.put(Unit.Heavy_Artillery, 6);
		unitMobilityFirst.put(Unit.Heavy_Tank, 7);
		unitMobilityFirst.put(Unit.Heavy_Trooper, 6);
		unitMobilityFirst.put(Unit.Helicopter, 15);
		unitMobilityFirst.put(Unit.Hovercraft, 12);
		unitMobilityFirst.put(Unit.Jetfighter, 18);
		unitMobilityFirst.put(Unit.Light_Artillery, 9);
		unitMobilityFirst.put(Unit.Raider, 12);
		unitMobilityFirst.put(Unit.Speedboat, 12);
		unitMobilityFirst.put(Unit.Submarine, 9);
		unitMobilityFirst.put(Unit.Tank, 9);
		unitMobilityFirst.put(Unit.Trooper, 9);
	}

	/** Unit mobility second move sepcs (Name: Movement Points) */
	public static Map<String, Integer> unitMobilitySecond = new HashMap<String, Integer>();

	static {
		unitMobilitySecond.put(Unit.Anti_Aircraft, 0);
		unitMobilitySecond.put(Unit.Assault_Artillery, 0);
		unitMobilitySecond.put(Unit.Battleship, 0);
		unitMobilitySecond.put(Unit.Berserker, 0);
		unitMobilitySecond.put(Unit.Bomber, 6);
		unitMobilitySecond.put(Unit.DFA, 0);
		unitMobilitySecond.put(Unit.Destroyer, 0);
		unitMobilitySecond.put(Unit.Heavy_Artillery, 0);
		unitMobilitySecond.put(Unit.Heavy_Tank, 0);
		unitMobilitySecond.put(Unit.Heavy_Trooper, 0);
		unitMobilitySecond.put(Unit.Helicopter, 3);
		unitMobilitySecond.put(Unit.Hovercraft, 0);
		unitMobilitySecond.put(Unit.Jetfighter, 6);
		unitMobilitySecond.put(Unit.Light_Artillery, 0);
		unitMobilitySecond.put(Unit.Raider, 0);
		unitMobilitySecond.put(Unit.Speedboat, 0);
		unitMobilitySecond.put(Unit.Submarine, 0);
		unitMobilitySecond.put(Unit.Tank, 0);
		unitMobilitySecond.put(Unit.Trooper, 0);
	}

	/** Unit types (Name: Type) */
	public static Map<String, String> unitTypes = new HashMap<String, String>();

	static {
		unitTypes.put(Unit.Anti_Aircraft, Unit.Hard_Type);
		unitTypes.put(Unit.Assault_Artillery, Unit.Hard_Type);
		unitTypes.put(Unit.Battleship, Unit.Boat_Type);
		unitTypes.put(Unit.Berserker, Unit.Hard_Type);
		unitTypes.put(Unit.Bomber, Unit.Air_Type);
		unitTypes.put(Unit.DFA, Unit.Hard_Type);
		unitTypes.put(Unit.Destroyer, Unit.Boat_Type);
		unitTypes.put(Unit.Heavy_Artillery, Unit.Hard_Type);
		unitTypes.put(Unit.Heavy_Tank, Unit.Hard_Type);
		unitTypes.put(Unit.Heavy_Trooper, Unit.Soft_Type);
		unitTypes.put(Unit.Helicopter, Unit.Air_Type);
		unitTypes.put(Unit.Hovercraft, Unit.Amphibic_Type);
		unitTypes.put(Unit.Jetfighter, Unit.Air_Type);
		unitTypes.put(Unit.Light_Artillery, Unit.Hard_Type);
		unitTypes.put(Unit.Raider, Unit.Hard_Type);
		unitTypes.put(Unit.Speedboat, Unit.Speedboat_Type);
		unitTypes.put(Unit.Submarine, Unit.Sub_Type);
		unitTypes.put(Unit.Tank, Unit.Hard_Type);
		unitTypes.put(Unit.Trooper, Unit.Soft_Type);
	}

	private static final String DEFAULT_KEY = "default";

	/** Unit types (Name: (Type: MinRange)) */
	public static Map<String, Map<String, Integer>> unitMinRange = new HashMap<String, Map<String, Integer>>();

	static {
		// since the min range is the same against all unit types for each unit
		// these Maps are just used for consistency between unitMinRange and
		// unitMaxRange. this would be useful if it varied, like how Anti-Air
		// have a different max range vs air than vs all others
		Map<String, Integer> defaultIsOne = new DefaultValueMap<String, Integer>(
				new HashMap<String, Integer>(), 1, DEFAULT_KEY);
		Map<String, Integer> defaultIsTwo = new DefaultValueMap<String, Integer>(
				new HashMap<String, Integer>(), 2, DEFAULT_KEY);
		Map<String, Integer> defaultIsThree = new DefaultValueMap<String, Integer>(
				new HashMap<String, Integer>(), 3, DEFAULT_KEY);
		unitMinRange.put(Unit.Anti_Aircraft, defaultIsOne);
		unitMinRange.put(Unit.Assault_Artillery, defaultIsOne);
		unitMinRange.put(Unit.Battleship, defaultIsOne);
		unitMinRange.put(Unit.Berserker, defaultIsOne);
		unitMinRange.put(Unit.Bomber, defaultIsOne);
		unitMinRange.put(Unit.DFA, defaultIsTwo);
		unitMinRange.put(Unit.Destroyer, defaultIsOne);
		unitMinRange.put(Unit.Heavy_Artillery, defaultIsThree);
		unitMinRange.put(Unit.Heavy_Tank, defaultIsOne);
		unitMinRange.put(Unit.Heavy_Trooper, defaultIsOne);
		unitMinRange.put(Unit.Helicopter, defaultIsOne);
		unitMinRange.put(Unit.Hovercraft, defaultIsOne);
		unitMinRange.put(Unit.Jetfighter, defaultIsOne);
		unitMinRange.put(Unit.Light_Artillery, defaultIsTwo);
		unitMinRange.put(Unit.Raider, defaultIsOne);
		unitMinRange.put(Unit.Speedboat, defaultIsOne);
		unitMinRange.put(Unit.Submarine, defaultIsOne);
		unitMinRange.put(Unit.Tank, defaultIsOne);
		unitMinRange.put(Unit.Trooper, defaultIsOne);
	}

	public static int MAX_UNIT_RANGE = 5;

	/** Unit types (Name: (Type: MaxRange)) */
	public static Map<String, Map<String, Integer>> unitMaxRange = new HashMap<String, Map<String, Integer>>();

	static {
		// this is necessary since Anti-Air have a different max range vs air
		// than vs all others
		Map<String, Integer> defaultIsOne = new DefaultValueMap<String, Integer>(
				new HashMap<String, Integer>(), 1, DEFAULT_KEY);
		Map<String, Integer> defaultIsTwo = new DefaultValueMap<String, Integer>(
				new HashMap<String, Integer>(), 2, DEFAULT_KEY);
		Map<String, Integer> defaultIsThree = new DefaultValueMap<String, Integer>(
				new HashMap<String, Integer>(), 3, DEFAULT_KEY);
		Map<String, Integer> defaultIsFour = new DefaultValueMap<String, Integer>(
				new HashMap<String, Integer>(), 4, DEFAULT_KEY);
		Map<String, Integer> defaultIsFive = new DefaultValueMap<String, Integer>(
				new HashMap<String, Integer>(), 5, DEFAULT_KEY);

		unitMaxRange.put(Unit.Anti_Aircraft, defaultIsThree);
		unitMaxRange.get(Unit.Anti_Aircraft).put(Unit.Air_Type, 1);
		unitMaxRange.put(Unit.Assault_Artillery, defaultIsTwo);
		unitMaxRange.put(Unit.Battleship, defaultIsFour);
		unitMaxRange.put(Unit.Berserker, defaultIsOne);
		unitMaxRange.put(Unit.Bomber, defaultIsOne);
		unitMaxRange.put(Unit.DFA, defaultIsFive);
		unitMaxRange.put(Unit.Destroyer, defaultIsThree);
		unitMaxRange.put(Unit.Heavy_Artillery, defaultIsFour);
		unitMaxRange.put(Unit.Heavy_Tank, defaultIsOne);
		unitMaxRange.put(Unit.Heavy_Trooper, defaultIsOne);
		unitMaxRange.put(Unit.Helicopter, defaultIsOne);
		unitMaxRange.put(Unit.Hovercraft, defaultIsOne);
		unitMaxRange.put(Unit.Jetfighter, defaultIsOne);
		unitMaxRange.put(Unit.Light_Artillery, defaultIsThree);
		unitMaxRange.put(Unit.Raider, defaultIsOne);
		unitMaxRange.put(Unit.Speedboat, defaultIsOne);
		unitMaxRange.put(Unit.Submarine, defaultIsTwo);
		unitMaxRange.put(Unit.Tank, defaultIsOne);
		unitMaxRange.put(Unit.Trooper, defaultIsOne);
	}

	/** Units that cannot enter enemy harbors */
	public static List<String> unitsCannotEnterEnemyHarbor = new LinkedList<String>();

	static {
		unitsCannotEnterEnemyHarbor.add(Unit.Battleship);
		unitsCannotEnterEnemyHarbor.add(Unit.Destroyer);
		unitsCannotEnterEnemyHarbor.add(Unit.Submarine);
	}

}
