package com.aviraldg.popularmovies.api;

import java.util.List;

public class ApiResult<T> {
    private long totalPages;
    private long totalResults;
    private List<T> results;
    private long page;

    public long getTotalPages() {
        return totalPages;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public List<T> getResults() {
        return results;
    }

    public long getPage() {
        return page;
    }
}
