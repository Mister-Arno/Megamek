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
import megamek.common.event.*;

/**
 * This listener uses the <code>GameListener</code> interface to
 * listen for game events and make the rating system respond accordingly
 * <p>
 * Listens for GameEndEvents.
 * </p>
 *
 * @see GameListener
 * @see GameEvent
 */
public class RatingListener extends GameListenerAdapter {
    private final IRatingHandler handler;

    public RatingListener(IRatingHandler handler) {
        this.handler = handler;
        MegaMek.getLogger().info("Rating Listener connected.");
    }

    public void gamePlayerAdded(GamePlayerAddedEvent e) {
        // TODO: Handle newly added / set players
        MegaMek.getLogger().info(e.getPlayer().getName() + " has been added to the game.");
        handler.addPlayer(e.getPlayer());
    }

    @Override
    public void gameVictory(GameVictoryEvent e) {
        MegaMek.getLogger().info("Victory event triggered, updating ratings...");
        handler.updateRatings();
        MegaMek.getLogger().info("Ratings updated");
    }


}
