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

package com.harshalworks.businessbg.events;

import com.harshalworks.businessbg.Game;
import com.harshalworks.businessbg.PublishableGame;
import com.harshalworks.businessbg.TestConstants;
import com.harshalworks.businessbg.TestGame;
import com.harshalworks.businessbg.board.Board;
import com.harshalworks.businessbg.board.cell.*;
import com.harshalworks.businessbg.dice.MockFixedOutputDice;
import com.harshalworks.businessbg.dice.StandardSixSidedDice;
import com.harshalworks.businessbg.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ObserversTest {

    private PublishableGame game;
    private Board board;

    @Before
    public void setup() {
        board = new Board(new Cell[]{
                new BlankCell(), new BlankCell(), new BankRewardCell(100),
                new PayToBankCell(200),
                new RentableCell(new RentableMemberbership[]{
                        new RentableMemberbership("dummy", 500,100)})});
        game = new TestGame(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.INITIAL_AMOUNT_OF_BANK,
                new StandardSixSidedDice(), board);
    }

    @Test
    public void viewersShouldBeAbleToSubscribeToGameEventsLikePlayerRegister(){
        //given
        TestViewer viewer = new TestViewer();

        //when
        game.subscribe(viewer);
        game.registerPlayer("test");

        //then
        GameEvent event = viewer.getLatestEvent();
        Assert.assertNotNull(event);
        Assert.assertEquals(event.getType(), "PLAYER_JOINED");
        Assert.assertEquals(event.getEventMessage(), "test");
    }

    @Test
    public void viewersShouldBeNotifiedOfGameStartEventWithNamesOfPlayers(){
        //given
        TestViewer viewer = new TestViewer();
        game.subscribe(viewer);
        game.registerPlayer("test1");
        game.registerPlayer("test2");
        game.registerPlayer("test3");
        game.registerPlayer("test4");

        //when
        game.start();

        //then
        GameEvent event = viewer.getLatestEvent();
        Assert.assertNotNull(event);
        Assert.assertEquals(event.getType(), "GAME_STARTED");
        Assert.assertEquals(4, event.getEventMessage().split(",").length);
    }

    @Test
    public void viewersShouldBeNotifiedWhenTurnChanges(){
        //given
        TestViewer viewer = new TestViewer();
        game.subscribe(viewer);
        Player player1 = game.registerPlayer(TestConstants.PLAYER_1);
        game.registerPlayer(TestConstants.PLAYER_2);
        game.start();

        //when
        game.makeMove(player1);

        //then
        GameEvent event = viewer.getLatestEvent();
        Assert.assertNotNull(event);
        Assert.assertEquals(event.getType(), "TURN_CHANGED");
        Assert.assertEquals(event.getEventMessage(), TestConstants.PLAYER_2);
    }

    @Test
    public void viewersShouldBeNotifiedWithTheNextNumberDiceShowsUp(){
        //given
        TestViewer viewer = new TestViewer();
        game.subscribe(viewer);
        Player player1 = game.registerPlayer(TestConstants.PLAYER_1);
        game.registerPlayer(TestConstants.PLAYER_2);
        game.start();

        //when
        game.makeMove(player1);

        //then
        GameEvent event = viewer.getEventFromLast(1);
        Assert.assertNotNull(event);
        Assert.assertEquals("DICE_ROLLED",event.getType());
        int i = Integer.parseInt(event.getEventMessage());
        Assert.assertTrue(i>0 && i<7);
    }

    @Test
    public void viewersShouldBeNotifiedWithSomeRentableCellBeingPurchasedByPlayers(){
        //given
        RentableCell rentableCell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("", 500, 100)});
        board = new Board(new Cell[]{new BlankCell(), rentableCell});
        game = new TestGame(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.INITIAL_AMOUNT_OF_BANK,
                new MockFixedOutputDice(new int[]{1}), board);

        TestViewer viewer = new TestViewer();
        game.subscribe(viewer);
        Player player1 = game.registerPlayer(TestConstants.PLAYER_1);
        game.registerPlayer(TestConstants.PLAYER_2);
        game.start();

        //when
        game.makeMove(player1);
        game.purchaseCurrentCellAsset(player1);

        //then
        GameEvent event = viewer.getLatestEvent();
        Assert.assertEquals("PURCHASED",event.getType());
        Assert.assertEquals(TestConstants.PLAYER_1 + "-Cell-" + 1 , event.getEventMessage());
    }

}
