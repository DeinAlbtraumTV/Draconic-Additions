package net.foxmcloud.draconicadditions;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.draconicevolution.handlers.DESounds;

import codechicken.lib.vec.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.registries.ForgeRegistries;

public class CommonMethods {

	public static final DamageSource chaosBurst = new DamageSource("chaosBurst").bypassArmor();
	private static final short gracePeriod = 100;

	//Must be called every tick, else check will fail.
	public static boolean cheatCheck(ItemStack stack, World world) {
		long containerTime = ItemNBTHelper.getLong(stack, "cheatCheck", 0);
		long serverTime = world.getGameTime();
		boolean isCheating = false;
		if (containerTime < serverTime - gracePeriod && containerTime > gracePeriod)  {
			isCheating = true;
		}
		ItemNBTHelper.setLong(stack, "cheatCheck", serverTime);
		return isCheating;
	}

	public static void explodeEntity(Vector3 pos, World world) {
		world.playSound(null, new BlockPos(pos.x, pos.y, pos.z), DESounds.beam, SoundCategory.MASTER, 0.25F, 0.5F);
		world.playSound(null, new BlockPos(pos.x, pos.y, pos.z), DESounds.fusionComplete, SoundCategory.MASTER, 1.0F, 2.0F);
		if (world.isClientSide) {
			for (int i = 0; i < 5; i++) {
				//BCEffectHandler.spawnFX(DEParticles.ARROW_SHOCKWAVE, world, pos, pos, 128D, 2);
			}
		}
	}

	public static Direction getHorizontalDirectionFromLookAngle(Vector2f lookAngle) {
		float r = lookAngle.y;
		while (r < 0) {
			r += 360;
		}
		r = r % 360;
		if (r > 315 || r <= 45) {
			return Direction.SOUTH;
		}
		else if (r > 45 && r <= 135) {
			return Direction.WEST;
		}
		else if (r > 135 && r <= 225) {
			return Direction.NORTH;
		}
		else return Direction.EAST;
	}

	public static Direction getDirectionFromLookAngle(Vector2f lookAngle) {
		if (lookAngle.x > 45) {
			return Direction.UP;
		}
		else if (lookAngle.x < -45) {
			return Direction.DOWN;
		}
		else return getHorizontalDirectionFromLookAngle(lookAngle);
	}

	/**
	 * Used to set a block state in a world without calling ANY update code whatsoever.
	 * This is very unsafe and should ONLY be used to temporarily set a block during a tick,
	 * then before the tick ends, set it back to what it was originally or call the update methods.
	 * @param world The world to set the block state in.
	 * @param pos The position of the block to set the block state.
	 * @param blockState The block state to set at the given position.
	 */

	public static void overwriteBlockStateUnsafe(World world, BlockPos pos, BlockState blockState) {
		pos = pos.immutable();
		int x = pos.getX() & 15;
		int y = pos.getY();
		int z = pos.getZ() & 15;
		Chunk chunk = world.getChunkAt(pos);
		chunk.setBlockState(pos, blockState, false);
		/*
		ChunkSection chunksection = chunk.getSections()[y >> 4];
		if (chunksection == Chunk.EMPTY_SECTION) {
			chunksection = new ChunkSection(y >> 4 << 4);
			chunk.getSections()[y >> 4] = chunksection;
		}
		TileEntity oldTile = world.getBlockEntity(pos);
		BlockState oldState = chunksection.getStates().getAndSetUnchecked(x, y & 15, z, blockState);
		if (oldState == blockState) {
			return;
		}
		if (blockState.hasTileEntity()) {
			if (oldTile != null && !oldTile.getType().getRegistryName().toString().contentEquals(blockState.getBlock().getRegistryName().toString())) {
				world.setBlockEntity(pos, blockState.createTileEntity(world));
			}
		}
		*/
	}

	public static CompoundNBT createFakeNBT(BlockPos pos) {
		CompoundNBT fakeNBT = new CompoundNBT();
		fakeNBT.putInt("x", pos.getX());
		fakeNBT.putInt("y", pos.getY());
		fakeNBT.putInt("z", pos.getZ());
		return fakeNBT;
	}

	/**
	 * Used to essentially take a snapshot of a block.  Has additional helper methods not present
	 * in the Forge BlockSnapshot to help with saving this data to items for later restoration.
	 * <p>
	 * Also useful for temporarily setting a block during a tick then removing the block
	 * before the tick finishes, like in the {@link net.foxmcloud.draconicadditions.items.curios.ModularHarness}.
	 */

	public static class BlockStorage {
		private World oldWorld;
		private BlockPos oldPos;
		public BlockState blockState;
		public CompoundNBT tileNBT;

		public BlockStorage(World world, BlockPos pos) {
			storeBlockAt(world, pos);
		}

		public BlockStorage(World world, BlockPos pos, boolean removeBlock) {
			storeBlockAt(world, pos, removeBlock);
		}

		public BlockStorage(World world, BlockPos pos, BlockState blockState, CompoundNBT tileNBT) {
			oldWorld = world;
			oldPos = pos;
			this.blockState = blockState;
			this.tileNBT = tileNBT;
		}

		public void storeBlockAt(World world, BlockPos pos) {
			storeBlockAt(world, pos, false);
		}

		public void storeBlockAt(World world, BlockPos pos, boolean removeBlock) {
			oldWorld = world;
			oldPos = pos;
			blockState = world.getBlockState(pos);
			TileEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity != null) {
				tileNBT = tileEntity.serializeNBT();
				if (removeBlock) {
					world.getBlockEntity(pos).deserializeNBT(createFakeNBT(pos));
					world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				}
			}
		}

