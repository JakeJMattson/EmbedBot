# User Guide

Guide style: 
* The assumed prefix will be `e!`.
* Commands use a readable format, but case does not matter (`Help` vs `help`).

## The Basics

Before we start working, we need to create a new embed. You can do this with the `Create` command.
* `e!Create hello`

This creates a new embed, but it's empty. Let's add some text.
* `e!SetTitle Hello World`

Now you can display it on discord by sending it.
* `e!Send`

You've just created your first embed!

## Commands

To find information about commands, you can use the `Help` feature.
* `e!Help` will list all available commands.
* `e!Help [Command]` will give help for a specific command.

## Editing Embeds

If your embed is more permanent, like something in a #rules channel, we want to have a way to update it.

The easiest way is to keep track of your embed when you send it.

`e!Send true`

This saves the message information. Once you make all of your changes, you can update it with one command. 

`e!Update`

If you didn't track your embed when you first sent it, you can also update a message's embed.

`e!UpdateTarget`


## Updating Sent Embeds

## Creating Multiple Embeds

This bot can store as many embeds as you want, but this means that the bot needs to know which embed you're talking about when you run a command. It does this by keeping a single embed loaded. This allows you to edit embeds without having to specify the name each time.

* You can run `e!ListEmbeds` to see all the embeds you have.
* You can run `e!Load [Embed Name]` to load a different embed.

## Managing Multiple Embeds

## Importing Embeds

## Other useful commands