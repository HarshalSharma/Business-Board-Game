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

package com.harshalworks.businessbg.board;

import com.harshalworks.businessbg.board.cell.Cell;
import com.harshalworks.businessbg.exceptions.CannotInitializeBoardException;
import com.harshalworks.businessbg.exceptions.InvalidBoardPositionException;
import com.harshalworks.businessbg.exceptions.MessageConstants;
import com.harshalworks.businessbg.rules.Rule;

public class Board {

    private final Cell[] cells;

    public Board(final Cell[] cells) {
        validateBoardLengthIsNonZero(cells);

        this.cells = cells;
    }

    private void validateBoardLengthIsNonZero(Cell[] cells) {
        if(cells == null || cells.length == 0)
            throw new CannotInitializeBoardException(MessageConstants.GIVEN_BOARD_LENGTH_IS_ZERO);
    }

    public int getBoardLength() {
        return cells.length;
    }

    public Rule getCellRule(int position) {
        Rule rule;
        try{
            rule = cells[position].getRule();
        }catch (ArrayIndexOutOfBoundsException e){
            throw new InvalidBoardPositionException(position, cells.length);
        }
        return rule;
    }
}