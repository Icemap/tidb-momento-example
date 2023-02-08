# Integration Examples for TiDB and Momento

This repository is an example repository for [TiDB](https://www.pingcap.com/) and [Momento](https://www.gomomento.com/).

## What is TiDB

TiDB (/’taɪdiːbi:/, "Ti" stands for Titanium) is an open-source distributed SQL database that supports Hybrid Transactional and Analytical Processing (HTAP) workloads. It is MySQL compatible and features horizontal scalability, strong consistency, and high availability. The goal of TiDB is to provide users with a one-stop database solution that covers OLTP (Online Transactional Processing), OLAP (Online Analytical Processing), and HTAP services. TiDB is suitable for various use cases that require high availability and strong consistency with large-scale data.

## What is Momento

Momento Serverless Cache is the world's first truly serverless caching service. It provides instant elasticity, scale-to-zero capability, and blazing-fast performance. Gone are the days where you need to choose, manage, and provision capacity. With Momento Serverless Cache, you grab the SDK, you get an end point, input a few lines into your code, and you're off and running.

## Why We Integrate

If you are a web developer, you might already use the serverless function to create your own modern service. But, there are two big hills you need to confront:

- How can I deal with the huge number of connections between the database and serverless functions?
- How can I build a cache for serverless functions?

If you struggle with these two pieces of stuff, it's your lucky day. Because you can use TiDB and Momento to solve those slappy things radically. TiDB can retain thousands of connections, and Momento can help you to accomplish the cache easily.

## Examples

We don't want that you have no idea to integrate TiDB and Momento. So, we offer common scenarios and programming language examples for you. We hope those examples can help you. If you have a good idea also, please feel free to give an issue.

- Java:

  - [Read-Aside Cache](/java-read-aside/)
  - Write-Aside Cache(TODO)

- Golang:

  - Read-Aside Cache(TODO)
  - Write-Aside Cache(TODO)

- Python:

  - Read-Aside Cache(TODO)
  - Write-Aside Cache(TODO)

- TypeScript:

  - Read-Aside Cache(TODO)
  - Write-Aside Cache(TODO)
