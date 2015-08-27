#ARKCraft: Survival Evolved Code

ARKCraft Survival Evolved is a mod that adds the features from ARK: Survival Evolved into minecraft.
![Image of Logo](http://i61.tinypic.com/ou8phc.jpg)

Many features are added, such as:

1. Dinos, birds, prehistoric creatures and more models are added into the game.
2. New breeding and taming mechanics are added into the game, enabling you to ride and play with the creatures that ARKCraft    provides.
3. Many items and new GUI's are added into the game, enabling you to tame and breed dinos.

If you would like more information and screenshots, take a look at our [forum page](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/wip-mods/2482915-wip-arkcraft-survival-evolved-dinos-taming).



-------------------------------------------
Source installation information for Contributers
-------------------------------------------

This code follows the Minecraft Forge installation methodology. It will apply
some small patches to the vanilla MCP source code, giving you and it access 
to some of the data and functions you need to build a successful mod.

Note also that the patches are built against "unrenamed" MCP source code (aka
srgnames) - this means that you will not be able to read them directly against
normal code.

Source pack installation information:

#Standalone source installation

##Eclipse

1. Open your command line to where you cloned this repo.

2. Once you have a command window up in the folder that the downloaded material was placed, type:

Windows: "gradlew setupDecompWorkspace"
Linux/Mac OS: "./gradlew setupDecompWorkspace"

3. After all that finished, you're left with a choice.
For eclipse, run "gradlew eclipse" (./gradlew eclipse if you are on Mac/Linux)
Look Below for IntelliJ.

4. Create another folder, for your workspace.

5. Set your workspace to that folder, then import your built cloned repo folder using **File>Import.** Select the root folder as your built cloned repo folder.

Then your done! You have this repo inside your workspace, and can edit whatever you want.

####Terminoligy:

- *Commit* - Gets your changes ready to push.
- *Push* - Gets your changes published onto the repo.
- *Pull* - Synchs up your repo with other people's repos.

####Useful Commands for git:

- **git commit -m "My Commit Message"** - Commits your uncommited changes.
- **git push -u origin master** - Pushes your commited changes.
- **git pull -u origin master** - Pulls any unsynched changes.
- **git rm --cached <filename>** - Ignores a file.
- **git rm --cached -r <foldername>** - Ignores an entire folder and the contents within it. 

##IntelliJ IDEA

If you preffer to use IntelliJ, steps are a little different.
1. Open IDEA, and import project.
2. Select your build.gradle file and have it import.
3. Once it's finished you must close IntelliJ and run the following command:

"gradlew genIntellijRuns" (./gradlew genIntellijRuns if you are on Mac/Linux)

Step 4: The final step is to open Eclipse and switch your workspace to /eclipse/ (if you use IDEA, it should automatically start on your project)

If at any point you are missing libraries in your IDE, or you've run into problems you can run "gradlew --refresh-dependencies" to refresh the local cache. "gradlew clean" to reset everything {this does not effect your code} and then start the processs again.

Should it still not work, 
Refer to #ForgeGradle on EsperNet for more information about the gradle environment.

Tip:
If you do not care about seeing Minecraft's source code you can replace "setupDecompWorkspace" with one of the following:
"setupDevWorkspace": Will patch, deobfusicated, and gather required assets to run minecraft, but will not generated human readable source code.
"setupCIWorkspace": Same as Dev but will not download any assets. This is useful in build servers as it is the fastest because it does the least work.

Tip:
When using Decomp workspace, the Minecraft source code is NOT added to your workspace in a editable way. Minecraft is treated like a normal Library. Sources are there for documentation and research purposes and usually can be accessed under the 'referenced libraries' section of your IDE.

