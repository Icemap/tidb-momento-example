# TiDB and Momento Integration Project

The project source includes function code and supporting resources:

- `src/main` - A Java function.
- `src/test` - A unit test and helper classes.
- `template.yml` - An AWS CloudFormation template that creates an application.
- `pom.xml` - A Maven build file.
- `1-create-bucket.sh`, `2-deploy.sh`, etc. - Shell scripts that use the AWS CLI to deploy and manage the application.

Use the following instructions to deploy the sample application.

## Requirements

- [Java 8 runtime environment (SE JRE)](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Maven 3](https://maven.apache.org/docs/history.html)
- The Bash shell. For Linux and macOS, this is included by default. In Windows 10, you can install the [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows-integrated version of Ubuntu and Bash.
- [The AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) v1.17 or newer.

If you use the AWS CLI v2, add the following to your [configuration file](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-files.html) (`~/.aws/config`):

```sh
echo "cli_binary_format = raw-in-base64-out" >> ~/.aws/config
```

This setting enables the AWS CLI v2 to load JSON events from a file, matching the v1 behavior.

## Setup

1. Download or clone this repository.

    ```bash
    git clone git@github.com:Icemap/tidb-momento-example.git
    cd tidb-momento-example
    ```

2. To create a new bucket for deployment artifacts, run `1-create-bucket.sh`.

    ```bash
    sh 1-create-bucket.sh
    make_bucket: lambda-artifacts-a5e491dbb5b22e0d
    ```

## Deploy

1. To deploy the application, run `2-deploy.sh`.

    ```bash
    ./2-deploy.sh
    BUILD SUCCESSFUL in 1s
    Successfully packaged artifacts and wrote output template to file out.yml.
    Waiting for changeset to be created..
    Successfully created/updated stack - blank-java
    ```

2. This script uses AWS CloudFormation to deploy the Lambda functions and an IAM role. If the AWS CloudFormation stack that contains the resources already exists, the script updates it with any changes to the template or function code.

    ```bash
    ./3-deploy.sh
    [INFO] Scanning for projects...
    [INFO] 
    [INFO] --------------------< com.pingcap:java-read-aside >---------------------
    [INFO] Building java-read-aside-function 1.0-SNAPSHOT
    [INFO] --------------------------------[ jar ]---------------------------------
    ...
    ```

## Test

### X-Ray Viewer

1. To invoke the function, run `3-invoke.sh`.

    ```bash
    ./3-invoke.sh
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }
    ```

2. Let the script invoke the function a few times and then press `CRTL+C` to exit.

3. The application uses AWS X-Ray to trace requests. Open the [X-Ray console](https://console.aws.amazon.com/xray/home#/service-map) to view the service map.

4. Choose a node in the main function graph. Then choose **View traces** to see a list of traces. Choose any trace to view a timeline that breaks down the work done by the function.

5. Finally, view the application in the Lambda console.

### Application Viewer

1. Open the [applications page](https://console.aws.amazon.com/lambda/home#/applications) in the Lambda console.
2. Choose **java-read-aside**.

## Cleanup

1. To delete the application, run `4-cleanup.sh`.

    ```bash
    ./4-cleanup.sh
    ```