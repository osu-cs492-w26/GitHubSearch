package edu.oregonstate.cs492.githubsearch

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.oregonstate.cs492.githubsearch.data.GitHubRepo
import edu.oregonstate.cs492.githubsearch.data.GitHubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val githubService = GitHubService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dummySearchResults = listOf(
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results"),
            GitHubRepo("Dummy search results")
        )

        val searchBoxET: EditText = findViewById(R.id.et_search_box)
        val searchBtn: Button = findViewById(R.id.btn_search)

        val searchResultsListRV: RecyclerView = findViewById(R.id.rv_search_results)
        searchResultsListRV.layoutManager = LinearLayoutManager(this)
        searchResultsListRV.setHasFixedSize(true)

        val adapter = GitHubRepoListAdapter()
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
        githubService.searchRepositories(query).enqueue(
            object : Callback<String> {
                override fun onFailure(call: Call<String?>, t: Throwable) {
                    Log.d("MainActivity", "Error making API call: ${t.message}")
                }

                override fun onResponse(call: Call<String?>, response: Response<String?>) {
                    Log.d("MainActivity", "Status code: ${response.code()}")
                    Log.d("MainActivity", "Response body: ${response.body()}")
                }
            }
        )
    }
}