# InternCal

I wrote this because making intern schedules by hand is a nightmare. This is just a simple Java tool that tries to distribute night shifts (gardes) and day duties (HDJ) as fairly as possible so nobody ends up with a burnout.

It simulates a 12-week semester and makes sure the workload is balanced using a basic priority system.

## What it actually does

Instead of guessing who should work when, the script follows two simple rules:
1. **Fill the gaps:** If we don't have enough people for the week, it grabs the person who has worked the *least* so far and gives them a double shift.
2. **Respect the rest:** When assigning a day, it picks the person who has been resting the longest. No more working Monday, Tuesday, and Wednesday in a row.

## How to run it

You don't need anything fancy, just Java.

1. Put `InternCal.java` and `Intern.java` in the same folder.
2. Compile it:
   ```bash
   javac *.java
