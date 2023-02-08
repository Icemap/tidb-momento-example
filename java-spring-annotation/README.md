# TiDB and Momento Spring Annotation Project

The project source includes function code and supporting resources:

- `src/main` - A Java Spring service, in this example, we use Spring Boot.
- `pom.xml` - A Maven build file.

## Requirements

- [TiDB Cluster](https://tidbcloud.com/console/clusters)
- [Momento CLI](https://github.com/momentohq/momento-cli)
- [JDK](https://www.oracle.com/java/technologies/javase-downloads.html) 17 or higher.
- [Spring](https://spring.io/) 3.0.2 or higher.
- [Maven 3.8+](https://maven.apache.org/docs/history.html).

## Prepare

1. If you didn't have a TiDB cluster, create a [TiDB Cloud](https://tidbcloud.com/console/clusters) Serverless Tier Cluster. You can see the [help documents](https://docs.pingcap.com/tidbcloud/create-tidb-cluster) to get more information.
2. If you not configured Memonto before, configure Momento by Momento CLI:

    1. Download and create token:

        ```sh
        brew tap momentohq/tap
        brew install momento-cli
        momento account signup aws --email <your e-mail> --region <cached region>
        ```

    2. Get the token of Momento from the e-mail.
    3. Initial Momento:

        ```sh
        momento configure
        ```

    4. Make a cache named `tidb_cache`.

        ```sh
        momento cache create --name tidb_cache
        ```

## Setup

1. Download or clone this repository.

    ```bash
    git clone git@github.com:Icemap/tidb-momento-example.git
    cd tidb-momento-example/java-spring-annotation
    ```

2. Copy the `src/main/resources/application.yml.template` file to `src/main/resources/application.yml`, and change the `spring.datasource.url` and `application.momento-token` values to yourselves.

    ```yml
    spring:
        datasource:
            # change this line
            url: jdbc:mysql://<host>:<port>/test?user=<username>&password=<password>&sslMode=VERIFY_IDENTITY&enabledTLSProtocols=TLSv1.2,TLSv1.3

    # ...

    application:
        # change this line
        momento-token: "*******"
        momento-name: tidb_cache
    ```

## Run

You can use any IDE to run this Spring Boot application. If you want to use the bash to start it, you can use this command:

```sh
mvn spring-boot:run
```

## Core Code

In class [ExampleServiceImpl](src/main/java/com/pingcap/example/service/impl/ExampleServiceImpl.java). There are 2 methods: `setAndGetExample` and `setAndGetWithCacheExample`.

```java
@Override
public ExampleBean setAndGetExample(Long id) {
    exampleRepository.save(new ExampleBean(id));
    exampleRepository.flush();
    return exampleRepository.findById(id).orElse(null);
}

@Override
@ReadThroughSingleCache(namespace = "example", expiration = 300)
public ExampleBean setAndGetWithCacheExample(@ParameterValueKeyProvider Long id) {
    return setAndGetExample(id);
}
```

Basicly speaking, `setAndGetWithCacheExample` is just call the `setAndGetExample`. Except two annotation:

- `@ReadThroughSingleCache(namespace = "example", expiration = 300)`:

  - `@ReadThroughSingleCache`: Tell the framework, this function need to be cached.
  - `namespace = "example"`: Add a prefix in the cache key, the prefix is `example`.
  - `expiration = 300`: The cache will expired at 300 seconds after.

- `@ParameterValueKeyProvider`: This input parameter is the cache key.

The `setAndGetExample` method will set the current time to the field, and read it. We can use this way to observe if the cache activated.

## Verify

### Without cache

You can use `/no-cache` endpoint to call the `setAndGetExample` method directly.

```sh
$ curl http://localhost:8080/no-cache
{"id":1,"updateTime":"2023-02-08T06:22:30.783+00:00"}
$ curl http://localhost:8080/no-cache
{"id":1,"updateTime":"2023-02-08T06:22:33.901+00:00"}
```

The 2 `updateTime` are different, because we set it twice.

### With cache

You can use `/cache` endpoint to call the `setAndGetWithCacheExample` method.

```sh
$ curl http://localhost:8080/cache
{"id":0,"updateTime":"2023-02-08T06:26:19.540+00:00"}
$ curl http://localhost:8080/cache
{"id":0,"updateTime":"2023-02-08T06:26:19.540+00:00"}
```

The 2 `updateTime` are exactly the same, because we just set the time once, and the second time we read it from the cache.
