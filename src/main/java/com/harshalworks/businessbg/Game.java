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
import com.harshalworks.businessbg.exceptions.*;
import com.harshalworks.businessbg.player.BoardGamePlayer;
import com.harshalworks.businessbg.player.Player;
import com.harshalworks.businessbg.player.PlayerFactory;
import com.harshalworks.businessbg.rules.Rule;

import java.util.*;

/**
 *
 * The main Business Board game.
 *
 * responsibilities:
 * 1. context - stores the object reference for board, bank, players, queue of the game.
 * 2. starts the game and reports the running status of the game.
 * 3. changes turns
 * 4. registers players.
 * 5. handles requests from players to make their moves.
 * 6. tells about the money in the bank or with some players.
 */
public abstract class Game {

    protected final static int GAME_STATE_WAITING = 0;
    protected final static int GAME_STATE_RUNNING = 1;
    protected final static int GAME_STATE_FINISHED = 2;

    protected Map<String, BoardGamePlayer> uniquePlayers;
    protected final Queue<BoardGamePlayer> playersTurnOrder;
    protected BoardGamePlayer playerWithCurrentChance;
    protected final Dice dice;
    protected final Board board;
    protected final Bank bank;
    protected final PlayerFactory playerFactory;

    protected String scoreboard;
    protected int gameState = GAME_STATE_WAITING;
    protected final int FIXED_START_AMOUNT_FOR_PLAYER;

    public Game(final int fixedAmountForPlayer, final PlayerFactory playerFactory, final Bank bank,
                final Dice dice, final Board board) {
        this.FIXED_START_AMOUNT_FOR_PLAYER = fixedAmountForPlayer;
        this.bank = bank;
        this.dice = dice;
        this.board = board;
        this.uniquePlayers = new HashMap<>();
        this.playersTurnOrder = new LinkedList<>();
        this.playerFactory = playerFactory;
    }

    public Player registerPlayer(final String uniqueName) {
        if(isRunning())
            throw new CannotRegisterPlayerException(ExceptionMessageConstants.WHEN_THE_GAME_HAS_ALREADY_STARTED);

        BoardGamePlayer player = playerFactory.newPlayerInstance(FIXED_START_AMOUNT_FOR_PLAYER, uniqueName);
        if (!uniquePlayers.containsKey(uniqueName)) {
                registerUniquePlayer(uniqueName, player);
        }
        return player;
    }

    protected void registerUniquePlayer(String uniqueName, BoardGamePlayer player) {
        uniquePlayers.put(uniqueName, player);
        playersTurnOrder.add(player);
    }

    public void start() {
        validateIfRunnable();
        runGame();
    }

    private void runGame() {
        gameState = GAME_STATE_RUNNING;
        playerWithCurrentChance = playersTurnOrder.poll();
    }


    private void validateIfRunnable() {
        synchronized (this) {
            if (isRunning()) {
                throw new CannotStartGameException(ExceptionMessageConstants.GAME_IS_ALREADY_RUNNING);
            }
            if (isFinished()) {
                throw new CannotStartGameException(ExceptionMessageConstants.GAME_HAS_FINISHED);
            }
            if (uniquePlayers.size() < 2) {
                throw new CannotStartGameException(ExceptionMessageConstants.NOT_ENOUGH_PLAYERS);
            }
        }
    }

    public int getBankMoneyValue() {
        return bank.getAvailableAmount();
    }

    private void nextTurn() {
        playersTurnOrder.add(playerWithCurrentChance);
        playerWithCurrentChance = playersTurnOrder.poll();
    }

    public String getPlayerWithCurrentTurn() {
        validateIfGameHasStarted();
        return playerWithCurrentChance.getUniqueName();
    }

    private void validateIfGameHasStarted() {
        if (!isRunning())
            throw new GameIsNotStartedException();
    }

    public void makeMove(Player player) {
        validateMove(player);
        movePlayerAheadByAmount(playerWithCurrentChance, rollTheDice());
        applyRuleAtCurrentPosition(playerWithCurrentChance);
        updateGameState();
    }

    protected void updateGameState() {
        if(isFinishState())
            this.gameState = GAME_STATE_FINISHED;
        else
            nextTurn();
    }

    protected void validateMove(Player player) {
        validateIfGameHasStarted();
        validateIfThisPlayerHaveTurn(player);
    }

    protected int rollTheDice() {
        return this.dice.rollTheDice();
    }

    /**
     * Apply rules on the player based on their current position on the board.
     *
     * @param player the player currently being evaluated.
     */
    private void applyRuleAtCurrentPosition(BoardGamePlayer player) {
        //get the rules applicable on the board at current position.
        Rule rule = getRuleforCurrentPosition(player);

        // apply these rules to the player.
        rule.execute(player, bank);
    }

    private Rule getRuleforCurrentPosition(BoardGamePlayer player) {
        return board.getRule(player.getCurrentPosition());
    }

    protected void movePlayerAheadByAmount(BoardGamePlayer boardGamePlayer, int amount) {
        int newPosition = boardGamePlayer.getCurrentPosition();
        newPosition += amount;
        newPosition %= board.getBoardLength();
        boardGamePlayer.setPosition(newPosition);
    }

    private void validateIfThisPlayerHaveTurn(Player player) {
        if (!playerWithCurrentChance.equals(player))
            throw new PlayerCannotMakeTurnException(ExceptionMessageConstants.TURNS_WHEN_IT_S_NOT_THEIR_CHANCE);
    }

    public void purchaseCurrentCellAsset(Player player) {
        int position = player.getCurrentPosition();
        board.purchaseCellAsset(position, uniquePlayers.get(player.getUniqueName()), bank);

    }

    public boolean isRunning() {
        return gameState == GAME_STATE_RUNNING;
    }

    public boolean isFinished(){
        return gameState == GAME_STATE_FINISHED;
    }

    protected void setFinished() {
        gameState = GAME_STATE_FINISHED;
    }

    protected abstract boolean isFinishState();

}
