package es.jotero.skybags.command;

import es.jotero.skybags.Skybags;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandManager {
    public static CommandSpec bagCommand;
    private Game game;
    private Skybags skybags;

    public CommandManager(Game game, Skybags skybags) {
        this.game = game;
        this.skybags = skybags;
        registerCommands();
    }

    private void registerCommands() {
        bagCommand = CommandSpec.builder()
                .description(BagCommand.getDescription())
                .executor(new BagCommand(skybags))
                .arguments(GenericArguments.optional(GenericArguments.playerOrSource(Text.of("player"))))
                .build();

        game.getCommandManager().register(skybags, bagCommand, BagCommand.getAlias());
    }
}
