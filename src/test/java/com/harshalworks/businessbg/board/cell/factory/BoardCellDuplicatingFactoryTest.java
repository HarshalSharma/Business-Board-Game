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

package com.harshalworks.businessbg.board.cell.factory;

import com.harshalworks.businessbg.TestConstants;
import com.harshalworks.businessbg.bank.Bank;
import com.harshalworks.businessbg.board.cell.*;
import com.harshalworks.businessbg.exceptions.CellTypeNotDefinedException;
import com.harshalworks.businessbg.player.BoardGamePlayer;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BoardCellDuplicatingFactoryTest {

    @Test(expected = CellTypeNotDefinedException.class)
    public void givenInvalidNameFactoryDontGenerateAnyCell(){
        //given
        Map<String, Cell> map = new HashMap<>();
        BoardCellDuplicatingFactory factory = new StandardBoardCellDuplicatingFactory(map);

        //when
        factory.createCell("wjfoiwejf");
        //assert exception.
    }

    @Test(expected = CellTypeNotDefinedException.class)
    public void givenValidNameButNoDefinationFactoryDontGenerateAnyCell(){
        //given
        Map<String, Cell> map = new HashMap<>();
        BoardCellDuplicatingFactory factory = new StandardBoardCellDuplicatingFactory(map);

        //when
        factory.createCell(StandardCellTypes.BANK_REWARD.name());
        //assert exception.
    }

    @Test
    public void payToBankCellsCouldBeCreatedFromGivenCellsAndDesiredType(){
        //given
        PayToBankCell payToBankCell = new PayToBankCell(100);
        BoardGamePlayer player = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT, "");
        Bank bank = new Bank(TestConstants.INITIAL_AMOUNT_OF_BANK);
        Map<String, Cell> map = new HashMap<>();
        map.put(StandardCellTypes.PAY_TO_BANK.name(), payToBankCell);
        BoardCellDuplicatingFactory factory = new StandardBoardCellDuplicatingFactory(map);
        
        //when
        Cell pay_to_bank = factory.createCell(StandardCellTypes.PAY_TO_BANK.name());
        pay_to_bank.execute(player, bank);

        //then
        Assert.assertNotEquals(pay_to_bank, payToBankCell);
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT - 100, player.getMoneyValue());
        Assert.assertEquals(TestConstants.INITIAL_AMOUNT_OF_BANK + 100, bank.getAvailableAmount());
    }

    @Test
    public void rentableCellsCouldBeCreatedFromGivenCellsAndDesiredType(){
        //given
        RentableCell rentableCell = new RentableCell(
                new RentableMemberbership[]{new RentableMemberbership("Test",100,10)}
        );
        BoardGamePlayer player = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT, "");
        Map<String, Cell> map = new HashMap<>();
        map.put(StandardCellTypes.RENT_ABLE.name(), rentableCell);
        BoardCellDuplicatingFactory factory = new StandardBoardCellDuplicatingFactory(map);

        //when
        Cell rent_able_cell = factory.createCell(StandardCellTypes.RENT_ABLE.name());
        ((RentableCell) rent_able_cell).purchase(player);

        //then
        Assert.assertNotEquals(rent_able_cell, rentableCell);
        Assert.assertEquals(player, ((RentableCell) rent_able_cell).getOwner());
        Assert.assertNull(rentableCell.getOwner());

    }

    @Test
    public void bankRewardCellsCouldBeCreatedFromGivenCellsAndDesiredType(){
        //given
        BankRewardCell bankRewardCell = new BankRewardCell(200);
        BoardGamePlayer player = new BoardGamePlayer(TestConstants.START_PLAYER_AMOUNT, "");
        Bank bank = new Bank(TestConstants.INITIAL_AMOUNT_OF_BANK);
        Map<String, Cell> map = new HashMap<>();
        map.put(StandardCellTypes.BANK_REWARD.name(), bankRewardCell);
        BoardCellDuplicatingFactory factory = new StandardBoardCellDuplicatingFactory(map);

        //when
        Cell bank_reward_cell = factory.createCell(StandardCellTypes.BANK_REWARD.name());
        bank_reward_cell.execute(player, bank);

        //then
        Assert.assertNotEquals(bank_reward_cell, bankRewardCell);
        Assert.assertEquals(TestConstants.START_PLAYER_AMOUNT + 200, player.getMoneyValue());
        Assert.assertEquals(TestConstants.INITIAL_AMOUNT_OF_BANK - 200, bank.getAvailableAmount());
    }

    @Test
    public void blankCellsCouldBeCreatedAsWellWithGivenType(){
        //given
        Map<String, Cell> map = new HashMap<>();
        BlankCell blankCell = new BlankCell();
        map.put(StandardCellTypes.BLANK.name(), blankCell);
        BoardCellDuplicatingFactory factory = new StandardBoardCellDuplicatingFactory(map);

        //when
        Cell cell = factory.createCell(StandardCellTypes.BLANK.name());

        //then
        Assert.assertNotEquals(cell, blankCell);
    }

}