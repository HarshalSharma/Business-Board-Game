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

package com.harshalworks.businessbg.bank;

import com.harshalworks.businessbg.dealers.MarketAssistant;
import com.harshalworks.businessbg.dealers.PropertyAck;

public class Bank implements MarketAssistant {

    private int bankMoneyValue;

    public Bank(int initialBankAmount) {
        this.bankMoneyValue = initialBankAmount;
    }

    public int getAvailableAmount() {
        return bankMoneyValue;
    }

    @Override
    public void addMoney(int amount) {
        bankMoneyValue += amount;
    }

    @Override
    public void deductMoney(int amount) {
        bankMoneyValue -= amount;
    }

    @Override
    public void addProperty(PropertyAck propertyDetails) {
        bankMoneyValue += propertyDetails.getPropertyValue();
    }

    @Override
    public boolean haveAvailableAmount(int amount) {
        return amount <= bankMoneyValue;
    }
}
