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

package com.harshalworks.businessbg.board;

import com.harshalworks.businessbg.TestConstants;
import com.harshalworks.businessbg.bank.Bank;
import com.harshalworks.businessbg.board.cell.BlankCell;
import com.harshalworks.businessbg.board.cell.Cell;
import com.harshalworks.businessbg.board.cell.RentableCell;
import com.harshalworks.businessbg.board.cell.RentableMemberbership;
import com.harshalworks.businessbg.exceptions.CannotInitializeBoardException;
import com.harshalworks.businessbg.exceptions.CannotPurchaseThisAsset;
import com.harshalworks.businessbg.exceptions.InvalidBoardPositionException;
import com.harshalworks.businessbg.player.BoardGamePlayer;
import com.harshalworks.businessbg.rules.Rule;
import org.junit.Assert;
import org.junit.Test;

public class BoardTest {
    @Test(expected = CannotPurchaseThisAsset.class)
    public void cannotPurchaseANonPurchaaseAbleCell(){
        //given
        Cell[] boardCells = new Cell[]{new BlankCell()};
        Board board = new Board(boardCells);

        //when
        int position = 0;
        board.purchaseCellAsset(position, null, null);

        //expect  cannot purcahse a blank cell.
    }


    @Test(expected = CannotInitializeBoardException.class)
    public void boardShouldBeInitializedWithNonZeroLength() {
        //given
        int length = 0;

        //when
        Board board = new Board(new Cell[]{});

        //expect
        // board Cannot have zero sized length for the board.
    }

    @Test
    public void boardShouldBeInitializedWithBlankRuledCellsIfEmptyTypeProvided() {
        //given
        Cell[] boardCells = new Cell[1];
        Board board = new Board(boardCells);
        int position = 0;

        //when
        Rule actual = board.getRule(position);

        //then
        Assert.assertTrue(actual instanceof BlankCell);
    }


    @Test(expected = InvalidBoardPositionException.class)
    public void boardShouldAcceptCallsToNonNegativePositionsOnly(){
        //given
        Cell[] boardCells = new Cell[5];
        Board board = new Board(boardCells);

        //when
        int position = -2;
        board.getRule(position);

        //expect invalid board position
    }

    @Test(expected = InvalidBoardPositionException.class)
    public void boardShouldAcceptCallsToWithinLimitsPositionsOnly(){
        //given
        Cell[] boardCells = new Cell[5];
        Board board = new Board(boardCells);

        //when
        int position = 6;
        board.getRule(position);

        //expect invalid board position
    }

    @Test
    public void boardCanSellCellsToMarketingAssistants(){
        //given
        Bank seller = new Bank(TestConstants.INITIAL_AMOUNT_OF_BANK);
        RentableCell cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("",1000,100)
        });
        Cell[] boardCells = new Cell[]{cell};
        Board board = new Board(boardCells);
        BoardGamePlayer player = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.PLAYER_1);

        //when
        int position = 0;
        board.purchaseCellAsset(position, player, seller);

        //then
        Assert.assertEquals(player, cell.getOwner());
    }

    @Test
    public void boardCanOnlySellAssetIfNotAlreadyAcquired(){
        Bank seller = new Bank(TestConstants.INITIAL_AMOUNT_OF_BANK);
        RentableCell cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("",1000,100)
        });
        Cell[] boardCells = new Cell[]{cell};
        Board board = new Board(boardCells);
        BoardGamePlayer player1 = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.PLAYER_1);
        BoardGamePlayer player2 = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.PLAYER_2);

        //when
        int position = 0;
        board.purchaseCellAsset(position, player1, seller);
        try{
            board.purchaseCellAsset(position, player2, seller);
        }catch (Exception e){
            Assert.assertTrue(e instanceof CannotPurchaseThisAsset);
        }

        //then
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT, player2.getMoneyValue());
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT - 1000, player1.getMoneyValue());

    }
}