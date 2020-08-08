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

package com.harshalworks.businessbg.player;

import com.harshalworks.businessbg.TestConstants;
import com.harshalworks.businessbg.bank.Bank;
import com.harshalworks.businessbg.board.Board;
import com.harshalworks.businessbg.board.cell.Cell;
import com.harshalworks.businessbg.board.cell.RentableCell;
import com.harshalworks.businessbg.board.cell.RentableMemberbership;
import com.harshalworks.businessbg.dealers.Payee;
import com.harshalworks.businessbg.dealers.PropertyAck;
import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;
import java.util.Set;

public class BoardGamePlayerTest {

    @Test
    public void ifTwoPlayerObjectsHaveSameUniqueNameThenTheyAreEqual(){
        //given
        BoardGamePlayer player = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.PLAYER_1);

        //then
        Assert.assertEquals(Objects.hash(TestConstants.PLAYER_1), player.hashCode());
    }

    @Test
    public void boardGamePlayerShouldHaveAListOfPropertiesItOwns(){
        //given
        BoardGamePlayer player = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.PLAYER_1);

        //when
        int propertyOwned1 = 500, propertyOwned2 = 1500;
        PropertyAck propertyAck1 = new PropertyAck("1", propertyOwned1);
        PropertyAck propertyAck2 = new PropertyAck("3", propertyOwned2);
        player.addProperty(propertyAck1);
        player.addProperty(propertyAck2);

        //then
        Set<PropertyAck> propertyAcknowledgements = player.getPropertiesOwned();
        Assert.assertEquals(2, propertyAcknowledgements.size());
        Assert.assertEquals(propertyOwned1 + propertyOwned2, player.getTotalAssetValue());
    }

    @Test
    public void boardGamePlayerShouldHaveAListOfUniquePropertiesItUpgraded(){
        //given
        BoardGamePlayer player = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT,
                TestConstants.PLAYER_1);
        String propertyId = "1";
        int propertyOwnedInitial = 500, propertyOwnedUpgraded = 1500;
        PropertyAck propertyAck1 = new PropertyAck(propertyId, propertyOwnedInitial);
        PropertyAck propertyAck2 = new PropertyAck(propertyId, propertyOwnedUpgraded);

        //when
        player.addProperty(propertyAck1);
        player.addProperty(propertyAck2);

        //then
        Set<PropertyAck> propertyAcknowledgements = player.getPropertiesOwned();
        Assert.assertEquals(1, propertyAcknowledgements.size());
        Assert.assertEquals(propertyOwnedUpgraded, player.getTotalAssetValue());
    }

    @Test
    public void purchasingACellWouldAddPurchaseAckSlipWithPlayer() {
        //given
        int purchaseAmount = 500;
        int cellPosition = 0;
        BoardGamePlayer player = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT, TestConstants.PLAYER_1);
        RentableCell cell = new RentableCell(new RentableMemberbership[]{
                new RentableMemberbership("", 500, 100)});
        Board board = new Board(new Cell[]{ cell });
        Bank bank = new Bank(TestConstants.INITIAL_AMOUNT_OF_BANK);

        //when
        board.purchaseCellAsset(cellPosition, player, bank);

        //then
        Set<PropertyAck> propertiesOwned = player.getPropertiesOwned();
        Assert.assertEquals(1, propertiesOwned.size());
        PropertyAck slip = propertiesOwned.iterator().next();
        Assert.assertEquals("CELL_" + cellPosition, slip.getPropertyId());
        Assert.assertEquals(purchaseAmount,slip.getPropertyValue());
    }
}