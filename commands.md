# Commands

Legend:

| Annotation | Meaning                |
| ------     | ------                 |
| (   )      | Optional argument      |
| Embed      | The name of the embed  |

## CoreCommands

| Command    | Arguments    | Effect                                    |
| ------     | ------       | ------                                    |
| Send       |              | Send the currently loaded embed.          |
| Create     | Embed        | Create a new embed with this name.        |
| Delete     | (Embed)      | Delete the embed with this name.          |
| Load       | Embed        | Load the embed with this name into memory.|
| Copy       | Embed, Mesage| Copy an embed by its message ID.          |
| Import     | JSON String  | Import a JSON String as an embed.         |
| Export     |              | Export the currently loaded embed to JSON.|
| ListEmbeds |              | List all embeds created in this guild.    |

## EditCommands
| Command        | Arguments   | Effect                                              |
| ------         | ------      | ------                                              |
| SetTitle       | Title       | Set the title for the currently loaded embed.       |
| SetDescription | Description | Set the description for the currently loaded embed. |
| SetAuthor      | Author      | Set the author for the currently loaded embed.      |
| SetColor       | Hex color   | Set the color for the currently loaded embed.       |
| SetThumbnail   | Image URL   | Set the thumbnail for the currently loaded embed.   |
