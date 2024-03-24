package net.realdarkstudios.commons.misc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IBaseCommand extends CommandExecutor, TabExecutor {
    @Override
    boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);

    @Nullable
    @Override
    List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);

    default List<String> filterByText(@NotNull List<String> options, @NotNull String text) {
        return options.stream().filter(s -> s.contains(text)).toList();
    }
}
