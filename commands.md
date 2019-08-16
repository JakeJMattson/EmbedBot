# Commands

## Key
| Symbol     | Meaning                    |
| ---------- | -------------------------- |
| (Argument) | This argument is optional. |

## BotConfiguration
| Commands  | Arguments      | Description                                                       |
| --------- | -------------- | ----------------------------------------------------------------- |
| Leave     | <none>         | Leave this guild and delete all associated information.           |
| ResetBot  | (Bot Owner ID) | Delete all embeds in all guilds. Delete all guild configurations. |
| SetPrefix | Prefix         | Set the prefix required for the bot to register a command.        |

## GuildConfiguration
| Commands        | Arguments     | Description                                   |
| --------------- | ------------- | --------------------------------------------- |
| DeleteAll       | <none>        | Delete all embeds and clusters in this guild. |
| SetRequiredRole | Role          | Set the role required to use this bot.        |
| Setup           | Required Role | Set up this bot for use.                      |

## Core
| Commands   | Arguments                | Description                                |
| ---------- | ------------------------ | ------------------------------------------ |
| Create     | Embed Name               | Create a new embed with this name.         |
| Delete     | (Embed)                  | Delete the embed with this name.           |
| Duplicate  | Embed Name, (Embed)      | Create a new embed from an existing embed. |
| Export     | (Embed)                  | Export the currently loaded embed to JSON. |
| Import     | Embed Name, JSON         | Import a JSON String as an embed.          |
| Load       | Embed                    | Load the embed with this name into memory. |
| Send       | (Channel), (shouldTrack) | Send the currently loaded embed.           |
| SilentMode | On or Off                | Silent mode ignores                        |

## Copy
| Commands       | Arguments                         | Description                                             |
| -------------- | --------------------------------- | ------------------------------------------------------- |
| CopyPrevious   | Embed Name, (Channel)             | Copy the previous embed in the target channel.          |
| CopyTarget     | Embed Name, (Channel), Message ID | Copy an embed by its message ID.                        |
| UpdateOriginal | <none>                            | Update the original embed this content was copied from. |

## Field
| Commands        | Arguments               | Description                                              |
| --------------- | ----------------------- | -------------------------------------------------------- |
| AddBlankField   | (isInline)              | Add a blank field to the loaded embed.                   |
| AddField        | Field Data              | Add a field in the following format: title\|body\|inline |
| EditField       | Field Index, Field Data | Edit a field at a given index with the given data.       |
| EditFieldInline | Field Index, Boolean    | Get a field by its index and edit its inline value.      |
| EditFieldText   | Field Index, Text       | Get a field by its index and edit its text value.        |
| EditFieldTitle  | Field Index, Text       | Get a field by its index and edit its title value.       |
| InsertField     | Index, Field Data       | Insert a field at an index to the loaded embed.          |
| RemoveField     | Field Index             | Remove a field from the loaded embed by its index.       |

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
| SetTimestamp   | <none>            | Set the timestamp for the currently loaded embed.   |
| SetTitle       | Text              | Set the title for the currently loaded embed.       |

## Information
| Commands   | Arguments | Description                             |
| ---------- | --------- | --------------------------------------- |
| Info       | (Embed)   | Get extended info for the target embed. |
| Limits     | <none>    | Display the discord embed limits.       |
| ListEmbeds | <none>    | List all embeds created in this guild.  |

## Utility
| Commands     | Arguments | Description                                 |
| ------------ | --------- | ------------------------------------------- |
| BotInfo      | <none>    | Display the bot information.                |
| ListCommands | <none>    | List all available commands.                |
| Ping         | <none>    | Display the network ping of the bot.        |
| Source       | <none>    | Display the (source code) repository link.  |
| Uptime       | <none>    | Displays how long the bot has been running. |
| help         | (Word)    | Display a help menu                         |

