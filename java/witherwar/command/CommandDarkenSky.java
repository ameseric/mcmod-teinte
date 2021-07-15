package witherwar.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.IClientCommand;

public class CommandDarkenSky extends CommandBase implements IClientCommand{
	
	private final List<String> aliases;
	
	public CommandDarkenSky() {
		this.aliases = new ArrayList<>();
		this.aliases.add( "darkensky");
	}

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getName() {
		return "darkensky";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "darkensky <integer 1-100>";
	}

	@Override
	public List<String> getAliases() {
		return this.aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        System.out.println( "--------------> Executing");
        
        if (args.length == 1) {
        	System.out.println( "Command arg length correct...");
        	int i = parseInt( args[1] ,0 ,10);
        	System.out.print( i);
        	//WorldProviderPillar pillar = server.worlds[3].provider;
        }else {
        	System.out.println( "Nope");
        }
        
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
		// TODO Auto-generated method stub
		return false;
	}

}
