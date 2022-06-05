import java.nio.file.Path;
import io.github.coolcrabs.brachyura.decompiler.BrachyuraDecompiler;
import io.github.coolcrabs.brachyura.decompiler.fernflower.FernflowerDecompiler;
import io.github.coolcrabs.brachyura.fabric.FabricContext;
import io.github.coolcrabs.brachyura.fabric.FabricContext.ModDependencyCollector;
import io.github.coolcrabs.brachyura.fabric.FabricLoader;
import io.github.coolcrabs.brachyura.fabric.FabricMaven;
import io.github.coolcrabs.brachyura.fabric.Intermediary;
import io.github.coolcrabs.brachyura.fabric.SimpleFabricProject;
import io.github.coolcrabs.brachyura.fabric.Yarn;
import io.github.coolcrabs.brachyura.maven.Maven;
import io.github.coolcrabs.brachyura.maven.MavenId;
import io.github.coolcrabs.brachyura.minecraft.Minecraft;
import io.github.coolcrabs.brachyura.minecraft.VersionMeta;
import io.github.coolcrabs.brachyura.processing.ProcessorChain;
import io.github.coolcrabs.brachyura.quilt.QuiltMaven;
import net.fabricmc.mappingio.tree.MappingTree;

public class Buildscript extends SimpleFabricProject {
    @Override
    protected FabricContext createContext() {
        // Fixes Intermediary
        return new IntermediaryPatcher();
    }
    @Override
    public VersionMeta createMcVersion() {
        // Minecraft Version
        return Minecraft.getVersion(Versions.MINECRAFT_VERSION);
    }

    @Override
    public MappingTree createMappings() {
        // Uses Mojang Official Mappings
        return Yarn.ofMaven("https://maven.legacyfabric.net", FabricMaven.yarn(Versions.YARN_MAPPINGS)).tree;
    }

    @Override
    public FabricLoader getLoader() {
        // Fabric Loader Version
        return new FabricLoader(FabricMaven.URL, FabricMaven.loader(Versions.FABRIC_LOADER));
    }

    @Override
    public String getModId() {
        // Mod Name
        return "mousedelayfix";
    }

    @Override
    public String getVersion() {
        // Mod Version
        return Versions.MOD_VERSION;
    }

    @Override
    public void getModDependencies(ModDependencyCollector d) {}

    @Override
    public BrachyuraDecompiler decompiler() {
        // Uses QuiltFlower instead of CFR
        return new FernflowerDecompiler(Maven.getMavenJarDep(QuiltMaven.URL, new MavenId("org.quiltmc", "quiltflower", Versions.QUILTFLOWER)));
    };

    @Override
    public Path getBuildJarPath() {
        // Changes the jar file name
        return getBuildLibsDir().resolve(getModId() + "-" + "mc" + createMcVersion().version + "-" + getVersion() + ".jar");
    }

    @Override
    public ProcessorChain resourcesProcessingChain() {
        // Adds version to fabric.mod.json
        return new ProcessorChain(super.resourcesProcessingChain(), new FmjVersionFixer(this));
    }

    public class IntermediaryPatcher extends SimpleFabricContext {
        @Override
        protected MappingTree createIntermediary() {
            // use legacy fabric intermediary
            return Intermediary.ofMaven("https://maven.legacyfabric.net", new MavenId("net.fabricmc", "intermediary", Versions.MINECRAFT_VERSION)).tree;
        }
    }
}
