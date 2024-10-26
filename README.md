# Valuable Drop Prices
This plugin adds the option to display High Alch prices, in the Valuable Drops notification in chat.
The plugin is **still under development**, but should be working well. Make sure to **disable the default notification drop in game settings**.


You can set a minimum amount for notifications, if you want only the GE price, the HA price, or both at the same time.

You can also modify the colour of the chat notification.

# Known Issues
- If default notification drops are enabled, both the plugins notifications and the default ones will be written.
  - Temporary fix: disable default in-game notifications.
- Valuable drop wasn't shown in chat, this is due to the way I am currently checking if a drop is new or old.
  - This should be a rare occurrence, but nevertheless *can* happen.
- If display type is set to i.e. High Alch, but primary value is set to Both and and the item has no value in HA, but does in GE - Then the valueable drop will say "no value" even though it has a GE value.

If you spot any issues or have any requests, please let me know under the issues tab!

# To do
- Do so that default notifications are not written in chat when the default setting is enabled.
- Add handling of conflicting display type and primary value.
- Better implementation for checking if drop is new or old.