package com.agrongemajli.cse3200_midterm_a.ui.notifications

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.agrongemajli.cse3200_midterm_a.databinding.FragmentNotificationsBinding
import com.agrongemajli.cse3200_midterm_a.ui.home.HomeViewModel
import java.util.concurrent.TimeUnit

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    private var timerStarted: Boolean = false

    //we will be using handlers here, jsut to make it easier.
    private var watchHandler: Handler? = null

    private lateinit var notificationsViewModel: NotificationsViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboardTime
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val startButton: Button = binding.startButton
        startButton.setOnClickListener( View.OnClickListener {
            startTheTimer()
            Log.i("GEMA", "Started Timer " + textView.text.toString())
        })

        val stopButton: Button = binding.stopButton
        stopButton.setOnClickListener(View.OnClickListener {
            stopWatch()
            Log.i("GEMA", "Stopped Timer " + textView.text.toString())
        })

        val resetButton: Button = binding.resetButton
        resetButton.setOnClickListener( View.OnClickListener {
            resetWatch()
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startClicked(){
        //if timer not started, start the timer
        if(!timerStarted){
            startTheTimer()
        }

    }


    private fun startTheTimer(){
        watchHandler = Handler(Looper.getMainLooper())
        watchChecker.run()

    }

    private var watchChecker: Runnable = object : Runnable {
        override fun run() {
            try{
                notificationsViewModel.timeSinceStart += 1
                updateStopWatch()
            } finally {
                //this will run the handler after every second. It sets the callbacks so that when we stop the clock it will stop.
                watchHandler!!.postDelayed(this, "1000".toLong())
            }
        }
    }

    private fun updateStopWatch(){
        Log.e("time", notificationsViewModel.timeSinceStart.toString())
        val updatedWatch = convertTime(notificationsViewModel.timeSinceStart.toInt())
        notificationsViewModel.text.observe(viewLifecycleOwner){
            binding.textDashboardTime.text = updatedWatch
        }
    }

    private fun stopWatch() {
        //this will stop the stopwatch
        watchHandler?.removeCallbacks(watchChecker)
    }

    private fun convertTime(timeInSec: Int): String {
        //this will return the proper time string
        var finalTime = ""
        val hours = TimeUnit.SECONDS.toHours(timeInSec.toLong())
        val minutes = TimeUnit.SECONDS.toMinutes(timeInSec.toLong())
        val seconds = TimeUnit.SECONDS.toSeconds(timeInSec.toLong())
        return "%2d:%2d:%2d".format(hours, minutes, seconds)
    }

    private fun resetWatch(){
        //set time since start to 0.
        //Stop the watch.
        //update the watch to be 00:00:00
        notificationsViewModel.timeSinceStart.apply { "0".toLong() }
        stopWatch()
        updateStopWatch()
        notificationsViewModel.text.observe(viewLifecycleOwner){
            binding.textDashboardTime.text = "00:00:00"
        }

    }
}