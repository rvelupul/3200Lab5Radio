package com.radio

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.radio.data.RadioStation
import com.radio.databinding.RadioStationItemBinding

class RadioStationsAdapter(
    private var list: MutableList<RadioStation>,
    private val context: Context,
    private val onPlayPauseClick: (RadioStation) -> Unit
) :
    RecyclerView.Adapter<RadioStationsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RadioStationItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel: RadioStation = list[position]
        holder.bind(dataModel, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setSmsList(items: List<RadioStation>) {
        list = items.toMutableList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(var itemRowBinding: RadioStationItemBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {

        fun bind(station: RadioStation, position: Int) {

            itemRowBinding.apply {

                Glide.with(context).load(station.radioIcon).into(ivRadioIcon)

                tvRadioName.text = station.radioName

                if(station.playing){
                    btnPlayPause.setBackground(AppCompatResources.getDrawable(context, R.drawable.baseline_pause_circle_24))
                }else{
                    btnPlayPause.setBackground(AppCompatResources.getDrawable(context, R.drawable.baseline_play_circle_24))
                }


                btnPlayPause.setOnClickListener {
                    station.playing = !station.playing
                    if(station.playing){
                        btnPlayPause.background = AppCompatResources.getDrawable(context, R.drawable.baseline_pause_circle_24)
                    }else{
                        btnPlayPause.background = AppCompatResources.getDrawable(context, R.drawable.baseline_play_circle_24)
                    }

                    for(radioStation in list){
                        if(station.radioStreamUrl != radioStation.radioStreamUrl) {
                            radioStation.playing = false
                        }
                    }


                    notifyDataSetChanged()

                    onPlayPauseClick(station)
                }
            }
        }
    }
}
