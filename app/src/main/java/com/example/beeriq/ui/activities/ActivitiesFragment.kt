package com.example.beeriq.ui.activities

import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beeriq.R
import com.example.beeriq.databinding.FragmentActivitiesBinding
import com.example.beeriq.FirebaseRepo
import java.io.ByteArrayOutputStream
import android.util.Base64
import android.view.Gravity
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toolbar
import androidx.core.view.marginTop
import com.example.beeriq.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ActivitiesFragment : Fragment() {
    private lateinit var binding: FragmentActivitiesBinding
    private lateinit var listView: ListView
    private lateinit var viewModel: SharedViewModel
    private lateinit var activitiesAdapter: ActivitiesAdapter
    private lateinit var activitiesList: MutableList<Post>
    private lateinit var friendsList: MutableList<String>
    private lateinit var factory: SharedViewModel.SharedViewModelFactory
    private lateinit var repo: FirebaseRepo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentActivitiesBinding.inflate(inflater, container, false)



        showLoadingScreen()


        val sharedPreferences = requireContext().getSharedPreferences("UserData", MODE_PRIVATE)

        repo = FirebaseRepo(sharedPreferences)
        listView = binding.activitiesList
        factory = SharedViewModel.SharedViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory).get(SharedViewModel::class.java)

        activitiesList = mutableListOf()

        friendsList = mutableListOf()



        activitiesAdapter = ActivitiesAdapter(requireContext(), activitiesList)
        listView.adapter = activitiesAdapter

        viewModel.friendsListData.observe(viewLifecycleOwner, { friends ->
            if (friends != null){
                for (friend in friends) {
                    repo.fetchActivities(friend)
                }
            }

        })

        viewModel.activitiesListData.observe(viewLifecycleOwner, {
            if (it != null){
                activitiesList.clear()
                activitiesList.addAll(it)
                activitiesAdapter.replace(activitiesList)
                activitiesAdapter.notifyDataSetChanged()
            }
        })



        loadData()


        return binding.root
    }

    private fun showLoadingScreen(){
        binding.progressBar.visibility = View.VISIBLE
        binding.activitiesList.visibility = View.GONE
        binding.noActivitesText.visibility = View.GONE
    }

    private fun loadData(){
        Thread{

            Thread.sleep(2000)
            requireActivity().runOnUiThread{
                binding.progressBar.visibility = View.GONE
                if (activitiesList.isEmpty()){
                    binding.noActivitesText.visibility = View.VISIBLE
                }else{
                    binding.activitiesList.visibility = View.VISIBLE
                }

            }
        }.start()
    }

}