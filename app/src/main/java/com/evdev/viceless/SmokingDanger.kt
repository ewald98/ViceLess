package com.evdev.viceless

data class SmokingDanger (
    val dangerText: String,
    val icon: Int
)

object Supplier {

    // TODO: add onclick redirect to a url?
    val smokingDangers = listOf<SmokingDanger>(
        SmokingDanger("Smoking can affect sperm quality and decreases fertility",
            R.drawable.smoking_danger_icon1),
        SmokingDanger("Smoking clogs the arteries and causes heart attacks and strokes",
            R.drawable.smoking_danger_icon2),
        SmokingDanger("Smoking has an indirect relation with hearing loss",
            R.drawable.smoking_danger_icon3),
        SmokingDanger("Smoking causes impotence in men",
            R.drawable.smoking_danger_icon4),
        SmokingDanger("Cigarette smoke contains benzene, nitrosamines, formaldehyde and hydrogen cyanide.",
            R.drawable.smoking_danger_icon5),
        SmokingDanger("Smoking when pregnant harms your baby",
            R.drawable.smoking_danger_icon6),
        SmokingDanger("Smoking causes tooth decay",
            R.drawable.smoking_danger_icon7),
        SmokingDanger("Smoking causes bad breath",
            R.drawable.smoking_danger_icon8),
        SmokingDanger("Smoking contributes to the development of arteriosclerosis or hardening of the arteries, that can contribute to or worsen vascular disease of the eyes",
            R.drawable.smoking_danger_icon9),
        SmokingDanger("Cigars Are Not A Safe Alternative To Cigarettes",
            R.drawable.smoking_danger_icon10),
        SmokingDanger("Smoking reduces blood circulation",
            R.drawable.smoking_danger_icon2),
        SmokingDanger("Cigarette smoke also harms those who don't smoke",
            R.drawable.smoking_danger_icon5),
        SmokingDanger("Smoking causes lung cancer",
            R.drawable.smoking_danger_icon10)
    )
}