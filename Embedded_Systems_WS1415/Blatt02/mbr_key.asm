;------------------------------------------------------------------------------------
; Keyboard test program using the BIOS.
;------------------------------------------------------------------------------------
.include ../tools/x86_16b.inc
.include ../tools/IBM_PC.inc

.=0x7C00
  JMP( start )

mn_key: 0x90

start:

;------------------------------------------------------------------------------------
; Set segment registers and stack.
;------------------------------------------------------------------------------------

  XOR( AX, AX )
  MOV( DS, AX )
  MOV( SI, IM16( 32 ) )

  CLI()
  MOV( SS, AX )
  MOV( SP, IM16( 0x7BFE ) )
  STI()

;------------------------------------------------------------------------------------
; Make sure the video mode is 80x25 16 colors (mode 3)
;------------------------------------------------------------------------------------

  MOV( AX, IM16( 3 ) )
  INT( 0x10 )

;------------------------------------------------------------------------------------
; Clear screen by writing the same character at all the positions in video RAM.
;------------------------------------------------------------------------------------

  MOV( CX, IM16( 80 * 25 ) )
  XOR( DI, DI )
  MOV( AX, IM16( 0xB800 ) )
  MOV( ES, AX )
  MOV( AX, IM16( ( ATTRIBUTE( light_blue, black ) << 8 ) + ' ' ) )
  CLD()
  REP() STOSW()

;------------------------------------------------------------------------------------
; display string using BIOS service.
;------------------------------------------------------------------------------------

  PUSH( ES )                            ; Save ES (which points to video RAM).
  PUSH( DS )
  POP( ES )                             ; Load ES with our segment where the string
                                        ; to be displayed resides.
  MOV( AX, IM16( 0x1301 ) )             ; service code (13h) and write mode.
  MOV( BX, IM16( ATTRIBUTE( green, black ) ) )
                                        ; video page (0) and attribute byte.
  MOV( CX, IM16( welcome_length ) )     ; message length.
  MOV( DX, IM16( 0x1800 ) )             ; row and column.
  MOV( BP, IM16( welcome ) )
  INT( 0x10 )                           ; call BIOS video service interrupt.
  POP( ES )                             ; restore ES for video memory addressing.

;------------------------------------------------------------------------------------
; Display menu, highlighting the currently selected item.
;------------------------------------------------------------------------------------

MN_WAIT_FOR_KEY:
  MOV( AH, IM8( 1 ) )                   ; Service 1 of int 16h: ask if a key has
  INT( 0x16 )                           ; been pressed.
  JZ( MN_WAIT_FOR_KEY )                 ; Loop if not.

  XOR( AH, AH )                         ; service 0 of int 16h: get the key code
  INT( 0x16 )                           ; and ASCII of the character (if possible).

  MOV( AT_DISP16( mn_key ), AH )        ; Save the scan code of the key.

  SEG( ES ) MOV( AT_DISP16( 0 ), AL )   ; display the character corresponding to the
                                        ; key, on the screen (top left).
                                        ; Please note the segment override to
                                        ; address the video memory using ES instead
                                        ; of the DS default segment.

  ;convert ScanCode to hex and print
  MOV(CX, IM16(2))                      ; setup Loop
  MOV(DI, IM16(0xA0))                   ; setup Position (2nd line, 1st char)

  SHR(AH, IM8(4))                       ; Shift by 4 for get the "High-Nibble"
  LOOP_DISPLAY:
    CMP(AH, IM8(0xA))                   ; Compare AL to 10
    JB(LESS_THEN)                       ; IF AL is LESS jump to LESS_THEN
    ADD(AH, IM8('A' - 10))              ; CASE (AL > 9) ==> AL = AL + 'A' - 10
    JMP(ENDIF)                          ; Jump OVER LESS_THEN

  LESS_THEN:
    ADD(AH, IM8('0'))                   ; CASE (AL <= 9) ==> AL = AL + '0'

  ENDIF:
    SEG (ES) MOV(AT_DI_DISP16(0), AH)   ; Write Current-Nibble to VideoMem
    MOV(AH, AT_DISP16(mn_key))          ; Reload Scancode
    AND(AH, IM8(0x0F))                  ; Get the "Low-Nibble"
    ADD(DI, IM16(2))                    ; Move Position to 2nd line 2nd char
  LOOP(LOOP_DISPLAY)
