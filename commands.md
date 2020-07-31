# Commands

## Key
| Symbol     | Meaning                    |
| ---------- | -------------------------- |
| (Argument) | This argument is optional. |

## Cluster
| Commands          | Arguments                         | Description                                                  |
| ----------------- | --------------------------------- | ------------------------------------------------------------ |
| AddToCluster      | Cluster, Embed...                 | Add an embed into a cluster.                                 |
| CloneCluster      | Cluster Name, (Channel), Amount   | Clone a group of embeds into a cluster.                      |
| CreateCluster     | Cluster Name, (Embed...)          | Create a cluster for storing and deploying groups of embeds. |
| DeleteCluster     | Cluster                           | Delete a cluster and all of its embeds.                      |
| Deploy            | Cluster, (Channel), (shouldTrack) | Deploy a cluster into a target channel.                      |
| InsertIntoCluster | Cluster, Index, Embed             | Insert an embed into a cluster at an index.                  |
| RemoveFromCluster | Embed...                          | Remove embeds from their current cluster.                    |
| RenameCluster     | Cluster, New Name                 | Change the name of an existing cluster.                      |
| UpdateCluster     | Cluster                           | Update the original embeds this cluster was copied from.     |

## Copy
| Commands       | Arguments                         | Description                                             |
| -------------- | --------------------------------- | ------------------------------------------------------- |
| CopyTarget     | Embed Name, (Channel), Message ID | Copy an embed by its message ID.                        |
| UpdateOriginal |                                   | Update the original embed this content was copied from. |
| UpdateTarget   | (Channel), Message ID             | Replace the target message embed with the loaded embed. |

## Core
| Commands   | Arguments                  | Description                                |
| ---------- | -------------------------- | ------------------------------------------ |
| Create     | Embed Name                 | Create a new embed with this name.         |
| Delete     | (Embed...)                 | Delete the embed with this name.           |
| Duplicate  | Embed Name, (Embed)        | Create a new embed from an existing embed. |
| ExecuteAll | Commands                   | Execute a batch of commands in sequence.   |
| Export     | (Embed), (preferFile)      | Export the currently loaded embed to JSON. |
| Import     | Embed Name, File \| String | Import a JSON file or string as an embed.  |
| Load       | Embed                      | Load the embed with this name into memory. |
| Send       | (Channel), (shouldTrack)   | Send the currently loaded embed.           |

## Edit
| Commands       | Arguments         | Description                                         |
| -------------- | ----------------- | --------------------------------------------------- |
| Clear          | (Clear Target)    | Clear a target field from the loaded embed.         |
| Rename         | (Embed), New Name | Change the name of an existing embed.               |
| SetAuthor      | User              | Set the author for the currently loaded embed.      |
| SetColor       | Hex Color         | Set the color for the currently loaded embed.       |
| SetDescription | Text              | Set the description for the currently loaded embed. |
| SetFooter      | Icon URL, Text    | Set the footer for the currently loaded embed.      |
| SetImage       | URL               | Set the image for the currently loaded embed.       |
| SetThumbnail   | URL               | Set the thumbnail for the currently loaded embed.   |
| SetTimestamp   |                   | Set the timestamp for the currently loaded embed.   |
| SetTitle       | Text              | Set the title for the currently loaded embed.       |

## Field
| Commands       | Arguments               | Description                                              |
| -------------- | ----------------------- | -------------------------------------------------------- |
| AddBlankField  | (isInline)              | Add a blank field to the loaded embed.                   |
| AddField       | Field Data              | Add a field in the following format: title\|body\|inline |
| InsertField    | Index, Field Data       | Insert a field at an index to the loaded embed.          |
| RemoveField    | Field Index             | Remove a field from the loaded embed by its index.       |
| SetField       | Field Index, Field Data | Edit a field at a given index with the given data.       |
| SetFieldInline | Field Index, Boolean    | Get a field by its index and edit its inline value.      |
| SetFieldText   | Field Index, Text       | Get a field by its index and edit its text value.        |
| SetFieldTitle  | Field Index, Text       | Get a field by its index and edit its title value.       |

## GuildConfiguration
| Commands  | Arguments | Description                                                |
| --------- | --------- | ---------------------------------------------------------- |
| DeleteAll |           | Delete all embeds and clusters in this guild.              |
| SetPrefix | Prefix    | Set the prefix required for the bot to register a command. |
| SetRole   | Role      | Set the role required to use this bot.                     |

## Information
| Commands   | Arguments | Description                             |
| ---------- | --------- | --------------------------------------- |
| Info       | (Embed)   | Get extended info for the target embed. |
| Limits     |           | Display the discord embed limits.       |
| ListEmbeds |           | List all embeds created in this guild.  |

## Owner
| Commands    | Arguments                         | Description                                             |
| ----------- | --------------------------------- | ------------------------------------------------------- |
| Broadcast   | Text                              | Send a direct message to all guild owners.              |
| GuildReport |                                   | Get a complete list of guilds.                          |
| Kill        |                                   | Kill the bot. It will remember this decision.           |
| Leave       | (Guild)                           | Leave this guild and delete all associated information. |
| Transfer    | (Embed), Target Guild, (New Name) | Send an embed to another guild.                         |

## Utility
| Commands             | Arguments | Description                              |
| -------------------- | --------- | ---------------------------------------- |
| Help                 | (Command) | Display a help menu.                     |
| Status, Ping, Uptime |           | Display network status and total uptime. |

