; Definitions that are specific to the IBM PC.

; colors

black   = 0
blue    = 1
green   = 2
cyan    = 3
red     = 4
magenta = 5
brown   = 6
gray   = 7
light_gray    = 8
light_blue    = 9
light_green   = 10
light_cyan    = 11
light_red     = 12
light_magenta = 13
yellow        = 14
white         = 15

.macro ATTRIBUTE(foreground,background) ( ((background) << 4) + (foreground))

; special characters

CR = 13
LF = 10