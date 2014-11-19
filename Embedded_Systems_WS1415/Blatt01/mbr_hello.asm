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

; prep BIOS INT 10h02 SET CURSOR POSITION
MOV(AH, IM8(0x02))                    ; AH == BIOS Function number
MOV(BH, IM8(0))                       ; BH == page number 
MOV(DX, IM16(0))                      ; DH == row DL == column
INT(0x10)                             ; CALL Interrupt

; prep BIOS INT 10h0A WRITE CHAR ONLY AT CURSOR POSITION
MOV(AH, IM8(0x0A))                    ; AH == BIOS Function number
MOV(AL, IM8(0x20))                    ; AL == char to write (0x20 ==> whitespace)
MOV(CX, IM16(80 * 25))                ; CX == number of times to write char
INT(0x10)                             ; CALL Interrupt

; display string 

MOV( AX, IM16( 0x1300 ) )             ; service code (13h) and write mode.
MOV( BX, IM16( 0x00F0 ) )             ; video page (0) and attribute byte.
MOV( CX, IM16( msg1_length ) )        ; message length.
MOV( DX, IM16( 0 ) )                  ; row and column.
MOV( BP, IM16( msg1 ) )
INT( 0x10 )                           ; call BIOS video service interrupt.

wfe:                                  ; loop forever.
JMP(wfe)

msg1:

"Hi from below again!"

end_msg1:

msg1_length = end_msg1 - msg1

FILL_UNTIL( (0x7C00 + 510), 42)

0x55 0xAA

; INT 10H service used here:

; AH = 13h
; AL = write mode
; bit 0:
; Update cursor after writing
; bit 1:
; String contains alternating characters and attributes
; bits 2-7:
; BH = page number. 
; BL = attribute if string contains only characters. 
; CX = number of characters in string. 
; DH,DL = row,column at which to start writing.
; ES:BP -> string to write
