# @(#) $Header: 4.0 Standard ~/.login$
# November 1995 Dipl. Phys. Thomas Scheunemann    HRZ Uni -GH- Duisburg
# Diese Datei sollte vom Benutzer nicht geaendert werden, um ein spaeteres
# Ersetzen durch eine neuere Version zu Ermoeglichen. Wenn Sie Anpassungen
# vornehmen wollen, legen Sie eine Datei .mylogin an. Diese wird, wenn sie
# vorhanden ist im Anschluss an dieses Skript ausgefuehrt.

# Zur Referenz
if( ! $?RC_FILES ) then
	setenv RC_FILES .login
else
	setenv RC_FILES ${RC_FILES}:.login
endif

# Default TERM
if ( ! $?TERM ) then
	setenv TERM unknown
endif

# Default HOME
if ( ! $?HOME ) then
	setenv HOME /
endif

# Versuche das Terminal richtig zu setzen
stty erase ^h kill ^u intr ^c eof ^d susp ^z start ^q stop ^s echoe
if ( -r $HOME/.ttytype ) then
	# Wenn die Datei .ttytype existiert, dann benutze deren Inhalt
	setenv TERM `cat $HOME/.ttytype`
	set noglob
	eval `tset -s -Q $TERM`
else
	# Ist dies eventuell ein X-Terminal
	if ( $?DISPLAY ) then
		# Falls nichts vorgegeben ist
		if( $TERM == unknown ) then
			setenv TERM xterm
		endif
	else
		# Wenn kein Terminaltyp vorhanden
		if( $TERM == unknown ) then
			setenv TERM vt100
		endif
	endif
	# Gibt es einen alten Terminaltyp
	if ( -r $HOME/.savettytype ) then
		setenv TERM `cat $HOME/.savettytype`
	endif
	@ count=0
	while ( $count < 3 )
		set noglob
		eval `tset -s -Q -m :?$TERM`
		if ( $TERM == unknown ) then
			@ count+=1
			setenv TERM vt100
		else
			@ count=4
		endif
	end
	echo $TERM > $HOME/.savettytype
endif

# DISPLAY an andere Rechner weitergeben
if ( $?DISPLAY ) then
	echo $DISPLAY > $HOME/.display
else
	# Gibt es einen alten DISPLAY-Eintrag
	if ( -r $HOME/.display ) then
		setenv DISPLAY `cat $HOME/.display`
		echo Assumed DISPLAY=$DISPLAY
	endif
endif

# Setze LINES und COLUMNS (werden von der Shell verwaltet)
setenv LINES
setenv COLUMNS

# Setze Standard Pager
setenv PAGER less

# Setze Standard Editor
setenv EDITOR pico
setenv VISUAL pico

# Verhindere das Ausloggen durch ^D
set ignoreeof

# Fuehre nun die Benutzereigene Datei .mylogin aus
if ( -r $HOME/.mylogin ) then
	source $HOME/.mylogin
endif
