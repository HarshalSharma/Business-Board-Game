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
import com.harshalworks.businessbg.dealers.Payee;
import com.harshalworks.businessbg.exceptions.CannotPurchaseThisCell;
import com.harshalworks.businessbg.player.BoardGamePlayer;

public class RentableCell extends Cell {

    private MarketAssistant owner;
    private final RentableMemberbership[] rentableMemberberships;
    private int membershipStatus = 0;

    public RentableCell(RentableMemberbership[] memberberships) {
        this.rentableMemberberships = memberberships;
    }

    @Override
    public void execute(BoardGamePlayer player, MarketAssistant bank) {
        if(owner!=null) {
            payRent(player);
        }
    }

    protected void payRent(BoardGamePlayer player) {
        int rent = rentableMemberberships[membershipStatus].getRent();
        owner.addMoney(rent);
        player.deductMoney(rent);
    }

    public Payee getOwner() {
        return owner;
    }

    public void purchase(MarketAssistant customer) {
        if(this.owner != null)
            throw new CannotPurchaseThisCell("");

        this.owner = customer;
        customer.deductMoney(rentableMemberberships[0].getCost());
    }

    public boolean upgradeMembership(MarketAssistant customer) {
        if(customer != owner)
            throw new CannotPurchaseThisCell("Upgrade not allowed.");

        if(checkIfUpgradePossible())
            return false;

        if(askIfCustomerHaveAvailableAmount(customer)){
            allotNewMembership(customer);
            return true;
        }

        return false;
    }

    protected void allotNewMembership(MarketAssistant customer) {
        customer.deductMoney(rentableMemberberships[membershipStatus+1].getCost()
                - rentableMemberberships[membershipStatus].getCost());
        membershipStatus++;
    }

    protected boolean askIfCustomerHaveAvailableAmount(MarketAssistant customer) {
        return customer.haveAvailableAmount(rentableMemberberships[membershipStatus+1].getCost() - rentableMemberberships[membershipStatus].getCost());
    }

    protected boolean checkIfUpgradePossible() {
        return membershipStatus + 1 == rentableMemberberships.length;
    }

    public RentableMemberbership getMembershipStatus() {
        return rentableMemberberships[membershipStatus];
    }
}