JMP( MN_WAIT_FOR_KEY )

welcome:
  "The character corresponding to the last pressed key is displayed on top."
welcome_length = . - welcome

cur_address = .
.print "The last code address is %0x\n",cur_address

FILL_UNTIL( (0x7C00 + 510), 42)

0x55 0xAA

;************************************************************************************
; INT 10H services used here:
;************************************************************************************

;------------------Set video mode----------------------------------------------------
; AH = 0
;------------------------------------------------------------------------------------
; AL = desired video mode:
;   3h: 80x25
;   5h: 320x200 4 colors (CGA+)
;   6h: 640x200 2 colors (CGA+)
;  12h: 640x480 16 colors (VGA+)
;  13h: 320x200 256 colors (VGA+)


;------------------Write a string on the screen--------------------------------------
; AH = 13h
;------------------------------------------------------------------------------------
; AL = write mode
;   bit 0:
;   Update cursor after writing
;   bit 1:
;   String contains alternating characters and attributes
;   bits 2-7:
; BH = page number.
; BL = attribute if string contains only characters.
; CX = number of characters in string.
; DH,DL = row,column at which to start writing.
; ES:BP -> string to write

;************************************************************************************
; INT 16H services used here:
;************************************************************************************

;------------------Get key-----------------------------------------------------------
; AH = 0
;------------------------------------------------------------------------------------

; Returns:
; AH: scan code of the key. It depends onm the PC type, see:
; "http://webpages.charter.net/danrollins/techhelp/0057.HTM".
; AL = ASCII character code (see at the bottom of this file) or extended ASCII
; keystroke:

;  When INT 16H Fn 00H returns AL=0, then AH will contain an extended ASCII
;  keystroke as listed in these tables.  When a DOS Character I/O function
;  returns a character of 00H, you should make a second call to obtain the
;  extended ASCII value.
;
;  +------------------------------------------------------------------------+
;  - Key Hex Dec - Key       Hex Dec - Key       Hex Dec - Key      Hex Dec -
;  -------------------------------------------------------------------------+
;  - F1   3b  59 - Shift-F1   54  84 - Ctrl-F1   5e   94 - Alt-F1   68  104 -
;  - F2   3c  60 - Shift-F2   55  85 - Ctrl-F2   5f   95 - Alt-F2   69  105 -
;  - F3   3d  61 - Shift-F3   56  86 - Ctrl-F3   60   96 - Alt-F3   6a  106 -
;  - F4   3e  62 - Shift-F4   57  87 - Ctrl-F4   61   97 - Alt-F4   6b  107 -
;  - F5   3f  63 - Shift-F5   58  88 - Ctrl-F5   62   98 - Alt-F5   6c  108 -
;  - F6   40  64 - Shift-F6   59  89 - Ctrl-F6   63   99 - Alt-F6   6d  109 -
;  - F7   41  65 - Shift-F7   5a  90 - Ctrl-F7   64  100 - Alt-F7   6e  110 -
;  - F8   42  66 - Shift-F8   5b  91 - Ctrl-F8   65  101 - Alt-F8   6f  111 -
;  - F9   43  67 - Shift-F9   5c  92 - Ctrl-F9   66  102 - Alt-F9   70  112 -
;  - F10  44  68 - Shift-F10  5d  93 - Ctrl-F10  67  103 - Alt-F10  71  113 -
;  -------------------------------------------------------------------------+

