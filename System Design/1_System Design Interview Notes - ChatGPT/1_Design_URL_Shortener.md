1. Clarify requirements
    - How many URLs per day? What sort of size should we cater for?
        - Suppose 1 billion URLs a year
            - QPS: 100000 seconds in a day, 36.5million seconds in a year. 1B / 36.5 ~ 30QPS
            - Storage: Assume each URL is ~1KB. Then we'll need 1B KB --> 1M MB --> 1000 GB --> 1 TB
            - Conclusion: Distributed datastore and service is needed. 
    - Base-62 encoding acceptable? 
        - Yes
        - Then Base62 will give us ~1T combinations in 7 characteres, so we can shorten URL to length 7
    - Should we allow users to choose their own custom URLs?
        - Yes
        - Then we need a column in datastore to flag that URL is customised by user, as well as a check to see if key exists in database before adding a new entry
    - How long should we store the URL?
        - Indefinitely, unless user specifies otherwise
        - Therefore, at 1TB a year, in 5 years we'll need 5TB of storage
    - For duplicates, do we deduplicate, or return a different shortened URL
        - Return the same URL
        - Need a way to check if URL exists. Possibly Bloom Filter, for fast lookup?
    - Any latency/cost/scaling constraints?
        - Low latency, P90 < 100MS
        - High availability 
        - Strong consistency

2. Align Functional requirements
    - The URL shortener will shorten any URLs given into a URL with 7 base62 characters. i.e. http://google.com/some/web/page <---> http://bit.ly/aBcD123
    - Shortened URLs do not expire unless set by user
    - Need to return the same URL given the same webpage
    - Users should be able to choose their own custom URL

3. Align non-functional requirements
    - If no

4. Draw out high level architecture
    - API Gateway
        - Handles auth
    - Load Balancer
        - Since we need a distributed system (given 30 QPS), we need a load balancer to distribute load over multiple services
        - Probably distribute load using in a compute-aware manner, so no service gets overwhelemed with requests
    - Shortening Service
        - Flow
            - Check 3rd party blacklist service to check if URL is blacklisted
            - Check DB if URL already exists
            - Check URL is valid
            - Convert to base62
            - Return to User
    - Database
        - Since DB will be hit for every URL, we need to make accomodations for read-heavy system
            - Would recommend multiple read-only replicas with 1 leader
        - Limited relational data, primarily we need to lookup a short URL given a long URL and vice versa
            - Key-value database (NoSQL) preferred over SQL
            - Something like Cassandra or AWS DynamoDB would be good
        - Since the same shortened URL will need to be returned for the same long URL, strong consistency is required
            - Probably will have to rely on functionality in DDB/Cassandra with consensus algorithms to ensure consistent reads/writes
        - Synchronous replication needed, to enforce strong consistency
    - Caches
        - For such service, we expect that the bulk of our traffic will come from some frequently requested URLs. For such cases, hitting the service and the DB is very expensive
        - We can add a cache between the service and the database, to avoid such a case
        - This can be a write-through cache; so when a new write occurs, the write is stored in both the cache and the DB. This helps to ensure strong consistency, because any subsequent query will return from cache before hitting database, which allows the synchronous replication to happen
    - CDN
        - Since we expect the requests to be geographically distributed, we can use a CDN as well to reduce serving latency to people in different regions
        - We can also make use of CDN caches (edge caching) to ensure latency to users that are in different geographies remain in acceptable range

5. Discuss scalability
    - Since we are dealing with systems that may be highly concurrent, might be useful to throw in a message queue between client and shortening service, and/or between shortening service and database
        - This will help decouple the client request, URL shortening, and database write. 
        - If any component fails, we can simply add the request to a dead letter queue to be picked up after a new application gets spun up

6. Q&A with Interviewer (if any)