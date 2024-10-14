package org.allaymc.terra.allay.generator;

import org.allaymc.api.utils.AllayStringUtils;
import org.allaymc.api.world.biome.BiomeType;
import org.allaymc.api.world.generator.WorldGenerator;
import org.allaymc.api.world.generator.context.NoiseContext;
import org.allaymc.api.world.generator.context.PopulateContext;
import org.allaymc.api.world.generator.function.Noiser;
import org.allaymc.api.world.generator.function.Populator;
import org.allaymc.terra.allay.TerraAllayPlugin;
import org.allaymc.terra.allay.delegate.AllayProtoChunk;
import org.allaymc.terra.allay.delegate.AllayProtoWorld;
import org.allaymc.terra.allay.delegate.AllayServerWorld;

import java.util.Locale;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.util.GeneratorWrapper;
import com.dfsek.terra.api.world.info.WorldProperties;


/**
 * @author daoge_cmd
 */
public class AllayGeneratorWrapper implements GeneratorWrapper {

    protected static final String DEFAULT_PACK_NAME = "overworld";
    protected static final String OPTION_PACK_NAME = "pack";
    protected static final String OPTION_SEED = "seed";

    protected final BiomeProvider biomeProvider;
    protected final ConfigPack configPack;
    protected final ChunkGenerator chunkGenerator;
    protected final long seed;
    protected final WorldGenerator allayWorldGenerator;
    protected WorldProperties worldProperties;
    protected AllayServerWorld allayServerWorld;

    public AllayGeneratorWrapper(String preset) {
        var options = AllayStringUtils.parseOptions(preset);
        var packName = options.getOrDefault(OPTION_PACK_NAME, DEFAULT_PACK_NAME);
        this.seed = Long.parseLong(options.getOrDefault(OPTION_SEED, "0"));
        this.configPack = createConfigPack(packName);
        this.chunkGenerator = createGenerator(this.configPack);
        this.biomeProvider = this.configPack.getBiomeProvider();
        this.allayWorldGenerator = WorldGenerator
            .builder()
            .name("TERRA")
            .preset(preset)
            .noisers(new AllayNoiser())
            .populators(new AllayPopulator())
            .onDimensionSet(dimension -> {
                this.allayServerWorld = new AllayServerWorld(this, dimension);
                this.worldProperties = new WorldProperties() {

                    private final Object fakeHandle = new Object();

                    @Override
                    public long getSeed() {
                        return seed;
                    }

                    @Override
                    public int getMaxHeight() {
                        return dimension.getDimensionInfo().maxHeight();
                    }

                    @Override
                    public int getMinHeight() {
                        return dimension.getDimensionInfo().minHeight();
                    }

                    @Override
                    public Object getHandle() {
                        return fakeHandle;
                    }
                };
            })
            .build();
    }

    public class AllayNoiser implements Noiser {

        @Override
        public boolean apply(NoiseContext context) {
            var chunk = context.getCurrentChunk();
            var chunkX = chunk.getX();
            var chunkZ = chunk.getZ();
            chunkGenerator.generateChunkData(
                new AllayProtoChunk(chunk),
                worldProperties, biomeProvider,
                chunkX, chunkZ
            );
            var minHeight = context.getDimensionInfo().minHeight();
            var maxHeight = context.getDimensionInfo().maxHeight();
            for(int x = 0; x < 16; x++) {
                for(int y = minHeight; y < maxHeight; y++) {
                    for(int z = 0; z < 16; z++) {
                        chunk.setBiome(
                            x, y, z,
                            (BiomeType) biomeProvider.getBiome(chunkX * 16 + x, y, chunkZ * 16 + z, seed).getPlatformBiome().getHandle()
                        );
                    }
                }
            }
            return true;
        }

        @Override
        public String getName() {
            return "TERRA_NOISER";
        }
    }


    public class AllayPopulator implements Populator {

        @Override
        public boolean apply(PopulateContext context) {
            var tmp = new AllayProtoWorld(allayServerWorld, context);
            try {
                for(var generationStage : configPack.getStages()) {
                    generationStage.populate(tmp);
                }
            } catch(Exception e) {
                TerraAllayPlugin.INSTANCE.getPluginLogger().error("Error while populating chunk", e);
            }
            return true;
        }

        @Override
        public String getName() {
            return "TERRA_POPULATOR";
        }
    }

    protected static ConfigPack createConfigPack(String packName) {
        var byId = TerraAllayPlugin.PLATFORM.getConfigRegistry().getByID(packName);
        return byId.orElseGet(
            () -> TerraAllayPlugin.PLATFORM.getConfigRegistry().getByID(packName.toUpperCase(Locale.ENGLISH))
                .orElseThrow(() -> new IllegalArgumentException("Cant find terra config pack named " + packName))
        );
    }

    protected static ChunkGenerator createGenerator(ConfigPack configPack) {
        return configPack.getGeneratorProvider().newInstance(configPack);
    }

    @Override
    public ChunkGenerator getHandle() {
        return chunkGenerator;
    }

    public BiomeProvider getBiomeProvider() {
        return this.biomeProvider;
    }

    public ConfigPack getConfigPack() {
        return this.configPack;
    }

    public long getSeed() {
        return this.seed;
    }

    public WorldGenerator getAllayWorldGenerator() {
        return this.allayWorldGenerator;
    }
}
