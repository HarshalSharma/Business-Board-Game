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

import com.harshalworks.businessbg.board.cell.BlankCell;
import com.harshalworks.businessbg.board.cell.Cell;
import com.harshalworks.businessbg.exceptions.CellTypeNotDefinedException;
import com.harshalworks.businessbg.exceptions.ExceptionMessageConstants;

import java.util.Map;

public abstract class BoardCellDuplicatingFactory<T> {

    private Map<T, Cell> cellReference;

    public BoardCellDuplicatingFactory(Map<T, Cell> cellReference) {
        this.cellReference = cellReference;
    }

    public Cell createCell(T cellType) {
        if (isBlankCell(cellType)) return new BlankCell();
        Cell cell = cellReference.get(cellType);
        if(cell == null)
            throw new CellTypeNotDefinedException(cellType.toString());
        return duplicateCell(cell, cellType);
    }

    protected abstract Cell duplicateCell(Cell cell, T cellType);

    protected boolean isBlankCell(T cell) {
        return cell == null;
    }
}
