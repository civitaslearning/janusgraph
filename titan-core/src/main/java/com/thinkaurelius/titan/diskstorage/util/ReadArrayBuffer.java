package com.thinkaurelius.titan.diskstorage.util;

import com.thinkaurelius.titan.diskstorage.ReadBuffer;

import java.nio.ByteBuffer;

/**
 * Implementation of {@link ReadBuffer} against a byte array.
 *
 * Note, that the position does not impact the state of the object. Meaning, equals, hashcode,
 * and compare ignore the position.
 *
 * @author Matthias Broecheler (me@matthiasb.com)
 */

public class ReadArrayBuffer extends StaticArrayBuffer implements ReadBuffer {

    public ReadArrayBuffer(byte[] array) {
        super(array);
    }

    ReadArrayBuffer(StaticArrayBuffer buffer) {
        super(buffer);
    }

    protected ReadArrayBuffer(byte[] array, int offset, int limit) {
        super(array,offset,limit);
    }

    @Override
    void reset(int newOffset, int newLimit) {
        position=0;
        super.reset(newOffset,newLimit);
    }

    /*
    ############ IDENTICAL CODE #############
     */


    private transient int position=0;

    private int updatePos(int update) {
        int pos = position;
        position+=update;
        return pos;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public boolean hasRemaining() {
        return position<length();
    }

    @Override
    public void movePositionTo(int newPosition) {
        assert newPosition >= -1 && newPosition <= length();
        position = newPosition;
    }

    @Override
    public byte getByte() {
        return getByte(updatePos(1));
    }

    @Override
    public short getShort() {
        return getShort(updatePos(2));
    }

    @Override
    public int getInt() {
        return getInt(updatePos(4));
    }

    @Override
    public long getLong() {
        return getLong(updatePos(8));
    }

    @Override
    public char getChar() {
        return getChar(updatePos(2));
    }

    @Override
    public float getFloat() {
        return getFloat(updatePos(4));
    }

    @Override
    public double getDouble() {
        return getDouble(updatePos(8));
    }

    @Override
    public<T> T asRelative(final Factory<T> factory) {
        if (position==0) return as(factory);
        else {
            return as(new Factory<T>() {
                @Override
                public T get(byte[] array, int offset, int limit) {
                    return factory.get(array,offset+position,limit);
                }
            });
        }
    }

    @Override
    public ReadBuffer subrange(int length, boolean invert) {
        return super.subrange(position,length,invert).asReadBuffer();
    }


}
