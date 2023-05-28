/*
 * MegaMek - Copyright (C) 2005 Ben Mazur (bmazur@sev.org)
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

package megamek.common.event;

/**
 * This adapter class provides default implementations for the methods described
 * by the <code>GameListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>GamedEvent</code>s can extend this
 * class and override only the methods which they are interested in.
 * </p>
 * 
 * @see GameListener
 * @see GameEvent
 */
public class GameListenerAdapter implements GameListener {

    public void gamePlayerConnected(GamePlayerConnectedEvent e) {
        // no action default
    }

    public void gamePlayerAdded(GamePlayerAddedEvent e) {
        // no action default
    }

    public void gamePlayerDisconnected(GamePlayerDisconnectedEvent e) {
        // no action default
    }

    public void gamePlayerChange(GamePlayerChangeEvent e) {
        // no action default
    }

    public void gamePlayerChat(GamePlayerChatEvent e) {
        // no action default
    }

    public void gamePhaseChange(GamePhaseChangeEvent e) {
        // no action default
    }

    public void gameTurnChange(GameTurnChangeEvent e) {
        // no action default
    }

    public void gameReport(GameReportEvent e) {
        // no action default
    }

    public void gameEnd(GameEndEvent e) {
        // no action default
    }

    public void gameBoardNew(GameBoardNewEvent e) {
        // no action default
    }

    public void gameBoardChanged(GameBoardChangeEvent e) {
        // no action default
    }

    public void gameSettingsChange(GameSettingsChangeEvent e) {
        // no action default
    }

    public void gameMapQuery(GameMapQueryEvent e) {
        // no action default
    }

    public void gameEntityNew(GameEntityNewEvent e) {
        // no action default
    }

    public void gameEntityNewOffboard(GameEntityNewOffboardEvent e) {
        // no action default
    }

    public void gameEntityRemove(GameEntityRemoveEvent e) {
        // no action default
    }

    public void gameEntityChange(GameEntityChangeEvent e) {
        // no action default
    }

    public void gameNewAction(GameNewActionEvent e) {
        // no action default
    }
    
    @Override
    public void gameClientFeedbackRequest(GameCFREvent evt) {
        // no action default
    }

    @Override
    public void gameVictory(GameVictoryEvent e) {
        // no action default
    }

}
