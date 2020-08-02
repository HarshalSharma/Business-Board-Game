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

package com.harshalworks.businessbg.board.cell.standard;

import com.harshalworks.businessbg.TestConstants;
import com.harshalworks.businessbg.board.cell.RentableCell;
import com.harshalworks.businessbg.board.cell.RentableMemberbership;
import com.harshalworks.businessbg.dealers.Payee;
import com.harshalworks.businessbg.exceptions.CannotPurchaseASoldCell;
import com.harshalworks.businessbg.player.BoardGamePlayer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RentableCellTest {

    @Test
    public void rentableCellsCanHaveOwnerIfPurchased() {
        //given
        BoardGamePlayer player = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT, TestConstants.PLAYER_1);
        RentableCell cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("", 500, 100)});
        //when
        cell.purchase(player);

        //then
        Payee owner = cell.getOwner();
        Assert.assertEquals(player, owner);
    }

    @Test
    public void purchasingACellWouldRequireMoneyToBeSpent() {
        //given
        int purchaseAmount = 500;
        BoardGamePlayer player = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT, TestConstants.PLAYER_1);
        RentableCell cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("", 500, 100)});
        //when
        cell.purchase(player);

        //then
        Payee owner = cell.getOwner();
        Assert.assertEquals(player, owner);
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT -  purchaseAmount,player.getMoneyValue());
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
        cell.purchase(player1);
        int initial = player1.getMoneyValue();

        //when
        cell.execute(player2,null);

        //then
        Assert.assertEquals(initial + rent,player1.getMoneyValue());
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT - rent,player2.getMoneyValue());
    }

    @Test(expected = CannotPurchaseASoldCell.class)
    public void anotherPlayerCannotPurchaseAPurchasedCell() {
        //given
        RentableCell cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("",500, 100)});
        BoardGamePlayer player = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT, TestConstants.PLAYER_1);
        BoardGamePlayer player2 = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT, TestConstants.PLAYER_2);

        //when
        cell.purchase(player);
        cell.purchase(player2);

        //then expect cannot purchased a sold cell exception.
    }

    @Test(expected = CannotPurchaseASoldCell.class)
    public void samePlayerCannotPurchaseAPurchasedCell() {
        //given
        RentableCell cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("",500, 100)});
        BoardGamePlayer player = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT, TestConstants.PLAYER_1);

        //when
        cell.purchase(player);
        cell.purchase(player);

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
        cell.purchase(player);
        cell.upgradeMembership(player);

        //then
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT - initialCost - upgradeCost,
                player.getMoneyValue());
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
        cell.purchase(player1);

        //when
        cell.upgradeMembership(player1);
        cell.execute(player2,null);

        //then
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT - upgradedRent,player2.getMoneyValue());
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT + upgradedRent - 1000,player1.getMoneyValue());
    }

    @Test(expected = CannotPurchaseASoldCell.class)
    public void onlyOwnerCanUpgradeTheirMembership(){
        RentableCell  cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("",500, 100)});
        BoardGamePlayer player1 = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.PLAYER_1);
        BoardGamePlayer player2 = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT, TestConstants.PLAYER_2);
        cell.purchase(player1);

        //when
        cell.upgradeMembership(player2);

        //then expect cannot purchase a sold cell exception.
    }

    @Test
    public void playerCannotUpgradeMembershipIfMoneyNotAvialable(){
        //given
        RentableCell  cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("",500, 100),
                new RentableMemberbership("",1000, 200),
                new RentableMemberbership("",1500, 300)});
        BoardGamePlayer player1 = new BoardGamePlayer(500,
                TestConstants.PLAYER_1);

        //when
        cell.purchase(player1);

        //then
        Assert.assertFalse(cell.upgradeMembership(player1));
    }

    @Test
    public void playerCannotUpgradeMembershipBeyondAvailale(){
        //given
        RentableCell  cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("",500, 100),
                new RentableMemberbership("",1000, 200),
                new RentableMemberbership("",1500, 300)});
        BoardGamePlayer player1 = new BoardGamePlayer(Integer.MAX_VALUE,
                TestConstants.PLAYER_1);

        //when
        cell.purchase(player1);

        //then
        Assert.assertTrue(cell.upgradeMembership(player1));
        Assert.assertTrue(cell.upgradeMembership(player1));
        Assert.assertFalse(cell.upgradeMembership(player1));
    }

    @Test
    public void rentableCellShouldTellMembershipPurchased(){
        //given
        RentableMemberbership purchaseMembership = new RentableMemberbership("Silver",500, 100);
        RentableCell  cell = new RentableCell(new RentableMemberbership[]{ purchaseMembership });
        BoardGamePlayer player1 = new BoardGamePlayer(Integer.MAX_VALUE,
                TestConstants.PLAYER_1);

        //when
        cell.purchase(player1);

        //then assert
        RentableMemberbership memberbership = cell.getMembershipStatus();
        Assert.assertEquals(memberbership, new RentableMemberbership(
                purchaseMembership.getName(),
                purchaseMembership.getCost(),
                purchaseMembership.getRent()));
        Assert.assertEquals(memberbership.getName(), purchaseMembership.getName());
    }
}
