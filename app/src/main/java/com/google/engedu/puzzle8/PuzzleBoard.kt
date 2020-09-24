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
import androidx.core.graphics.scale
import java.util.*
import kotlin.math.min

class PuzzleBoard {
    private var tiles: MutableList<PuzzleTile?> = MutableList(NUM_TILES * NUM_TILES) { null }

    internal constructor(bitmap: Bitmap, parentWidth: Int) {
        val lengthOfSide = min(bitmap.height, bitmap.width)
        val squareBitmap = Bitmap.createBitmap(bitmap, 0, 0, lengthOfSide, lengthOfSide)
        val scaledBitmap = Bitmap.createScaledBitmap(squareBitmap, parentWidth, parentWidth, true)
        val tileWidth = parentWidth / NUM_TILES
        for (i in 0 until tiles.size - 1) {
            val slice = Bitmap.createBitmap(scaledBitmap, (i % NUM_TILES) * tileWidth, (i / NUM_TILES) * tileWidth, tileWidth, tileWidth)
            tiles[i] = PuzzleTile(slice, i)
        }
    }

    internal constructor(otherBoard: PuzzleBoard) {
        tiles = otherBoard.tiles.toMutableList()
    }

    fun reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
    }

    override fun equals(other: Any?): Boolean {
        return if (other == null) false else tiles == (other as PuzzleBoard).tiles
    }

    fun draw(canvas: Canvas) {
        for (i in 0 until NUM_TILES * NUM_TILES) {
            val tile = tiles[i]
            tile?.draw(canvas, i % NUM_TILES, i / NUM_TILES)
        }
    }

    fun click(x: Float, y: Float): Boolean {
        for (i in 0 until NUM_TILES * NUM_TILES) {
            val tile = tiles[i]
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
            if (nullX in 0 until NUM_TILES && nullY in 0 until NUM_TILES && tiles[XYtoIndex(nullX, nullY)] == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY))
                return true
            }
        }
        return false
    }

    fun resolved(): Boolean {
        for (i in 0 until NUM_TILES * NUM_TILES - 1) {
            val tile = tiles[i]
            if (tile == null || tile.number != i) return false
        }
        return true
    }

    private fun XYtoIndex(x: Int, y: Int): Int {
        return if (x in 0 until NUM_TILES && y in 0 until NUM_TILES) {
            x + y * NUM_TILES
        } else -1
    }

    private fun swapTiles(i: Int, j: Int) {
        val temp = tiles[i]
        tiles[i] = tiles[j]
        tiles[j] = temp
    }

    fun neighbours(): List<PuzzleBoard> {
        val nullIndex = tiles.indexOf(null)
        val x = nullIndex % NUM_TILES
        val y = nullIndex / NUM_TILES
        val ans = mutableListOf<PuzzleBoard>()
        for (coordinate in NEIGHBOUR_COORDS) {
            val neighbourIndex = XYtoIndex(x + coordinate[0], y + coordinate[1])
            if (neighbourIndex >= 0) {
                val neighbour = PuzzleBoard(this)
                neighbour.swapTiles(nullIndex, neighbourIndex)
                ans.add(neighbour)
            }
        }
        return ans.toList()
    }

    fun priority(): Int {
        return 0
    }

    override fun hashCode(): Int {
        return tiles.hashCode() ?: 0
    }

    companion object {
        private const val NUM_TILES = 3
        private val NEIGHBOUR_COORDS = arrayOf(intArrayOf(-1, 0), intArrayOf(1, 0), intArrayOf(0, -1), intArrayOf(0, 1))
    }
}