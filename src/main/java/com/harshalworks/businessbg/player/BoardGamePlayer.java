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

import com.harshalworks.businessbg.Player;
import com.harshalworks.businessbg.dealers.MarketAssistant;
import com.harshalworks.businessbg.dealers.Payee;
import com.harshalworks.businessbg.dealers.Spender;

import java.util.Objects;

public class BoardGamePlayer implements Player, MarketAssistant {

    private int moneyValue;
    private final String uniqueName;
    private int currentPosition;

    public BoardGamePlayer(final int moneyValue, final String uniqueName) {
        this.moneyValue = moneyValue;
        this.uniqueName = uniqueName;
    }

    @Override
    public int getMoneyValue() {
        return moneyValue;
    }

    @Override
    public String getUniqueName() {
        return uniqueName;
    }

    @Override
    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardGamePlayer that = (BoardGamePlayer) o;
        return uniqueName.equals(that.uniqueName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueName);
    }

    public void setPosition(int position) {
        this.currentPosition = position;
    }

    @Override
    public void addMoney(int amount) {
        moneyValue += amount;
    }

    @Override
    public void deductMoney(int amount) {
        moneyValue -= amount;
    }

    @Override
    public boolean haveAvailableAmount(int amount) {
        return moneyValue >= amount;
    }
}