		public boolean restoreBlockAt(World world, BlockPos pos, @Nullable Vector2f rotation) {
			return restoreBlockAt(world, pos, rotation, true);
		}

		public boolean restoreBlockAt(World world, BlockPos pos, @Nullable Vector2f rotation, boolean permanent) {
			if (blockState != null) {
				BlockStorage oldBlock = new BlockStorage(world, pos, false);
				int flags = permanent ? BlockFlags.UPDATE_NEIGHBORS + BlockFlags.DEFAULT : 0;
				if (permanent) world.setBlock(pos, blockState, flags);
				else overwriteBlockStateUnsafe(world, pos, blockState);
				BlockState newState = world.getBlockState(pos);
				if (newState.getBlock() != blockState.getBlock()) {
					oldBlock.restoreBlock();
					return false;
				}
				if (blockState.hasTileEntity() && tileNBT != null && tileNBT.contains("id")) {
					TileEntity tileEntity = world.getBlockEntity(pos);
					if (tileEntity != null) {
						String savedID = tileNBT.getString("id");
						String tileID = tileEntity.getType().getRegistryName().toString();
						if (savedID.contentEquals(tileID)) {
							tileEntity.deserializeNBT(tileNBT);
							tileEntity.setLevelAndPosition(world, pos);
							if (permanent) tileEntity.requestModelDataUpdate();
						}
					}
				}
				if (rotation != null) {
					blockState.getValues().forEach((prop, comp) -> {
						if (prop instanceof DirectionProperty) {
							DirectionProperty dirProp = (DirectionProperty)prop;
							if (dirProp == BlockStateProperties.FACING) {
								blockState = blockState.setValue(dirProp, getDirectionFromLookAngle(rotation));
							}
							else if (dirProp == BlockStateProperties.HORIZONTAL_FACING) {
								Direction dir = getHorizontalDirectionFromLookAngle(rotation);
								if (dir != null) {
									blockState = blockState.setValue(dirProp, dir);
								}
							}
						}
					});
				}
				if (permanent) world.setBlock(pos, blockState, flags);
				else overwriteBlockStateUnsafe(world, pos, blockState);
				return true;
			}
			return false;
		}

		public void restoreBlock() {
			restoreBlock(null);
		}

		public void restoreBlock(@Nullable Vector2f rotation) {
			restoreBlock(rotation, true);
		}

		public void restoreBlock(@Nullable Vector2f rotation, boolean permanent) {
			restoreBlockAt(oldWorld, oldPos, rotation, permanent);
		}

		public CompoundNBT storeBlockInTag(CompoundNBT nbt) {
			return storeBlockInTag(blockState, tileNBT, nbt);
		}

		public static CompoundNBT storeBlockInTag(World world, BlockPos pos, boolean removeBlock, CompoundNBT nbt) {
			BlockStorage block = new BlockStorage(world, pos, removeBlock);
			storeBlockInTag(block.blockState, block.tileNBT, nbt);
			if (removeBlock) {
				world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			}
			return nbt;
		}

		public static CompoundNBT storeBlockInTag(BlockState blockState, CompoundNBT tileNBT, CompoundNBT nbt) {
			String blockName = blockState.getBlock().getRegistryName().toString();
			nbt.putString("storedBlock", blockName);
			nbt.putInt("storedBlockState", Block.getId(blockState));
			if (tileNBT != null) {
				nbt.put("storedTileEntity", tileNBT);
			}
			return nbt;
		}

		public static boolean restoreBlockFromTag(World world, BlockPos pos, @Nullable Vector2f rotation, CompoundNBT nbt, boolean permanent, boolean clearNBT) {
			if (nbt == null) {
				return false;
			}
			Block block = getBlockFromTag(nbt);
			BlockState blockState = getBlockStateFromTag(nbt);
			if (block == null || blockState == null) {
				return false;
			}
			if (blockState.getBlock() != block) {
				blockState = block.defaultBlockState();
			}
			CompoundNBT tileNBT = null;
			if (nbt.contains("storedTileEntity")) {
				tileNBT = (CompoundNBT)nbt.get("storedTileEntity");
			}
			BlockStorage newBlock = new BlockStorage(world, pos, blockState, tileNBT);
			if (!newBlock.restoreBlockAt(world, pos, rotation, permanent)) {
				return false;
			}
			if (clearNBT) {
				nbt.remove("storedBlock");
				nbt.remove("storedBlockState");
				nbt.remove("storedTileEntity");
			}
			return true;
		}

		/**
		 * Used as a fallback in case {@link net.foxmcloud.draconicadditions.CommonMethods.BlockStorage.getBlockStateFromTag} fails.
		 * 
		 * @param nbt The Compound NBT Tag to extract the block from.
		 * @return The block stored in the NBT Tag, or null if it doesn't exist.
		 */
		public static Block getBlockFromTag(CompoundNBT nbt) {
			return nbt == null ? null : ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("storedBlock")));
		}

		/**
		 * Used to get the stored BlockState from the NBT Tag.
		 * 
		 * @param nbt The Compound NBT Tag to extract the BlockState from.
		 * @return The BlockState stored in the NBT Tag, or null if it doesn't exist.
		 */

		public static BlockState getBlockStateFromTag(CompoundNBT nbt) {
			return nbt != null && nbt.contains("storedBlockState") ? Block.stateById(nbt.getInt("storedBlockState")) : null;
		}
	}
}