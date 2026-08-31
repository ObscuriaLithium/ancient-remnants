package dev.obscuria.remnants.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.remnants.AncientRemnantsHelper;
import dev.obscuria.remnants.registry.AncientRemnantsRegistries;
import lombok.experimental.UtilityClass;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;

@UtilityClass
public final class BlessingCommand {

    public static void register(
            CommandDispatcher<CommandSourceStack> dispatcher,
            CommandBuildContext context,
            Commands.CommandSelection selection
    ) {
        dispatcher.register(Commands.literal(AncientRemnants.MOD_ID)
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("blessing")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.literal("set")
                                        .then(Commands.argument("blessing", ResourceArgument.resource(context, AncientRemnantsRegistries.Keys.BLESSING))
                                                .executes(BlessingCommand::setBlessing)))
                                .then(Commands.literal("remove")
                                        .executes(BlessingCommand::removeBlessing)))));
    }

    private static int setBlessing(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        var player = EntityArgument.getPlayer(command, "player");
        var blessing = ResourceArgument.getResource(command, "blessing", AncientRemnantsRegistries.Keys.BLESSING);
        AncientRemnantsHelper.setBlessing(player, blessing);
        return 1;
    }

    private static int removeBlessing(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        var player = EntityArgument.getPlayer(command, "player");
        AncientRemnantsHelper.setBlessing(player, null);
        return 1;
    }
}
