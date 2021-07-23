package com.example.musicapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.musicapp.ui.fragments.AlbumFragment
import com.example.musicapp.ui.fragments.ArtistFragment
import com.example.musicapp.ui.fragments.SongFragment

class ViewPagerMusicAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val pageTitles = listOf("SONG", "ALBUM", "ARTIST")

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SongFragment.newInstance()
            1 -> AlbumFragment.newInstance()
            2 -> ArtistFragment.newInstance()
            else -> {
                return Fragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return pageTitles[position]
    }

    override fun getCount(): Int = 3

}