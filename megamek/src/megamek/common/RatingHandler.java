/*
 * Copyright (c) 2023 - The MegaMek Team. All Rights Reserved.
 *
 * This file is part of MegaMek.
 *
 * MegaMek is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MegaMek is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MegaMek. If not, see <http://www.gnu.org/licenses/>.
 */

package megamek.common;

import megamek.MegaMek;
import megamek.common.event.GamePlayerChangeEvent;
import megamek.server.Server;
import megamek.server.victory.VictoryResult;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class RatingHandler {
    private Server server;
    private Map<String, RatingInfoStruct> ratings;

    private static final int DEFAULT_RATING = 0;

    private static final int LOSS_PENALTY = 50;
    private static final int WIN_GAIN = 100;

    private static class RatingInfoStruct {
        public int wins = 0;
        public int losses = 0;
        public int currentRating = DEFAULT_RATING;

    }

    public RatingHandler(Server server) {
        this.server = server;
        this.ratings = new HashMap<>();
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server s) {
        if (server == null) {
            server = s;
        }
    }

    public void addPlayer(IPlayer player) {
        if (!ratings.containsKey(player.getName())) {
            ratings.put(player.getName(), new RatingInfoStruct());
            player.setEloRating(DEFAULT_RATING);
        } else {
            player.setEloRating(ratings.get(player.getName()).currentRating);
        }
    }

    private void updatePlayerRating(IPlayer player, boolean playerWon) {
        RatingInfoStruct ratingInfo = ratings.get(player.getName());
        if (playerWon) {
            ratingInfo.currentRating += WIN_GAIN;
            ratingInfo.wins += 1;
        } else {
            ratingInfo.currentRating = Math.max(ratingInfo.currentRating - LOSS_PENALTY, 0);
            ratingInfo.losses += 1;
        }
        player.setEloRating(ratingInfo.currentRating);
        server.getGame().processGameEvent(new GamePlayerChangeEvent(this, player));
    }

    /**
     * Function is called when the game ends
     */
    public void updateRatings() {
        IGame game = server.getGame();
        int winnerTeam = game.getVictoryTeam();
        int winnerPlayer = game.getVictoryPlayerId();
        VictoryResult vr = game.getVictory().checkForVictory(game, game.getVictoryContext());

        for (Enumeration<IPlayer> p = game.getPlayers(); p.hasMoreElements();) {
            IPlayer player = p.nextElement();

            // Not interested in observers
            if (player.isObserver()) {
                continue;
            }

            if (!ratings.containsKey(player.getName())) {
                addPlayer(player);
            }

            if ((winnerTeam == IPlayer.TEAM_NONE) || (winnerTeam == IPlayer.TEAM_UNASSIGNED)) {
                updatePlayerRating(player, winnerPlayer == player.getId());
            } else {
                updatePlayerRating(player, winnerTeam == player.getTeam());
            }

        }
    }

}
