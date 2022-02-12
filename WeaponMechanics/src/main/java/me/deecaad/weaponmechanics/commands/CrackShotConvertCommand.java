package me.deecaad.weaponmechanics.commands;

import me.deecaad.core.commands.CommandPermission;
import me.deecaad.core.commands.SubCommand;
import me.deecaad.core.file.TaskChain;
import me.deecaad.weaponmechanics.WeaponMechanics;
import me.deecaad.weaponmechanics.WeaponMechanicsAPI;
import me.deecaad.weaponmechanics.utils.CrackShotConverter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;

@CommandPermission(permission = "weaponmechanics.commands.convert")
public class CrackShotConvertCommand extends SubCommand {

    public CrackShotConvertCommand() {
        super("wm", "convert", "Converts CrackShot files to WeaponMechanics");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        WeaponMechanics plugin = WeaponMechanicsAPI.getInstance();
        File outputPath = new File(plugin.getDataFolder().getPath() + "/crackshotconvert/");
        new TaskChain(WeaponMechanics.getPlugin())
                .thenRunAsync(() -> new CrackShotConverter().convertAllFiles(outputPath))
                .thenRunSync(() -> sender.sendMessage(ChatColor.GREEN + "CrackShot configurations converted to folder WeaponMechanics/crackshotconvert/. This is still experimental."));
    }
}