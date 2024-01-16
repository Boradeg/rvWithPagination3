package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
// ItemAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
// Item.kt
data class Item(val text: String)

class ItemAdapter(private val itemList: List<Item>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.textView.text = item.text
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}




class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: ItemAdapter

    private val itemList = mutableListOf<Item>()

    private var currentPage = 1
    private val itemsPerPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)

        adapter = ItemAdapter(itemList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load initial data
        loadNextPage()

        // Implement pagination with progress bar visibility
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItem) >= totalItemCount) {
                    showProgressBar()
                    loadNextPage()
                    hideProgressBar()
                }
            }
        })
    }

    private fun showProgressBar() {
       progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
       progressBar.visibility = View.GONE
    }

    private fun loadNextPage() {

        // Simulate loading data from a data source asynchronously

            val newData = generateRandomItems(itemsPerPage)
            itemList.addAll(newData)
            adapter.notifyDataSetChanged()
            currentPage++


      // Simulating a delay of 2000 milliseconds (2 seconds)
    }

    private fun generateRandomItems(count: Int): List<Item> {
        val randomItems = mutableListOf<Item>()
        repeat(count) {
            randomItems.add(Item(generateRandomText()))
        }
        return randomItems
    }

    private fun generateRandomText(): String {
        // Implement your logic to generate random text here
        return "Random Text ${System.currentTimeMillis()}"
    }
}

