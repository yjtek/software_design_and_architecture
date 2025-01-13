This will look into a general approach to answering any system design interview question

1. Ask questions
    - Scale
        - What performance requirements?
            - Users? QPS? 
        - Any constraints?
            - Latency? Scalability? Cost?
        - Expected scaling plans?
            - Need to double users in 6 months?
    - Requirements
        - Functional Requirements: What does the system need to do? Define in-scope and out-of-scope
        - Non-Functional Requirements: 
            - Scalability
            - Availability
            - Latency
            - Consistency
            - Security

2. Define high level architecture to meet **functional** requirements. While this is intended as a comprehensive list, when you first discuss it, scope it to the minimum possible complexity to meet the requirements. Think of it as an MVP. 
    - Core functional layers:
        - API/API Gateway Layer: Handles requests. Deals with authentication/authorisation
        - Application Layer: Processes business logic
        - Database Layer: Stores and retrieves data. DB should also handle:
            - data replication & sharding
            - Data Privacy & Compliance: Manage data encryption, masking, and compliance with regulations
        - Caching Layer: Reduces latency for frequent operations
        - Load Balancer: Distributes traffic across servers
        - Search & Indexing Layer: Optimizes data retrieval for search-heavy applications, often using specialized tools like Elasticsearch, Solr, or OpenSearch for full-text search, filtering, and ranking
    
    - Performance/Reliability Layers: 
        - Monitoring & Logging: Tracks performance and issues (Prometheus/Grafana/OpenTelemetry for metrics, logs, and tracing)
        - Failover/Disaster Recovery Layer: Ensures system resilience and continuity in case of failures, using strategies like active-passive or active-active configurations
        - Rate Limiting & Quota Management: Prevents abuse and ensures fair use of system resources

    - Scalability/Communication Layers:
        - Message Queue/Asynchronous Processing Layer: Handles asynchronous tasks and communication between services using tools like RabbitMQ, Kafka, or AWS SQS
        - Service Discovery Layer: Allows microservices to locate and communicate with each other dynamically, often implemented using tools like Consul, Zookeeper, or Kubernetes' internal DNS
        - Orchestration & Scheduling Layer: Manages workflows and scheduled tasks, such as with Airflow, Kubernetes CronJobs, or AWS Step Functions.
        - CDN (Content Delivery Network): Speeds up delivery of static and dynamic content to users by caching data geographically closer to them
            - Push CDN: Push content from server to CDN before it is requested. More performant, but cold start and storage costs higher b/c you may store useless stuff in CDN
            - Pull CDN: Pull content from server when it is requested AND not already in CDN. Less performant, but you store less things

    - User Experience & Engagement:
        - Authentication & Authorization Layer: Manages user identity, authentication (e.g., OAuth, JWT), and access control to ensure secure operations
        - Notification Layer: Sends alerts and notifications (e.g., email, SMS, push notifications) to users, often implemented using tools like Twilio, Firebase Cloud Messaging, or SES.
    
    - Analytics & Insights
        - Data Analytics/ETL/Business Intelligence Layer: Processes raw data for analytics and insights, commonly using frameworks like Apache Spark, Flink, or managed solutions like AWS Glue + Tools for reporting like Tableau, Power BI, or Looker
        Event-Driven Architecture
        - Event-Driven Architecture: Processes real-time events for analytics or system actions (Tools: Kafka, AWS EventBridge)
        - Experimentation Layer: Supports A/B testing or feature flagging using tools like Optimizely, LaunchDarkly, or in-house frameworks.

    - Developer Operations & Infrastructure
        - DevOps Layer: Infrastructure as code, CI/CD pipelines, and container orchestration are critical for deploying, managing, and scaling applications efficiently.
        - Testing/QA: Ensures system quality via automated and manual testing frameworks.
        - Audit & Compliance Logging: Records tamper-proof logs for compliance and forensic analysis.

