# Commands

## Core
| Commands   | Arguments        | Description                                |
| ---------- | ---------------- | ------------------------------------------ |
| Create     | Embed Name       | Create a new embed with this name.         |
| Delete     | Embed Name       | Delete the embed with this name.           |
| Export     | <none>           | Export the currently loaded embed to JSON. |
| Import     | Embed Name, JSON | Import a JSON String as an embed.          |
| ListEmbeds | <none>           | List all embeds created in this guild.     |
| Load       | Embed Name       | Load the embed with this name into memory. |
| Send       | <none>           | Send the currently loaded embed.           |
  
## Copy
| Commands     | Arguments                           | Description                                    |
| ------------ | ----------------------------------- | ---------------------------------------------- |
| Copy         | Embed Name, TextChannel, Message ID | Copy an embed by its message ID.               |
| CopyPrevious | Embed Name, TextChannel             | Copy the previous embed in the target channel. |

## Field
| Commands    | Arguments               | Description                                            |
| ----------- | ----------------------- | ------------------------------------------------------ |
| AddField    | Field Data              | Add a field in the following format: title\|body\|inline |
| EditField   | Field Index, Field Data | Edit a field at a given index with the given data.     |
| RemoveField | Field Index             | Remove a field from the loaded embed by its index.     |

## Edit
| Commands       | Arguments      | Description                                         |
| -------------- | -------------- | --------------------------------------------------- |
| SetAuthor      | Text           | Set the author for the currently loaded embed.      |
| SetColor       | Hex Color      | Set the color for the currently loaded embed.       |
| SetDescription | Text           | Set the description for the currently loaded embed. |
| SetFooter      | Icon URL, Text | Set the footer for the currently loaded embed.      |
| SetImage       | URL            | Set the image for the currently loaded embed.       |
| SetThumbnail   | URL            | Set the thumbnail for the currently loaded embed.   |
| SetTimestamp   | <none>         | Set the timestamp for the currently loaded embed.   |
| SetTitle       | Text           | Set the title for the currently loaded embed.       |

## Clear
| Commands         | Arguments | Description                                            |
| ---------------- | --------- | ------------------------------------------------------ |
| ClearAuthor      | <none>    | Clear the author from the currently loaded embed.      |
| ClearColor       | <none>    | Clear the color from the currently loaded embed.       |
| ClearDescription | <none>    | Clear the description from the currently loaded embed. |
| ClearFooter      | <none>    | Clear the footer from the currently loaded embed.      |
| ClearImage       | <none>    | Clear the image from the currently loaded embed.       |
| ClearThumbnail   | <none>    | Clear the thumbnail from the currently loaded embed.   |
| ClearTimestamp   | <none>    | Clear the timestamp from the currently loaded embed.   |
| ClearTitle       | <none>    | Clear the title from the currently loaded embed.       |

## ClearGroup
| Commands       | Arguments | Description                                                   |
| -------------- | --------- | ------------------------------------------------------------- |
| Clear          | <none>    | Clear the currently loaded embed.                             |
| ClearFields    | <none>    | Clear all fields from the currently loaded embed.             |
| ClearNonFields | <none>    | Clear all non-field entities from the currently loaded embed. |

## Utility
| Commands     | Arguments | Description                                 |
| ------------ | --------- | ------------------------------------------- |
| BotInfo      | <none>    | Display the bot information.                |
| ListCommands | <none>    | List all available commands.                |
| Ping         | <none>    | Display the network ping of the bot.        |
| Source       | <none>    | Display the (source code) repository link.  |
| Uptime       | <none>    | Displays how long the bot has been running. |
| help         | Word      | Display a help menu                         |
