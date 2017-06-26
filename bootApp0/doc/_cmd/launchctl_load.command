cd ~/_PRJ/ecprj/bootContact.git/bootApp0/doc/_cmd
cp ./bootApp0.plist ~/Library/LaunchAgents/bootApp0.plist
launchctl load ~/Library/LaunchAgents/bootApp0.plist
launchctl list | grep bootApp0

