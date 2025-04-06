package org.airwatch.airquality.config;

import com.influxdb.Cancellable;
import com.influxdb.LogLevel;
import com.influxdb.client.*;
import com.influxdb.client.domain.*;
import com.influxdb.client.write.Point;
import com.influxdb.client.write.WriteParameters;
import com.influxdb.client.write.events.AbstractWriteEvent;
import com.influxdb.client.write.events.EventListener;
import com.influxdb.client.write.events.ListenerRegistration;
import com.influxdb.exceptions.InfluxException;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Simple mock implementation of InfluxDBClient for development and testing.
 */
@Slf4j
public class MockInfluxDBClient implements InfluxDBClient {

  @Override
  public WriteApiBlocking getWriteApiBlocking() {
    return new MockWriteApiBlocking();
  }

  @Override
  public WriteApi getWriteApi() {
    return new MockWriteApi();
  }

  @Override
  public WriteApi getWriteApi(WriteOptions writeOptions) {
    return getWriteApi();
  }

  @Override
  public QueryApi getQueryApi() {
    return new MockQueryApi();
  }

  // All other methods return null or do nothing
  @Override
  public BucketsApi getBucketsApi() {
    return null;
  }

  @Override
  public OrganizationsApi getOrganizationsApi() {
    return null;
  }

  @Override
  public AuthorizationsApi getAuthorizationsApi() {
    return null;
  }

  @Override
  public UsersApi getUsersApi() {
    return null;
  }

  @Override
  public DeleteApi getDeleteApi() {
    return null;
  }

  @Override
  public LabelsApi getLabelsApi() {
    return null;
  }

  @Override
  public TasksApi getTasksApi() {
    return null;
  }

  @Override
  public SourcesApi getSourcesApi() {
    return null;
  }

  @Override
  public VariablesApi getVariablesApi() {
    return null;
  }

  @Override
  public ScraperTargetsApi getScraperTargetsApi() {
    return null;
  }

  @Override
  public TelegrafsApi getTelegrafsApi() {
    return null;
  }

  @Override
  public NotificationEndpointsApi getNotificationEndpointsApi() {
    return null;
  }

  @Override
  public NotificationRulesApi getNotificationRulesApi() {
    return null;
  }

  @Override
  public ChecksApi getChecksApi() {
    return null;
  }

  @Override
  public DashboardsApi getDashboardsApi() {
    return null;
  }

  @Override
  public <S> S getService(Class<S> serviceClass) {
    return null;
  }

  @Override
  public Ready ready() {
    return null;
  }

  @Override
  public void close() {
  }

  @Override
  public InfluxQLQueryApi getInfluxQLQueryApi() {
    return null;
  }

  @Override
  public InvokableScriptsApi getInvokableScriptsApi() {
    return null;
  }

  @Override
  public HealthCheck health() {
    return null;
  }

  @Override
  public Boolean ping() {
    return true;
  }

  @Override
  public String version() {
    return "mock-version";
  }

  @Override
  public OnboardingResponse onBoarding(OnboardingRequest onboarding) {
    return null;
  }

  @Override
  public Boolean isOnboardingAllowed() {
    return false;
  }

  @Override
  public LogLevel getLogLevel() {
    return LogLevel.NONE;
  }

  @Override
  public InfluxDBClient setLogLevel(LogLevel logLevel) {
    return this;
  }

  @Override
  public InfluxDBClient enableGzip() {
    return this;
  }

  @Override
  public InfluxDBClient disableGzip() {
    return this;
  }

  @Override
  public boolean isGzipEnabled() {
    return false;
  }

  @Override
  public WriteApi makeWriteApi() {
    return getWriteApi();
  }

  @Override
  public WriteApi makeWriteApi(WriteOptions writeOptions) {
    return getWriteApi(writeOptions);
  }

  /**
   * Simple mock implementation of WriteApiBlocking
   */
  private class MockWriteApiBlocking implements WriteApiBlocking {
    @Override
    public void writeRecord(WritePrecision precision, String record) {
      log.debug("Mock write record");
    }

    @Override
    public void writeRecord(String bucket, String org, WritePrecision precision, String record) {
      log.debug("Mock write record");
    }

