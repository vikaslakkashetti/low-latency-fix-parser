package com.vikas.main;

/**
 * Lightweight, allocation-free view over a segment of an ASCII byte array.
 * <p>
 * This class is created to avoid string allocation of fix fields.
 * assumes ASCII input as per fix protocol.
 * It's backed directly by the original FIX message buffer and uses offset and length.
 * No copying of bytes and no string creation.
 * The hashing algorithm used is standard Java polynomial rolling hash (same as String.hashCode()) using multiplier 31
 * This also gives us flexibility to use other hashing algorihms if need be.
 */
public final class AsciiStringView implements CharSequence {

    private byte[] buffer;
    private int offset;
    private int length;

    /**
     * Wraps a region of the provided buffer without copying.
     */
    public void wrap(byte[] buffer, int offset, int length) {
        this.buffer = buffer;
        this.offset = offset;
        this.length = length;
    }

    @Override
    public int length() {
        return length;
    }

    /**
     * Returns character at index by directly accessing underlying byte array.
     * Assumes ASCII encoding.
     */
    @Override
    public char charAt(int index) {
        return (char) buffer[offset + index];
    }

    /**
     * Not supported to avoid additional object allocation.
     */
    @Override
    public CharSequence subSequence(int start, int end) {
        throw new UnsupportedOperationException();
    }

    /**
     * Allocates a String representation only when explicitly requested.
     * Use only for debugging purposes
     * Parsing itself does not trigger allocation.
     */
    @Override
    public String toString() {
        return new String(buffer, offset, length);
    }

    //Methods added to check equality.
    public boolean equalsAscii(String s) {
        if (s.length() != length) return false;
        for (int i = 0; i < length; i++) {
            if ((char) buffer[offset + i] != s.charAt(i))
                return false;
        }
        return true;
    }

    public boolean equalsAscii(byte[] other) {
        if (other.length != length) return false;

        for (int i = 0; i < length; i++) {
            if (buffer[offset + i] != other[i])
                return false;
        }
        return true;
    }

    public boolean equalsAscii(AsciiStringView other) {
        if (this.length != other.length)
            return false;

        for (int i = 0; i < length; i++) {
            if (this.buffer[this.offset + i] !=
                    other.buffer[other.offset + i])
                return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AsciiStringView)) return false;

        AsciiStringView other = (AsciiStringView) obj;

        if (this.length != other.length)
            return false;

        for (int i = 0; i < length; i++) {
            if (buffer[offset + i] !=
                    other.buffer[other.offset + i])
                return false;
        }
        return true;
    }


    @Override
    public int hashCode() {
        int h = 0;
        for (int i = 0; i < length; i++) {
            h = 31 * h + buffer[offset + i];
        }
        return h;
    }

}