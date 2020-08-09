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

import com.harshalworks.businessbg.board.cell.*;

import java.util.Map;

public class StandardBoardCellDuplicatingFactory extends BoardCellDuplicatingFactory<StandardCellTypes> {

    public StandardBoardCellDuplicatingFactory(Map<StandardCellTypes, Cell> cellReference) {
        super(cellReference);
    }

    @Override
    protected Cell duplicateCell(Cell cell, StandardCellTypes cellType) {
        switch (cellType){
            case PAY_TO_BANK:
                return new PayToBankCell((PayToBankCell) cell);
            case BANK_REWARD:
                return new BankRewardCell((BankRewardCell) cell);
            case RENT_ABLE:
                return new RentableCell((RentableCell) cell);
        }
        return new BlankCell();
    }
}
