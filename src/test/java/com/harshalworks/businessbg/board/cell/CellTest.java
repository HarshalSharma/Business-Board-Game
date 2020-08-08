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

package com.harshalworks.businessbg.board.cell;

import com.harshalworks.businessbg.Game;
import com.harshalworks.businessbg.TestGame;
import com.harshalworks.businessbg.player.Player;
import com.harshalworks.businessbg.TestConstants;
import com.harshalworks.businessbg.board.Board;
import com.harshalworks.businessbg.dice.MockFixedOutputDice;
import org.junit.Assert;
import org.junit.Test;

public class CellTest {

    private Game game;

    private Player setupGameAndGetPlayer(Board board, int[] dice) {
        game = new TestGame(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.INITIAL_AMOUNT_OF_BANK,
                new MockFixedOutputDice(dice),
                board);
        Player player = game.registerPlayer(TestConstants.PLAYER_1);
        game.registerPlayer(TestConstants.PLAYER_2);
        game.start();
        return player;
    }

    @Test
    public void cellCouldHaveRuleToPayChargesToBank(){
        int amountToBank = 100;
        //given
        Board board = new Board(new Cell[]{ new PayToBankCell(amountToBank)});
        int[] dice = new int[]{ 1 };
        Player player = setupGameAndGetPlayer(board, dice);

        //when
        game.makeMove(player);

        //then
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT - amountToBank, player.getMoneyValue());
        Assert.assertEquals(TestConstants.INITIAL_AMOUNT_OF_BANK + amountToBank, game.getBankMoneyValue());
    }

    @Test
    public void cellCouldHaveRewardToBePaidByBank(){
        int reward = 100;
        //given
        Board board = new Board(new Cell[]{ new BankRewardCell(reward) });
        int[] dice = new int[]{ 1 };
        Player player = setupGameAndGetPlayer(board, dice);

        //when
        game.makeMove(player);

        //then
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT + reward, player.getMoneyValue());
        Assert.assertEquals(TestConstants.INITIAL_AMOUNT_OF_BANK - reward, game.getBankMoneyValue());
    }

}