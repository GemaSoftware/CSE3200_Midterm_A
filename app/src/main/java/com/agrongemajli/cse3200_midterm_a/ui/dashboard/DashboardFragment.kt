package com.agrongemajli.cse3200_midterm_a.ui.dashboard

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
import com.agrongemajli.cse3200_midterm_a.databinding.FragmentDashboardBinding
import com.agrongemajli.cse3200_midterm_a.ui.notifications.NotificationsViewModel
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private var timerStarted: Boolean = false

    //we will be using handlers here, jsut to make it easier.
    private var watchHandler: Handler? = null

    private lateinit var dashboardViewModel: DashboardViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboardTime
        dashboardViewModel.text.observe(viewLifecycleOwner) {
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
                dashboardViewModel.timeSinceStart += 1
                updateStopWatch()
            } finally {
                //this will run the handler after every second. It sets the callbacks so that when we stop the clock it will stop.
                watchHandler!!.postDelayed(this, "1000".toLong())
            }
        }
    }

    private fun updateStopWatch(){
        Log.e("time", dashboardViewModel.timeSinceStart.toString())
        val updatedWatch = convertTime(dashboardViewModel.timeSinceStart.toInt())
        dashboardViewModel.text.observe(viewLifecycleOwner){
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
        dashboardViewModel.timeSinceStart.apply { "0".toLong() }
        stopWatch()
        updateStopWatch()
        dashboardViewModel.text.observe(viewLifecycleOwner){
            binding.textDashboardTime.text = "00:00:00"
        }

    }
}