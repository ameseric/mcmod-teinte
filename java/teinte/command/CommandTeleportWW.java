package teinte.command;


import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandTeleportWW extends CommandBase {
	private final List<String> aliases;


    public CommandTeleportWW() {
        aliases = new ArrayList<>();
        aliases.add( "tpw");
    }


    @Override
    @Nonnull
    public String getName() {
        return "tpw";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "tpw";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {

        if (sender instanceof EntityPlayer) {
            TeleporterWW.switchDimensions((EntityPlayer) sender);
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return Collections.emptyList();
    }

}