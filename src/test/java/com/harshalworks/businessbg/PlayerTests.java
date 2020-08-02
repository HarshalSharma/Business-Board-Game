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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayerTests {

    private Game game;

    @Before
    public void setup() {
        game = new Game(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.INITIAL_AMOUNT_OF_BANK,
                null, null);
    }

    @Test
    public void playersShouldBeAbleToKnowTheirRegisteredName() {
        //given
        String expected = TestConstants.PLAYER_1;
        Player player;

        //when
        player = game.registerPlayer(TestConstants.PLAYER_1);

        //then
        assertEquals(expected, player.getUniqueName());
    }


    /*@Test
    public void gameShouldNotStartAnyTurnBeforeStartCalled(){
        //given
        TestChanceSubscription chanceSubscriptionP1 = new TestChanceSubscription();
        TestChanceSubscription chanceSubscriptionP2 = new TestChanceSubscription();

        //when
        game.registerPlayer(TestConstants.PLAYER_1, chanceSubscriptionP1);
        game.registerPlayer(TestConstants.PLAYER_2, chanceSubscriptionP2);

        //then
        expectGameToBeInitializedWithZeroTurns(chanceSubscriptionP1);
        expectGameToBeInitializedWithZeroTurns(chanceSubscriptionP2);
    }*/

    /*@Test
    public void playerShoudGetNotifiedAboutTheirChanceInOrder() {
        //given
        TestChanceSubscription chanceSubscriptionP1 = new TestChanceSubscription();
        TestChanceSubscription chanceSubscriptionP2 = new TestChanceSubscription();
        game.registerPlayer(TestConstants.PLAYER_1, chanceSubscriptionP1);
        game.registerPlayer(TestConstants.PLAYER_2, chanceSubscriptionP2);
        TestChanceSubscription.EXE_SEQUENCE = 0;

        //when
        game.start();
        int turns = 5;

        //then
        validateTurns(chanceSubscriptionP1, chanceSubscriptionP2, turns);
        assertEquals(2 * turns, chanceSubscriptionP1.turnChanged);
        assertEquals(2 * turns, chanceSubscriptionP2.turnChanged);
    }*/

    /*private void validateTurns(TestChanceSubscription chanceSubscriptionP1, TestChanceSubscription chanceSubscriptionP2, int turns) {
        for (int i = 0; i < turns; i++) {
            expectPlayerChanceOrderIsCorrect(i, chanceSubscriptionP1);

            //when
            game.nextTurn();

            //then
            expectPlayerChanceOrderIsCorrect(i + 3, chanceSubscriptionP2);

            //when
            game.nextTurn();
        }
    }*/

    private void expectPlayerChanceOrderIsCorrect(int i, TestChanceSubscription testChanceSubscription) {
        assertEquals(i + 1, testChanceSubscription.gotMyChance);
        assertEquals(i + 2, testChanceSubscription.diceGotRolled);
        assertEquals(i + 3, testChanceSubscription.chanceCompleted);
    }

    private void expectGameToBeInitializedWithZeroTurns(TestChanceSubscription chanceSubscription) {
        assertEquals(0, chanceSubscription.turnChanged);
        assertEquals(0, chanceSubscription.gotMyChance);
        assertEquals(0, chanceSubscription.diceGotRolled);
        assertEquals(0, chanceSubscription.chanceCompleted);
    }

}