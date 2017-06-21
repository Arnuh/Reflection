/*
 *  CaptainBern-Reflection-Framework contains several utils and tools
 *  to make Reflection easier.
 *  Copyright (C) 2014  CaptainBern
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.captainbern.jbel.commons.attribute.annotation.elementvalue;

import com.captainbern.jbel.ConstantPool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EnumElementValue extends ElementValue {

    private int typeIndex;
    private int valueIndex;

    public EnumElementValue(EnumElementValue enumElementValue) {
        this(enumElementValue.getTypeIndex(), enumElementValue.getValueIndex(), enumElementValue.getConstantPool());
    }

    public EnumElementValue(DataInputStream codeStream, ConstantPool constantPool) throws IOException {
        this(codeStream.readUnsignedShort(), codeStream.readUnsignedShort(), constantPool);
    }

    public EnumElementValue(int typeIndex, int valueIndex, ConstantPool constantPool) {
        super(TYPE_ENUM, constantPool);
        this.typeIndex = typeIndex;
        this.valueIndex = valueIndex;
    }

    public int getTypeIndex() {
        return this.typeIndex;
    }

    public void setTypeIndex(int typeIndex) {
        this.typeIndex = typeIndex;
    }

    public int getValueIndex() {
        return this.valueIndex;
    }

    public void setValueIndex(int valueIndex) {
        this.valueIndex = valueIndex;
    }

    @Override
    public void write(DataOutputStream codeStream) throws IOException {
        super.write(codeStream);
        codeStream.writeShort(this.typeIndex);
        codeStream.writeShort(this.valueIndex);
    }
}
