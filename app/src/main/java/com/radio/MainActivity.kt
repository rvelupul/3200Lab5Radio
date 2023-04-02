package com.radio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.radio.data.RadioStation
import com.radio.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var stations = mutableListOf<RadioStation>()

    lateinit var adapter: RadioStationsAdapter

    var player: MediaPlayer? = null

    private var audioManager: AudioManager? = null

    private var muted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        setupRadioStations()
        setVolumeControls()
    }

    private fun setupRadioStations() {

        val station1 = RadioStation(
            radioStreamUrl = "http://stream.live.vc.bbcmedia.co.uk/bbc_radio_one",
            radioName = "BBC Radio 1",
            radioIcon = ContextCompat.getDrawable(this, R.drawable.station_1)
        )
        val station2 = RadioStation(
            radioStreamUrl = "http://stream.live.vc.bbcmedia.co.uk/bbc_1xtra",
            radioName = "BBC Radio 1 Extra",
            radioIcon = ContextCompat.getDrawable(this, R.drawable.station_2)
        )
        val station3 = RadioStation(
            radioStreamUrl = "http://stream.live.vc.bbcmedia.co.uk/bbc_radio_two",
            radioName = "BBC Radio 2",
            radioIcon = ContextCompat.getDrawable(this, R.drawable.station_3)
        )
        val station4 = RadioStation(
            radioStreamUrl = "http://stream.live.vc.bbcmedia.co.uk/bbc_radio_three",
            radioName = "BBC Radio 3",
            radioIcon = ContextCompat.getDrawable(this, R.drawable.station_4)
        )
        val station5 = RadioStation(
            radioStreamUrl = "http://stream.live.vc.bbcmedia.co.uk/bbc_radio_fourfm",
            radioName = "BBC Radio 4",
            radioIcon = ContextCompat.getDrawable(this, R.drawable.station_5)
        )
        val station6 = RadioStation(
            radioStreamUrl = "http://stream.live.vc.bbcmedia.co.uk/bbc_radio_fourlw",
            radioName = "BBC Radio 4 LW",
            radioIcon = ContextCompat.getDrawable(this, R.drawable.station_6)
        )
        val station7 = RadioStation(
            radioStreamUrl = "http://stream.live.vc.bbcmedia.co.uk/bbc_radio_four_extra",
            radioName = "BBC Radio 4 Extra",
            radioIcon = ContextCompat.getDrawable(this, R.drawable.station_7)
        )
        val station8 = RadioStation(
            radioStreamUrl = "http://stream.live.vc.bbcmedia.co.uk/bbc_radio_five_live",
            radioName = "BBC Radio 5 Live",
            radioIcon = ContextCompat.getDrawable(this, R.drawable.station_8)
        )
        val station9 = RadioStation(
            radioStreamUrl = "http://stream.live.vc.bbcmedia.co.uk/bbc_radio_five_live_sports_extra",
            radioName = "BBC Radio 5 Sport Extra",
            radioIcon = ContextCompat.getDrawable(this, R.drawable.station_9)
        )
        val station10 = RadioStation(
            radioStreamUrl = "http://stream.live.vc.bbcmedia.co.uk/bbc_6music",
            radioName = "BBC Radio 6 Music",
            radioIcon = ContextCompat.getDrawable(this, R.drawable.station_10)
        )

        stations.add(station1)
        stations.add(station2)
        stations.add(station3)
        stations.add(station4)
        stations.add(station5)
        stations.add(station6)
        stations.add(station7)
        stations.add(station8)
        stations.add(station9)
        stations.add(station10)


        adapter = RadioStationsAdapter(
            stations, this,
            onPlayPauseClick = { station ->

                if (player?.isPlaying == true) {
                    player?.stop()
                }

                if (station.playing) {
                    Handler(Looper.getMainLooper()).post {
                        initializeMediaPlayer(station.radioStreamUrl)
                    }
                } else {
                    player?.pause()
                }

            }
        )
        binding.rvRadioStations.adapter = adapter
    }

    private fun initializeMediaPlayer(url: String) {
        player = MediaPlayer()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            player?.setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build()
            )
        } else {
            player?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }
        try {
            player?.setOnPreparedListener(OnPreparedListener { mp -> mp.start() })
            player?.setDataSource(url)
            player?.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setVolumeControls() {
        binding.apply {

            btnVolumeDown.setOnClickListener {
                audioManager?.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
            }

            btnVolumeUp.setOnClickListener {
                audioManager?.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            }

            btnMute.setOnClickListener {
                muted = !muted

                if (muted) {
                    player?.setVolume(0f, 0f)
                    btnMute.text = "Unmute"
                } else {
                    player?.setVolume(1f, 1f)
                    btnMute.text = "Mute"
                }
            }

        }
    }

}