    @Override
    public void writeRecord(String record, WriteParameters parameters) {
      log.debug("Mock write record");
    }

    @Override
    public void writeRecords(WritePrecision precision, List<String> records) {
      log.debug("Mock write records");
    }

    @Override
    public void writeRecords(String bucket, String org, WritePrecision precision, List<String> records) {
      log.debug("Mock write records");
    }

    @Override
    public void writeRecords(List<String> records, WriteParameters parameters) {
      log.debug("Mock write records");
    }

    @Override
    public void writePoint(Point point) {
      log.debug("Mock write point");
    }

    @Override
    public void writePoint(String bucket, String org, Point point) {
      log.debug("Mock write point");
    }

    @Override
    public void writePoint(Point point, WriteParameters parameters) {
      log.debug("Mock write point");
    }

    @Override
    public void writePoints(@NotNull List<Point> list) throws InfluxException {

    }

    @Override
    public void writePoints(String bucket, String org, List<Point> points) {
      log.debug("Mock write points");
    }

    @Override
    public void writePoints(List<Point> points, WriteParameters parameters) {
      log.debug("Mock write points");
    }

    @Override
    public <M> void writeMeasurement(WritePrecision precision, M measurement) {
      log.debug("Mock write measurement");
    }

    @Override
    public <M> void writeMeasurement(String bucket, String org, WritePrecision precision, M measurement) {
      log.debug("Mock write measurement");
    }

    @Override
    public <M> void writeMeasurement(M measurement, WriteParameters parameters) {
      log.debug("Mock write measurement");
    }

    @Override
    public <M> void writeMeasurements(WritePrecision precision, List<M> measurements) {
      log.debug("Mock write measurements");
    }

    @Override
    public <M> void writeMeasurements(String bucket, String org, WritePrecision precision, List<M> measurements) {
      log.debug("Mock write measurements");
    }

    @Override
    public <M> void writeMeasurements(List<M> measurements, WriteParameters parameters) {
      log.debug("Mock write measurements");
    }
  }

  /**
   * Simple mock implementation of WriteApi
   */
  private class MockWriteApi implements WriteApi {
    @Override
    public void writeRecord(WritePrecision precision, String record) {
      log.debug("Mock write record");
    }

    @Override
    public void writeRecord(String bucket, String org, WritePrecision precision, String record) {
      log.debug("Mock write record");
    }

    @Override
    public void writeRecord(String record, WriteParameters parameters) {
      log.debug("Mock write record");
    }

    @Override
    public void writeRecords(WritePrecision precision, List<String> records) {
      log.debug("Mock write records");
    }

    @Override
    public void writeRecords(String bucket, String org, WritePrecision precision, List<String> records) {
      log.debug("Mock write records");
    }

    @Override
    public void writeRecords(List<String> records, WriteParameters parameters) {
      log.debug("Mock write records");
    }

    @Override
    public void writePoint(Point point) {
      log.debug("Mock write point");
    }

    @Override
    public void writePoint(String bucket, String org, Point point) {
      log.debug("Mock write point");
    }

    @Override
    public void writePoint(Point point, WriteParameters parameters) {
      log.debug("Mock write point");
    }

    @Override
    public void writePoints(List<Point> points) {
      log.debug("Mock write points");
    }

    @Override
    public void writePoints(String bucket, String org, List<Point> points) {
      log.debug("Mock write points");
    }

    @Override
    public void writePoints(List<Point> points, WriteParameters parameters) {
      log.debug("Mock write points");
    }

    @Override
    public <M> void writeMeasurement(WritePrecision precision, M measurement) {
      log.debug("Mock write measurement");
    }

    @Override
    public <M> void writeMeasurement(String bucket, String org, WritePrecision precision, M measurement) {
      log.debug("Mock write measurement");
    }

    @Override
    public <M> void writeMeasurement(M measurement, WriteParameters parameters) {
      log.debug("Mock write measurement");
    }

    @Override
    public <M> void writeMeasurements(WritePrecision precision, List<M> measurements) {
      log.debug("Mock write measurements");
    }

    @Override
    public <M> void writeMeasurements(String bucket, String org, WritePrecision precision, List<M> measurements) {
      log.debug("Mock write measurements");
    }