;  -------------------------------------------------------------------------+
;  - Key   Hex  Dec- Key   Hex  Dec - Key      Hex  Dec - Key      Hex  Dec -
;  -------------------------------------------------------------------------+
;  - Alt-A  1e  30 - Alt-P  19   25 - Alt-3     7a  122 - down  v   50   80 -
;  - Alt-B  30  48 - Alt-Q  10   16 - Alt-4     7b  123 - left  <   4b   75 -
;  - Alt-C  2e  46 - Alt-R  13   19 - Alt-5     7c  124 - right >   4d   77 -
;  - Alt-D  20  32 - Alt-S  1f   31 - Alt-6     7d  125 - up    ^   48   72 -
;  - Alt-E  12  18 - Alt-T  14   20 - Alt-7     7e  126 - End       4f   79 -
;  - Alt-F  21  33 - Alt-U  16   22 - Alt-8     7f  127 - Home      47   71 -
;  - Alt-G  22  34 - Alt-V  2f   47 - Alt-9     80  128 - PgDn      51   81 -
;  - Alt-H  23  35 - Alt-W  11   17 - Alt--     82  130 - PgUp      49   73 -
;  - Alt-I  17  23 - Alt-X  2d   45 - Alt-=     83  131 -                   -
;  - Alt-J  24  36 - Alt-Y  15   21 -                   - ^left     73  115 -
;  - Alt-K  25  37 - Alt-Z  2c   44 - NUL       03    3 - ^right    74  116 -
;  - Alt-L  26  38 -                - Shift-Tab 0f   15 - ^End      75  117 -
;  - Alt-M  32  50 - Alt-0  81  129 - Ins       52   82 - ^Home     77  119 -
;  - Alt-N  31  49 - Alt-1  78  120 - Del       53   83 - ^PgDn     76  118 -
;  - Alt-O  18  24 - Alt-2  79  121 - ^PrtSc    72  114 - ^PgUp     84  132 -
;  -------------------------------------------------------------------------+
;              - 101-key Keyboard Extensions Supported by BIOS -
;    -----------------------------------------------------------------------+
;    - Key      Hex  Dec - Key           Hex Dec - Key           Hex  Dec   -
;    -----------------------------------------------------------------------+
;    - F11       85  133 - Alt-Bksp      0e   14 -  Alt- K /      a4  164   -
;    - F12       86  134 - Alt-Enter     1c   28 -  Alt- K *      37   55   -
;    - Shft-F11  87  135 - Alt-Esc       01    1 -  Alt- K -      4a   74   -
;    - Shft-F12  88  136 - Alt-Tab       a5  165 -  Alt- K +      4e   78   -
;    - Ctrl-F11  89  137 - Ctrl-Tab      94  148 -  Alt- K Enter  a6  166   -
;    - Ctrl-F12  8a  138 -                       -                          -
;    - Alt-F11   8b  139 - Alt-up     ^  98  152 - Ctrl- K /      95  149   -
;    - Alt-F12   8c  140 - Alt-down   v  a0  160 - Ctrl- K *      96  150   -
;    - Alt-[     1a   26 - Alt-left   <  9b  155 - Ctrl- K -      8e  142   -
;    - Alt-]     1b   27 - Alt-right  >  9d  157 - Ctrl- K +      90  144   -
;    - Alt-;     27   39 -                       -                          -
;    - Alt-'     28   40 - Alt-Delete    a3  163 - Ctrl- K ^  [8] 8d  141   -
;    - Alt-`     29   41 - Alt-End       9f  159 - Ctrl- K 5  [5] 8f  143   -
;    - Alt-\     2b   43 - Alt-Home      97  151 - Ctrl- K v  [2] 91  145   -
;    - Alt-,     33   51 - Alt-Insert    a2  162 - Ctrl- K Ins[0] 92  146   -
;    - Alt-.     34   52 - Alt-PageUp    99  153 - Ctrl- K Del[.] 93  147   -
;    - Alt-/     35   53 - Alt-PageDown  a1  161 -                          -
;    -----------------------------------------------------------------------+
;       K indicates a key on the numeric keypad^ (when not in NumLock mode)
;
; --Detecting the press and release of Alt--
;   The BIOS does not put the Alt key or other shift keys into the keyboard
;   buffer.  If you want to take notice of the press (and release) of say,
;   Alt, you will need to either intercept INT 09H and read the raw scan
;   codes, or poll the byte at 0040:0017 regularly and watch for changes to
;   the keyboard shift-state flags.


;------------------Ask if a key has been pressed-------------------------------------
; AH = 1
;------------------------------------------------------------------------------------

; Returns:
; ZF is set if there are no keys in the BIOS keyboard buffer.
; If there is a key (at least), ZF is reset.
; AH and AL contain the same information as for function 0, but the key is not
; removed from the buffer.
; This function is very useful if you want your program to do something while
; you are waiting for a key.

;************************************************************************************
; ASCII table:
;************************************************************************************

; Char  Dec  Oct  Hex | Char  Dec  Oct  Hex | Char  Dec  Oct  Hex | Char Dec  Oct   Hex
; -------------------------------------------------------------------------------------
; (nul)   0 0000 0x00 | (sp)   32 0040 0x20 | @      64 0100 0x40 | `      96 0140 0x60
; (soh)   1 0001 0x01 | !      33 0041 0x21 | A      65 0101 0x41 | a      97 0141 0x61
; (stx)   2 0002 0x02 | "      34 0042 0x22 | B      66 0102 0x42 | b      98 0142 0x62
; (etx)   3 0003 0x03 | #      35 0043 0x23 | C      67 0103 0x43 | c      99 0143 0x63
; (eot)   4 0004 0x04 | $      36 0044 0x24 | D      68 0104 0x44 | d     100 0144 0x64
; (enq)   5 0005 0x05 | %      37 0045 0x25 | E      69 0105 0x45 | e     101 0145 0x65
; (ack)   6 0006 0x06 | &      38 0046 0x26 | F      70 0106 0x46 | f     102 0146 0x66
; (bel)   7 0007 0x07 | '      39 0047 0x27 | G      71 0107 0x47 | g     103 0147 0x67
; (bs)    8 0010 0x08 | (      40 0050 0x28 | H      72 0110 0x48 | h     104 0150 0x68
; (ht)    9 0011 0x09 | )      41 0051 0x29 | I      73 0111 0x49 | i     105 0151 0x69
; (nl)   10 0012 0x0a | *      42 0052 0x2a | J      74 0112 0x4a | j     106 0152 0x6a
; (vt)   11 0013 0x0b | +      43 0053 0x2b | K      75 0113 0x4b | k     107 0153 0x6b
; (np)   12 0014 0x0c | ,      44 0054 0x2c | L      76 0114 0x4c | l     108 0154 0x6c
; (cr)   13 0015 0x0d | -      45 0055 0x2d | M      77 0115 0x4d | m     109 0155 0x6d
; (so)   14 0016 0x0e | .      46 0056 0x2e | N      78 0116 0x4e | n     110 0156 0x6e
; (si)   15 0017 0x0f | /      47 0057 0x2f | O      79 0117 0x4f | o     111 0157 0x6f
; (dle)  16 0020 0x10 | 0      48 0060 0x30 | P      80 0120 0x50 | p     112 0160 0x70
; (dc1)  17 0021 0x11 | 1      49 0061 0x31 | Q      81 0121 0x51 | q     113 0161 0x71
; (dc2)  18 0022 0x12 | 2      50 0062 0x32 | R      82 0122 0x52 | r     114 0162 0x72
; (dc3)  19 0023 0x13 | 3      51 0063 0x33 | S      83 0123 0x53 | s     115 0163 0x73
; (dc4)  20 0024 0x14 | 4      52 0064 0x34 | T      84 0124 0x54 | t     116 0164 0x74
; (nak)  21 0025 0x15 | 5      53 0065 0x35 | U      85 0125 0x55 | u     117 0165 0x75
; (syn)  22 0026 0x16 | 6      54 0066 0x36 | V      86 0126 0x56 | v     118 0166 0x76
; (etb)  23 0027 0x17 | 7      55 0067 0x37 | W      87 0127 0x57 | w     119 0167 0x77
; (can)  24 0030 0x18 | 8      56 0070 0x38 | X      88 0130 0x58 | x     120 0170 0x78
; (em)   25 0031 0x19 | 9      57 0071 0x39 | Y      89 0131 0x59 | y     121 0171 0x79
; (sub)  26 0032 0x1a | :      58 0072 0x3a | Z      90 0132 0x5a | z     122 0172 0x7a
; (esc)  27 0033 0x1b | ;      59 0073 0x3b | [      91 0133 0x5b | {     123 0173 0x7b
; (fs)   28 0034 0x1c | <      60 0074 0x3c | \      92 0134 0x5c | |     124 0174 0x7c
; (gs)   29 0035 0x1d | =      61 0075 0x3d | ]      93 0135 0x5d | }     125 0175 0x7d
; (rs)   30 0036 0x1e | >      62 0076 0x3e | ^      94 0136 0x5e | ~     126 0176 0x7e
; (us)   31 0037 0x1f | ?      63 0077 0x3f | _      95 0137 0x5f | (del) 127 0177 0x7f
