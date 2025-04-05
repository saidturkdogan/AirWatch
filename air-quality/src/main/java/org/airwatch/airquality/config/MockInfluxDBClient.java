package org.airwatch.airquality.config;

import com.influxdb.client.*;
import com.influxdb.client.domain.Authorization;
import com.influxdb.client.domain.Bucket;
import com.influxdb.client.domain.BucketRetentionRules;
import com.influxdb.client.domain.Organization;
import com.influxdb.client.domain.Ready;
import com.influxdb.client.domain.User;
import com.influxdb.client.service.InfluxDBClientService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Slf4j
public class MockInfluxDBClient implements InfluxDBClient {

    @Override
    public InfluxDBClientOptions getOptions() {
        return null;
    }

    @Override
    public WriteApiBlocking getWriteApiBlocking() {
        return new WriteApiBlocking() {
            @Override
            public void writeRecord(String record) {
                log.debug("Mock write record: {}", record);
            }

            @Override
            public void writeRecord(String record, WritePrecision precision) {
                log.debug("Mock write record with precision: {}", record);
            }

            @Override
            public void writeRecords(List<String> records) {
                log.debug("Mock write records: {}", records);
            }

            @Override
            public void writeRecords(List<String> records, WritePrecision precision) {
                log.debug("Mock write records with precision: {}", records);
            }

            @Override
            public <M> void writeMeasurement(M measurement) {
                log.debug("Mock write measurement: {}", measurement);
            }

            @Override
            public <M> void writeMeasurement(M measurement, WritePrecision precision) {
                log.debug("Mock write measurement with precision: {}", measurement);
            }

            @Override
            public <M> void writeMeasurements(List<M> measurements) {
                log.debug("Mock write measurements: {}", measurements);
            }

            @Override
            public <M> void writeMeasurements(List<M> measurements, WritePrecision precision) {
                log.debug("Mock write measurements with precision: {}", measurements);
            }

            @Override
            public void writePoint(Point point) {
                log.debug("Mock write point: {}", point);
            }

            @Override
            public void writePoints(List<Point> points) {
                log.debug("Mock write points: {}", points);
            }
        };
    }

    @Override
    public WriteApi getWriteApi() {
        return new WriteApi() {
            @Override
            public void writePoint(Point point) {
                log.debug("Mock write point: {}", point);
            }

            @Override
            public void writePoints(List<Point> points) {
                log.debug("Mock write points: {}", points);
            }

            @Override
            public <M> void writeMeasurement(M measurement) {
                log.debug("Mock write measurement: {}", measurement);
            }

            @Override
            public <M> void writeMeasurement(M measurement, WritePrecision precision) {
                log.debug("Mock write measurement with precision: {}", measurement);
            }

            @Override
            public <M> void writeMeasurements(List<M> measurements) {
                log.debug("Mock write measurements: {}", measurements);
            }

            @Override
            public <M> void writeMeasurements(List<M> measurements, WritePrecision precision) {
                log.debug("Mock write measurements with precision: {}", measurements);
            }

            @Override
            public void writeRecord(String record) {
                log.debug("Mock write record: {}", record);
            }

            @Override
            public void writeRecord(String record, WritePrecision precision) {
                log.debug("Mock write record with precision: {}", record);
            }

            @Override
            public void writeRecords(List<String> records) {
                log.debug("Mock write records: {}", records);
            }

            @Override
            public void writeRecords(List<String> records, WritePrecision precision) {
                log.debug("Mock write records with precision: {}", records);
            }

            @Override
            public void listenEvents(BiConsumer<List<WriteSuccessEvent>, InfluxDBClientException> onSuccess, BiConsumer<Throwable, List<WriteRetryEvent>> onRetry, Consumer<InfluxDBClientException> onError) {
                // Do nothing
            }

            @Override
            public void close() {
                // Do nothing
            }
        };
    }

    @Override
    public WriteApi getWriteApi(WriteOptions writeOptions) {
        return getWriteApi();
    }

    @Override
    public QueryApi getQueryApi() {
        return new QueryApi() {
            @Override
            public List<FluxTable> query(String query) {
                log.debug("Mock query: {}", query);
                return List.of();
            }

            @Override
            public void query(String query, Consumer<FluxRecord> onNext, Consumer<Throwable> onError, Runnable onComplete) {
                log.debug("Mock query with callbacks: {}", query);
                onComplete.run();
            }

            @Override
            public void query(String query, BiConsumer<Cancellable, FluxRecord> onNext, Consumer<Throwable> onError, Runnable onComplete) {
                log.debug("Mock query with cancellable: {}", query);
                onComplete.run();
            }

            @Override
            public <M> List<M> query(String query, Class<M> measurementType) {
                log.debug("Mock query with measurement type: {}", query);
                return List.of();
            }

            @Override
            public <M> void query(String query, Class<M> measurementType, Consumer<M> onNext, Consumer<Throwable> onError, Runnable onComplete) {
                log.debug("Mock query with measurement type and callbacks: {}", query);
                onComplete.run();
            }

            @Override
            public <M> void query(String query, Class<M> measurementType, BiConsumer<Cancellable, M> onNext, Consumer<Throwable> onError, Runnable onComplete) {
                log.debug("Mock query with measurement type and cancellable: {}", query);
                onComplete.run();
            }

            @Override
            public String queryRaw(String query) {
                log.debug("Mock query raw: {}", query);
                return "{}";
            }

            @Override
            public void queryRaw(String query, Consumer<String> onResponse, Consumer<Throwable> onError, Runnable onComplete) {
                log.debug("Mock query raw with callbacks: {}", query);
                onComplete.run();
            }
        };
    }

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
    public InfluxDBClientService getService() {
        return null;
    }

    @Override
    public Ready ready() {
        return null;
    }

    @Override
    public Organization findOrganizationByName(String name) {
        return null;
    }

    @Override
    public Organization findOrganizationByID(String id) {
        return null;
    }

    @Override
    public Bucket findBucketByName(String name) {
        return null;
    }

    @Override
    public Bucket findBucketByID(String id) {
        return null;
    }

    @Override
    public Bucket createBucket(String name) {
        return null;
    }

    @Override
    public Bucket createBucket(String name, BucketRetentionRules retentionRules) {
        return null;
    }

    @Override
    public Bucket createBucket(String name, String orgID) {
        return null;
    }

    @Override
    public Bucket createBucket(String name, BucketRetentionRules retentionRules, String orgID) {
        return null;
    }

    @Override
    public Bucket createBucket(String name, String orgID, String id) {
        return null;
    }

    @Override
    public Bucket createBucket(String name, BucketRetentionRules retentionRules, String orgID, String id) {
        return null;
    }

    @Override
    public User findUserByName(String name) {
        return null;
    }

    @Override
    public User findUserByID(String id) {
        return null;
    }

    @Override
    public User createUser(String name) {
        return null;
    }

    @Override
    public User createUser(String name, String password) {
        return null;
    }

    @Override
    public Authorization findAuthorizationByID(String id) {
        return null;
    }

    @Override
    public void close() {
        // Do nothing
    }
}
