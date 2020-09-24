/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.engedu.puzzle8

import android.graphics.Bitmap
import android.graphics.Canvas
import java.util.*

class PuzzleBoard {
    private var tiles: ArrayList<PuzzleTile?>? = null

    internal constructor(bitmap: Bitmap?, parentWidth: Int) {}
    internal constructor(otherBoard: PuzzleBoard) {
        tiles = otherBoard.tiles!!.clone() as ArrayList<PuzzleTile?>
    }

    fun reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
    }

    override fun equals(o: Any?): Boolean {
        return if (o == null) false else tiles == (o as PuzzleBoard).tiles
    }

    fun draw(canvas: Canvas?) {
        if (tiles == null) {
            return
        }
        for (i in 0 until NUM_TILES * NUM_TILES) {
            val tile = tiles!![i]
            tile?.draw(canvas, i % NUM_TILES, i / NUM_TILES)
        }
    }

    fun click(x: Float, y: Float): Boolean {
        for (i in 0 until NUM_TILES * NUM_TILES) {
            val tile = tiles!![i]
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES)
                }
            }
        }
        return false
    }

    private fun tryMoving(tileX: Int, tileY: Int): Boolean {
        for (delta in NEIGHBOUR_COORDS) {
            val nullX = tileX + delta[0]
            val nullY = tileY + delta[1]
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES && tiles!![XYtoIndex(nullX, nullY)] == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY))
                return true
            }
        }
        return false
    }

    fun resolved(): Boolean {
        for (i in 0 until NUM_TILES * NUM_TILES - 1) {
            val tile = tiles!![i]
            if (tile == null || tile.number != i) return false
        }
        return true
    }

    private fun XYtoIndex(x: Int, y: Int): Int {
        return x + y * NUM_TILES
    }

    protected fun swapTiles(i: Int, j: Int) {
        val temp = tiles!![i]
        tiles!![i] = tiles!![j]
        tiles!![j] = temp
    }

    fun neighbours(): ArrayList<PuzzleBoard>? {
        return null
    }

    fun priority(): Int {
        return 0
    }

    companion object {
        private const val NUM_TILES = 3
        private val NEIGHBOUR_COORDS = arrayOf(intArrayOf(-1, 0), intArrayOf(1, 0), intArrayOf(0, -1), intArrayOf(0, 1))
    }
}