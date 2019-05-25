# Commands

## Core
| Commands   | Arguments                           | Description                                |
| ---------- | ----------------------------------- | ------------------------------------------ |
| Copy       | Embed Name, TextChannel, Message ID | Copy an embed by its message ID.           |
| Create     | Embed Name                          | Create a new embed with this name.         |
| Delete     | Embed Name                          | Delete the embed with this name.           |
| Export     | <none>                              | Export the currently loaded embed to JSON. |
| Import     | Embed Name, JSON                    | Import a JSON String as an embed.          |
| ListEmbeds | <none>                              | List all embeds created in this guild.     |
| Load       | Embed Name                          | Load the embed with this name into memory. |
| Send       | <none>                              | Send the currently loaded embed.           |

## Edit
| Commands       | Arguments | Description                                         |
| -------------- | --------- | --------------------------------------------------- |
| SetAuthor      | Text      | Set the author for the currently loaded embed.      |
| SetColor       | Hex Color | Set the color for the currently loaded embed.       |
| SetDescription | Text      | Set the description for the currently loaded embed. |
| SetThumbnail   | URL       | Set the thumbnail for the currently loaded embed.   |
| SetTitle       | Text      | Set the title for the currently loaded embed.       |

## Clear
| Commands         | Arguments | Description                                                   |
| ---------------- | --------- | ------------------------------------------------------------- |
| Clear            | <none>    | Clear the currently loaded embed.                             |
| ClearAuthor      | <none>    | Clear the author from the currently loaded embed.             |
| ClearColor       | <none>    | Clear the color from the currently loaded embed.              |
| ClearDescription | <none>    | Clear the description from the currently loaded embed.        |
| ClearFields      | <none>    | Clear all fields from the currently loaded embed.             |
| ClearImage       | <none>    | Clear the image from the currently loaded embed.              |
| ClearNonFields   | <none>    | Clear all non-field entities from the currently loaded embed. |
| ClearThumbnail   | <none>    | Clear the thumbnail from the currently loaded embed.          |
| ClearTitle       | <none>    | Clear the title of the currently loaded embed.                |
