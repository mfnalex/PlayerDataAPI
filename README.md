## Dependency
```xml
<repository>
  <id>jeff-media-public</id>
  <name>JEFF Media GbR Repository</name>
  <url>https://repo.jeff-media.com/public</url>
</repository>
<dependency>
  <groupId>com.jeff_media.playerdatapi</groupId>
  <artifactId>playerdataapi-dist</artifactId>
  <version>1.0-SNAPSHOT</version>
  <scope>provided</scope>
</dependency>
```

## Usage
1. Depend on "PlayerDataAPI"
2. Get the API through the Plugin Manager, and cast it to PlayerDataAPI
3. See src/main/tests for details about usage

### Spigot
```java
PlayerDataAPI playerDataAPI = (PlayerDataAPI) Bukkit.getPluginManager().getPlugin("PlayerDataAPI");
DataProvider provider = playerDataAPI.getProvider();
```

### BungeeCord
```java
PlayerDataAPI playerDataApi = (PlayerDataAPI) ProxyServer.getInstance().getPluginManager().getPlugin("PlayerDataAPI");
DataProvider provider = playerDataApi.getProvider();
```

# MySQL & Redis configuration
For the unit tests, you can just spin up a MySQL and Redis server on localhost. The unit tests will automatically use the default ports and default credentials.
```docker
docker run -p 3306:3306 -e MARIADB_PASSWORD=test -e MARIADB_USER=test -e MARIADB_DATABASE=test -e MARIADB_RANDOM_ROOT_PASSWORD=y -d mariadb:latest
```
