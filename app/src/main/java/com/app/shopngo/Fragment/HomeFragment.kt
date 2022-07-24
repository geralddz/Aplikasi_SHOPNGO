package com.app.shopngo.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.shopngo.R


class HomeFragment : Fragment() {
    private lateinit var scan: TextView
    private lateinit var profile: TextView
    private lateinit var history: TextView
    private lateinit var cart: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        scan = view.findViewById(R.id.tvscan)
        profile = view.findViewById(R.id.tvakun)
        history = view.findViewById(R.id.tvhistory)
        cart = view.findViewById(R.id.tvcart)

        scan.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, ScannerFragment())
                ?.commit()
        }

        profile.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, SettingFragment())
                ?.commit()
        }

        history.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, HistoryFragment())
                ?.commit()
        }

        cart.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, CartFragment())
                ?.commit()
        }

        return view
    }

}