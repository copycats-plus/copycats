package com.copycatsplus.copycats.multiloader;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.util.thread.BlockableEventLoop;

import java.util.function.Function;
import java.util.function.Supplier;

public class LogicalSidedProvider<T> {
    public static final LogicalSidedProvider<BlockableEventLoop<? super TickTask>> WORKQUEUE = new LogicalSidedProvider<>(Supplier::get, Supplier::get);

    private static Supplier<Minecraft> client;
    private static Supplier<MinecraftServer> server;

    public static void setClient(Supplier<Minecraft> client) {
        LogicalSidedProvider.client = client;
    }

    public static void setServer(Supplier<MinecraftServer> server) {
        LogicalSidedProvider.server = server;
    }

    private LogicalSidedProvider(Function<Supplier<Minecraft>, T> clientSide, Function<Supplier<MinecraftServer>, T> serverSide) {
        this.clientSide = clientSide;
        this.serverSide = serverSide;
    }

    private final Function<Supplier<Minecraft>, T> clientSide;
    private final Function<Supplier<MinecraftServer>, T> serverSide;

    public T get(final Platform.Environment side) {
        return side == Platform.Environment.CLIENT ? clientSide.apply(client) : serverSide.apply(server);
    }
}
