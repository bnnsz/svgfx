
# Contributing to SVGFX

First off, thank you for considering contributing to SVGFX.

## Where do I go from here?

If you've noticed a bug or have a feature request, make sure to check our [issues](https://github.com/bnnsz/svgfx/issues) if there's something similar. If there isn't, you can open a new issue.

## Fork & create a branch

If this is something you think you can fix, then fork SVGFX and create a branch with a descriptive name.

A good branch name would be (where issue #325 is the ticket you're working on):

```bash
git checkout -b feature/325-add-jump-to-definition
```

## Implement your fix or feature

At this point, you're ready to make your changes! Feel free to ask for help; everyone is a beginner at first.

## Test your changes

Ensure that your code works as expected and all existing tests pass.

## Create a pull request

At this point, you should switch back to your master branch and make sure it's up to date with SVGFX's master branch:

```bash
git remote add upstream git@github.com:bnnsz/svgfx.git
git checkout master
git pull upstream master
```

Then update your feature branch from your local copy of master, and push it!

```bash
git checkout feature/325-add-jump-to-definition
git rebase master
git push --set-upstream origin feature/325-add-jump-to-definition
```

Go to the SVGFX repo and press the "Compare & pull request" button.

## Wait for me

I will check your changes and merge them if they are of high quality. If there are any issues, I will give you feedback so you can make necessary changes.

## That's it! Thank you for your contribution!

After your pull request is merged, you can safely delete your branch and pull the changes from the main (upstream) repository.
