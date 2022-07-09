package de.varoplugin.varo.game;

import de.varoplugin.varo.VaroPlugin;
import de.varoplugin.varo.data.VaroDataGame;
import de.varoplugin.varo.game.entity.player.VaroPlayerContainer;
import de.varoplugin.varo.game.entity.team.VaroTeamContainer;
import de.varoplugin.varo.game.world.ItemChestContainer;

public interface Varo extends VaroPlayerContainer, VaroTeamContainer, AutoStartable, ItemChestContainer, VaroDataGame {

    void initialize(VaroPlugin plugin);

    VaroState getState();

    boolean setState(VaroState state);

    VaroPlugin getPlugin();

}