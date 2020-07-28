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

import com.harshalworks.businessbg.board.Board;
import com.harshalworks.businessbg.board.cell.Cell;
import com.harshalworks.businessbg.dice.Dice;
import com.harshalworks.businessbg.dice.MockFixedOutputDice;
import com.harshalworks.businessbg.dice.StandardSixSidedDice;
import com.harshalworks.businessbg.exceptions.GameIsNotStartedException;
import com.harshalworks.businessbg.exceptions.PlayerCannotMakeTurnException;
import com.harshalworks.businessbg.rules.Rule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class GameTest {

    private Game game;

    @Before
    public void setup() {
        game = new Game(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.INITIAL_AMOUNT_OF_BANK,
                new StandardSixSidedDice(),
                new Board(new Cell[10]));
    }

    @Test
    public void gameShouldStartTurnWithPlayer1() {
        //given
        Player player1 = game.registerPlayer(TestConstants.PLAYER_1);
        game.registerPlayer(TestConstants.PLAYER_2);

        //when
        game.start();

        //then
        expectPlayerWithTurn(player1);
    }

    @Test
    public void gameShouldChangeTurnsForPlayersWhenTheyMakeTheirMove() {
        //given
        Player player1 = game.registerPlayer(TestConstants.PLAYER_1);
        Player player2 = game.registerPlayer(TestConstants.PLAYER_2);

        //when
        game.start();

        for (int i = 0; i < 5; i++) {
            //then
            expectPlayerWithTurn(player1);

            //when
            game.makePlayerMove(player1);

            //then
            expectPlayerWithTurn(player2);

            //when
            game.makePlayerMove(player2);
        }
    }

    @Test(expected = GameIsNotStartedException.class)
    public void playersCannotMakeMovesIfTheGameIsNotStarted() {
        //given
        Player player = game.registerPlayer(TestConstants.PLAYER_1);

        //when
        game.makePlayerMove(player);

        //expect exception that game is not started
    }

    @Test(expected = PlayerCannotMakeTurnException.class)
    public void gameShouldNotAllowPlayersToMakeMoveIfItsNotTheirTurn() {
        //given
        Player player1 = game.registerPlayer(TestConstants.PLAYER_1);
        Player player2 = game.registerPlayer(TestConstants.PLAYER_2);

        //when
        game.start();
        game.makePlayerMove(player2);

        //expects throw new exception that its not your turn.
    }

    @Test(expected = GameIsNotStartedException.class)
    public void gameCannotAssignTurnsIfNotStarted() {
        //given
        game.registerPlayer(TestConstants.PLAYER_1);
        game.registerPlayer(TestConstants.PLAYER_2);

        //when
        game.getPlayerWithCurrentTurn();

        //expect thorws Null pointer Exception that no one has a turn.
    }

    private void expectPlayerWithTurn(Player player) {
        Assert.assertEquals(player.getUniqueName(), game.getPlayerWithCurrentTurn());
    }


    @Test
    public void playersShouldRepeatFromCell1WhenReachedBeyondProvidedBoardLength() {
        //given
        int BOARD_LENGTH = 16;
        int diceOutput[] = setupDiceOutput(10);
        game = new Game(TestConstants.START_PLAYER_AMOUNT, TestConstants.INITIAL_AMOUNT_OF_BANK,
                new MockFixedOutputDice(diceOutput), new Board(new Cell[BOARD_LENGTH]));
        Player player1 = game.registerPlayer(TestConstants.PLAYER_1);
        Player player2 = game.registerPlayer(TestConstants.PLAYER_2);
        game.start();

        //when
        for (int i = 0; i < 5; i++) {
            game.makePlayerMove(player1);
            game.makePlayerMove(player2);
        }

        //then
        Assert.assertTrue(player1.getCurrentPosition() <= BOARD_LENGTH);
        Assert.assertTrue(player1.getCurrentPosition() <= BOARD_LENGTH);
    }

    @Test
    public void gameShouldMovePlayersAheadByTheAmountOnDiceTheyGetEverytime() {
        //given
        int boardLength = 10;
        int[] diceOutput = setupDiceOutput(4);
        Dice customDice = new MockFixedOutputDice(diceOutput);
        game = new Game(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.INITIAL_AMOUNT_OF_BANK,
                customDice,
                new Board(new Cell[boardLength]));
        Player player1 = game.registerPlayer(TestConstants.PLAYER_1);
        Player player2 = game.registerPlayer(TestConstants.PLAYER_2);

        //when
        game.start();

        //then
        int initialPos;
        for (int i = 0; i < diceOutput.length; i += 2) {
            //when
            initialPos = player1.getCurrentPosition();
            game.makePlayerMove(player1);

            //then
            expectCurrentPosition(player1, (initialPos + diceOutput[i])
                    % boardLength);

            //when
            initialPos = player2.getCurrentPosition();
            game.makePlayerMove(player2);

            //then
            expectCurrentPosition(player2, (initialPos + diceOutput[i + 1])
                    % boardLength);
        }
    }

    @Test
    public void onReachingPayToBankCell_GameShouldMakePlayerPayToBank(){
        //given
        Player player1 = setupGameForRule(new Cell(Rule.NONE));

        //when
        game.makePlayerMove(player1);

        //then
//        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT - rule.getCharges(),
//                player1.getMoneyValue());
//        Assert.assertEquals(TestConstants.INITIAL_AMOUNT_OF_BANK + rule.getCharges(),
//                game.getBankMoneyValue());
    }

    private Player setupGameForRule(Cell cellWithRule) {
        game = new Game(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.START_PLAYER_AMOUNT ,
                new MockFixedOutputDice(new int[]{1}),
                new Board(new Cell[]{ cellWithRule }));
        Player player1 = game.registerPlayer(TestConstants.PLAYER_1);
        game.registerPlayer(TestConstants.PLAYER_2);
        game.start();
        return player1;
    }

    @Test
    public void onReachingRewardToPlayerCell_GameShouldMakeBankPayToPlayer(){

    }

    @Test
    public void onReachingPayRentCell_GameShouldMakePlayerPayToOwner(){

    }

    private void expectCurrentPosition(Player player1, int expected) {
        Assert.assertEquals(expected, player1.getCurrentPosition());
    }

    private int[] setupDiceOutput(int length) {
        Random random = new Random();
        int out[] = new int[length];
        for (int i = 0; i < out.length; i++) {
            out[i] = getRandomDiceValue(random);
        }
        return out;
    }

    private int getRandomDiceValue(Random random) {
        return 1 + random.nextInt(12);
    }
}