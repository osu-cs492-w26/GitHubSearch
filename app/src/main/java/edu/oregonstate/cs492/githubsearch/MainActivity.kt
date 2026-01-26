package edu.oregonstate.cs492.githubsearch

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.oregonstate.cs492.githubsearch.data.GitHubRepo

class MainActivity : AppCompatActivity() {
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
                adapter.updateRepoList(dummySearchResults)
                searchResultsListRV.scrollToPosition(0)
            }
        }
    }
}