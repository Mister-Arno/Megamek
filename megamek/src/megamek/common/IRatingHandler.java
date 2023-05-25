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

import megamek.server.Server;

import java.util.Map;

/**
 * Rating Handler interface
 * provides an interface to player rating handlers
 * that are used when a dedicated server is started
 */
public interface IRatingHandler {

    /**
     * @return the server the handler is attached to
     */
    Server getServer();

    /**
     * Setter to change the server the handler is attached to if possible
     * @param s new server object
     */
    void setServer(Server s);

    /**
     * Method to get the ratings of every player
     * Player ratings and the ratings in the player objects
     * should be synced at all times. So, no need for a `getPlayerRating` method
     * the handler has the final say
     * @return Map of player identifiers (names, usernames, etc.) and their score
     */
    Map<String, Double> getRatings();

    /**
     * Called when a PlayerAdded GameEvent is observed
     * Ratings are kept in the handler class rather than the player
     * because otherwise the data is not persistent on disconnect or game end
     * @param player the player that got added to the game
     */
    void addPlayer(IPlayer player);

    /**
     * Call this function to update the ratings after a game victory
     * Will update the ratings based on a multiplayer elo calculation
     */
    void updateRatings();
}
