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

package com.captainbern.reflection;

/**
 * A fairly small utility class which allows one to dynamically add values to an Enum
 * @param <T>
 */
public interface EnumModifier<T extends Enum<T>> {

    /**
     * Allows one to dynamically create a new instance of a given enum class
     * @param name
     * @param args
     */
    public T addEnumValue(String name, Object... args);

    /**
     * Returns the enum type
     * @return
     */
    public Class<T> getType();

}
