# User Guide

Guide style: 
* The assumed prefix will be `e!`.
* Commands use a readable format, but case does not matter (`Help` vs `help`).

<details>
<summary>The Basics</summary>

Before we start working, we need to create a new embed. You can do this with the `Create` command.
* `e!Create hello`

This creates a new embed, but it's empty. Let's add some text.
* `e!SetTitle Hello World`

Now you can `Send` it.
* `e!Send`

You've just created your first embed!
</details>

<details>
<summary>Commands</summary>

To find information about commands, you can use the `Help` feature.
* `e!Help` will list all available commands.
* `e!Help [Command]` will give help for a specific command.
</details>

<details>
<summary>Loaded Embed</summary>

This bot can store as many embeds as you want. This means that the bot needs to know which embed you're talking about when you run a command. It does this by keeping one embed loaded. This allows you to edit embeds without having to specify the name each time.

* You can run `e!ListEmbeds` to see all the embeds you have, including the loaded one.
* You can run `e!Load [Embed Name]` to load a different embed.

</details>

<details>
<summary>Updating Sent Embeds</summary>



</details>
