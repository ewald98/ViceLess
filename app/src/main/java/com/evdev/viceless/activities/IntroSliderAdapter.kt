package com.evdev.viceless.activities

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.evdev.viceless.R


class IntroSliderAdapter(private val introSlides: List<IntroSlide>) :
    RecyclerView.Adapter<IntroSliderAdapter.IntroSlideViewHolder>() {

    private var _retData: Array<String>

    init {
        _retData = Array(introSlides.size, { i -> ""})
    }

    inner class IntroSlideViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val textQuestion = view.findViewById<TextView>(R.id.textQuestion)
        private val imageIcon = view.findViewById<ImageView>(R.id.imageSlideIcon)
        private val answer = view.findViewById<EditText>(R.id.answer)

        fun bind(introSlide: IntroSlide) {
            textQuestion.text = introSlide.question
            imageIcon.setImageResource(introSlide.icon)
        }

        fun getAnswer(): EditText {
            return answer
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSlideViewHolder {


        return IntroSlideViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.slide_item_container,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return introSlides.size
    }

    override fun onBindViewHolder(holder: IntroSlideViewHolder, position: Int) {
        holder.bind(introSlides[position])

        holder.getAnswer().addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                _retData[position] = s.toString()
                Log.d("Data get from layouts", "Answer 1 "+ _retData[0] + "Answer 2 "+ _retData[1] + "Answer 3 "+ _retData[2] ) //data check in logs

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    fun retrieveData(): Array<String> {
        return _retData
    }

}