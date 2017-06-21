package com.captainbern.reflection.provider.type;

import com.captainbern.reflection.Reflection;
import com.captainbern.reflection.SafeField;

import java.lang.reflect.Field;

public interface FieldProvider<T> {

    public Reflection getReflection();

    public <T> SafeField<T> asSafeField();

    public Field reflectedField();
}
