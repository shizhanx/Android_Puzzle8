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

class PuzzleTile(private val bitmap: Bitmap, val number: Int) {
    fun draw(canvas: Canvas, x: Int, y: Int) {
        canvas.drawBitmap(bitmap, x * bitmap.width.toFloat(), y * bitmap.height.toFloat(), null)
    }

    fun isClicked(clickX: Float, clickY: Float, tileX: Int, tileY: Int): Boolean {
        val tileX0 = tileX * bitmap.width
        val tileX1 = (tileX + 1) * bitmap.width
        val tileY0 = tileY * bitmap.width
        val tileY1 = (tileY + 1) * bitmap.width
        return clickX >= tileX0 && clickX < tileX1 && clickY >= tileY0 && clickY < tileY1
    }
}