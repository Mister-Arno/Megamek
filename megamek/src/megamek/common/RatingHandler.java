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

import megamek.common.event.GamePlayerChangeEvent;
import megamek.server.Server;
import megamek.server.victory.VictoryResult;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Some calculations are inspired by the write-up below
 * https://towardsdatascience.com/developing-a-generalized-elo-rating-system-for-multiplayer-games-b9b495e87802
 */
public class RatingHandler implements IRatingHandler {
    private Server server;
    private final Map<String, RatingInfoStruct> ratings;

    private static final int DEFAULT_RATING = 1500;
    private static final int DEFAULT_D_VALUE = 400;

    /**
     * A small data class to hold information about a player's rating
     * Designed with adaptability in mind, other metrics can easily be added
     * to support another rating methods such as TrueSkill
     */
    private static class RatingInfoStruct {
        private int wins = 0;
        private int losses = 0;
        private int currentRating = DEFAULT_RATING;

        public int getWins() {
            return wins;
        }

        public void setWins(int wins) {
            this.wins = wins;
        }

        public int getLosses() {
            return losses;
        }

        public void setLosses(int losses) {
            this.losses = losses;
        }

        public int getCurrentRating() {
            return currentRating;
        }

        public void setCurrentRating(int currentRating) {
            this.currentRating = currentRating;
        }
    }

    public RatingHandler(Server server) {
        this.server = server;
        this.ratings = new HashMap<>();
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public void setServer(Server s) {
        if (server == null) {
            server = s;
        }
    }

    @Override
    public Map<String, Double> getRatings() {
        Map<String, Double> result = new HashMap<>(ratings.size());
        for (Map.Entry<String, RatingInfoStruct> entry : ratings.entrySet()) {
            result.put(entry.getKey(), (double) entry.getValue().getCurrentRating());
        }
        return result;
    }

    @Override
    public void addPlayer(IPlayer player) {
        if (!ratings.containsKey(player.getName())) {
            ratings.put(player.getName(), new RatingInfoStruct());
            player.setEloRating(DEFAULT_RATING);
        } else {
            player.setEloRating(ratings.get(player.getName()).getCurrentRating());
        }
    }

    /**
     * Call this function to update the ratings after a game victory
     * Will update the ratings based on a multiplayer elo calculation
     */
    @Override
    public void updateRatings() {
        IGame game = server.getGame();
        int activePlayersNbr = getActivePlayersNbr();

        // Get the victory result to easily check winners
        VictoryResult vr = game.getVictory().checkForVictory(game);
        int tiedPlayersNbr = getTiedPlayersNbr(vr); // Could also mean winners on the same team

        if (tiedPlayersNbr == 0) {
            return;
        }

        for (Enumeration<IPlayer> p = game.getPlayers(); p.hasMoreElements();) {
            IPlayer player = p.nextElement();

            // Not interested in observers
            if (player.isObserver()) {
                continue;
            }

            // If the player isn't in our map yet somehow, we add it with a reset elo
            if (!ratings.containsKey(player.getName())) {
                addPlayer(player);
            }

            // Winners get an adjusted elo factor
            if (vr.isWinningPlayer(player.getId()) || vr.isWinningTeam(player.getTeam())) {
                updatePlayerRating(player, 1.0 / tiedPlayersNbr, activePlayersNbr);
            } else {
                updatePlayerRating(player, 0.0, activePlayersNbr);
            }

        }
    }

    private void updatePlayerRating(IPlayer player, double factor, int activePlayersNbr) {
        RatingInfoStruct ratingInfo = ratings.get(player.getName());

        if (factor > 0.0) {
            ratingInfo.setWins(ratingInfo.getWins() + 1);
        } else {
            ratingInfo.setLosses(ratingInfo.getLosses() + 1);
        }

        // Calculate elo update
        // Standard elo handles ties by factoring each player with a 0.5
        // However, in a multiplayer setting you have to multiply by number of players - 1 (is exactly 1 in chess)
        // Then we calculate the factor as 1/#winners since you can't really end as second or third place
        // So we just see it all as an N-way draw even if the winners are on the same team
        int k = findK(ratingInfo.getCurrentRating());
        double fDelta = k * (activePlayersNbr - 1) * (factor - calculateExpectedPlayerScore(player));
        int eloChange = (int) Math.round(fDelta);

        ratingInfo.setCurrentRating(Math.max(ratingInfo.getCurrentRating() + eloChange, 0));
        player.setEloRating(ratingInfo.getCurrentRating());

        server.getGame().processGameEvent(new GamePlayerChangeEvent(this, player));
    }

    /**
     * Calculates expected score for the player
     * @param player player for which the expected score is calculated
     * @return expected score
     */
    private double calculateExpectedPlayerScore(IPlayer player) {
        IGame game = server.getGame();
        RatingInfoStruct playerRating = ratings.get(player.getName());

        double expectedNumerator = 0.0;
        int n = 1; // Account for current player

        // Calculated as:
        // E_A = \sum_{i, i != A} (1.0) / (1.0 + 10^((R_i - R_A) / D) )
        // E_A = 2 * E_A / N(N-1)
        for (Enumeration<IPlayer> p = game.getPlayers(); p.hasMoreElements();) {
            IPlayer opponent = p.nextElement();
            if ((opponent.getId() == player.getId()) || (opponent.isObserver())) {
                continue;
            }

            RatingInfoStruct opponentRating = ratings.get(opponent.getName());
            // The power term (R_i - R_A) / D)
            double pow = (double) (opponentRating.getCurrentRating() - playerRating.getCurrentRating()) / DEFAULT_D_VALUE;
            double denominator = 1.0 + Math.pow(10.0, pow);
            expectedNumerator += (1.0 / denominator);
            n += 1;
        }
        double expectedDenominator = ((double) (n * (n - 1)) / 2);
        return expectedNumerator / expectedDenominator;
    }

    /**
     * Returns the number of active players in the game (non-observers)
     * @return number of active players
     */
    private int getActivePlayersNbr() {
        IGame game = getServer().getGame();
        int n = 0;
        for (Enumeration<IPlayer> p = game.getPlayers(); p.hasMoreElements();) {
            IPlayer player = p.nextElement();

            if (player.isObserver()) {
                continue;
            }

            n += 1;
        }
        return n;
    }

    /**
     * Returns the number of tied players for elo calculations, players on the winning team are also considered
     * tied compared to each other.
     * @param vr Victory result object to test if a player won
     * @return number of tied players
     */
    private int getTiedPlayersNbr(VictoryResult vr) {
        IGame game = getServer().getGame();
        int tiedPlayers = 0;
        for (Enumeration<IPlayer> p = game.getPlayers(); p.hasMoreElements();) {
            IPlayer player = p.nextElement();

            if (player.isObserver()) {
                continue;
            }

            if (vr.isWinningPlayer(player.getId()) || vr.isWinningTeam(player.getTeam())) {
                tiedPlayers += 1;
            }
        }
        return tiedPlayers;
    }

    /**
     * Determines the optimal K-factor, uses the former USCF K-factor
     * @param elo the player's elo to find the optimal K-factor for
     * @return integer either 32, 24, or 16 depending on the player's elo
     */
    private int findK(int elo) {
        if (elo < 2100) {
            return 32;
        } else if (elo < 2400) {
            return 24;
        } else {
            return 16;
        }
    }
}
