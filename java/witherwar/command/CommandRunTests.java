package witherwar.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import witherwar.TEinTE;
import witherwar.test.TestMain;

public class CommandRunTests extends CommandBase{
	private final List<String> aliases;
	private final String NAME = "runtests";
	
	
    public CommandRunTests() {
        this.aliases = new ArrayList<>();
        this.aliases.add( NAME);
    }

	
	
	@Override
	public String getName() {
		return NAME;
	}
	
	
	

	@Override
	public String getUsage(ICommandSender sender) {
		return NAME;
	}

	
	
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		TestMain tester = new TestMain();
		tester.run();
	}

	
	
	
}
