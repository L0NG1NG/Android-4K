package com.longing.beatbox

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.SoundPool
import android.util.Log

private const val TAG = "BeatBox"
private const val SOUND_FOLDER = "sample_sounds"
private const val MAX_SOUNDS = 5

class BeatBox(private val assets: AssetManager) {

    val sounds: List<Sound>
    private val soundPool = SoundPool.Builder()
        .setMaxStreams(MAX_SOUNDS)
        .build()

    init {
        sounds = loadSounds()
    }

    fun play(sound: Sound) {
        sound.soundId?.let {
            soundPool.play(it, 1.0f, 1.0f, 1, 0, 1.0f)

        }
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

            loadSound(sound)
            sounds.add(sound) //说好的异常呢？


        }
        return sounds

    }

    fun release() {
        soundPool.release()
    }

    private fun loadSound(sound: Sound) {
        val afd: AssetFileDescriptor = assets.openFd(sound.assetPath)
        val soundId = soundPool.load(afd, 1)
        sound.soundId = soundId

    }
}