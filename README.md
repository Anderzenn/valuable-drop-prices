# Valuable Drop Prices
This plugin adds the option to display High Alch prices, in the Valuable Drops notification in chat.
The plugin is **still under development**, but should be working well.

**Make sure the default drop notifications are enabled!**

You can set a minimum amount for notifications, if you want only the GE price, the HA price, or both at the same time.
You can also modify the colour of the chat notification.
(Colour modifications, as well as which price has priority is temporarily disabled as I work on a better implementation)

# Known Issues
- If display type is set to i.e. High Alch, but primary value is set to Both and and the item has no value in HA, but does in GE - Then the valueable drop will say "no value" even though it has a GE value.

If you spot any issues or have any requests, please let me know under the issues tab!

# To do
- Add handling of conflicting display type and primary value.
- Redo colour modification
