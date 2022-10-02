<img src="https://cdn.discordapp.com/app-icons/1012790735151583302/434fa2f4e66cda479124b1e5ca890e6b.png?size=128" align="left">

# TransL
A Discord bot that allows people from all over the world to communicate!  
It uses the excellent [DeepL Translator](https://deepl.com) for maximum translation accuracy.

[TransL Demo.webm](https://user-images.githubusercontent.com/68816703/192163260-4e2ed324-2d6f-4e15-a0d1-8999fcd789f6.webm)

## Features

TransL is designed so that all the text in your server is in the same language.
You can set your preferred server language through a command, and all messages will – by default – be translated into it.

If a message happens to be written in a wrong language, you can right-click it and translate it for yourself after the fact.

All commands, parameters, options, etc. are available in all Discord-supported languages, so users can always read them.
They all are shown in different languages depending on what language a user has selected in their Discord settings.

### Supported languages
TransL supports all languages DeepL supports. Currently, that's these:
*   Bulgarian
*   Czech
*   Danish
*   German
*   Greek
*   English (translating *to* English, you can choose between American and Βritish English)
*   Spanish
*   Estonian
*   Finnish
*   French
*   Hungarian
*   Indonesian
*   Italian
*   Japanese
*   Lithuanian
*   Latvian
*   Dutch
*   Polish
*   Portuguese (translating *to* Portuguese, you can choose between Portuguese for Portugal or Brazil)
*   Romanian
*   Russian
*   Slovak
*   Slovenian
*   Swedish
*   Turkish
*   Ukrainian
*   Chinese

Of course, it can also auto-detect the input language.

### [**Add TransL to your server!**](https://discord.com/api/oauth2/authorize?client_id=1012790735151583302&permissions=0&scope=bot%20applications.commands)

## Development setup
### Prerequisites
*   A PostgreSQL database (running locally or on a server)
*   IntelliJ IDEA (recommended, but other IDEs/editors will work too)

1.  [Create a Discord Application](https://discord.com/developers/applications) and add a bot user to it.
    Make sure to save the bot token somewhere secure.
2.  [Create a DeepL Developer Account](https://www.deepl.com/pro#developer). The free tier will be more than enough.
    Save the authentication key, just like the Discord bot token.
3.  Clone this repository to your local computer.
4.  Create a new file in the project root directory called `.env`, with the following configs:
    ```conf
    # The bot token for your application, which you get from the Discord Developer Portal after creating a bot account.
    TRANSL_DISCORD_BOT_TOKEN="..."

    # The authentication key for accessing DeepL, which you get after creating a DeepL Developer Account.
    TRANSL_DEEPL_AUTH_KEY="..."

    # The host (name, port, and path if needed) to the PostgreSQL database cluster.
    TRANSL_DB_HOST="localhost:5432" # this is the default after installing PostgreSQL locally

    # The name of the database.
    TRANSL_DB_NAME="transl"

    # The username of the database user.
    TRANSL_DB_USERNAME="transl"

    # The password of the database user.
    TRANSL_DB_PASSWORD="..."

    # The Guild ID used for testing. If set, the bot will register all commands only to this guild, instead of globally.
    # This is important for testing, because guild commands update instantly, as opposed to global commands, which take up to 1 hour.
    # You can get this by enabling Developer Mode in your Discord settings (under Advanced) and then right-clicking a guild.
    TRANSL_TEST_GUILD="..."
    ```
5.  You can now start developing TransL! 🎉

### Important note
When you first start TransL, it will try to translate all commands, parameters, choices, etc. to all languages.
This can take a few minutes and will use up about 17500 characters off of your DeepL budget.
All translations are cached persistently, so all later startups won't take nearly as long, and will not call DeepL anymore.

### Some useful Gradle commands
If you're using an IDE, you might not need these, but if you're running the Gradle Wrapper manually, you will.
Run them by executing `./gradlew <command>` (or `./gradlew.bat <command>` on Windows).
| command | what it does                                                                                                |
| ------: | :---------------------------------------------------------------------------------------------------------- |
|  `jar`  | builds and packages TransL into a JAR file, which you can distribute, upload to a server, or whatever else. |
| `check` | runs all automated tests in the project, to make sure everything is working as it should.                   |
| `build` | does all of the above.                                                                                      |
| `clean` | cleans the project's build directory and the build cache. Useful when some weird behavior/error arises.     |
|  `run`  | builds and runs the project.                                                                                |
|  `help` | shows a list of all available commands.                                                                     |