    @Override
    public <M> void writeMeasurements(List<M> measurements, WriteParameters parameters) {
      log.debug("Mock write measurements");
    }

    @Override
    public <T extends AbstractWriteEvent> ListenerRegistration listenEvents(Class<T> eventType, EventListener<T> listener) {
      return () -> {
      };
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }
  }

  /**
   * Simple mock implementation of QueryApi
   */
  private class MockQueryApi implements QueryApi {
    @Override
    public List<FluxTable> query(String query) {
      return Collections.emptyList();
    }

    @NotNull
    @Override
    public List<FluxTable> query(@NotNull String s, @NotNull String s1) {
      return List.of();
    }

    @NotNull
    @Override
    public List<FluxTable> query(@NotNull String s, @NotNull String s1, @Nullable Map<String, Object> map) {
      return List.of();
    }

    @NotNull
    @Override
    public List<FluxTable> query(@NotNull Query query) {
      return List.of();
    }

    @NotNull
    @Override
    public List<FluxTable> query(@NotNull Query query, @NotNull String s) {
      return List.of();
    }

    @Override
    public <M> List<M> query(String query, Class<M> measurementType) {
      return Collections.emptyList();
    }

    @NotNull
    @Override
    public <M> List<M> query(@NotNull String s, @NotNull String s1, @NotNull Class<M> aClass) {
      return List.of();
    }

    @Override
    public <M> List<M> query(@NotNull String s, @NotNull String s1, @NotNull Class<M> aClass, @Nullable Map<String, Object> map) {
      return List.of();
    }

    @NotNull
    @Override
    public <M> List<M> query(@NotNull Query query, @NotNull Class<M> aClass) {
      return List.of();
    }

    @NotNull
    @Override
    public <M> List<M> query(@NotNull Query query, @NotNull String s, @NotNull Class<M> aClass) {
      return List.of();
    }

    @Override
    public void query(@NotNull String s, @NotNull BiConsumer<Cancellable, FluxRecord> biConsumer) {

    }

    @Override
    public void query(@NotNull String s, @NotNull String s1, @NotNull BiConsumer<Cancellable, FluxRecord> biConsumer) {

    }

    @Override
    public void query(@NotNull Query query, @NotNull BiConsumer<Cancellable, FluxRecord> biConsumer) {

    }

    @Override
    public void query(@NotNull Query query, @NotNull String s, @NotNull BiConsumer<Cancellable, FluxRecord> biConsumer) {

    }

    @Override
    public <M> void query(@NotNull String s, @NotNull Class<M> aClass, @NotNull BiConsumer<Cancellable, M> biConsumer) {

    }

    @Override
    public <M> void query(@NotNull String s, @NotNull String s1, @NotNull Class<M> aClass, @NotNull BiConsumer<Cancellable, M> biConsumer) {

    }

    @Override
    public <M> void query(@NotNull Query query, @NotNull Class<M> aClass, @NotNull BiConsumer<Cancellable, M> biConsumer) {

    }

    @Override
    public <M> void query(@NotNull Query query, @NotNull String s, @NotNull Class<M> aClass, @NotNull BiConsumer<Cancellable, M> biConsumer) {

    }

    @Override
    public void query(@NotNull String s, @NotNull BiConsumer<Cancellable, FluxRecord> biConsumer, @NotNull Consumer<? super Throwable> consumer) {

    }

    @Override
    public void query(@NotNull String s, @NotNull String s1, @NotNull BiConsumer<Cancellable, FluxRecord> biConsumer, @NotNull Consumer<? super Throwable> consumer) {

    }

    @Override
    public void query(@NotNull Query query, @NotNull BiConsumer<Cancellable, FluxRecord> biConsumer, @NotNull Consumer<? super Throwable> consumer) {

    }

    @Override
    public void query(@NotNull Query query, @NotNull String s, @NotNull BiConsumer<Cancellable, FluxRecord> biConsumer, @NotNull Consumer<? super Throwable> consumer) {

    }

    @Override
    public <M> void query(@NotNull String s, @NotNull Class<M> aClass, @NotNull BiConsumer<Cancellable, M> biConsumer, @NotNull Consumer<? super Throwable> consumer) {

    }

