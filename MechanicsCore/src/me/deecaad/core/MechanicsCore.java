package me.deecaad.core;

import me.deecaad.core.events.triggers.EquipListener;
import me.deecaad.core.file.JarInstancer;
import me.deecaad.core.file.Serializer;
import me.deecaad.core.packetlistener.PacketHandlerListener;
import me.deecaad.core.placeholder.PlaceholderAPI;
import me.deecaad.core.utils.Debugger;
import me.deecaad.core.utils.FileUtil;
import me.deecaad.core.utils.LogLevel;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public class MechanicsCore extends JavaPlugin {

    private static MechanicsCore plugin;
    private static List<Serializer<?>> serializersList;

    // public so people can import a static variable
    public static Debugger debug;

    @Override
    public void onLoad() {
        int level = getConfig().getInt("Debug_Level");
        boolean printTraces = getConfig().getBoolean("Print_Traces");
        debug = new Debugger(getLogger(), level, printTraces);
        plugin = this;
    }

    @Override
    public void onEnable() {
        debug.debug("Loading config.yml");
        FileUtil.copyResourcesTo(getClass(), getClassLoader(), "resources/MechanicsMain", getDataFolder());
        FileUtil.ensureDefaults(getClassLoader(), "resources/MechanicsMain/config.yml", new File(getDataFolder(), "config.yml"));

        try {
            List<?> serializers = new JarInstancer(new JarFile(getFile())).createAllInstances(Serializer.class, true);
            //noinspection unchecked
            serializersList = (List<Serializer<?>>) serializers;
            new BukkitRunnable() {
                @Override
                public void run() {
                    serializersList = null;
                    debug.debug("Cleared serializers list");
                }
            }.runTaskLater(this, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PacketHandlerListener packetListener = new PacketHandlerListener(this, debug);
        packetListener.addPacketHandler(EquipListener.SINGLETON, true);

        Bukkit.getPluginManager().registerEvents(EquipListener.SINGLETON, this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        getServer().getScheduler().cancelTasks(this);
        PlaceholderAPI.onDisable();
        plugin = null;
        serializersList = null;
        debug = null;
    }

    /**
     * @return the MechanicsCore plugin instance
     */
    public static MechanicsCore getPlugin() {
        return plugin;
    }

    /**
     * @return the list of all serializers added to core
     */
    public static List<Serializer<?>> getListOfSerializers(Plugin plugin) {
        if (serializersList == null) {
            debug.log(LogLevel.WARN, plugin.getName() + " tried to get serializer list after startup...");
            return null;
        }

        // Return copy of list
        return new ArrayList<>(serializersList);
    }

    /**
     * Add serializer for MechanicsCore serializer list
     *
     * @param serializer the serializer
     */
    public static void addSerializer(Plugin plugin, Serializer<?> serializer) {
        if (serializersList == null) {
            debug.log(LogLevel.WARN, plugin.getName() + " tried to add serializer after startup...");
            return;
        }
        serializersList.add(serializer);
    }

    /**
     * Add list of serializers for MechanicsCore serializer list
     *
     * @param serializers the list of serializers
     */
    public static void addSerializers(Plugin plugin, List<Serializer<?>> serializers) {
        if (serializers != null && serializers.size() > 0) {
            for (Serializer<?> serializer : serializers) {
                addSerializer(plugin, serializer);
            }
        }
    }
}
