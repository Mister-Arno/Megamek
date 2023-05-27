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
import java.util.ArrayList;
import java.util.List;

import megamek.common.IGame;
import megamek.common.Report;
import megamek.common.options.GameOptions;
import megamek.common.options.OptionsConstants;

public class Victory implements Serializable {
    private static final long serialVersionUID = -8633873540471130320L;
    
    private boolean checkForVictory;
    private int neededVictoryConditions;

    private IVictoryConditions force = new ForceVictory();
    private IVictoryConditions lastMan = new LastManStandingVictory();
    private IVictoryConditions[] VCs = null;

    public Victory(GameOptions options) {
        checkForVictory = options.booleanOption(OptionsConstants.VICTORY_CHECK_VICTORY);

        if (checkForVictory) {
            VCs = buildVClist(options);
        }
    }

    private IVictoryConditions[] buildVClist(GameOptions options) {
        neededVictoryConditions = options.intOption(OptionsConstants.VICTORY_ACHIEVE_CONDITIONS);
        List<IVictoryConditions> victories = new ArrayList<IVictoryConditions>();
        // BV related victory conditions
        if (options.booleanOption(OptionsConstants.VICTORY_USE_BV_DESTROYED)) {
            victories.add(new BVDestroyedVictory(options.intOption(OptionsConstants.VICTORY_BV_DESTROYED_PERCENT)));
        }
        if (options.booleanOption(OptionsConstants.VICTORY_USE_BV_RATIO)) {
            victories.add(new BVRatioVictory(options.intOption(OptionsConstants.VICTORY_BV_RATIO_PERCENT)));
        }

        // Kill count victory condition
        if (options.booleanOption(OptionsConstants.VICTORY_USE_KILL_COUNT)) {
            victories.add(new KillCountVictory(options.intOption(OptionsConstants.VICTORY_GAME_KILL_COUNT)));
        }

        // Commander killed victory condition
        if (options.booleanOption(OptionsConstants.VICTORY_COMMANDER_KILLED)) {
            victories.add(new EnemyCmdrDestroyedVictory());
        }
        return victories.toArray(new IVictoryConditions[0]);
    }

    public VictoryResult checkForVictory(IGame currentGame) {
        VictoryResult victoryResult;

        // Check for forced victory condition
        victoryResult = force.victory(currentGame);
        if (victoryResult.victory()) {
            return victoryResult;
        }

        // Check for optional victory conditions
        if (checkForVictory) {
            if (VCs == null) {
                VCs = buildVClist(currentGame.getOptions());
            }
            victoryResult = checkOptionalVictory(currentGame);
            if (victoryResult.victory()) {
                return victoryResult;
            }
        } else {
            return victoryResult;
        }

        // Check for last man standing victory condition
        VictoryResult lastManResult = lastMan.victory(currentGame);
        if (!victoryResult.victory() && lastManResult.victory()) {
            return lastManResult;
        } else {
            return victoryResult;
        }
    }


    private VictoryResult checkOptionalVictory(IGame game) {
        VictoryResult vr = new VictoryResult(true);

        combineScores(vr, game);
        double highScore = calculateHighScoresAndUpdate(vr);

        boolean victory = vr.victory() && highScore >= neededVictoryConditions;
        vr.setVictory(victory);

        if (vr.victory()) {
            return vr;
        }

        if (game.gameTimerIsExpired()) {
            return VictoryResult.drawResult();
        }

        return vr;
    }

    private void combineScores(VictoryResult vr, IGame game) {
        for (IVictoryConditions v : VCs) {
            VictoryResult res = v.victory(game);
            for (Report r : res.getReports()) {
                vr.addReport(r);
            }
            if (res.victory()) {
                vr.setVictory(true);
            }
            for (int pl : res.getPlayers()) {
                vr.addPlayerScore(pl, vr.getPlayerScore(pl) + res.getPlayerScore(pl));
            }
            for (int t : res.getTeams()) {
                vr.addTeamScore(t, vr.getTeamScore(t) + res.getTeamScore(t));
            }
        }
    }

    private double calculateHighScoresAndUpdate(VictoryResult vr) {
        double highScore = 0.0;
        for (int pl : vr.getPlayers()) {
            double sc = vr.getPlayerScore(pl);
            vr.addPlayerScore(pl, sc / VCs.length);
            highScore = Math.max(highScore, sc);
        }
        for (int t : vr.getTeams()) {
            double sc = vr.getTeamScore(t);
            vr.addTeamScore(t, sc / VCs.length);
            highScore = Math.max(highScore, sc);
        }
        return highScore;
    }

}