    @Override
    public <M> void query(@NotNull String s, @NotNull String s1, @NotNull Class<M> aClass, @NotNull BiConsumer<Cancellable, M> biConsumer, @NotNull Consumer<? super Throwable> consumer) {

    }

    @Override
    public <M> void query(@NotNull Query query, @NotNull Class<M> aClass, @NotNull BiConsumer<Cancellable, M> biConsumer, @NotNull Consumer<? super Throwable> consumer) {

    }

    @Override
    public <M> void query(@NotNull Query query, @NotNull String s, @NotNull Class<M> aClass, @NotNull BiConsumer<Cancellable, M> biConsumer, @NotNull Consumer<? super Throwable> consumer) {

    }

    @Override
    public void query(@NotNull String s, @NotNull BiConsumer<Cancellable, FluxRecord> biConsumer, @NotNull Consumer<? super Throwable> consumer, @NotNull Runnable runnable) {

    }

    @Override
    public void query(@NotNull String s, @NotNull String s1, @NotNull BiConsumer<Cancellable, FluxRecord> biConsumer, @NotNull Consumer<? super Throwable> consumer, @NotNull Runnable runnable) {

    }

    @Override
    public void query(@NotNull String s, @NotNull String s1, @NotNull BiConsumer<Cancellable, FluxRecord> biConsumer, @NotNull Consumer<? super Throwable> consumer, @NotNull Runnable runnable, @Nullable Map<String, Object> map) {

    }

    @Override
    public void query(@NotNull Query query, @NotNull BiConsumer<Cancellable, FluxRecord> biConsumer, @NotNull Consumer<? super Throwable> consumer, @NotNull Runnable runnable) {

    }

    @Override
    public void query(@NotNull Query query, @NotNull String s, @NotNull BiConsumer<Cancellable, FluxRecord> biConsumer, @NotNull Consumer<? super Throwable> consumer, @NotNull Runnable runnable) {

    }

    @Override
    public <M> void query(@NotNull String s, @NotNull Class<M> aClass, @NotNull BiConsumer<Cancellable, M> biConsumer, @NotNull Consumer<? super Throwable> consumer, @NotNull Runnable runnable) {

    }


    @Override
    public <M> void query(@NotNull String s, @NotNull String s1, @NotNull Class<M> aClass, @NotNull BiConsumer<Cancellable, M> biConsumer, @NotNull Consumer<? super Throwable> consumer, @NotNull Runnable runnable) {

    }

    @Override
    public <M> void query(@NotNull String s, @NotNull String s1, @NotNull Class<M> aClass, @NotNull BiConsumer<Cancellable, M> biConsumer, @NotNull Consumer<? super Throwable> consumer, @NotNull Runnable runnable, @Nullable Map<String, Object> map) {

    }

    @Override
    public <M> void query(@NotNull Query query, @NotNull Class<M> aClass, @NotNull BiConsumer<Cancellable, M> biConsumer, @NotNull Consumer<? super Throwable> consumer, @NotNull Runnable runnable) {

    }

    @Override
    public <M> void query(@NotNull Query query, @NotNull String s, @NotNull Class<M> aClass, @NotNull BiConsumer<Cancellable, M> biConsumer, @NotNull Consumer<? super Throwable> consumer, @NotNull Runnable runnable) {

    }

    @Override
    public String queryRaw(String query) {
      return "{}";
    }

    @NotNull
    @Override
    public String queryRaw(@NotNull String s, @NotNull String s1) {
      return "";
    }

    @NotNull
    @Override
    public String queryRaw(@NotNull String s, @Nullable Dialect dialect) {
      return "";
    }

    @NotNull
    @Override
    public String queryRaw(@NotNull String s, @Nullable Dialect dialect, @NotNull String s1) {
      return "";
    }

    @NotNull
    @Override
    public String queryRaw(@NotNull String s, @Nullable Dialect dialect, @NotNull String s1, @Nullable Map<String, Object> map) {
      return "";
    }

    @NotNull
    @Override
    public String queryRaw(@NotNull Query query) {
      return "";
    }

    @NotNull
    @Override
    public String queryRaw(@NotNull Query query, @NotNull String s) {
      return "";
    }

