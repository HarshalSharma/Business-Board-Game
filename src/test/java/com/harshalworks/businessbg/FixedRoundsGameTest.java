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
import com.harshalworks.businessbg.board.cell.BlankCell;
import com.harshalworks.businessbg.board.cell.Cell;
import com.harshalworks.businessbg.dice.MockFixedOutputDice;
import com.harshalworks.businessbg.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FixedRoundsGameTest {

    private FixedRoundsGame game;

    @Before
    public void setupGame() {
        int turns = 5;
        Cell[] boardPath = new Cell[]{
                new BlankCell(), new BlankCell(), new BlankCell(), new BlankCell()
        };
        int[] diceoutput = {6,6,6,6,6,6,6,6};
        game = new FixedRoundsGame(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.INITIAL_AMOUNT_OF_BANK,new MockFixedOutputDice(diceoutput),
                new Board(boardPath), turns);
    }

    @Test
    public void fixedRoundsGameFinishesAfterCertainRoundsCompleted(){
        //given
        setupGame();
        Player player1 = game.registerPlayer(TestConstants.PLAYER_1);
        Player player2 = game.registerPlayer(TestConstants.PLAYER_2);
        game.start();

        //validate
        Assert.assertFalse(game.isFinishState());

        //when
        for (int i = 0; i < 3; i++) {
            game.makeMove(player1);
            game.makeMove(player2);
        }

        //validate
        Assert.assertFalse(game.isFinished());

        //when
        game.makeMove(player1);
        game.makeMove(player2);

        //then
        Assert.assertTrue(game.isFinished());
    }

}