3. Dive into specifics to meet **non-functional** requirements
    - Database design
        - NoSQL / SQL / GraphDB
            - How to shard a DB? **HASH RING**, to avoid incurring rehashing of every entry in the DB
        - Schema (table, primary key, indexes)
        - Algorithms (if any)
            - e.g. in recommender system, collaborative filtering
            - e.g. in URL shortener, SHA256 vs MD5
        - APIs
            - Define API endpoints, inputs, and outputs
        - Caching
            - Where?
                - Client-side/Browser?
                    - Usually used for static assets
                - Edge/Content Delivery Network (CDN)?
                    - Cache in server that are closer to users to reduce latency
                    - Popular options are Cloudflare/AWS Cloudfront
                - Application/Backend Caching?
                    - For frequent API queries to reduce load on backend 
                    - Tools are Redis, Memcached
                    - e.g. Cache results of a /getUserDetails API call for a short duration if user data doesn’t change frequently 
                - Database Caching
                    - If query is frequent and computationally expensive, cache 
                    - e.g. Store results of analytics queries in Redis to avoid repeating complex joins or aggregations.
            - Types?
                - Key-Value Store Cache (e.g. Redis)
                    - Can cache structured/unstructured data
                - Object Cache
                    - Cache entire object/model in memory to avoid serialisation
            - How?
                - Cache-Aside
                    - This is lazy loading
                    - For any query, check cache first. Application will hit DB what you want isn't found in cache
                - Write-through
                    - Write to both the cache and database at the same time when new value is available
                - Read-through  
                    - For any query, check cache first. Cache will hit DB what you want isn't found in cache
                    - This is like `Cache-Aside`, except instead of having your application hitting your database on cache miss, the cache does it instead
                - Write-back
                    - This is like write-through, except the writing to database is asynchronous from writing to cache
                    - Very useful in write heavy databases!!
            - Eviction?
                - Least Recently Used (LRU) eviction: Cache of size `x`, and if it exceeds `x` , the first item gets evicted
                    - Usually implemented as a hashmap (to access every item in cache in O(1)) coupled with a doubly linked list
                - Least Frequently Used (LFU): Remove the least frequently accessed data
                - Time-to-Live (TTL): Automatically remove cached items after a certain period
            - Tradeoffs with caching:
                - Consistency: Cached data can become stale
                    - Mitigation: Use short TTLs for frequently changing data
                    - Mitigation: Invalidate or update the cache on database updates 
                        - Example: If user profile data changes, invalidate the cached profile entry
                - Memory usage: Redis is expensive!
                - Cold start: Initially, without values in the cache, latency can be high
                - Cache Stampede: If multiple clients request the same cache-miss, then all of them hit the database at the same time.
                    - Mitigation: Coalesce requests (i.e. all same requests within some time window are combined before hitting DB)
                    - Mitigation: Acquire lock for requested data, so block other clients from requesting the same data
        - Communication:
            - Synchronous (REST/gRPC) vs. Asynchronous (message queues like Kafka). Considerations:
                - Failure management: Synchronous has immediate feedback on failure; async communication are non-blocking but will require more effort to deal with failures
                    - Async systems will need retries, dead letter queues, error processing
                - Scalability: Synchronous systems may struggle with traffic spikes unless they are backed by load balancers/replicated services. Async systems can just scale out the queue
                - Consistency: Async systems are eventually consistent by definition, because the request and processing time are decouplde
                - Coupling: Synchronous systems are more tightly coupled, so if the server fails, the entire flow breaks. Async systems are more tolerant of failure
            - REST vs gRPC
                - gRPC is strongly typed through protobuf, REST is not
                - gRPC has native support for streaming, REST will require some sort of web socket connection/polling
                - gRPC is often faster due to protobuf usage, resulting in smaller payload
            - REST/gRPC vs GraphQL
                - graphQL usually preferred for data heavy applications, because you can specify the exact field you want
                - Best explained with an example
                    - Imagine you're building an e-commerce platform where customers can view products. Each product can have multiple variants (e.g., size, color) and each variant might have different attributes (e.g., price, stock level, images).
                    - For REST, you may need multiple endpoints
                        - GET /products: Retrieves a list of products
                        - GET /products/{product_id}: Retrieves details for a specific product
                        - GET /products/{product_id}/variants: Retrieves all variants of a product
                        - GET /products/{product_id}/variants/{variant_id}: Retrieves details of a specific variant
                    - For graphQL, you can specify the exact fields you need
                        - ```   
                            query {
                                product(id: "123") {
                                    id
                                    name
                                    price
                                    variants {
                                    id
                                    color
                                    size
                                    price
                                    stockLevel
                                    }
                                }
                            }
                        ```
            - REST vs Websocket/Polling/Long-Polling
                - REST is 1 off, you send a request, you get a response
                - Some use cases are a little more sophisticated (i.e. FB messenger). How would we do this?
                    - Ask the server "do i have a new message" every second (i.e. **polling**)
                        - This is wasteful, because you need to maintain the connection 
                    - Ask the server "do i have a new message" once, and have the server maintain the request. When a new message comes in for me, the server returns. (i.e. **long polling**)
                        - This is less wasteful, but you still need to maintain a connection somewhere. 
                        - And it doesn't really help if traffic is "bursty". Like if you are 99% inactive, but 1% of the time you may get a flood of messages
                    - Maintain an open 2-way connection with the server for the duration of a session (i.e. websocket)
                        - This is ideal for things like FBM, but there is limit to how many connections a server can have
                        - Websockets need to connect to specific ports via TCP protocol, and each port number is about 16 bits. This gives us around 65000 ports to work with

4. Talk about Scale/Reliability/Other non-functional requirements
    - Latency:
        - Cache frequently accessed data?
        - Optimize query performance with indexes?
    - Availability:
        - Replicas for database?
        - Multi-region deployment?
    - Consistency:
        - Are you strongly consistent or eventually consistent?
            - Twitter, eventual consistency is probably fine
            - ECommerce/Banking, you want strong consistency. Can't be that someone places an order that goes through when stock is already 0
    - Monitoring/Logging:
        - Grafana/Prometheus to track system health?

5. Talk about tradeoffs
    - Performance vs. Cost:
        - Example: In-memory caching is fast but expensive
    - Consistency vs. Availability:
        - Example: Strong consistency can hurt availability in a distributed system
    - Latency vs. Complexity:
        - Example: Adding caching improves latency but increases system complexity

6. Future enhancements
    - Add new features?
    - Scaling strategy?
    - Automation of scaling/backup/monitoring?