    @Override
    public void queryRaw(@NotNull String s, @NotNull BiConsumer<Cancellable, String> biConsumer) {

    }

    @Override
    public void queryRaw(@NotNull String s, @NotNull String s1, @NotNull BiConsumer<Cancellable, String> biConsumer) {

    }

    @Override
    public void queryRaw(@NotNull Query query, @NotNull String s, @NotNull BiConsumer<Cancellable, String> biConsumer) {

    }

    @Override
    public void queryRaw(@NotNull Query query, @NotNull BiConsumer<Cancellable, String> biConsumer) {

    }

    @Override
    public void queryRaw(@NotNull String s, @Nullable Dialect dialect, @NotNull BiConsumer<Cancellable, String> biConsumer) {

    }

    @Override
    public void queryRaw(@NotNull String s, @Nullable Dialect dialect, @NotNull String s1, @NotNull BiConsumer<Cancellable, String> biConsumer) {

    }

    @Override
    public void queryRaw(@NotNull String s, @NotNull BiConsumer<Cancellable, String> biConsumer, @NotNull Consumer<? super Throwable> consumer) {

    }

    @Override
    public void queryRaw(@NotNull String s, @NotNull String s1, @NotNull BiConsumer<Cancellable, String> biConsumer, @NotNull Consumer<? super Throwable> consumer) {

    }

    @Override
    public void queryRaw(@NotNull String s, @NotNull String s1, @NotNull BiConsumer<Cancellable, String> biConsumer, @NotNull Consumer<? super Throwable> consumer, @Nullable Map<String, Object> map) {

    }

    @Override
    public void queryRaw(@NotNull Query query, @NotNull BiConsumer<Cancellable, String> biConsumer, @NotNull Consumer<? super Throwable> consumer) {

    }

    @Override
    public void queryRaw(@NotNull Query query, @NotNull String s, @NotNull BiConsumer<Cancellable, String> biConsumer, @NotNull Consumer<? super Throwable> consumer) {

    }

    @Override
    public void queryRaw(@NotNull String s, @Nullable Dialect dialect, @NotNull BiConsumer<Cancellable, String> biConsumer, @NotNull Consumer<? super Throwable> consumer) {

    }

    @Override
    public void queryRaw(@NotNull String s, @Nullable Dialect dialect, @NotNull String s1, @NotNull BiConsumer<Cancellable, String> biConsumer, @NotNull Consumer<? super Throwable> consumer) {

    }

    @Override
    public void queryRaw(@NotNull String s, @NotNull String s1, @NotNull BiConsumer<Cancellable, String> biConsumer, @NotNull Consumer<? super Throwable> consumer, @NotNull Runnable runnable) {

    }

    @Override
    public void queryRaw(@NotNull String s, @NotNull BiConsumer<Cancellable, String> biConsumer, @NotNull Consumer<? super Throwable> consumer, @NotNull Runnable runnable) {

    }

    @Override
    public void queryRaw(@NotNull String s, @Nullable Dialect dialect, @NotNull BiConsumer<Cancellable, String> biConsumer, @NotNull Consumer<? super Throwable> consumer, @NotNull Runnable runnable) {

    }

    @Override
    public void queryRaw(@NotNull String s, @Nullable Dialect dialect, @NotNull String s1, @NotNull BiConsumer<Cancellable, String> biConsumer, @NotNull Consumer<? super Throwable> consumer, @NotNull Runnable runnable) {

    }

    @Override
    public void queryRaw(@NotNull String s, @Nullable Dialect dialect, @NotNull String s1, @NotNull BiConsumer<Cancellable, String> biConsumer, @NotNull Consumer<? super Throwable> consumer, @NotNull Runnable runnable, @Nullable Map<String, Object> map) {

    }

    @Override
    public void queryRaw(@NotNull Query query, @NotNull String s, @NotNull BiConsumer<Cancellable, String> biConsumer, @NotNull Consumer<? super Throwable> consumer, @NotNull Runnable runnable) {

    }

    @Override
    public void queryRaw(@NotNull Query query, @NotNull BiConsumer<Cancellable, String> biConsumer, @NotNull Consumer<? super Throwable> consumer, @NotNull Runnable runnable) {

    }

  }
}
