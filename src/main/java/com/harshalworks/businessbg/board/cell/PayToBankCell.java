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

import com.harshalworks.businessbg.dealers.MarketAssistant;
import com.harshalworks.businessbg.player.BoardGamePlayer;

/**
 *
 * When any player lands on the jail cell on the board they need to pay an amount to the bank.
 *
 * Acting as a reciever for the Rules
 */
public class PayToBankCell extends Cell {

    private final int amount;

    public PayToBankCell(final int amount) {
        this.amount = amount;
    }

    /**
     * Copy Constructor
     * @param payToBankCell  existing pay to bank cell to duplicate from.
     */
    public PayToBankCell(final PayToBankCell payToBankCell) {
        this.amount = payToBankCell.amount;
    }

    @Override
    public void execute(BoardGamePlayer player, MarketAssistant bank) {
        bank.addMoney(amount);
        player.deductMoney(amount);
    }
}
