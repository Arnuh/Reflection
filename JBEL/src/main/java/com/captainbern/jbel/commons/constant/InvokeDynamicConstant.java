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

package com.captainbern.jbel.commons.constant;

import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;

public class InvokeDynamicConstant extends Constant {

    private int bootstrap;
    private int nameAndType;

    public InvokeDynamicConstant(InvokeDynamicConstant constant) {
        this(constant.getBootstrap(), constant.getNameAndType());
    }

    public InvokeDynamicConstant(DataInput stream) throws IOException {
        this(stream.readUnsignedShort(), stream.readUnsignedShort());
    }

    public InvokeDynamicConstant(int bootstrap, int nameAndType) {
        super(CONSTANT_InvokeDynamic);
        this.bootstrap = bootstrap;
        this.nameAndType = nameAndType;
    }

    public int getBootstrap() {
        return this.bootstrap;
    }

    public int getNameAndType() {
        return this.nameAndType;
    }

    @Override
    public void write(DataOutputStream codeStream) throws IOException {
        codeStream.writeByte(this.tag);
        codeStream.writeShort(this.bootstrap);
        codeStream.writeShort(this.nameAndType);
    }
}
