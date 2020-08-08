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

import com.harshalworks.businessbg.board.Asset;
import com.harshalworks.businessbg.dealers.MarketAssistant;
import com.harshalworks.businessbg.dealers.Payee;
import com.harshalworks.businessbg.exceptions.CannotPurchaseThisAsset;
import com.harshalworks.businessbg.player.BoardGamePlayer;

public class RentableCell extends Cell implements Asset {

    private MarketAssistant owner;
    private final RentableMemberbership[] rentableMemberberships;
    private int membershipStatus = -1;

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

    @Override
    public void purchase(MarketAssistant customer) {
        if(this.owner != null && customer != owner)
            throw new CannotPurchaseThisAsset("Purchase/Upgrade not allowed.");
        if(this.owner == null) {
            this.owner = customer;
            membershipStatus = 0;
            return;
        }
        upgradeMembership();
    }

    protected void upgradeMembership() {
        checkIfUpgradePossible();
        membershipStatus++;
    }

    @Override
    public int getCost() {
        if(owner == null)
            return rentableMemberberships[0].getCost();

        checkIfUpgradePossible();

        return rentableMemberberships[membershipStatus+1].getCost()
                - rentableMemberberships[membershipStatus].getCost();
    }

    protected boolean checkIfUpgradePossible() {
        if(membershipStatus + 1 >= rentableMemberberships.length)
            throw new CannotPurchaseThisAsset("No more memberships available");
        return true;
    }

    public RentableMemberbership getMembershipStatus() {
        return rentableMemberberships[membershipStatus];
    }
}
