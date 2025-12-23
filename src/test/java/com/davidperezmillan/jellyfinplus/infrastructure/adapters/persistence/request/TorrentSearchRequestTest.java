package com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TorrentSearchRequestTest {

    @Test
    void shouldCreateTorrentSearchRequestWithAllFields() {
        // Given
        String site = "1337x";
        TorrentSearchRequest.Criteria criteria = new TorrentSearchRequest.Criteria();
        criteria.setCategory("72");
        criteria.setMaxResults(10);

        // When
        TorrentSearchRequest request = new TorrentSearchRequest();
        request.setSite(site);
        request.setCriteria(criteria);

        // Then
        assertThat(request.getSite()).isEqualTo(site);
        assertThat(request.getCriteria()).isEqualTo(criteria);
        assertThat(request.getCriteria().getCategory()).isEqualTo("72");
        assertThat(request.getCriteria().getMaxResults()).isEqualTo(10);
    }

    @Test
    void shouldCreateTorrentSearchRequestWithParameterizedConstructor() {
        // Given
        String site = "1337x";
        TorrentSearchRequest.Criteria criteria = new TorrentSearchRequest.Criteria("72", "1", 10);

        // When
        TorrentSearchRequest request = new TorrentSearchRequest(site, criteria);

        // Then
        assertThat(request.getSite()).isEqualTo(site);
        assertThat(request.getCriteria()).isEqualTo(criteria);
        assertThat(request.getCriteria().getCategory()).isEqualTo("72");
        assertThat(request.getCriteria().getPage()).isEqualTo("1");
        assertThat(request.getCriteria().getMaxResults()).isEqualTo(10);
    }

    @Test
    void shouldCreateCriteriaWithParameterizedConstructor() {
        // Given
        String category = "72";
        String page = "1";
        int maxResults = 10;

        // When
        TorrentSearchRequest.Criteria criteria = new TorrentSearchRequest.Criteria(category, page, maxResults);

        // Then
        assertThat(criteria.getCategory()).isEqualTo(category);
        assertThat(criteria.getPage()).isEqualTo(page);
        assertThat(criteria.getMaxResults()).isEqualTo(maxResults);
    }
}
