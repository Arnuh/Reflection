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

package com.captainbern.jbel.commons.attribute;

import com.captainbern.jbel.ConstantPool;
import com.captainbern.jbel.Opcode;
import com.captainbern.jbel.commons.constant.ClassConstant;
import com.captainbern.jbel.commons.constant.DescriptorConstant;
import com.captainbern.jbel.commons.exception.ClassFormatException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Represents an EnclosingMethod attribute.
 *
 * @see <a href="http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.7">EnclosingMethod</a>
 */
public class EnclosingMethod extends Attribute implements Opcode {

    private int classIndex;
    private int methodIndex;

    public EnclosingMethod(EnclosingMethod enclosingMethod) {
        this(enclosingMethod.getNameIndex(), enclosingMethod.getLength(), enclosingMethod.getClassIndex(), enclosingMethod.getMethodIndex(), enclosingMethod.getConstantPool());
    }

    public EnclosingMethod(int index, int length, DataInputStream codeStream, ConstantPool constantPool) throws IOException {
        this(index, length, codeStream.readUnsignedShort(), codeStream.readUnsignedShort(), constantPool);
    }

    public EnclosingMethod(int index, int length, int classIndex, int methodIndex, ConstantPool constantPool) {
        super(ATTR_ENCLOSING_METHOD, index, length, constantPool);
        this.classIndex = classIndex;
        this.methodIndex = methodIndex;
    }

    public int getClassIndex() {
        return this.classIndex;
    }

    public void setClassIndex(int classIndex) {
        this.classIndex = classIndex;
    }

    public int getMethodIndex() {
        return this.methodIndex;
    }

    public void setMethodIndex(int methodIndex) {
        this.methodIndex = methodIndex;
    }

    public ClassConstant getClassConstant() throws ClassFormatException {
        ClassConstant constant = (ClassConstant) this.constantPool.getConstant(this.classIndex, CONSTANT_Class);
        return constant;
    }

    public DescriptorConstant getMethodNameAndTypeConstant() throws ClassFormatException {
        DescriptorConstant constant = (DescriptorConstant) this.constantPool.getConstant(this.methodIndex, CONSTANT_NameAndType);
        return constant;
    }

    @Override
    public void write(DataOutputStream codeStream) throws IOException {
        super.write(codeStream);
        codeStream.writeShort(this.classIndex);
        codeStream.writeShort(this.methodIndex);
    }
}
