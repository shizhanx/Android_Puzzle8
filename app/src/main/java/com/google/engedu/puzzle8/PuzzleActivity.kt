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

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class PuzzleActivity : AppCompatActivity() {
    private val imageBitmap: Bitmap? = null
    private var boardView: PuzzleBoardView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle)

        // This code programmatically adds the PuzzleBoardView to the UI.
        val container = findViewById<View>(R.id.puzzle_container) as RelativeLayout
        boardView = PuzzleBoardView(this)
        // Some setup of the view.
        boardView!!.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        container.addView(boardView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_puzzle, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    fun dispatchTakePictureIntent(view: View?) {}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}
    fun shuffleImage(view: View?) {
        boardView!!.shuffle()
    }

    fun solve(view: View?) {
        boardView!!.solve()
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }
}