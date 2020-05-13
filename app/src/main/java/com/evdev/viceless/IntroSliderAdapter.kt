package com.evdev.viceless

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class IntroSliderAdapter(private val introSlides: List<IntroSlide>) :
    RecyclerView.Adapter<IntroSliderAdapter.IntroSlideViewHolder>() {

    private lateinit var _retData: Array<String>

    init {
        _retData = Array<String>(introSlides.size, {i -> ""})
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
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                _retData.set(position, s.toString())
            }

        })
    }

    fun retrieveData(): Array<String> {
        return _retData
    }

}