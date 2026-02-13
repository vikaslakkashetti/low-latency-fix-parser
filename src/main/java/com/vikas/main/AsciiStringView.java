package com.vikas.main;

/**
 * Lightweight, allocation-free view over a segment of an ASCII byte array.
 *
 * This class is created to avoid string allocation of fix fields.
 * assumes ASCII input as per fix protocol.
 * It's backed directly by the original FIX message buffer and uses offset and length.
 * No copying of bytes and no string creation.
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
}