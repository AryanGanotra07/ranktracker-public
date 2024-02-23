package com.codingee.ranked.ranktracker.dto.ranking;

public record SeoRankingRequest(String keyword, String location_name, String device, String tag, String pingback_url, String language_code) {
}
