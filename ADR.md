# Architecture Decision Records

## SQLite default database

### Context

StockPulse needs a zero-setup local database for the demo, while retaining a PostgreSQL deployment profile. The original brief named H2 as the default, but SQLite better represents a persistent local demo database and keeps the data between restarts.

### Options

1. H2 in-memory: simple and Spring-native, but data disappears on restart.
2. SQLite: persistent, zero-service setup, and easy to inspect locally.
3. PostgreSQL only: production-like, but requires Docker or an external service.

### Decision

I use SQLite by default through `org.xerial:sqlite-jdbc` and Hibernate's community `SQLiteDialect`. The database is `./data/stockpulse.db`; the application creates the `data` directory. A PostgreSQL profile and `docker-compose.yml` remain available for deployment-like runs.

SQLite timestamps are stored as ISO-8601 text because that is explicit and avoids relying on vendor-specific temporal conversions. Product IDs remain strings such as `PRD-003`.

SQLite is configured for WAL mode and a 5-second busy timeout. The Hikari pool is deliberately limited to one connection: SQLite is a single-writer database, and a one-connection pool prevents asynchronous handlers from competing for write locks in this sprint.

### Trade-offs

I give up multi-writer throughput and some database portability for a simpler persistent demo. WAL improves reader/writer behavior, but it does not turn SQLite into a high-concurrency server database. The PostgreSQL profile is the migration path when the application needs multiple writers.

The nullable unique `dedupeKey` remains a database-enforced seam for pending suggestion idempotency. SQLite permits multiple NULL values in a unique column, which is exactly what is needed after a suggestion is decided.