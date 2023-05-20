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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import megamek.common.IPlayer;
import megamek.common.Report;

/**
 * quick implementation of a Victory.Result stores player scores and a flag if
 * game-ending victory is achieved or not
 */
public class VictoryResult implements IResult {
    protected boolean victory;
    protected Throwable tr;
    protected ArrayList<Report> reports = new ArrayList<>();
    protected HashMap<Integer, Double> playerScore = new HashMap<>();
    protected HashMap<Integer, Double> teamScore = new HashMap<>();
    protected double hiScore = 0;

    /**
     * Constructor for an object indicating a win or loss.
     * Initially, no scores are recorded
     * @param win: boolean indicating if the game is won or lost
     */
    protected VictoryResult(boolean win) {
        // Normal constructor
        this.victory = win;
        tr = new Throwable();
    }

    /**
     * Constructor for a single player/team victory indicating a win or loss
     * Initially they get a score of 1.0
     * @param win: boolean true if the game is won, false if lost
     * @param player: player ID of the winning player (or PLAYER_NONE)
     * @param team: team ID of the winning team (or TEAM_NONE)
     */
    protected VictoryResult(boolean win, int player, int team) {
    	this.victory = win;
    	tr = new Throwable();
        if (player != IPlayer.PLAYER_NONE) {
            addPlayerScore(player, 1.0);
        }
        if (team != IPlayer.TEAM_NONE) {
            addTeamScore(team, 1.0);
        }
    }

    /**
     * Constructor for a single player/team victory indicating no result (not a tie)
     * @return VictoryResult object indicating no result (not a tie)
     */
    protected static VictoryResult noResult() {
    	return new VictoryResult(false, IPlayer.PLAYER_NONE, IPlayer.TEAM_NONE);
    }

    /**
     * Constructor for a single player/team victory indicating a tie
     * @return VictoryResult object indicating a tie
     */
    protected static VictoryResult drawResult() {
        return new VictoryResult(true, IPlayer.PLAYER_NONE, IPlayer.TEAM_NONE);
    }

    /**
     * Retrieve the winning player ID (the player with the highest score)
     * @return int player ID of the winning player (or PLAYER_NONE if tie or when there is no player)
     */
    public int getWinningPlayer() {
        double max = Double.MIN_VALUE;
        int maxPlayer = IPlayer.PLAYER_NONE;
        boolean draw = false;

        for(Map.Entry<Integer, Double> entry : playerScore.entrySet()) {
            if (entry.getValue() == max) {
                draw = true;
            }
            if (entry.getValue() > max) {
                draw = false;
                max = entry.getValue();
                maxPlayer = entry.getKey();
            }
        }
        if (draw)
            return IPlayer.PLAYER_NONE;
        return maxPlayer;
    }

    /**
     * Retrieve the winning team ID (the team with the highest score)
     * @return int team ID of the winning team (or TEAM_NONE if tie or when there is no team)
     */
    public int getWinningTeam() {
        double max = Double.MIN_VALUE;
        int maxTeam = IPlayer.TEAM_NONE;
        boolean draw = false;

        //improved for loop
        for(Map.Entry<Integer, Double> entry : teamScore.entrySet()) {
            if (entry.getValue() == max) {
                draw = true;
            }
            if (entry.getValue() > max) {
                draw = false;
                max = entry.getValue();
                maxTeam = entry.getKey();
            }
        }

        if (draw)
            return IPlayer.TEAM_NONE;
        return maxTeam;
    }


    protected void updateHiScore() {
        hiScore = calculateMaxScore(playerScore.values());
        hiScore = Math.max(hiScore, calculateMaxScore(teamScore.values()));
    }

    private double calculateMaxScore(Collection<Double> scores) {
        return scores.stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(Double.MIN_VALUE);
    }

    /**
     * Adds the score for a player
     * @param id: player ID
     * @param score: score to add (it replaces the old score if any)
     */
    public void addPlayerScore(int id, double score) {
        playerScore.put(id, score);
        updateHiScore();
    }

    /**
     * Adds the score for a team
     * @param id: team ID
     * @param score: score to add (it replaces the old score if any)
     */
    public void addTeamScore(int id, double score) {
        teamScore.put(id, score);
        updateHiScore();
    }

    /**
     * Checks if a player is the winning player (has the highest score)
     * @param id: player ID
     * @return: true if the player is the winning player, false otherwise
     */
    public boolean isWinningPlayer(int id) {
        return Math.abs(getPlayerScore(id) - hiScore) < 0.01;
    }

    /**
     * Checks if a team is the winning team (has the highest score)
     * @param id: team ID
     * @return true if the team is the winning team, false otherwise
     */
    public boolean isWinningTeam(int id) {
        return Math.abs(getTeamScore(id) - hiScore) < 0.01;
    }

    /**
     * Checks if the game is won or lost
     * @return true if the game is won, false otherwise
     */
    public boolean victory() {
        return victory;
    }

    /**
     * Sets the game to won or lost
     * @param b: true if the game is won, false otherwise
     */
    public void setVictory(boolean b) {
        this.victory = b;
    }

    /**
     * Retrieves the score of a player
     * @param id: player ID
     * @return score of the player
     */
    public double getPlayerScore(int id) {
        if (playerScore.get(id) == null)
            return 0.0;
        return playerScore.get(id);
    }

    /**
     * Retrieves the ID of all players
     * @return ArrayList of player IDs
     */
    public int[] getPlayers() {
        return intify(playerScore.keySet().toArray(new Integer[0]));
    }

    /**
     * Retrieves the score of a team
     * @param id: team ID
     * @return: score of the team
     */
    public double getTeamScore(int id) {
        if (teamScore.get(id) == null)
            return 0.0;
        return teamScore.get(id);
    }

    /**
     * Retrieves the ID of all teams
     * @return ArrayList of team IDs
     */
    public int[] getTeams() {
        return intify(teamScore.keySet().toArray(new Integer[0]));
    }

    /**
     * Adds a report to the victory result
     * @param r: report to add
     */
    public void addReport(Report r) {
        reports.add(r);
    }

    /**
     * Retrieves all reports of the victory result
     * @return ArrayList of reports
     */
    public ArrayList<Report> getReports() {
        return reports;
    }

    protected String getTrace() {
        StringWriter sw = new StringWriter();
        PrintWriter pr = new PrintWriter(sw);
        tr.printStackTrace(pr);
        pr.flush();
        return sw.toString();
    }

    private int[] intify(Integer[] ar) {
        int[] ret = new int[ar.length];
        for (int i = 0; i < ar.length; i++)
            ret[i] = ar[i];
        return ret;
    }

    @Override
    public String toString() {
        return "victory provided to you by:" + getTrace();
    }

    /**
     * Checks if the victory result is a draw
     * @return true if the victory result is a draw, false otherwise
     */
    public boolean isDraw() {
        return (getWinningPlayer() == IPlayer.PLAYER_NONE && getWinningTeam() == IPlayer.TEAM_NONE);
    }
}