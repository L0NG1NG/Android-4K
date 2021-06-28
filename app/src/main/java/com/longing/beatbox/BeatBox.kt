package com.longing.beatbox

import android.content.res.AssetManager
import android.util.Log

private const val TAG = "BeatBox"
private const val SOUND_FOLDER = "sample_sounds"

class BeatBox(private val assets: AssetManager) {
    val sounds: List<Sound>

    init {
        sounds = loadSounds()
    }

    private fun loadSounds(): List<Sound> {
        val soundNames: Array<String>
        try {
            soundNames = assets.list(SOUND_FOLDER)!!
            Log.d(TAG, "find: --->${soundNames.size}sounds")
            soundNames.asList()
        } catch (e: Exception) {
            Log.e(TAG, "could not loadSounds: -->", e)
            return emptyList()
        }
        val sounds = mutableListOf<Sound>()
        soundNames.forEach { fileName ->
            val assetPath = "$SOUND_FOLDER/$fileName"
            val sound = Sound(assetPath)
            sounds.add(sound)

        }
        return sounds

    }
}