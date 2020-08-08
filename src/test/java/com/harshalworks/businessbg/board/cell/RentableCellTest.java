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
import com.harshalworks.businessbg.TestConstants;
import com.harshalworks.businessbg.TestGame;
import com.harshalworks.businessbg.bank.Bank;
import com.harshalworks.businessbg.board.Board;
import com.harshalworks.businessbg.dealers.Payee;
import com.harshalworks.businessbg.dice.MockFixedOutputDice;
import com.harshalworks.businessbg.exceptions.CannotPurchaseThisAsset;
import com.harshalworks.businessbg.player.BoardGamePlayer;
import com.harshalworks.businessbg.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RentableCellTest {

    private Bank bank;

    @Before
    public void setup(){
        bank = new Bank(TestConstants.INITIAL_AMOUNT_OF_BANK);
    }

    @Test
    public void rentableCellsCanHaveOwnerIfPurchased() {
        //given
        BoardGamePlayer player = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT, TestConstants.PLAYER_1);
        RentableCell cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("", 500, 100)});
        //when
        Board board = new Board(new Cell[]{ cell });
        board.purchaseCellAsset(0, player, bank);

        //then
        Payee owner = cell.getOwner();
        Assert.assertEquals(player, owner);
    }

    @Test
    public void purchasingACellWouldRequireMoneyToBeSpentAndAddedToBank() {
        //given
        int purchaseAmount = 500;
        BoardGamePlayer player = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT, TestConstants.PLAYER_1);
        RentableCell cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("", 500, 100)});
        //when
        Board board = new Board(new Cell[]{ cell });
        board.purchaseCellAsset(0, player, bank);

        //then
        Payee owner = cell.getOwner();
        Assert.assertEquals(player, owner);
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT -  purchaseAmount,player.getMoneyValue());
        Assert.assertEquals(TestConstants.INITIAL_AMOUNT_OF_BANK +  purchaseAmount,bank.getAvailableAmount());

    }

    @Test
    public void upgradingACellWouldRequireMoneyToBeSpentAndAddedToBank() {
        //given
        int purchaseAmount = 700;
        BoardGamePlayer player = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT, TestConstants.PLAYER_1);
        RentableCell cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("", 500, 100), new RentableMemberbership("2", 700, 200)});
        //when
        Board board = new Board(new Cell[]{ cell });
        board.purchaseCellAsset(0, player, bank);
        board.purchaseCellAsset(0, player, bank);

        //then
        Payee owner = cell.getOwner();
        Assert.assertEquals(player, owner);
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT -  purchaseAmount,player.getMoneyValue());
        Assert.assertEquals(TestConstants.INITIAL_AMOUNT_OF_BANK +  purchaseAmount,bank.getAvailableAmount());

    }


    @Test
    public void rentCellWouldHaveChargesToBePaidToOwnerIfOwnerIsPresent() {
        //given
        int rent = 100;
        RentableCell cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("",500, rent)});
        BoardGamePlayer player1 = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.PLAYER_1);
        BoardGamePlayer player2 = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT, TestConstants.PLAYER_2);
        Board board = new Board(new Cell[]{ cell });
        board.purchaseCellAsset(0, player1, bank);
        int initial = player1.getMoneyValue();

        //when
        cell.execute(player2,null);

        //then
        Assert.assertEquals(initial + rent,player1.getMoneyValue());
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT - rent,player2.getMoneyValue());
    }

    @Test(expected = CannotPurchaseThisAsset.class)
    public void anotherPlayerCannotPurchaseAPurchasedCell() {
        //given
        RentableCell cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("",500, 100)});
        BoardGamePlayer player = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT, TestConstants.PLAYER_1);
        BoardGamePlayer player2 = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT, TestConstants.PLAYER_2);

        //when
        Board board = new Board(new Cell[]{ cell });
        board.purchaseCellAsset(0, player, bank);
        board.purchaseCellAsset(0, player2, bank);


        //then expect cannot purchased a sold cell exception.
    }

    @Test
    public void purchasedRentableCellsCouldBeUpgradedByPayingUpgradeCost() {
        //given
        int initialCost = 500;
        int upgradeCost = 500;
        RentableCell cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("", initialCost, 100),
                new RentableMemberbership("", initialCost + upgradeCost, 200)});
        BoardGamePlayer player = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT, TestConstants.PLAYER_1);

        //when
        Board board = new Board(new Cell[]{ cell });
        board.purchaseCellAsset(0, player, bank);
        board.purchaseCellAsset(0, player, bank);

        //then
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT - initialCost - upgradeCost,
                player.getMoneyValue());
        Assert.assertEquals(TestConstants.INITIAL_AMOUNT_OF_BANK + initialCost + upgradeCost,
                bank.getAvailableAmount());
    }

    @Test
    public void rentCouldBeIncreasedByUpgradingMembership(){
        //given
        int upgradedRent = 200;
        RentableCell  cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("",500, 100),
                new RentableMemberbership("",1000, upgradedRent),
                new RentableMemberbership("",1500, 300)});
        BoardGamePlayer player1 = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.PLAYER_1);
        BoardGamePlayer player2 = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT, TestConstants.PLAYER_2);
        Board board = new Board(new Cell[]{ cell });
        board.purchaseCellAsset(0, player1, bank);

        //when
        board.purchaseCellAsset(0, player1, bank);
        cell.execute(player2,null);

        //then
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT - upgradedRent,player2.getMoneyValue());
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT + upgradedRent - 1000,player1.getMoneyValue());
    }

    @Test(expected = CannotPurchaseThisAsset.class)
    public void onlyOwnerCanUpgradeTheirMembership(){
        RentableCell  cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("",500, 100),
                new RentableMemberbership("",1500, 300),
        });
        BoardGamePlayer player1 = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.PLAYER_1);
        BoardGamePlayer player2 = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT, TestConstants.PLAYER_2);
        Board board = new Board(new Cell[]{ cell });
        board.purchaseCellAsset(0, player1, bank);

        //when
        board.purchaseCellAsset(0, player2, bank);

        //then expect cannot purchase a sold cell exception.
    }

    @Test(expected = CannotPurchaseThisAsset.class)
    public void playerCannotUpgradeMembershipIfMoneyNotAvialable(){
        //given
        RentableCell  cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("",500, 100),
                new RentableMemberbership("",1000, 200),
                new RentableMemberbership("",1500, 300)});
        BoardGamePlayer player1 = new BoardGamePlayer(500,
                TestConstants.PLAYER_1);
        Board board = new Board(new Cell[]{ cell });

        //when
        board.purchaseCellAsset(0, player1, bank);

        //then
        board.purchaseCellAsset(0, player1, bank);
    }

    @Test(expected = CannotPurchaseThisAsset.class)
    public void playerCannotUpgradeMembershipBeyondAvailale(){
        //given
        RentableCell  cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("",500, 100),
                new RentableMemberbership("",1000, 200),
                new RentableMemberbership("",1500, 300)});
        BoardGamePlayer player1 = new BoardGamePlayer(Integer.MAX_VALUE,
                TestConstants.PLAYER_1);
        Board board = new Board(new Cell[]{ cell });

        //when
        board.purchaseCellAsset(0, player1, bank);

        //then
        board.purchaseCellAsset(0, player1, bank);
        board.purchaseCellAsset(0, player1, bank);
        //expect exception CannotPurchaseAsset
        board.purchaseCellAsset(0, player1, bank);
    }

    @Test
    public void rentableCellShouldTellMembershipPurchased(){
        //given
        RentableMemberbership purchaseMembership = new RentableMemberbership("Silver",500, 100);
        RentableCell  cell = new RentableCell(new RentableMemberbership[]{ purchaseMembership });
        BoardGamePlayer player1 = new BoardGamePlayer(Integer.MAX_VALUE,
                TestConstants.PLAYER_1);
        Board board = new Board(new Cell[]{ cell });

        //when
        board.purchaseCellAsset(0, player1, bank);

        //then assert
        RentableMemberbership memberbership = cell.getMembershipStatus();
        Assert.assertEquals(memberbership, new RentableMemberbership(
                purchaseMembership.getName(),
                purchaseMembership.getCost(),
                purchaseMembership.getRent()));
        Assert.assertEquals(memberbership.getName(), purchaseMembership.getName());
    }

    @Test
    public void playersDoNotNeedToPayAnyRentIfRentableCellIsNotPurchased(){
        //given
        RentableCell  cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("",500, 100)});
        BoardGamePlayer player = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.PLAYER_1);
        //when
        cell.execute(player, null);

        //then
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT, player.getMoneyValue());
    }

    @Test
    public void playersAreAllowedToPurchaseRentableCellOnlyWhenTheyLandOnIt(){
        //given
        RentableCell rentableCell = new RentableCell(new RentableMemberbership[]
                {
                        new RentableMemberbership("",100,2)
                });
        Game game = new TestGame(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.INITIAL_AMOUNT_OF_BANK,
                new MockFixedOutputDice(new int[]{1}), new Board(new Cell[]{
                        new BlankCell(), rentableCell}));
        Player player = game.registerPlayer(TestConstants.PLAYER_1);
        game.registerPlayer(TestConstants.PLAYER_2);
        game.start();

        //check that
        Assert.assertEquals(0, player.getCurrentPosition());
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT, player.getMoneyValue());
        Assert.assertNull(rentableCell.getOwner());

        //when
        game.makeMove(player);
        game.purchaseCurrentCellAsset(player);

        //then
        Assert.assertEquals(1, player.getCurrentPosition());
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT - 100, player.getMoneyValue());
        Assert.assertEquals(player, rentableCell.getOwner());
    }
}
