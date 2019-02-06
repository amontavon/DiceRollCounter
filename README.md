# DiceRollCounter
Android application for keeping track of D20 rolls.

<b>Purpose:</b> This app is designed to keep track of d20 rolls for the tabletop game Dungeons and Dragons and present the data visually for the player. For each roll, the user can select the specific d20 they are using, the character they are playing as, what was rolled, and the type of roll it was. The number of rolls for each value of the d20 is displayed, and the user can filter this display by the die they rolled, the character they were using, and the type of roll.

<b>Use:</b> The top fragment is the Filter Fragment. To filter the data displayed in the Counter Fragment, you may select a value from any of these drop downs. With the exception of the roll type drop down, the other two will start empty. To populate these drop downs, you will need to tap on the settings button in the top right corner.

The settings activity will allow you to add or delete a die or character. To add, enter the name of your die or character and hit submit (NOTE: at this time, there are no checks to make sure the name you enter is valid and not a duplicate name). To delete, select the die or character you would like to delete. Then select whether you would like to keep the data associated with that die/character, or if you will want to delete all associated data. Keeping the data will keep the rolls, but reassign that die or character to the value "All" instead. Deleting the data removes it from the database.

The bottom fragment is the Update Fragment. This is where you can input your roll. You may select the die you rolled with, the character you are currently playing, the result of the roll (the natural result--at this time, the app does not support adding modifiers to the roll), and the type of roll you are making. Once you have selected the information you want to add, tap the Roll! button. The Counter Fragment will update to reflect the change.

<b>Planned updates:</b><br>
-Validation when adding a die/character<br>
-UI improvements<br>
-Ability to input whether a roll succeeded or failed<br>
-Ability to filter by whether a roll succeeded or failed<br>
-Ability to build graphs to better represent the data collected<br>
-Performance improvements as needed
