1. support header and footer gesture
2. support header and footer size change dynamically
3. support content scrolling with code
```kotlin
    R.id.btn_back_to_top -> {
        binding.recyclerView.scrollToPosition(0)
        Timber.d("recycler view position: %d", (binding.recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition())
        binding.recyclerView.post {
            binding.recyclerView.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH)
            binding.recyclerView.scrollBy(0, -1000)
        }
    }
```

4. support content's height wrap_content
5. test