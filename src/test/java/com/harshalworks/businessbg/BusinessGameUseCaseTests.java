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

import com.harshalworks.businessbg.dice.Dice;
import com.harshalworks.businessbg.exceptions.CannotRegisterPlayerWhileGameHasBegun;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BusinessGameUseCaseTests {

    private Game game;

    @Before
    public void setup() {
        game = new Game(TestConstants.START_PLAYER_AMOUNT, TestConstants.INITIAL_AMOUNT_OF_BANK);
    }

    @Test
    public void canNotStartAGameWithLessThan2UniquePlayers(){
        //Given
        game.registerPlayer(TestConstants.PLAYER_1);
        game.registerPlayer(TestConstants.PLAYER_1);

        //when
        game.start();

        //then
        assertFalse(game.isRunning());
    }

    @Test
    public void canStartAGameWithMoreThanOrEqualTo2UniquePlayers() {
        //Given
        game.registerPlayer(TestConstants.PLAYER_1);
        game.registerPlayer(TestConstants.PLAYER_2);

        //when
        game.start();

        //then
        assertTrue(game.isRunning());
    }

    @Test(expected = CannotRegisterPlayerWhileGameHasBegun.class)
    public void cannotRegisterMorePlayersAfterStartingTheGame(){
        //Given
        game.registerPlayer(TestConstants.PLAYER_1);
        game.registerPlayer(TestConstants.PLAYER_2);

        //when
        game.start();

        //then
        game.registerPlayer("New Player");
    }

    @Test
    public void eachPlayerShouldGetSamePositiveAmountUponRegisteringToGame() {
        //Given
        Player player1 = null, player2 = null;

        //when
        player1 = game.registerPlayer(TestConstants.PLAYER_1);
        player2 = game.registerPlayer(TestConstants.PLAYER_2);

        //then
        assertTrue(player1.getMoneyValue() > 0);
        assertTrue(player2.getMoneyValue() > 0);
        assertEquals(player1.getMoneyValue(), player2.getMoneyValue());
    }

    @Test
    public void gameShouldHaveAFixedValueOfMoneyForEachPlayerOnStart(){
        //given
        int[] amounts = new int[]{1500, 10, 300};
        Player player = null;

        for (int i = 0; i < amounts.length; i++) {
            //when
            game = new Game(amounts[i], 5000);
            player = game.registerPlayer(TestConstants.PLAYER_1);

            //then
            assertEquals(amounts[i], player.getMoneyValue());
        }
    }

    @Test
    public void gameShouldHaveABankWithInitialMoney(){
        //given
        int[] amounts = new int[]{100, 1055, 3010};
        for (int i = 0; i < amounts.length; i++) {
            //when
            game = new Game(TestConstants.START_PLAYER_AMOUNT, amounts[i]);

            //then
            assertEquals(amounts[i], game.getBankMoneyValue());
        }
    }

    private Dice createATestableDice() {
        return null;
    }


}
