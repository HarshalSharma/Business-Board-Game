/*
 * Copyright (c) 2020 Harshal Sharma
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.harshalworks.businessbg;

import com.harshalworks.businessbg.bank.Bank;
import com.harshalworks.businessbg.board.Board;
import com.harshalworks.businessbg.dice.Dice;
import com.harshalworks.businessbg.events.StandardGameEventPublisher;
import com.harshalworks.businessbg.player.BoardGamePlayer;
import com.harshalworks.businessbg.player.BoardGamePlayerFactory;

import java.util.*;

public class FixedRoundsGame extends PublishableGame{

    protected final int roundsToPlay;
    private final int lengthToTravel;
    private Map<String, Integer> playerLeftToTravelRounds;

    public FixedRoundsGame(int fixedAmountForPlayer, int initialAmountOfBank,
                           Dice dice, Board board, int roundsToPlay) {
        super(fixedAmountForPlayer, new BoardGamePlayerFactory(),
                new Bank(initialAmountOfBank), dice, board, new StandardGameEventPublisher());
        this.roundsToPlay = roundsToPlay;
        this.lengthToTravel = board.getBoardLength() * roundsToPlay;
        playerLeftToTravelRounds = new HashMap<>();
    }

    @Override
    public void start() {
        super.start();
        initializeAllPlayersWithDistanceToTravel();
    }

    private void initializeAllPlayersWithDistanceToTravel() {
        for (String uniquePlayer : uniquePlayers.keySet()) {
            playerLeftToTravelRounds.put(uniquePlayer, lengthToTravel);
        }
    }

    @Override
    protected void movePlayerAheadByAmount(BoardGamePlayer boardGamePlayer, int amount) {
        super.movePlayerAheadByAmount(boardGamePlayer, amount);
        updatePlayerTravelDistance(boardGamePlayer, amount);
    }

    private void updatePlayerTravelDistance(BoardGamePlayer boardGamePlayer, int amount) {
        int distance = playerLeftToTravelRounds.get(boardGamePlayer.getUniqueName()) - amount;
        if(distance <= 0)
            playerLeftToTravelRounds.remove(boardGamePlayer.getUniqueName());
        else
            playerLeftToTravelRounds.put(boardGamePlayer.getUniqueName(), distance);

    }

    @Override
    protected boolean isFinishState() {
        return playerLeftToTravelRounds.size() == 0;
    }
}
