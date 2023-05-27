/*
 * MegaMek - Copyright (C) 2007-2008 Ben Mazur (bmazur@sev.org)
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 */
package megamek.server.victory;

import java.io.Serializable;
import java.util.List;

import megamek.common.IGame;
import megamek.common.IPlayer;

/**
 * implementation of player-agreed victory
 */
public class ForceVictory implements IVictoryConditions, Serializable {

    private static final long serialVersionUID = 1782762191476942976L;

    public ForceVictory() {}


    public VictoryResult victory(IGame game) {
        if (!game.isForceVictory()) {
            return VictoryResult.noResult();
        }
        int victoryPlayerId = game.getVictoryPlayerId();
        int victoryTeam = game.getVictoryTeam();
        List<IPlayer> players = game.getPlayersVector();

        if (victoryPlayerId != IPlayer.PLAYER_NONE && checkIndividualVictory(players, victoryPlayerId)) {
            return new VictoryResult(true, victoryPlayerId, victoryTeam);
        }

        if (victoryTeam != IPlayer.TEAM_NONE && checkTeamVictory(players, victoryTeam)) {
            return new VictoryResult(true, victoryPlayerId, victoryTeam);
        }

        return VictoryResult.noResult();
    }

    private boolean checkIndividualVictory(List<IPlayer> players, int victoryPlayerId) {
        for (IPlayer player : players) {
            if (player.getId() != victoryPlayerId && !player.isObserver()) {
                if (!player.admitsDefeat()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkTeamVictory(List<IPlayer> players, int victoryTeam) {
        for (IPlayer player : players) {
            if (player.getTeam() != victoryTeam && !player.isObserver()) {
                if (!player.admitsDefeat()) {
                    return false;
                }
            }
        }
        return true;
    }
}