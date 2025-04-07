package net.educanet.oneslotchest.block;

import net.educanet.oneslotchest.blockentity.OneSlotChestBlockEntity;
import net.educanet.oneslotchest.database.DatabaseManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.sounds.SoundSource;
import net.educanet.oneslotchest.registry.ModSounds;
import org.h2.tools.Server;

import javax.annotation.Nullable;


public class OneSlotChestBlock extends BaseEntityBlock {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty TEXTURE_STATE = IntegerProperty.create("texture_state", 0, 2); // Side animation
    public static final IntegerProperty OPEN_TEXTURE_STATE = IntegerProperty.create("open_texture_state", 0, 1); // Top animation
    private static Server h2Server;
    public OneSlotChestBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CHEST)
                .strength(2.5F)
                .sound(SoundType.WOOD)
                .lightLevel((state) -> state.getValue(OPEN) ? 14 : 4)
                .randomTicks()
                .requiresCorrectToolForDrops() // Ensures proper breaking particles
        );

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(OPEN, Boolean.FALSE)
                .setValue(FACING, Direction.NORTH)
                .setValue(TEXTURE_STATE, 0)
                .setValue(OPEN_TEXTURE_STATE, 0));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (!level.isClientSide && placer instanceof Player player) {
            String playerName = player.getName().getString();
            String dimension = level.dimension().location().toString();

            // Zaznamenat položení bloku do databáze
            DatabaseManager.recordChestPlacement(
                    playerName,
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    dimension
            );
        }
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OneSlotChestBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            MenuProvider menuProvider = state.getMenuProvider(level, pos);
            if (menuProvider != null) {
                player.openMenu(menuProvider);

                // Toggle open state and change top texture animation
                boolean isOpen = !state.getValue(OPEN);
                int nextTopState = isOpen ? 1 : 0; // Set texture accordingly
                level.playSound(null, pos, ModSounds.CHEST_OPEN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);


                level.setBlock(pos, state.setValue(OPEN, isOpen)
                        .setValue(OPEN_TEXTURE_STATE, nextTopState), 2);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    // Randomly updates side animation
    public void randomTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!level.isClientSide) {
            int nextState = (state.getValue(TEXTURE_STATE) + 1) % 3; // Cycle through 0 → 1 → 2 → 0
            level.setBlock(pos, state.setValue(TEXTURE_STATE, nextState), 3);
            level.playSound(null, pos, ModSounds.CHEST_IDLE.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof OneSlotChestBlockEntity) {
                ((OneSlotChestBlockEntity) blockEntity).dropAllContents(level, pos);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPEN, FACING, TEXTURE_STATE, OPEN_TEXTURE_STATE);
    }

    // Ensures breaking particles match the chest
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(this);
    }

    // Makes the chest emit light when open
    public int getLightEmission(BlockState state, LevelReader level, BlockPos pos) {
        return state.getValue(OPEN) ? 10 : 2; // Bright when open, dim when closed
    }

    public static void startH2Console() {
        try {
            h2Server = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            System.out.println("H2 Console běží na http://localhost:8082");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopH2Console() {
        if (h2Server != null) {
            h2Server.stop();
        }
    }
}
