package com.hslee.fingerprint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.hslee.fingerprint.databinding.ActivityCardViewBinding
import com.hslee.fingerprint.ui.theme.adapter.CardAdapter
import com.hslee.fingerprint.ui.theme.adapter.CardIndicatorAdapter
import com.hslee.fingerprint.ui.theme.model.CardIndicatorInfo
import com.hslee.fingerprint.ui.theme.model.CardInfo
import kotlin.math.abs

class CardViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCardViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var cardInfos = arrayListOf<CardInfo>()
        cardInfos.add(CardInfo(R.drawable.ic_launcher_foreground, "KBCard", "1234-****-****-5678"))
        cardInfos.add(CardInfo(R.drawable.ic_launcher_foreground, "현대카드", "5678-****-****-1234"))
        cardInfos.add(CardInfo(R.drawable.ic_launcher_foreground, "삼성카드", "4321-****-****-8765"))
        cardInfos.add(CardInfo(R.drawable.ic_launcher_foreground, "우리카드", "8765-****-****-4321"))

        val cardAdapter = CardAdapter(cardInfos)
        binding.viewPager.adapter = cardAdapter
        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        var cardIndicatorInfo = arrayListOf<CardIndicatorInfo>()
        for (i in 0 until cardInfos.size) {
            if (i == 0) {
                cardIndicatorInfo.add(CardIndicatorInfo(true))
            } else {
                cardIndicatorInfo.add(CardIndicatorInfo(false))
            }
        }

        val cardIndicatorAdapter = CardIndicatorAdapter(cardIndicatorInfo, this)
        binding.recyclerIndicator.adapter = cardIndicatorAdapter

        binding.viewPager.setPageTransformer { page, position ->
            val v = 1 - abs(position)
            val temp = 0.9f + v * 0.05f

            page.scaleY = temp
            page.elevation = temp
        }

        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                for (i in 0 until cardIndicatorInfo.size) {
                    cardIndicatorInfo[i].flag = i == position
                    cardIndicatorAdapter.notifyItemChanged(i)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }
}