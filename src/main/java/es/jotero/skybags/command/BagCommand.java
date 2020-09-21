package es.jotero.skybags.command;

import es.jotero.skybags.Skybags;
import es.jotero.skybags.permissions.BagPermissions;
import es.jotero.skybags.utils.Bag;
import es.jotero.skybags.utils.ConfigurationManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandPermissionException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.serializer.TextSerializers;

public class BagCommand implements CommandExecutor {

    private Skybags skybags;

    public BagCommand(Skybags skybags) {
        this.skybags = skybags;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            Player sourcePlayer = (Player) src;
            Player argsPlayer = args.<Player>getOne("player").orElse(null);

            if (argsPlayer != null) {
                if (sourcePlayer.hasPermission(BagPermissions.COMMAND_BAG_SPEC)) {
                    final Bag bag = new Bag(argsPlayer, sourcePlayer, getBagSize(argsPlayer), true, skybags);
                    sourcePlayer.openInventory(bag.getBag());
                    Text text = Text.of(TextColors.GRAY, "[", TextColors.AQUA, TextStyles.BOLD, "S", TextColors.WHITE,
                            TextStyles.BOLD, "C", TextColors.GRAY, TextStyles.RESET, "]  ", TextColors.GOLD,
                            "Has abierto la mochila de ", TextColors.GREEN, argsPlayer.getName());
                    sourcePlayer.sendMessage(text);
                } else throw new CommandPermissionException(Text.of("You don't have permissions to spectate a bag."));
            } else {
                if (sourcePlayer.hasPermission(BagPermissions.COMMAND_BAG)) {
                    final Bag bag = new Bag(sourcePlayer, sourcePlayer, getBagSize(sourcePlayer), true, skybags);
                    sourcePlayer.openInventory(bag.getBag());

                    Text text = Text.of(TextColors.GRAY, "[", TextColors.AQUA, TextStyles.BOLD, "S", TextColors.WHITE,
                            TextStyles.BOLD, "C", TextColors.GRAY, TextStyles.RESET, "]  ", TextColors.GOLD,
                            "Has abierto tu mochila!");
                    sourcePlayer.sendMessage(text);
                } else throw new CommandPermissionException(Text.of("You don't have a bag."));
            }
        }
        return CommandResult.success();
    }

    public int getBagSize(Player player) {
        if (player.hasPermission(BagPermissions.COMMAND_BAG_SIZE_SIX))
            return 6;
        else if (player.hasPermission(BagPermissions.COMMAND_BAG_SIZE_FIVE))
            return 5;
        else if (player.hasPermission(BagPermissions.COMMAND_BAG_SIZE_FOUR))
            return 4;
        else if (player.hasPermission(BagPermissions.COMMAND_BAG_SIZE_THREE))
            return 3;
        else if (player.hasPermission(BagPermissions.COMMAND_BAG_SIZE_TWO))
            return 2;
        else if (player.hasPermission(BagPermissions.COMMAND_BAG_SIZE_ONE))
            return 1;
        return 1;
    }

    public static Text getDescription() {
        return Text.of("/bag, /mochila");
    }

    public static String[] getAlias() {
        return new String[]{"bag", "mochila"};
    }

}
