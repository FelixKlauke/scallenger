/*
 * MIT License
 *
 * Copyright (c) 2017 Felix Klauke
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
 */

package de.felix_klauke.scallenger.accessor

import sun.misc.Unsafe

import java.nio.Buffer

/**
 * Unsafe memory accessor.
 *
 * @author Felix Klauke <fklauke></fklauke>@itemis.de>
 */
class MemoryAccessor private constructor(private val unsafe: Unsafe, private val offset: Long) {

    fun compareAndSwapInt(any: Any? = null, position: Long, compare0: Int, compare1: Int) : Boolean {
        return unsafe.compareAndSwapInt(any, position, compare0, compare1)
    }

    fun getByte(l: Long): Byte {
        return unsafe.getByte(l)
    }

    fun putByte(l: Long, b: Byte) {
        unsafe.putByte(l, b)
    }

    fun getInt(l: Long): Int {
        return unsafe.getInt(l)
    }

    fun putInt(l: Long, i: Int) {
        unsafe.putInt(l, i)
    }

    fun fetchAddress(buffer: Buffer): Long {
        return unsafe.getLong(buffer, offset)
    }

    /**
     * Companion object for factory methods.
     */
    companion object {

        fun createAccessor(): MemoryAccessor? {
            try {
                val field = Unsafe::class.java.getDeclaredField("theUnsafe")
                field.isAccessible = true
                val unsafe = field.get(null) as Unsafe

                return createAccessor(unsafe)
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

            return null
        }

        private fun createAccessor(unsafe: Unsafe): MemoryAccessor {
            var offset: Long = 0

            try {
                offset = unsafe.objectFieldOffset(Buffer::class.java.getDeclaredField("address"))
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }

            return MemoryAccessor(unsafe, offset)
        }
    }
}
