# KianBot-v2

Discord music bot built with Java 21, JDA, LavaPlayer and Discord DAVE voice encryption support.

## Requirements

- Java 21+
- Maven Wrapper (included: `mvnw` / `mvnw.cmd`)
- A Discord bot token

## Configuration

Create a `.env` file in `KianBot2` (or provide the `TOKEN` environment variable):

```env
TOKEN=your_discord_bot_token
```

Do not commit the `.env` file or expose the bot token.

## Build

From `KianBot2`:

```powershell
.\mvnw.cmd clean verify
```

The packaged application is produced under `target/`.

## Development

The main entry point is `com.ktsocial.kianbot.KianBot`.

The music subsystem is centered around `PlayerManager`, `GuildMusicManager` and `TrackScheduler`.

## Commands

Use `/help` in a Discord text channel to see the available commands.

## Project notes

The project is being incrementally refactored toward clearer separation between Discord event handling and music-domain logic. Changes are intentionally kept small so each phase can be verified independently.
