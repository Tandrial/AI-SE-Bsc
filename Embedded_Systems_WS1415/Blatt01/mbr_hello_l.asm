.include ../tools/x86_16b.inc
.include ../tools/IBM_PC.inc

.=0x7C00

; Set segment registers and stack.

MOV( AX, IM16( 0 ) )
MOV( DS, AX )
MOV( ES, AX )
CLI()
MOV( SS, AX )
MOV( SP, IM16( 0x7BFE ) )
STI()

; Load Start of VideoMemory
MOV( AX, IM16( 0xB800 ) )

; DS:DI is being used to loop through the VideoMemory
MOV( DS, AX )
XOR( DI, DI )
MOV( CX, IM16( 25 * 80))

loop_clear:
   MOV(AT_DI_DISP16(0), IM16(0x20))       ; Writes Whitspace @ DS:DI
   ADD(DI, IM16(0x2))                     ; Each displayed char uses 2 Bytes (color|char)
LOOP(loop_clear)

; DS:DI is being used to loop through the VideoMemory
MOV( DI, IM16(160 * 1 + 2 * 10))          ; DI = 160 * row + 2 * column for the position
XOR( SI, SI )                             ; BP:SI to adress the string
MOV( BP, IM16(msg1))                      ; BP holds the adr of the first char
MOV( CX, IM16(msg1_length))               ; Loop over the whole string

loop_print:
   MOV(AH, IM8(ATTRIBUTE(white, black)))  ; AH holds the color for the displayed char
   MOV(AL, AT_BP_SI)                      ; AL holds the char to be displayed
   MOV(AT_DI_DISP16(0), AX)               ; AX is written to the VideoMemory @DS:DI
   ADD(DI, IM16(0x2))                     ; 2 Bytes for each displayed char
   ADD(SI, IM16(0x1))                     ; 1 Byte for each char in the string
LOOP(loop_print)

wfe:                                  	  ; loop forever.
JMP(wfe)

msg1:
"Hi from below again!"
end_msg1:

msg1_length = end_msg1 - msg1

FILL_UNTIL( (0x7C00 + 510), 42)
0x55 0xAA
