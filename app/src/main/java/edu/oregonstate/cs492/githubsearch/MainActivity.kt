package edu.oregonstate.cs492.githubsearch

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.squareup.moshi.JsonAdapter
import edu.oregonstate.cs492.githubsearch.data.GitHubRepo
import edu.oregonstate.cs492.githubsearch.data.GitHubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.squareup.moshi.Moshi
import edu.oregonstate.cs492.githubsearch.data.GitHubSearchResults

class MainActivity : AppCompatActivity() {
    private val githubService = GitHubService.create()
    private val adapter = GitHubRepoListAdapter()

    private lateinit var searchResultsListRV: RecyclerView
    private lateinit var searchErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val searchBoxET: EditText = findViewById(R.id.et_search_box)
        val searchBtn: Button = findViewById(R.id.btn_search)

        searchResultsListRV = findViewById(R.id.rv_search_results)
        searchErrorTV = findViewById(R.id.tv_search_error)
        loadingIndicator = findViewById(R.id.loading_indicator)

        searchResultsListRV.layoutManager = LinearLayoutManager(this)
        searchResultsListRV.setHasFixedSize(true)

        searchResultsListRV.adapter = adapter

        searchBtn.setOnClickListener {
            val query = searchBoxET.text.toString()
            if (!TextUtils.isEmpty(query)) {
                doRepoSearch(query)
//                adapter.updateRepoList(dummySearchResults)
                searchResultsListRV.scrollToPosition(0)
            }
        }
    }

    private fun doRepoSearch(query: String) {
        loadingIndicator.visibility = View.VISIBLE
        searchResultsListRV.visibility = View.INVISIBLE
        searchErrorTV.visibility = View.INVISIBLE
        githubService.searchRepositories(query).enqueue(
            object : Callback<GitHubSearchResults> {
                override fun onFailure(call: Call<GitHubSearchResults?>, t: Throwable) {
                    Log.d("MainActivity", "Error making API call: ${t.message}")
                }

                override fun onResponse(call: Call<GitHubSearchResults?>, response: Response<GitHubSearchResults?>) {
                    loadingIndicator.visibility = View.INVISIBLE
                    Log.d("MainActivity", "Status code: ${response.code()}")
                    Log.d("MainActivity", "Response body: ${response.body()}")
                    if (response.isSuccessful) {
//                        val moshi = Moshi.Builder().build()
//                        val jsonAdapter: JsonAdapter<GitHubSearchResults> = moshi.adapter(
//                            GitHubSearchResults::class.java)
//                        val searchResults = jsonAdapter.fromJson(response.body() ?: "")
                        searchResultsListRV.visibility = View.VISIBLE
                        adapter.updateRepoList(response.body()?.items)
                    } else {
                        searchErrorTV.visibility = View.VISIBLE
                        searchErrorTV.text = "An error occurred: ${response.errorBody()?.string()}"
                    }
                }
            }
        )
    }
}