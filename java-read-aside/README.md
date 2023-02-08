# TiDB and Momento Read-Aside Serverless Function Project

The project source includes function code and supporting resources:

- `src/main` - A Java function.
- `src/test` - A unit test and helper classes.
- `template.yml` - An AWS CloudFormation template that creates an application.
- `pom.xml` - A Maven build file.
- `1-create-bucket.sh`, `2-deploy.sh`, etc. - Shell scripts that use the AWS CLI to deploy and manage the application.

Use the following instructions to deploy the sample application.

## Requirements

- [TiDB Cluster](https://tidbcloud.com/console/clusters)
- [Momento CLI](https://github.com/momentohq/momento-cli)
- [Java runtime environment](https://www.oracle.com/java/technologies/javase-downloads.html) 8 or higher.
- [Maven 3.8+](https://maven.apache.org/docs/history.html).
- The Bash shell. For Linux and macOS, this is included by default. In Windows 10, you can install the [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows-integrated version of Ubuntu and Bash.
- [The AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) v2.

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

3. If you don't have an AWS user, please create it in the [IAM console](https://us-east-1.console.aws.amazon.com/iamv2/home?region=us-east-1#/users). Please make sure this user can access AWS using an [access key](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_access-keys.html), and has sufficient permissions. A typical user's permissions to create and deploy serverless functions are:

    - `AWSCloudFormationFullAccess`: AWS CLI uses [AWS CloudFormation](https://aws.amazon.com/cloudformation/) to proclaim the AWS resources.
    - `AmazonS3FullAccess`: AWS CloudFormation uses [Amazon S3](https://aws.amazon.com/s3/?nc2=h_ql_prod_fs_s3) to publish.
    - `AWSLambda_FullAccess`: Obviously, you need to manage the [AWS Lambda](https://aws.amazon.com/lambda/?nc2=h_ql_prod_fs_lbd).
    - `CloudWatchLogsFullAccess`: The [AWS CloudWatch](https://aws.amazon.com/cloudwatch/?nc2=h_ql_prod_fs_lbd) offering observability of the AWS Lambda.

4. Configure your AWS access key ID and secret access key if you have not.

    ```sh
    aws configure
    ```

5. Enables the AWS CLI v2 to load JSON events from a file:

    ```sh
    echo "cli_binary_format = raw-in-base64-out" >> ~/.aws/config
    ```

## Setup

1. Download or clone this repository.

    ```bash
    git clone git@github.com:Icemap/tidb-momento-example.git
    cd tidb-momento-example/java-read-aside
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

1. Copy the `event.json.template` file to `event.json`, and change the `tidbJDBCStr` and `momentoAuthToken` values to yourselves.
2. To invoke the function, run `3-invoke.sh`.

    ```bash
    ./3-invoke.sh
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }
    ```

3. Let the script invoke the function a few times and then press `CRTL+C` to exit.

4. The application uses AWS X-Ray to trace requests. Open the [X-Ray console](https://console.aws.amazon.com/xray/home#/service-map) to view the service map.

5. Choose a node in the main function graph. Then choose **View traces** to see a list of traces. Choose any trace to view a timeline that breaks down the work done by the function.

6. Finally, view the application in the Lambda console.

### Application Viewer

1. Open the [applications page](https://console.aws.amazon.com/lambda/home#/applications) in the Lambda console.
2. Choose **java-read-aside**.

## Cleanup

1. To delete the application, run `4-cleanup.sh`.

    ```bash
    ./4-cleanup.sh
    ```
