package com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TorrentSearchRequest {
    private String site;
    private Criteria criteria;

    public TorrentSearchRequest() {}

    public TorrentSearchRequest(String site, Criteria criteria) {
        this.site = site;
        this.criteria = criteria;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Criteria {
        private String category;
        private String query;
        private String page;
        @JsonProperty("max_results")
        private int maxResults;

        public Criteria() {}

        public Criteria(String category, String page, int maxResults) {
            this.category = category;
            this.page = page;
            this.maxResults = maxResults;
        }

    }
}
