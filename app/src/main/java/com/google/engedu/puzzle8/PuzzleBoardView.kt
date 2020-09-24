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

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import java.util.*

class PuzzleBoardView(context: Context?) : View(context) {
    private val activity: Activity? = context as Activity?
    private var puzzleBoard: PuzzleBoard? = null
    private var animation: ArrayDeque<PuzzleBoard>?
    fun initialize(imageBitmap: Bitmap) {
        val width = width
        puzzleBoard = PuzzleBoard(imageBitmap, width)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (puzzleBoard != null) {
            if (animation != null && animation!!.isNotEmpty()) {
                puzzleBoard = animation!!.poll()
                puzzleBoard!!.draw(canvas)
                if (animation!!.size == 0) {
                    animation = null
                    puzzleBoard!!.reset()
                    val toast = Toast.makeText(activity, "Solved! ", Toast.LENGTH_LONG)
                    toast.show()
                } else {
                    this.postInvalidateDelayed(500)
                }
            } else {
                puzzleBoard!!.draw(canvas)
            }
        }
    }

    fun shuffle() {
        if (animation == null && puzzleBoard != null) {
            for (step in 1..NUM_SHUFFLE_STEPS) {
                puzzleBoard = puzzleBoard!!.neighbours().random()
            }
            puzzleBoard!!.reset()
            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (animation == null && puzzleBoard != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> if (puzzleBoard!!.click(event.x, event.y)) {
                    invalidate()
                    if (puzzleBoard!!.resolved()) {
                        val toast = Toast.makeText(activity, "Congratulations!", Toast.LENGTH_LONG)
                        toast.show()
                    }
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    fun solve() {}

    companion object {
        const val NUM_SHUFFLE_STEPS = 40
    }

    init {
        animation = null
    }
}