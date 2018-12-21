package uk.co.jacekk.bukkit.bloodmoon;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import net.minecraft.server.v1_13_R2.EntityTypes;

public class DeprecationManager {

	public static Material getRandomStoneBricks(int random) {
		switch (random) {
		case 0:
			return Material.STONE_BRICKS;
		case 1:
			return Material.MOSSY_STONE_BRICKS;
		case 2:
			return Material.CRACKED_STONE_BRICKS;
		case 3:
			return Material.CHISELED_STONE_BRICKS;
		default:
			return Material.STONE_BRICKS;
		}
	}

	@SuppressWarnings("rawtypes")
	public static EntityTypes getEntityTypes(String type) {
		switch (type) {
		case "bat":
			return EntityTypes.BAT;
		case "blaze":
			return EntityTypes.BLAZE;
		case "cave_spider":
			return EntityTypes.CAVE_SPIDER;
		case "chicken":
			return EntityTypes.CHICKEN;
		case "cow":
			return EntityTypes.COW;
		case "creeper":
			return EntityTypes.CREEPER;
		case "donkey":
			return EntityTypes.DONKEY;
		case "elder_guardian":
			return EntityTypes.ELDER_GUARDIAN;
//			case "ender_dragon":
//				return EntityTypes.ENDER_DRAGON;
		case "enderman":
			return EntityTypes.ENDERMAN;
		case "endermite":
			return EntityTypes.ENDERMITE;
		case "evoker":
			return EntityTypes.EVOKER;
		case "ghast":
			return EntityTypes.GHAST;
		case "giant":
			return EntityTypes.GIANT;
		case "guardian":
			return EntityTypes.GUARDIAN;
		case "horse":
			return EntityTypes.HORSE;
		case "illusioner":
			return EntityTypes.ILLUSIONER;
		case "llama":
			return EntityTypes.LLAMA;
		case "magma_cube":
			return EntityTypes.MAGMA_CUBE;
		case "mule":
			return EntityTypes.MULE;
		case "mooshroom":
			return EntityTypes.MOOSHROOM;
		case "ocelot":
			return EntityTypes.OCELOT;
		case "parrot":
			return EntityTypes.PARROT;
		case "pig":
			return EntityTypes.PIG;
		case "zombie_pigman":
			return EntityTypes.ZOMBIE_PIGMAN;
		case "polar_bear":
			return EntityTypes.POLAR_BEAR;
		case "rabbit":
			return EntityTypes.RABBIT;
		case "sheep":
			return EntityTypes.SHEEP;
		case "silverfish":
			return EntityTypes.SILVERFISH;
		case "skeleton":
			return EntityTypes.SKELETON;
		case "skeleton_horse":
			return EntityTypes.SKELETON_HORSE;
		case "slime":
			return EntityTypes.SLIME;
		case "snow_golem":
			return EntityTypes.SNOW_GOLEM;
		case "spider":
			return EntityTypes.SPIDER;
		case "squid":
			return EntityTypes.SQUID;
		case "husk":
			return EntityTypes.HUSK;
		case "turtle":
			return EntityTypes.TURTLE;
		case "villager":
			return EntityTypes.VILLAGER;
		case "iron_golem":
			return EntityTypes.IRON_GOLEM;
		case "witch":
			return EntityTypes.WITCH;
		case "wolf":
			return EntityTypes.WOLF;
		case "zombie":
			return EntityTypes.ZOMBIE;
		case "zombie_horse":
			return EntityTypes.ZOMBIE_HORSE;
		case "zombie_villager":
			return EntityTypes.ZOMBIE_VILLAGER;
		case "cod":
			return EntityTypes.COD;
		case "dolphin":
			return EntityTypes.DOLPHIN;
		default:
			return EntityTypes.BAT;
		}
	}
	
	public static BlockFace directionToBlockFace(int dir) {
	    switch (dir) {
	    case 0:
	        return BlockFace.DOWN;
	    case 1:
	        return BlockFace.UP;
	    case 2:
	        return BlockFace.NORTH;
	    case 3:
	        return BlockFace.SOUTH;
	    case 4:
	        return BlockFace.WEST;
	    case 5:
	        return BlockFace.EAST;
	    default:
	        return BlockFace.SELF;
	    }
	}
}
