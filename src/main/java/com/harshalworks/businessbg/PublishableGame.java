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
import com.harshalworks.businessbg.events.GameEvent;
import com.harshalworks.businessbg.events.GameEventPublisher;
import com.harshalworks.businessbg.events.Viewer;
import com.harshalworks.businessbg.player.BoardGamePlayer;
import com.harshalworks.businessbg.player.Player;
import com.harshalworks.businessbg.player.PlayerFactory;

import java.util.Iterator;

public abstract class PublishableGame extends Game{

    protected final GameEventPublisher gameEventPublisher;

    public PublishableGame(int fixedAmountForPlayer, PlayerFactory playerFactory, Bank bank, Dice dice, Board board, GameEventPublisher gameEventPublisher) {
        super(fixedAmountForPlayer, playerFactory, bank, dice, board);
        this.gameEventPublisher = gameEventPublisher;
    }

    @Override
    public void start() {
        super.start();
        publishGameStartedEvent();
    }

    @Override
    protected void registerUniquePlayer(String uniqueName, BoardGamePlayer player) {
        super.registerUniquePlayer(uniqueName, player);
        publishNewPlayerRegisterEvent(uniqueName);
    }

    @Override
    protected int rollTheDice() {
        int diceValue = super.rollTheDice();
        publishDiceRolledEvent(diceValue);
        return diceValue;
    }

    @Override
    protected void updateGameState() {
        super.updateGameState();
        if(gameState == GAME_STATE_FINISHED){
            publishGameFinishedEvent();
        }else{
            publishTurnChangedEvent();
        }
    }

    @Override
    public void purchaseCurrentCellAsset(Player player) {
        super.purchaseCurrentCellAsset(player);
        publishPurchaseEvent(player, player.getCurrentPosition());
    }

    private void publishGameFinishedEvent() {
        gameEventPublisher.publishEvent(new GameEvent("FINISHED", scoreboard));
    }

    private void publishPurchaseEvent(Player player, int position) {
        gameEventPublisher.publishEvent(new GameEvent("PURCHASED", player.getUniqueName() + "-Cell-" + position));
    }


    private void publishDiceRolledEvent(int diceValue) {
        gameEventPublisher.publishEvent(new GameEvent("DICE_ROLLED", ""+diceValue));
    }

    private void publishTurnChangedEvent() {
        gameEventPublisher.publishEvent(new GameEvent("TURN_CHANGED", playerWithCurrentChance.getUniqueName()));
    }

    public void subscribe(Viewer viewer) {
        gameEventPublisher.addSubscriber(viewer);
    }

    protected void publishNewPlayerRegisterEvent(String name) {
        GameEvent player_joined = new GameEvent("PLAYER_JOINED", name);
        gameEventPublisher.publishEvent(player_joined);
    }

    private void publishGameStartedEvent() {
        StringBuilder playersList = new StringBuilder();
        for (Iterator<String> iterator = uniquePlayers.keySet().iterator(); iterator.hasNext(); ) {
            playersList.append(iterator.next());
            if(iterator.hasNext())
                playersList.append(", ");
        }
        gameEventPublisher.publishEvent(new GameEvent("GAME_STARTED", playersList.toString()));
    }
}
