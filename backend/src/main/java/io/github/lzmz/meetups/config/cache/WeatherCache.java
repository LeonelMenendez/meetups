package io.github.lzmz.meetups.config.cache;

public final class WeatherCache {

    /**
     * Cache name.
     */
    public static final String DAILY_FORECAST_NAME = "dailyForecast";

    /**
     * Cron expression to schedule the cache evict.
     * <p>It schedules cache eviction every day at midnight.</p>
     */
    public static final String DAILY_FORECAST_EVICT_EXPRESSION = "0 0 0 * * *";

}
